package school21.tictactoe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testValidTurnFlow() throws Exception {
        UUID uuid = UUID.randomUUID();

        // Step 1: User makes first move at (0, 0)
        String requestJson = """
                {
                    "id": "%s",
                    "board": {
                        "board": [
                            [1, 0, 0],
                            [0, 0, 0],
                            [0, 0, 0]
                        ]
                    }
                }
                """.formatted(uuid);

        MvcResult result = mockMvc.perform(post("/game/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseStr);

        assertEquals(uuid.toString(), responseJson.get("id").asText());
        JsonNode boardNode = responseJson.get("board").get("board");
        assertNotNull(boardNode);
        assertEquals(1, boardNode.get(0).get(0).asInt());

        // Computer should have made 1 move
        int computerMoves = 0;
        int compRow = -1;
        int compCol = -1;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (boardNode.get(r).get(c).asInt() == 2) {
                    computerMoves++;
                    compRow = r;
                    compCol = c;
                }
            }
        }
        assertEquals(1, computerMoves);

        // Step 2: User makes second move in an empty spot
        int userRow = (compRow == 1 && compCol == 1) ? 2 : 1;
        int userCol = (compRow == 1 && compCol == 1) ? 2 : 1;

        int[][] nextBoard = new int[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                nextBoard[r][c] = boardNode.get(r).get(c).asInt();
            }
        }
        nextBoard[userRow][userCol] = 1;

        String step2Json = objectMapper.writeValueAsString(new school21.tictactoe.web.model.Game(
                uuid,
                new school21.tictactoe.web.model.Board(nextBoard)
        ));

        MvcResult result2 = mockMvc.perform(post("/game/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(step2Json))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode res2Json = objectMapper.readTree(result2.getResponse().getContentAsString());
        JsonNode b2 = res2Json.get("board").get("board");
        assertEquals(1, b2.get(userRow).get(userCol).asInt());
    }

    @Test
    public void testTamperedBoardReturnsBadRequest() throws Exception {
        UUID uuid = UUID.randomUUID();

        // Step 1: User makes first move
        String requestJson = """
                {
                    "id": "%s",
                    "board": {
                        "board": [
                            [1, 0, 0],
                            [0, 0, 0],
                            [0, 0, 0]
                        ]
                    }
                }
                """.formatted(uuid);

        MvcResult result = mockMvc.perform(post("/game/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        // Step 2: User changes their move at (0, 0) to 0 and moves at (1, 1)
        String tamperedJson = """
                {
                    "id": "%s",
                    "board": {
                        "board": [
                            [0, 0, 0],
                            [0, 1, 0],
                            [0, 0, 0]
                        ]
                    }
                }
                """.formatted(uuid);

        MvcResult tamperedResult = mockMvc.perform(post("/game/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tamperedJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String errStr = tamperedResult.getResponse().getContentAsString();
        JsonNode errJson = objectMapper.readTree(errStr);
        assertNotNull(errJson.get("message"));
        assertTrue(errJson.get("message").asText().contains("modified"));
    }

    @Test
    public void testMismatchedUuidReturnsBadRequest() throws Exception {
        UUID pathUuid = UUID.randomUUID();
        UUID bodyUuid = UUID.randomUUID();

        String requestJson = """
                {
                    "id": "%s",
                    "board": {
                        "board": [
                            [1, 0, 0],
                            [0, 0, 0],
                            [0, 0, 0]
                        ]
                    }
                }
                """.formatted(bodyUuid);

        mockMvc.perform(post("/game/" + pathUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMultipleGamesSimultaneously() throws Exception {
        UUID game1 = UUID.randomUUID();
        UUID game2 = UUID.randomUUID();

        String req1 = """
                {
                    "id": "%s",
                    "board": {
                        "board": [
                            [1, 0, 0],
                            [0, 0, 0],
                            [0, 0, 0]
                        ]
                    }
                }
                """.formatted(game1);

        String req2 = """
                {
                    "id": "%s",
                    "board": {
                        "board": [
                            [0, 0, 0],
                            [0, 1, 0],
                            [0, 0, 0]
                        ]
                    }
                }
                """.formatted(game2);

        mockMvc.perform(post("/game/" + game1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req1))
                .andExpect(status().isOk());

        mockMvc.perform(post("/game/" + game2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req2))
                .andExpect(status().isOk());

        // Verify both games are stored independently
        MvcResult res1 = mockMvc.perform(get("/game/" + game1))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult res2 = mockMvc.perform(get("/game/" + game2))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode j1 = objectMapper.readTree(res1.getResponse().getContentAsString());
        JsonNode j2 = objectMapper.readTree(res2.getResponse().getContentAsString());

        assertEquals(1, j1.get("board").get("board").get(0).get(0).asInt());
        assertEquals(1, j2.get("board").get("board").get(1).get(1).asInt());
    }
}

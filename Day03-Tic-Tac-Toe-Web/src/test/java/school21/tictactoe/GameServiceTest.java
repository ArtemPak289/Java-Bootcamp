package school21.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school21.tictactoe.datasource.mapper.DatasourceMapperImpl;
import school21.tictactoe.datasource.repository.GameRepository;
import school21.tictactoe.datasource.repository.GameRepositoryImpl;
import school21.tictactoe.datasource.repository.GameStorage;
import school21.tictactoe.domain.model.Board;
import school21.tictactoe.domain.model.Game;
import school21.tictactoe.domain.service.GameService;
import school21.tictactoe.domain.service.GameServiceImpl;
import school21.tictactoe.domain.service.InvalidGameException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private GameService gameService;
    private GameRepository gameRepository;

    @BeforeEach
    public void setUp() {
        GameStorage storage = new GameStorage();
        DatasourceMapperImpl mapper = new DatasourceMapperImpl();
        gameRepository = new GameRepositoryImpl(storage, mapper);
        gameService = new GameServiceImpl(gameRepository, mapper);
    }

    @Test
    public void testFirstMoveValid() {
        UUID id = UUID.randomUUID();
        int[][] initialMatrix = {
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        Game game = new Game(id, new Board(initialMatrix));
        Game response = gameService.getNextMove(game);

        assertNotNull(response);
        assertEquals(id, response.getId());

        // Check that player's move is preserved
        assertEquals(1, response.getBoard().getCell(0, 0));

        // Check that computer made a move
        int computerMoves = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (response.getBoard().getCell(i, j) == 2) computerMoves++;
            }
        }
        assertEquals(1, computerMoves, "Computer must have made exactly 1 move");
    }

    @Test
    public void testTamperedPreviousMoveThrowsException() {
        UUID id = UUID.randomUUID();
        // Turn 1
        int[][] turn1 = {
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        Game game1 = new Game(id, new Board(turn1));
        Game response1 = gameService.getNextMove(game1);

        // Turn 2: User changes their previous move at (0, 0) from 1 to 0 or 2
        int[][] tamperedMatrix = response1.getBoard().getBoard();
        tamperedMatrix[0][0] = 0; // Erased previous move
        tamperedMatrix[1][1] = 1; // Made another move

        Game tamperedGame = new Game(id, new Board(tamperedMatrix));

        assertThrows(InvalidGameException.class, () -> {
            gameService.getNextMove(tamperedGame);
        }, "Tampering with previous move must throw InvalidGameException");
    }

    @Test
    public void testMultipleMovesInSingleTurnThrowsException() {
        UUID id = UUID.randomUUID();
        // User attempts to make 2 moves at once
        int[][] matrix = {
                {1, 1, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        Game game = new Game(id, new Board(matrix));

        assertThrows(InvalidGameException.class, () -> {
            gameService.getNextMove(game);
        }, "Making multiple moves in one turn must throw InvalidGameException");
    }

    @Test
    public void testGameEndedDetection() {
        UUID id = UUID.randomUUID();
        int[][] finishedMatrix = {
                {1, 1, 1},
                {2, 2, 0},
                {0, 0, 0}
        };
        Game game = new Game(id, new Board(finishedMatrix));
        assertTrue(gameService.isGameEnded(game));
    }
}

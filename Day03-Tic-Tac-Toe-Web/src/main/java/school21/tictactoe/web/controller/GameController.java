package school21.tictactoe.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school21.tictactoe.domain.service.GameService;
import school21.tictactoe.domain.service.InvalidGameException;
import school21.tictactoe.web.mapper.WebMapper;
import school21.tictactoe.web.model.Game;

import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final WebMapper webMapper;

    public GameController(GameService gameService, WebMapper webMapper) {
        this.gameService = gameService;
        this.webMapper = webMapper;
    }

    /**
     * Receives the current game with an updated board from the user
     * and returns the current game with the updated board for the computer's turn.
     *
     * @param uuid Path variable representing the UUID of the current game.
     * @param requestGame Current game with updated board sent by the user.
     * @return Updated current game with computer's move.
     */
    @PostMapping("/{uuid}")
    public ResponseEntity<Game> makeMove(
            @PathVariable("uuid") UUID uuid,
            @RequestBody(required = false) Game requestGame) {

        if (uuid == null) {
            throw new InvalidGameException("Game UUID in path cannot be null");
        }

        if (requestGame == null) {
            throw new InvalidGameException("Request body cannot be empty");
        }

        UUID bodyId = requestGame.getId() != null ? requestGame.getId() : requestGame.getUuid();
        if (bodyId != null && !uuid.equals(bodyId)) {
            throw new InvalidGameException(
                    "Path UUID (" + uuid + ") does not match body UUID (" + bodyId + ")"
            );
        }

        // Ensure the game ID matches the path UUID
        requestGame.setId(uuid);

        if (requestGame.getBoard() == null) {
            throw new InvalidGameException("Game board cannot be null");
        }

        school21.tictactoe.domain.model.Game domainRequest = webMapper.toDomain(requestGame);
        school21.tictactoe.domain.model.Game domainResponse = gameService.getNextMove(domainRequest);

        Game responseDto = webMapper.toWeb(domainResponse);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Game> getGame(@PathVariable("uuid") UUID uuid) {
        school21.tictactoe.domain.model.Game domainGame = gameService.getGame(uuid);
        if (domainGame == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(webMapper.toWeb(domainGame));
    }
}

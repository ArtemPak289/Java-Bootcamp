package edu.school21.tictactoe.controllers;

import edu.school21.tictactoe.models.CreateGameRequest;
import edu.school21.tictactoe.models.Game;
import edu.school21.tictactoe.models.GameResponse;
import edu.school21.tictactoe.models.MoveRequest;
import edu.school21.tictactoe.services.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    private UUID getCurrentUserId() {
        return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public ResponseEntity<GameResponse> createGame(@RequestBody CreateGameRequest request) {
        UUID userId = getCurrentUserId();
        Game game = gameService.createGame(userId, request.isVsComputer());
        return ResponseEntity.status(HttpStatus.CREATED).body(new GameResponse(game));
    }

    @GetMapping
    public ResponseEntity<List<GameResponse>> getAvailableGames() {
        List<Game> games = gameService.getAvailableGames();
        List<GameResponse> response = games.stream().map(GameResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<?> joinGame(@PathVariable UUID gameId) {
        UUID userId = getCurrentUserId();
        Game game = gameService.joinGame(gameId, userId);
        if (game != null) {
            return ResponseEntity.ok(new GameResponse(game));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot join game");
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<?> makeMove(@PathVariable UUID gameId, @RequestBody MoveRequest request) {
        UUID userId = getCurrentUserId();
        Game game = gameService.makeMove(gameId, userId, request.getX(), request.getY());
        if (game != null) {
            return ResponseEntity.ok(new GameResponse(game));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid move");
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<?> getGame(@PathVariable UUID gameId) {
        Optional<Game> game = gameService.getGame(gameId);
        if (game.isPresent()) {
            return ResponseEntity.ok(new GameResponse(game.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
    }
}

package school21.tictactoe.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school21.tictactoe.datasource.repository.GameRepository;
import school21.tictactoe.datasource.model.Game;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class HistoryController {
    
    private final GameRepository gameRepository;

    public HistoryController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/game/history")
    public ResponseEntity<List<Game>> getHistory() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = (UUID) auth.getPrincipal();
        return ResponseEntity.ok(gameRepository.findCompletedGamesByUserId(userId));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard(@RequestParam(defaultValue = "10") int topN) {
        List<Object[]> rawStats = gameRepository.findLeaderboard();
        List<Map<String, Object>> leaderboard = rawStats.stream()
                .limit(topN)
                .map(row -> Map.of(
                        "userId", row[0].toString(),
                        "winRatio", row[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(leaderboard);
    }
}

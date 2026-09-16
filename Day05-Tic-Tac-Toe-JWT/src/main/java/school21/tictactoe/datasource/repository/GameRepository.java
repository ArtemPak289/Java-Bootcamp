package school21.tictactoe.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import school21.tictactoe.datasource.model.Game;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
    
    @Query("SELECT g FROM Game g WHERE g.userId = :userId AND g.status IN ('WON', 'DRAW')")
    List<Game> findCompletedGamesByUserId(@Param("userId") UUID userId);

    @Query("SELECT g.userId as userId, " +
           "SUM(CASE WHEN g.status = 'WON' THEN 1 ELSE 0 END) * 1.0 / " +
           "NULLIF(SUM(CASE WHEN g.status IN ('LOST', 'DRAW') THEN 1 ELSE 0 END), 0) as winRatio " +
           "FROM Game g " +
           "WHERE g.status IN ('WON', 'LOST', 'DRAW') AND g.userId IS NOT NULL " +
           "GROUP BY g.userId " +
           "ORDER BY winRatio DESC")
    List<Object[]> findLeaderboard();
}

package school21.tictactoe.datasource.repository;

import school21.tictactoe.datasource.model.Game;

import java.util.Optional;
import java.util.UUID;

public interface GameRepository {

    /**
     * Saves the current game in datasource format.
     */
    void save(Game game);

    /**
     * Retrieves the current game in datasource format by UUID.
     */
    Game get(UUID id);

    /**
     * Saves the current game in domain format (maps to datasource format).
     */
    void save(school21.tictactoe.domain.model.Game game);

    /**
     * Retrieves the current game in domain format by UUID.
     */
    school21.tictactoe.domain.model.Game getDomain(UUID id);

    /**
     * Finds the game by UUID wrapped in Optional.
     */
    Optional<Game> findById(UUID id);
}

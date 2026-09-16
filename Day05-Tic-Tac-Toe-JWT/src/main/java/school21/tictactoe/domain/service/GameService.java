package school21.tictactoe.domain.service;

import school21.tictactoe.domain.model.Game;
import school21.tictactoe.domain.model.GameStatus;

import java.util.UUID;

public interface GameService {

    /**
     * Gets the next move of the current game using the Minimax algorithm.
     * Validates the game board, computes computer's move if game is not finished,
     * updates the game in repository, and returns the updated game.
     */
    Game getNextMove(Game game);

    /**
     * Validates the current game board against the previous game board.
     * Checks that previous moves have not been modified, that the player
     * made exactly one valid move, and that board dimensions/values are valid.
     */
    boolean validateGameBoard(Game currentGame, Game previousGame);

    /**
     * Validates the current game board against the stored previous game state.
     * Throws InvalidGameException if validation fails.
     */
    void validateGameBoard(Game currentGame);

    /**
     * Checks if the game has ended (player won, computer won, or draw).
     */
    boolean isGameEnded(Game game);

    /**
     * Alias for checking if the game has ended.
     */
    default boolean checkGameEnded(Game game) {
        return isGameEnded(game);
    }

    /**
     * Returns the current status of the game (IN_PROGRESS, PLAYER_WON, COMPUTER_WON, DRAW).
     */
    GameStatus getGameStatus(Game game);

    /**
     * Retrieves the current game from storage by its UUID.
     */
    Game getGame(UUID id);
}

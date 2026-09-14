package school21.tictactoe.domain.service;

import school21.tictactoe.datasource.mapper.DatasourceMapper;
import school21.tictactoe.datasource.mapper.DatasourceMapperImpl;
import school21.tictactoe.datasource.repository.GameRepository;
import school21.tictactoe.domain.model.Board;
import school21.tictactoe.domain.model.Game;
import school21.tictactoe.domain.model.GameStatus;

import java.util.Objects;
import java.util.UUID;

public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final DatasourceMapper datasourceMapper;

    public GameServiceImpl(GameRepository gameRepository) {
        this(gameRepository, new DatasourceMapperImpl());
    }

    public GameServiceImpl(GameRepository gameRepository, DatasourceMapper datasourceMapper) {
        this.gameRepository = gameRepository;
        this.datasourceMapper = datasourceMapper != null ? datasourceMapper : new DatasourceMapperImpl();
    }

    @Override
    public Game getNextMove(Game game) {
        if (game == null || game.getBoard() == null) {
            throw new InvalidGameException("Game and game board cannot be null");
        }

        // Validate the game board against stored state
        validateGameBoard(game);

        // Check if game ended after the player's move
        if (isGameEnded(game)) {
            saveGame(game);
            return game;
        }

        // Calculate computer's next move using Minimax
        Board boardWithComputerMove = Minimax.findBestMove(game.getBoard());
        Game updatedGame = new Game(game.getId(), boardWithComputerMove);

        // Save the updated game state in the repository
        saveGame(updatedGame);

        return updatedGame;
    }

    @Override
    public boolean validateGameBoard(Game currentGame, Game previousGame) {
        if (currentGame == null || currentGame.getBoard() == null) {
            throw new InvalidGameException("Current game and its board cannot be null");
        }

        int[][] currMatrix = currentGame.getBoard().getBoard();

        if (previousGame != null) {
            if (!Objects.equals(currentGame.getId(), previousGame.getId())) {
                throw new InvalidGameException("Game ID mismatch between current and previous game");
            }

            if (isGameEnded(previousGame)) {
                throw new InvalidGameException("Cannot make a move in a game that has already ended");
            }

            int[][] prevMatrix = previousGame.getBoard().getBoard();

            int prevPlayerMoves = 0;
            int prevComputerMoves = 0;
            int currPlayerMoves = 0;
            int currComputerMoves = 0;

            for (int i = 0; i < Board.SIZE; i++) {
                for (int j = 0; j < Board.SIZE; j++) {
                    int prevVal = prevMatrix[i][j];
                    int currVal = currMatrix[i][j];

                    if (prevVal != Board.EMPTY) {
                        if (currVal != prevVal) {
                            throw new InvalidGameException(
                                    "Previous move at (" + i + ", " + j + ") has been modified. Expected " + prevVal + " but got " + currVal
                            );
                        }
                    }

                    if (prevVal == Board.PLAYER) prevPlayerMoves++;
                    if (prevVal == Board.COMPUTER) prevComputerMoves++;
                    if (currVal == Board.PLAYER) currPlayerMoves++;
                    if (currVal == Board.COMPUTER) currComputerMoves++;
                }
            }

            if (currComputerMoves != prevComputerMoves) {
                throw new InvalidGameException("Player cannot modify or add computer moves");
            }

            if (currPlayerMoves != prevPlayerMoves + 1) {
                throw new InvalidGameException("Player must make exactly one move. Previous player moves: "
                        + prevPlayerMoves + ", current: " + currPlayerMoves);
            }

        } else {
            // New game (no previous history)
            int currPlayerMoves = 0;
            int currComputerMoves = 0;

            for (int i = 0; i < Board.SIZE; i++) {
                for (int j = 0; j < Board.SIZE; j++) {
                    int currVal = currMatrix[i][j];
                    if (currVal == Board.PLAYER) currPlayerMoves++;
                    if (currVal == Board.COMPUTER) currComputerMoves++;
                }
            }

            if (currComputerMoves > 0) {
                throw new InvalidGameException("A new game cannot start with computer moves already on the board");
            }

            if (currPlayerMoves > 1) {
                throw new InvalidGameException("Player cannot make more than one move on a new game. Count: " + currPlayerMoves);
            }
        }

        return true;
    }

    @Override
    public void validateGameBoard(Game currentGame) {
        if (currentGame == null || currentGame.getId() == null) {
            throw new InvalidGameException("Game or game UUID is null");
        }

        Game previousGame = getGame(currentGame.getId());
        validateGameBoard(currentGame, previousGame);
    }

    @Override
    public boolean isGameEnded(Game game) {
        if (game == null || game.getBoard() == null) {
            return false;
        }
        int winner = Minimax.checkWinner(game.getBoard());
        if (winner != Board.EMPTY) {
            return true;
        }
        return Minimax.isBoardFull(game.getBoard());
    }

    @Override
    public GameStatus getGameStatus(Game game) {
        if (game == null || game.getBoard() == null) {
            return GameStatus.IN_PROGRESS;
        }
        int winner = Minimax.checkWinner(game.getBoard());
        if (winner == Board.PLAYER) {
            return GameStatus.PLAYER_WON;
        }
        if (winner == Board.COMPUTER) {
            return GameStatus.COMPUTER_WON;
        }
        if (Minimax.isBoardFull(game.getBoard())) {
            return GameStatus.DRAW;
        }
        return GameStatus.IN_PROGRESS;
    }

    @Override
    public Game getGame(UUID id) {
        if (id == null) return null;
        school21.tictactoe.datasource.model.Game entity = gameRepository.get(id);
        if (entity != null) {
            return datasourceMapper.toDomain(entity);
        }
        return gameRepository.getDomain(id);
    }

    private void saveGame(Game game) {
        if (game != null) {
            gameRepository.save(datasourceMapper.toDatasource(game));
        }
    }
}

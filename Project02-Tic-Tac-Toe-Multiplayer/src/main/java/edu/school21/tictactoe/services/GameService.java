package edu.school21.tictactoe.services;

import edu.school21.tictactoe.models.Game;
import edu.school21.tictactoe.models.GameState;
import edu.school21.tictactoe.models.Token;
import edu.school21.tictactoe.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(UUID player1Id, boolean vsComputer) {
        Game game = new Game();
        game.setPlayer1Id(player1Id);
        game.setVsComputer(vsComputer);
        game.setPlayer1Token(Token.X);
        game.setPlayer2Token(Token.O);
        game.setBoard("EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY");

        if (vsComputer) {
            game.setState(GameState.TURN_OF_USER);
            game.setTurnOfUser(player1Id);
            // player2Id can be null for computer
        } else {
            game.setState(GameState.WAITING_FOR_PLAYERS);
        }

        return gameRepository.save(game);
    }

    public List<Game> getAvailableGames() {
        return gameRepository.findByState(GameState.WAITING_FOR_PLAYERS);
    }

    public Game joinGame(UUID gameId, UUID player2Id) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            if (game.getState() == GameState.WAITING_FOR_PLAYERS && !player2Id.equals(game.getPlayer1Id())) {
                game.setPlayer2Id(player2Id);
                game.setState(GameState.TURN_OF_USER);
                game.setTurnOfUser(game.getPlayer1Id()); // Player 1 goes first (X)
                return gameRepository.save(game);
            }
        }
        return null; // Could throw custom exception
    }

    public Optional<Game> getGame(UUID gameId) {
        return gameRepository.findById(gameId);
    }

    public Game makeMove(UUID gameId, UUID playerId, int x, int y) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isEmpty()) return null;

        Game game = optionalGame.get();

        if (game.getState() != GameState.TURN_OF_USER || !game.getTurnOfUser().equals(playerId)) {
            return null; // Not this player's turn
        }

        int index = y * 3 + x;
        if (x < 0 || x > 2 || y < 0 || y > 2) return null;

        String[] board = game.getBoard().split(",");
        if (!board[index].equals("EMPTY")) return null; // cell occupied

        Token currentPlayerToken = playerId.equals(game.getPlayer1Id()) ? game.getPlayer1Token() : game.getPlayer2Token();
        board[index] = currentPlayerToken.name();
        game.setBoard(String.join(",", board));

        checkWinOrDraw(game, board);

        if (game.getState() == GameState.TURN_OF_USER) {
            if (game.isVsComputer() && playerId.equals(game.getPlayer1Id())) {
                // Computer's turn
                makeComputerMove(game, board);
                checkWinOrDraw(game, board);
            } else {
                game.setTurnOfUser(playerId.equals(game.getPlayer1Id()) ? game.getPlayer2Id() : game.getPlayer1Id());
            }
        }

        return gameRepository.save(game);
    }

    private void makeComputerMove(Game game, String[] board) {
        List<Integer> emptyIndices = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i].equals("EMPTY")) {
                emptyIndices.add(i);
            }
        }
        if (!emptyIndices.isEmpty()) {
            int move = emptyIndices.get(new Random().nextInt(emptyIndices.size()));
            board[move] = Token.O.name();
            game.setBoard(String.join(",", board));
        }
    }

    private void checkWinOrDraw(Game game, String[] board) {
        int[][] winLines = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // cols
            {0, 4, 8}, {2, 4, 6}             // diagonals
        };

        for (int[] line : winLines) {
            if (!board[line[0]].equals("EMPTY") && 
                board[line[0]].equals(board[line[1]]) && 
                board[line[1]].equals(board[line[2]])) {
                
                game.setState(GameState.WIN_OF_USER);
                Token winningToken = Token.valueOf(board[line[0]]);
                if (winningToken == game.getPlayer1Token()) {
                    game.setWinnerId(game.getPlayer1Id());
                } else {
                    // Computer doesn't have a real UUID, so if winnerId is null, computer won.
                    game.setWinnerId(game.getPlayer2Id());
                }
                return;
            }
        }

        boolean draw = true;
        for (String cell : board) {
            if (cell.equals("EMPTY")) {
                draw = false;
                break;
            }
        }

        if (draw) {
            game.setState(GameState.DRAW);
        }
    }
}

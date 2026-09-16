package school21.tictactoe.domain.service;

import school21.tictactoe.domain.model.Board;

public class Minimax {

    public static Board findBestMove(Board board) {
        int[][] matrix = board.getBoard();
        int bestVal = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (matrix[i][j] == Board.EMPTY) {
                    matrix[i][j] = Board.COMPUTER;
                    int moveVal = minimax(matrix, 0, false);
                    matrix[i][j] = Board.EMPTY;

                    if (moveVal > bestVal) {
                        bestVal = moveVal;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }

        if (bestRow != -1 && bestCol != -1) {
            matrix[bestRow][bestCol] = Board.COMPUTER;
            return new Board(matrix);
        }

        return board;
    }

    private static int minimax(int[][] matrix, int depth, boolean isMax) {
        int score = evaluate(matrix);

        // If computer has won the game, return evaluated score
        if (score == 10) {
            return score - depth;
        }

        // If player has won the game, return evaluated score
        if (score == -10) {
            return score + depth;
        }

        // If no moves left, it's a draw
        if (!hasMovesLeft(matrix)) {
            return 0;
        }

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < Board.SIZE; i++) {
                for (int j = 0; j < Board.SIZE; j++) {
                    if (matrix[i][j] == Board.EMPTY) {
                        matrix[i][j] = Board.COMPUTER;
                        best = Math.max(best, minimax(matrix, depth + 1, false));
                        matrix[i][j] = Board.EMPTY;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < Board.SIZE; i++) {
                for (int j = 0; j < Board.SIZE; j++) {
                    if (matrix[i][j] == Board.EMPTY) {
                        matrix[i][j] = Board.PLAYER;
                        best = Math.min(best, minimax(matrix, depth + 1, true));
                        matrix[i][j] = Board.EMPTY;
                    }
                }
            }
            return best;
        }
    }

    public static int evaluate(int[][] matrix) {
        // Check rows for victory
        for (int row = 0; row < Board.SIZE; row++) {
            if (matrix[row][0] != Board.EMPTY &&
                    matrix[row][0] == matrix[row][1] &&
                    matrix[row][1] == matrix[row][2]) {
                if (matrix[row][0] == Board.COMPUTER) return +10;
                if (matrix[row][0] == Board.PLAYER) return -10;
            }
        }

        // Check columns for victory
        for (int col = 0; col < Board.SIZE; col++) {
            if (matrix[0][col] != Board.EMPTY &&
                    matrix[0][col] == matrix[1][col] &&
                    matrix[1][col] == matrix[2][col]) {
                if (matrix[0][col] == Board.COMPUTER) return +10;
                if (matrix[0][col] == Board.PLAYER) return -10;
            }
        }

        // Check diagonals for victory
        if (matrix[0][0] != Board.EMPTY &&
                matrix[0][0] == matrix[1][1] &&
                matrix[1][1] == matrix[2][2]) {
            if (matrix[0][0] == Board.COMPUTER) return +10;
            if (matrix[0][0] == Board.PLAYER) return -10;
        }

        if (matrix[0][2] != Board.EMPTY &&
                matrix[0][2] == matrix[1][1] &&
                matrix[1][1] == matrix[2][0]) {
            if (matrix[0][2] == Board.COMPUTER) return +10;
            if (matrix[0][2] == Board.PLAYER) return -10;
        }

        return 0;
    }

    public static boolean hasMovesLeft(int[][] matrix) {
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (matrix[i][j] == Board.EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBoardFull(Board board) {
        return !hasMovesLeft(board.getBoard());
    }

    public static int checkWinner(Board board) {
        int eval = evaluate(board.getBoard());
        if (eval == 10) return Board.COMPUTER;
        if (eval == -10) return Board.PLAYER;
        return Board.EMPTY;
    }
}

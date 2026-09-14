package school21.tictactoe;

import org.junit.jupiter.api.Test;
import school21.tictactoe.domain.model.Board;
import school21.tictactoe.domain.service.Minimax;

import static org.junit.jupiter.api.Assertions.*;

public class MinimaxTest {

    @Test
    public void testComputerTakesWinningMove() {
        // Board state:
        // Computer (2) has 2 in row 0: [2, 2, 0]
        // Player (1) has: [1, 1, 0]
        // [0, 0, 0]
        // Computer should win by placing at (0, 2)
        int[][] matrix = {
                {2, 2, 0},
                {1, 1, 0},
                {0, 0, 0}
        };
        Board board = new Board(matrix);
        Board nextBoard = Minimax.findBestMove(board);

        assertEquals(2, nextBoard.getCell(0, 2), "Computer must complete the winning row");
    }

    @Test
    public void testComputerBlocksPlayerWinningMove() {
        // Player (1) has: [1, 1, 0] at row 0
        // Computer (2) has: [0, 2, 0]
        // [0, 0, 0]
        // Computer must block at (0, 2)
        int[][] matrix = {
                {1, 1, 0},
                {0, 2, 0},
                {0, 0, 0}
        };
        Board board = new Board(matrix);
        Board nextBoard = Minimax.findBestMove(board);

        assertEquals(2, nextBoard.getCell(0, 2), "Computer must block the player from winning");
    }

    @Test
    public void testTerminalStateEvaluation() {
        int[][] winMatrix = {
                {2, 2, 2},
                {1, 1, 0},
                {0, 0, 0}
        };
        assertEquals(Board.COMPUTER, Minimax.checkWinner(new Board(winMatrix)));

        int[][] playerWinMatrix = {
                {1, 0, 2},
                {0, 1, 2},
                {0, 0, 1}
        };
        assertEquals(Board.PLAYER, Minimax.checkWinner(new Board(playerWinMatrix)));

        int[][] drawMatrix = {
                {1, 2, 1},
                {1, 2, 2},
                {2, 1, 1}
        };
        Board drawBoard = new Board(drawMatrix);
        assertEquals(Board.EMPTY, Minimax.checkWinner(drawBoard));
        assertTrue(Minimax.isBoardFull(drawBoard));
    }
}

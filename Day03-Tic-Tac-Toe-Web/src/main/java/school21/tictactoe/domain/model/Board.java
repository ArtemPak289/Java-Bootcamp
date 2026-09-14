package school21.tictactoe.domain.model;

import java.util.Arrays;

public class Board {
    public static final int EMPTY = 0;
    public static final int PLAYER = 1;
    public static final int COMPUTER = 2;
    public static final int SIZE = 3;

    private final int[][] matrix;

    public Board() {
        this.matrix = new int[SIZE][SIZE];
    }

    public Board(int[][] matrix) {
        if (matrix == null || matrix.length != SIZE) {
            throw new IllegalArgumentException("Board must be " + SIZE + "x" + SIZE);
        }
        this.matrix = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            if (matrix[i] == null || matrix[i].length != SIZE) {
                throw new IllegalArgumentException("Board row " + i + " must have length " + SIZE);
            }
            for (int j = 0; j < SIZE; j++) {
                int val = matrix[i][j];
                if (val != EMPTY && val != PLAYER && val != COMPUTER) {
                    throw new IllegalArgumentException("Invalid cell value at (" + i + "," + j + "): " + val);
                }
                this.matrix[i][j] = val;
            }
        }
    }

    public int[][] getBoard() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(this.matrix[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    public int[][] getMatrix() {
        return getBoard();
    }

    public int getCell(int row, int col) {
        return matrix[row][col];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return Arrays.deepEquals(matrix, board1.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            sb.append(Arrays.toString(matrix[i]));
            if (i < SIZE - 1) sb.append("\n");
        }
        return sb.toString();
    }
}

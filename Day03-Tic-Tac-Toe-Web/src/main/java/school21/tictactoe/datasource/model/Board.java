package school21.tictactoe.datasource.model;

import java.util.Arrays;

public class Board {
    public static final int SIZE = 3;
    private int[][] matrix;

    public Board() {
        this.matrix = new int[SIZE][SIZE];
    }

    public Board(int[][] matrix) {
        if (matrix == null) {
            this.matrix = new int[SIZE][SIZE];
            return;
        }
        this.matrix = new int[SIZE][SIZE];
        for (int i = 0; i < Math.min(matrix.length, SIZE); i++) {
            if (matrix[i] != null) {
                System.arraycopy(matrix[i], 0, this.matrix[i], 0, Math.min(matrix[i].length, SIZE));
            }
        }
    }

    public int[][] getBoard() {
        return matrix;
    }

    public void setBoard(int[][] matrix) {
        this.matrix = matrix;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
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
        return "DatasourceBoard{" + Arrays.deepToString(matrix) + '}';
    }
}

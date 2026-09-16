package school21.tictactoe.web.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.Arrays;

@JsonDeserialize(using = Board.BoardDeserializer.class)
public class Board {
    public static final int SIZE = 3;

    @JsonProperty("board")
    @JsonAlias({"matrix", "cells", "grid"})
    private int[][] board;

    public Board() {
        this.board = new int[SIZE][SIZE];
    }

    public Board(int[][] board) {
        if (board == null) {
            this.board = new int[SIZE][SIZE];
            return;
        }
        this.board = new int[SIZE][SIZE];
        for (int i = 0; i < Math.min(board.length, SIZE); i++) {
            if (board[i] != null) {
                System.arraycopy(board[i], 0, this.board[i], 0, Math.min(board[i].length, SIZE));
            }
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return Arrays.deepEquals(board, board1.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        return "WebBoard{" + Arrays.deepToString(board) + '}';
    }

    public static class BoardDeserializer extends JsonDeserializer<Board> {
        @Override
        public Board deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            if (node == null || node.isNull()) {
                return new Board();
            }

            int[][] matrix = new int[SIZE][SIZE];

            if (node.isArray()) {
                // Direct 2D array [[...], [...], [...]]
                for (int i = 0; i < Math.min(node.size(), SIZE); i++) {
                    JsonNode rowNode = node.get(i);
                    if (rowNode != null && rowNode.isArray()) {
                        for (int j = 0; j < Math.min(rowNode.size(), SIZE); j++) {
                            matrix[i][j] = rowNode.get(j).asInt(0);
                        }
                    }
                }
                return new Board(matrix);
            } else if (node.isObject()) {
                // Object containing "board" or "matrix" or "cells"
                JsonNode innerNode = null;
                if (node.has("board")) innerNode = node.get("board");
                else if (node.has("matrix")) innerNode = node.get("matrix");
                else if (node.has("cells")) innerNode = node.get("cells");

                if (innerNode != null && innerNode.isArray()) {
                    for (int i = 0; i < Math.min(innerNode.size(), SIZE); i++) {
                        JsonNode rowNode = innerNode.get(i);
                        if (rowNode != null && rowNode.isArray()) {
                            for (int j = 0; j < Math.min(rowNode.size(), SIZE); j++) {
                                matrix[i][j] = rowNode.get(j).asInt(0);
                            }
                        }
                    }
                }
                return new Board(matrix);
            }

            return new Board();
        }
    }
}

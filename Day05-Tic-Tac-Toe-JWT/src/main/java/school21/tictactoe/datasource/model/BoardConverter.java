package school21.tictactoe.datasource.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BoardConverter implements AttributeConverter<Board, String> {

    @Override
    public String convertToDatabaseColumn(Board board) {
        if (board == null) return null;
        StringBuilder sb = new StringBuilder();
        int[][] matrix = board.getMatrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(matrix[i][j]);
                if (j < 2) sb.append(",");
            }
            if (i < 2) sb.append(";");
        }
        return sb.toString();
    }

    @Override
    public Board convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return new Board();
        int[][] matrix = new int[3][3];
        String[] rows = dbData.split(";");
        for (int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(",");
            for (int j = 0; j < cols.length; j++) {
                if (!cols[j].trim().isEmpty()) {
                    matrix[i][j] = Integer.parseInt(cols[j]);
                }
            }
        }
        return new Board(matrix);
    }
}

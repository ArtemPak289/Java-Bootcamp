package school21.tictactoe.datasource.model;

import java.util.Objects;
import java.util.UUID;

public class Game {
    private UUID id;
    private Board board;

    public Game() {
    }

    public Game(UUID id, Board board) {
        this.id = id;
        this.board = board;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUuid() {
        return id;
    }

    public void setUuid(UUID uuid) {
        this.id = uuid;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(board, game.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, board);
    }

    @Override
    public String toString() {
        return "DatasourceGame{" +
                "id=" + id +
                ", board=" + board +
                '}';
    }
}

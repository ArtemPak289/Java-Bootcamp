package school21.tictactoe.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Game {
    private final UUID id;
    private final Board board;

    public Game(UUID id, Board board) {
        this.id = id != null ? id : UUID.randomUUID();
        this.board = board != null ? board : new Board();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUuid() {
        return id;
    }

    public Board getBoard() {
        return board;
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
        return "Game{" +
                "id=" + id +
                ", board=\n" + board +
                '}';
    }
}

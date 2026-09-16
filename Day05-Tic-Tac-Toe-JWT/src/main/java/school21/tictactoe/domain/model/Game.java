package school21.tictactoe.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Game {
    private final UUID id;
    private final Board board;
    private UUID userId;
    private LocalDateTime creationDate;
    private String status;

    public Game(UUID id, Board board) {
        this.id = id != null ? id : UUID.randomUUID();
        this.board = board != null ? board : new Board();
        this.creationDate = LocalDateTime.now();
        this.status = "IN_PROGRESS";
    }

    public UUID getId() { return id; }
    public UUID getUuid() { return id; }
    public Board getBoard() { return board; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

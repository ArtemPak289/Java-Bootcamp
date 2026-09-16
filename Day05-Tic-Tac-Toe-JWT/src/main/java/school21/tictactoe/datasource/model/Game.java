package school21.tictactoe.datasource.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {
    @Id
    private UUID id;

    // We need to convert Board to String for persistence
    @Convert(converter = BoardConverter.class)
    @Column(columnDefinition = "TEXT")
    private Board board;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    private String status;

    public Game() {
    }

    public Game(UUID id, Board board) {
        this.id = id;
        this.board = board;
        this.creationDate = LocalDateTime.now();
        this.status = "IN_PROGRESS";
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUuid() { return id; }
    public void setUuid(UUID uuid) { this.id = uuid; }
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
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

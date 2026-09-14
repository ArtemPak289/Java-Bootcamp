package edu.school21.tictactoe.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private GameState state;

    private UUID player1Id;
    private UUID player2Id;

    private UUID turnOfUser;
    private UUID winnerId;

    @Enumerated(EnumType.STRING)
    private Token player1Token;

    @Enumerated(EnumType.STRING)
    private Token player2Token;

    private boolean isVsComputer;

    // Board representation as a simple string, e.g., "EMPTY,EMPTY,EMPTY,..."
    @Column(length = 255)
    private String board;

    public Game() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }

    public UUID getPlayer1Id() { return player1Id; }
    public void setPlayer1Id(UUID player1Id) { this.player1Id = player1Id; }

    public UUID getPlayer2Id() { return player2Id; }
    public void setPlayer2Id(UUID player2Id) { this.player2Id = player2Id; }

    public UUID getTurnOfUser() { return turnOfUser; }
    public void setTurnOfUser(UUID turnOfUser) { this.turnOfUser = turnOfUser; }

    public UUID getWinnerId() { return winnerId; }
    public void setWinnerId(UUID winnerId) { this.winnerId = winnerId; }

    public Token getPlayer1Token() { return player1Token; }
    public void setPlayer1Token(Token player1Token) { this.player1Token = player1Token; }

    public Token getPlayer2Token() { return player2Token; }
    public void setPlayer2Token(Token player2Token) { this.player2Token = player2Token; }

    public boolean isVsComputer() { return isVsComputer; }
    public void setVsComputer(boolean vsComputer) { this.isVsComputer = vsComputer; }

    public String getBoard() { return board; }
    public void setBoard(String board) { this.board = board; }
}

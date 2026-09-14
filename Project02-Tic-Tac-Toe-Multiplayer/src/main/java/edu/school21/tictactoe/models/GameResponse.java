package edu.school21.tictactoe.models;

import java.util.UUID;

public class GameResponse {
    private UUID id;
    private GameState state;
    private UUID player1Id;
    private UUID player2Id;
    private UUID turnOfUser;
    private UUID winnerId;
    private String board;

    public GameResponse(Game game) {
        this.id = game.getId();
        this.state = game.getState();
        this.player1Id = game.getPlayer1Id();
        this.player2Id = game.getPlayer2Id();
        this.turnOfUser = game.getTurnOfUser();
        this.winnerId = game.getWinnerId();
        this.board = game.getBoard();
    }

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

    public String getBoard() { return board; }
    public void setBoard(String board) { this.board = board; }
}

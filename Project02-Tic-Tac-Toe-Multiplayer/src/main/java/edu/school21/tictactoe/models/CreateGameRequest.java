package edu.school21.tictactoe.models;

public class CreateGameRequest {
    private boolean vsComputer;

    public boolean isVsComputer() {
        return vsComputer;
    }

    public void setVsComputer(boolean vsComputer) {
        this.vsComputer = vsComputer;
    }
}

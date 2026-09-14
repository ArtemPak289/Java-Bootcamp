package school21.rogue.domain.model;

public class GameSession {
    public static final int TOTAL_LEVELS = 21;

    private Character player;
    private Level currentLevel;
    private int currentLevelNumber;
    private GameStats stats;
    private boolean gameOver;
    private boolean victory;
    private String statusMessage;

    public GameSession() {
        this.player = new Character();
        this.currentLevelNumber = 1;
        this.stats = new GameStats();
        this.gameOver = false;
        this.victory = false;
        this.statusMessage = "Welcome to the Dungeon! Explore to reach level 21.";
    }

    public Character getPlayer() {
        return player;
    }

    public void setPlayer(Character player) {
        this.player = player;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }

    public void setCurrentLevelNumber(int currentLevelNumber) {
        this.currentLevelNumber = currentLevelNumber;
        if (stats != null && currentLevelNumber > stats.getLevelReached()) {
            stats.setLevelReached(currentLevelNumber);
        }
    }

    public GameStats getStats() {
        return stats;
    }

    public void setStats(GameStats stats) {
        this.stats = stats;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}

package school21.rogue.domain.model;

public class GameStats {
    private int treasures;
    private int levelReached;
    private int enemiesDefeated;
    private int foodConsumed;
    private int elixirsConsumed;
    private int scrollsRead;
    private int hitsDealt;
    private int hitsMissed;
    private int hitsTaken;
    private int stepsTaken;

    public GameStats() {
        this.treasures = 0;
        this.levelReached = 1;
        this.enemiesDefeated = 0;
        this.foodConsumed = 0;
        this.elixirsConsumed = 0;
        this.scrollsRead = 0;
        this.hitsDealt = 0;
        this.hitsMissed = 0;
        this.hitsTaken = 0;
        this.stepsTaken = 0;
    }

    public int getTreasures() {
        return treasures;
    }

    public void setTreasures(int treasures) {
        this.treasures = treasures;
    }

    public void addTreasures(int amount) {
        this.treasures += amount;
    }

    public int getLevelReached() {
        return levelReached;
    }

    public void setLevelReached(int levelReached) {
        this.levelReached = levelReached;
    }

    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    public void setEnemiesDefeated(int enemiesDefeated) {
        this.enemiesDefeated = enemiesDefeated;
    }

    public void incrementEnemiesDefeated() {
        this.enemiesDefeated++;
    }

    public int getFoodConsumed() {
        return foodConsumed;
    }

    public void setFoodConsumed(int foodConsumed) {
        this.foodConsumed = foodConsumed;
    }

    public void incrementFoodConsumed() {
        this.foodConsumed++;
    }

    public int getElixirsConsumed() {
        return elixirsConsumed;
    }

    public void setElixirsConsumed(int elixirsConsumed) {
        this.elixirsConsumed = elixirsConsumed;
    }

    public void incrementElixirsConsumed() {
        this.elixirsConsumed++;
    }

    public int getScrollsRead() {
        return scrollsRead;
    }

    public void setScrollsRead(int scrollsRead) {
        this.scrollsRead = scrollsRead;
    }

    public void incrementScrollsRead() {
        this.scrollsRead++;
    }

    public int getHitsDealt() {
        return hitsDealt;
    }

    public void setHitsDealt(int hitsDealt) {
        this.hitsDealt = hitsDealt;
    }

    public void incrementHitsDealt() {
        this.hitsDealt++;
    }

    public int getHitsMissed() {
        return hitsMissed;
    }

    public void setHitsMissed(int hitsMissed) {
        this.hitsMissed = hitsMissed;
    }

    public void incrementHitsMissed() {
        this.hitsMissed++;
    }

    public int getHitsTaken() {
        return hitsTaken;
    }

    public void setHitsTaken(int hitsTaken) {
        this.hitsTaken = hitsTaken;
    }

    public void incrementHitsTaken() {
        this.hitsTaken++;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    public void incrementStepsTaken() {
        this.stepsTaken++;
    }
}

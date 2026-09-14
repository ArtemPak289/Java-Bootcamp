package school21.rogue.datalayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreEntry implements Comparable<ScoreEntry> {
    private String date;
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
    private String outcome;

    public ScoreEntry() {
    }

    public ScoreEntry(int treasures, int levelReached, int enemiesDefeated, int foodConsumed,
                      int elixirsConsumed, int scrollsRead, int hitsDealt, int hitsMissed,
                      int hitsTaken, int stepsTaken, String outcome) {
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.treasures = treasures;
        this.levelReached = levelReached;
        this.enemiesDefeated = enemiesDefeated;
        this.foodConsumed = foodConsumed;
        this.elixirsConsumed = elixirsConsumed;
        this.scrollsRead = scrollsRead;
        this.hitsDealt = hitsDealt;
        this.hitsMissed = hitsMissed;
        this.hitsTaken = hitsTaken;
        this.stepsTaken = stepsTaken;
        this.outcome = outcome;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTreasures() {
        return treasures;
    }

    public void setTreasures(int treasures) {
        this.treasures = treasures;
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

    public int getFoodConsumed() {
        return foodConsumed;
    }

    public void setFoodConsumed(int foodConsumed) {
        this.foodConsumed = foodConsumed;
    }

    public int getElixirsConsumed() {
        return elixirsConsumed;
    }

    public void setElixirsConsumed(int elixirsConsumed) {
        this.elixirsConsumed = elixirsConsumed;
    }

    public int getScrollsRead() {
        return scrollsRead;
    }

    public void setScrollsRead(int scrollsRead) {
        this.scrollsRead = scrollsRead;
    }

    public int getHitsDealt() {
        return hitsDealt;
    }

    public void setHitsDealt(int hitsDealt) {
        this.hitsDealt = hitsDealt;
    }

    public int getHitsMissed() {
        return hitsMissed;
    }

    public void setHitsMissed(int hitsMissed) {
        this.hitsMissed = hitsMissed;
    }

    public int getHitsTaken() {
        return hitsTaken;
    }

    public void setHitsTaken(int hitsTaken) {
        this.hitsTaken = hitsTaken;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    @Override
    public int compareTo(ScoreEntry o) {
        // Sort descending by treasures, then descending by level
        int cmp = Integer.compare(o.treasures, this.treasures);
        if (cmp != 0) return cmp;
        return Integer.compare(o.levelReached, this.levelReached);
    }
}

package school21.rogue.domain.model;

public class Buff {
    private StatType statType;
    private int amount;
    private int remainingTurns;

    public Buff() {
    }

    public Buff(StatType statType, int amount, int remainingTurns) {
        this.statType = statType;
        this.amount = amount;
        this.remainingTurns = remainingTurns;
    }

    public StatType getStatType() {
        return statType;
    }

    public void setStatType(StatType statType) {
        this.statType = statType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRemainingTurns() {
        return remainingTurns;
    }

    public void setRemainingTurns(int remainingTurns) {
        this.remainingTurns = remainingTurns;
    }

    public boolean tick() {
        remainingTurns--;
        return remainingTurns <= 0;
    }
}

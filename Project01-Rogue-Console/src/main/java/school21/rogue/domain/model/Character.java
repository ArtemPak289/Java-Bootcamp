package school21.rogue.domain.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Character {
    private int maxHealth;
    private int currentHealth;
    private int baseAgility;
    private int baseStrength;
    private Item equippedWeapon;
    private Position position;
    private Backpack backpack;
    private List<Buff> activeBuffs;
    private int sleepTurns;

    // 3D raycasting orientation (Task 9)
    private double viewAngle; // in radians
    private double fov;       // Field of view in radians (e.g. ~ PI / 3.5)
    private double viewDistance;

    public Character() {
        this.maxHealth = 25;
        this.currentHealth = 25;
        this.baseAgility = 12;
        this.baseStrength = 10;
        this.equippedWeapon = null;
        this.position = new Position(0, 0);
        this.backpack = new Backpack();
        this.activeBuffs = new ArrayList<>();
        this.sleepTurns = 0;
        this.viewAngle = 0.0;
        this.fov = Math.PI / 3.5;
        this.viewDistance = 30.0;
    }

    public Character(int maxHealth, int currentHealth, int baseAgility, int baseStrength, Position position) {
        this();
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.baseAgility = baseAgility;
        this.baseStrength = baseStrength;
        this.position = position;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getBaseAgility() {
        return baseAgility;
    }

    public void setBaseAgility(int baseAgility) {
        this.baseAgility = baseAgility;
    }

    public int getBaseStrength() {
        return baseStrength;
    }

    public void setBaseStrength(int baseStrength) {
        this.baseStrength = baseStrength;
    }

    public Item getEquippedWeapon() {
        return equippedWeapon;
    }

    public void setEquippedWeapon(Item equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
    }

    public List<Buff> getActiveBuffs() {
        return activeBuffs;
    }

    public void setActiveBuffs(List<Buff> activeBuffs) {
        this.activeBuffs = activeBuffs;
    }

    public int getSleepTurns() {
        return sleepTurns;
    }

    public void setSleepTurns(int sleepTurns) {
        this.sleepTurns = sleepTurns;
    }

    public double getViewAngle() {
        return viewAngle;
    }

    public void setViewAngle(double viewAngle) {
        this.viewAngle = viewAngle;
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }

    public double getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(double viewDistance) {
        this.viewDistance = viewDistance;
    }

    public int getEffectiveAgility() {
        int bonus = 0;
        for (Buff b : activeBuffs) {
            if (b.getStatType() == StatType.AGILITY) {
                bonus += b.getAmount();
            }
        }
        return Math.max(1, baseAgility + bonus);
    }

    public int getEffectiveStrength() {
        int bonus = 0;
        if (equippedWeapon != null) {
            bonus += equippedWeapon.getValue();
        }
        for (Buff b : activeBuffs) {
            if (b.getStatType() == StatType.STRENGTH) {
                bonus += b.getAmount();
            }
        }
        return Math.max(1, baseStrength + bonus);
    }

    public int getEffectiveMaxHealth() {
        int bonus = 0;
        for (Buff b : activeBuffs) {
            if (b.getStatType() == StatType.MAX_HEALTH) {
                bonus += b.getAmount();
            }
        }
        return Math.max(1, maxHealth + bonus);
    }

    public void applyBuff(Buff buff) {
        activeBuffs.add(buff);
        if (buff.getStatType() == StatType.MAX_HEALTH) {
            currentHealth += buff.getAmount();
        }
    }

    public void applyPermanentBoost(StatType stat, int amount) {
        switch (stat) {
            case MAX_HEALTH:
                maxHealth += amount;
                currentHealth += amount;
                break;
            case AGILITY:
                baseAgility += amount;
                break;
            case STRENGTH:
                baseStrength += amount;
                break;
            case HEALTH:
                heal(amount);
                break;
        }
    }

    public void heal(int amount) {
        currentHealth = Math.min(getEffectiveMaxHealth(), currentHealth + amount);
    }

    public void takeDamage(int amount) {
        currentHealth -= amount;
    }

    public void reduceMaxHealth(int amount) {
        maxHealth = Math.max(1, maxHealth - amount);
        if (currentHealth > getEffectiveMaxHealth()) {
            currentHealth = getEffectiveMaxHealth();
        }
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void tickTurn() {
        if (sleepTurns > 0) {
            sleepTurns--;
        }

        Iterator<Buff> it = activeBuffs.iterator();
        while (it.hasNext()) {
            Buff buff = it.next();
            if (buff.tick()) {
                it.remove();
                if (buff.getStatType() == StatType.MAX_HEALTH) {
                    if (currentHealth > getEffectiveMaxHealth()) {
                        currentHealth = getEffectiveMaxHealth();
                    }
                    if (currentHealth <= 0) {
                        currentHealth = 1; // minimum health to continue game
                    }
                }
            }
        }
    }
}

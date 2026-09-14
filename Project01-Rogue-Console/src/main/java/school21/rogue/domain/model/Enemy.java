package school21.rogue.domain.model;

import java.util.UUID;

public class Enemy {
    private String id;
    private EnemyType type;
    private int maxHealth;
    private int currentHealth;
    private int agility;
    private int strength;
    private int hostility;
    private Position position;
    private int roomId;

    // Special mechanics
    private boolean wasFirstAttacked;        // Vampire: first attack against it always misses
    private boolean isResting;              // Ogre: rests one turn after each attack
    private boolean isGuaranteedCounter;    // Ogre: guaranteed counterattack after resting
    private boolean isInvisible;            // Ghost: periodically invisible until player engages
    private boolean isAwakened;             // Mimic: true once player contacts/approaches it
    private ItemType mimickedItemType;      // Mimic: what item it looks like

    // Snake mage diagonal movement state
    private int diagonalDx = 1;
    private int diagonalDy = 1;

    public Enemy() {
        this.id = UUID.randomUUID().toString();
    }

    public Enemy(String id, EnemyType type, int maxHealth, int agility, int strength, int hostility, Position position, int roomId) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.type = type;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.agility = agility;
        this.strength = strength;
        this.hostility = hostility;
        this.position = position;
        this.roomId = roomId;
        this.wasFirstAttacked = false;
        this.isResting = false;
        this.isGuaranteedCounter = false;
        this.isInvisible = false;
        this.isAwakened = (type != EnemyType.MIMIC);
        this.mimickedItemType = (type == EnemyType.MIMIC ? ItemType.FOOD : null);
    }

    public static Enemy createZombie(Position pos, int roomId, double difficultyMult) {
        return new Enemy(UUID.randomUUID().toString(), EnemyType.ZOMBIE,
                (int)(25 * difficultyMult), (int)(5 * difficultyMult), (int)(7 * difficultyMult), 5, pos, roomId);
    }

    public static Enemy createVampire(Position pos, int roomId, double difficultyMult) {
        return new Enemy(UUID.randomUUID().toString(), EnemyType.VAMPIRE,
                (int)(22 * difficultyMult), (int)(14 * difficultyMult), (int)(8 * difficultyMult), 8, pos, roomId);
    }

    public static Enemy createGhost(Position pos, int roomId, double difficultyMult) {
        Enemy g = new Enemy(UUID.randomUUID().toString(), EnemyType.GHOST,
                (int)(12 * difficultyMult), (int)(16 * difficultyMult), (int)(4 * difficultyMult), 4, pos, roomId);
        g.setInvisible(true);
        return g;
    }

    public static Enemy createOgre(Position pos, int roomId, double difficultyMult) {
        return new Enemy(UUID.randomUUID().toString(), EnemyType.OGRE,
                (int)(35 * difficultyMult), (int)(4 * difficultyMult), (int)(13 * difficultyMult), 6, pos, roomId);
    }

    public static Enemy createSnakeMage(Position pos, int roomId, double difficultyMult) {
        return new Enemy(UUID.randomUUID().toString(), EnemyType.SNAKE_MAGE,
                (int)(18 * difficultyMult), (int)(18 * difficultyMult), (int)(6 * difficultyMult), 8, pos, roomId);
    }

    public static Enemy createMimic(Position pos, int roomId, ItemType disguise, double difficultyMult) {
        Enemy m = new Enemy(UUID.randomUUID().toString(), EnemyType.MIMIC,
                (int)(28 * difficultyMult), (int)(15 * difficultyMult), (int)(5 * difficultyMult), 3, pos, roomId);
        m.setAwakened(false);
        m.setMimickedItemType(disguise != null ? disguise : ItemType.FOOD);
        return m;
    }

    public int calculateDroppedTreasure() {
        // Based on hostility, strength, agility, and max health
        int base = (hostility * 2) + (strength * 2) + agility + (maxHealth / 2);
        int variation = (int)(Math.random() * (base / 2 + 1));
        return Math.max(5, base + variation);
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (type == EnemyType.GHOST) {
            isInvisible = false; // visible in combat
        }
        if (type == EnemyType.MIMIC) {
            isAwakened = true;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EnemyType getType() {
        return type;
    }

    public void setType(EnemyType type) {
        this.type = type;
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

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getHostility() {
        return hostility;
    }

    public void setHostility(int hostility) {
        this.hostility = hostility;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public boolean isWasFirstAttacked() {
        return wasFirstAttacked;
    }

    public void setWasFirstAttacked(boolean wasFirstAttacked) {
        this.wasFirstAttacked = wasFirstAttacked;
    }

    public boolean isResting() {
        return isResting;
    }

    public void setResting(boolean resting) {
        isResting = resting;
    }

    public boolean isGuaranteedCounter() {
        return isGuaranteedCounter;
    }

    public void setGuaranteedCounter(boolean guaranteedCounter) {
        isGuaranteedCounter = guaranteedCounter;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }

    public boolean isAwakened() {
        return isAwakened;
    }

    public void setAwakened(boolean awakened) {
        isAwakened = awakened;
    }

    public ItemType getMimickedItemType() {
        return mimickedItemType;
    }

    public void setMimickedItemType(ItemType mimickedItemType) {
        this.mimickedItemType = mimickedItemType;
    }

    public int getDiagonalDx() {
        return diagonalDx;
    }

    public void setDiagonalDx(int diagonalDx) {
        this.diagonalDx = diagonalDx;
    }

    public int getDiagonalDy() {
        return diagonalDy;
    }

    public void setDiagonalDy(int diagonalDy) {
        this.diagonalDy = diagonalDy;
    }
}

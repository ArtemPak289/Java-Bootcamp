package school21.rogue.domain.model;

public enum EnemyType {
    ZOMBIE('z', "Zombie", "GREEN"),
    VAMPIRE('v', "Vampire", "RED"),
    GHOST('g', "Ghost", "WHITE"),
    OGRE('O', "Ogre", "YELLOW"),
    SNAKE_MAGE('s', "Snake Mage", "WHITE"),
    MIMIC('m', "Mimic", "WHITE");

    private final char symbol;
    private final String displayName;
    private final String color;

    EnemyType(char symbol, String displayName, String color) {
        this.symbol = symbol;
        this.displayName = displayName;
        this.color = color;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColor() {
        return color;
    }
}

package school21.rogue.domain.model;

public enum ItemType {
    FOOD('f', "Food"),
    ELIXIR('e', "Elixir"),
    SCROLL('S', "Scroll"),
    WEAPON('w', "Weapon"),
    TREASURE('*', "Treasure"),
    KEY('k', "Key");

    private final char symbol;
    private final String displayName;

    ItemType(char symbol, String displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getDisplayName() {
        return displayName;
    }
}

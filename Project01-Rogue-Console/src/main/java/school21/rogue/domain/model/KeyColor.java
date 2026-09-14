package school21.rogue.domain.model;

public enum KeyColor {
    RED("Red"),
    BLUE("Blue"),
    YELLOW("Yellow");

    private final String displayName;

    KeyColor(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

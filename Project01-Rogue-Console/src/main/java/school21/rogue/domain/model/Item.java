package school21.rogue.domain.model;

public class Item {
    private String id;
    private String name;
    private ItemType type;
    private StatType statType;
    private int value;
    private int duration; // for temporary elixirs
    private KeyColor keyColor; // for keys
    private Position position;

    public Item() {
    }

    public Item(String id, String name, ItemType type, StatType statType, int value, int duration, KeyColor keyColor, Position position) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.statType = statType;
        this.value = value;
        this.duration = duration;
        this.keyColor = keyColor;
        this.position = position;
    }

    public static Item createFood(String name, int healthRestore, Position pos) {
        return new Item(java.util.UUID.randomUUID().toString(), name, ItemType.FOOD, StatType.HEALTH, healthRestore, 0, null, pos);
    }

    public static Item createElixir(String name, StatType stat, int boost, int durationTurns, Position pos) {
        return new Item(java.util.UUID.randomUUID().toString(), name, ItemType.ELIXIR, stat, boost, durationTurns, null, pos);
    }

    public static Item createScroll(String name, StatType stat, int boost, Position pos) {
        return new Item(java.util.UUID.randomUUID().toString(), name, ItemType.SCROLL, stat, boost, 0, null, pos);
    }

    public static Item createWeapon(String name, int strengthBonus, Position pos) {
        return new Item(java.util.UUID.randomUUID().toString(), name, ItemType.WEAPON, StatType.STRENGTH, strengthBonus, 0, null, pos);
    }

    public static Item createTreasure(int goldValue, Position pos) {
        return new Item(java.util.UUID.randomUUID().toString(), "Treasure (" + goldValue + " gold)", ItemType.TREASURE, null, goldValue, 0, null, pos);
    }

    public static Item createKey(KeyColor color, Position pos) {
        return new Item(java.util.UUID.randomUUID().toString(), color.getDisplayName() + " Key", ItemType.KEY, null, 0, 0, color, pos);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public StatType getStatType() {
        return statType;
    }

    public void setStatType(StatType statType) {
        this.statType = statType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public KeyColor getKeyColor() {
        return keyColor;
    }

    public void setKeyColor(KeyColor keyColor) {
        this.keyColor = keyColor;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        if (type == ItemType.WEAPON) {
            return name + " (STR +" + value + ")";
        } else if (type == ItemType.FOOD) {
            return name + " (+" + value + " HP)";
        } else if (type == ItemType.ELIXIR) {
            return name + " (+" + value + " " + statType + " for " + duration + " turns)";
        } else if (type == ItemType.SCROLL) {
            return name + " (+" + value + " " + statType + " perm)";
        } else if (type == ItemType.KEY) {
            return keyColor.getDisplayName() + " Key";
        }
        return name;
    }
}

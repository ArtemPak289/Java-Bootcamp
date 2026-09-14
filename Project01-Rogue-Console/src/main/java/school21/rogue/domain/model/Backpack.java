package school21.rogue.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Backpack {
    public static final int MAX_ITEMS_PER_TYPE = 9;

    private List<Item> weapons;
    private List<Item> foods;
    private List<Item> elixirs;
    private List<Item> scrolls;
    private int treasureGold;
    private Set<KeyColor> keys;

    public Backpack() {
        this.weapons = new ArrayList<>();
        this.foods = new ArrayList<>();
        this.elixirs = new ArrayList<>();
        this.scrolls = new ArrayList<>();
        this.treasureGold = 0;
        this.keys = new HashSet<>();
    }

    public boolean canAddItem(Item item) {
        if (item == null) return false;
        return switch (item.getType()) {
            case WEAPON -> weapons.size() < MAX_ITEMS_PER_TYPE;
            case FOOD -> foods.size() < MAX_ITEMS_PER_TYPE;
            case ELIXIR -> elixirs.size() < MAX_ITEMS_PER_TYPE;
            case SCROLL -> scrolls.size() < MAX_ITEMS_PER_TYPE;
            case TREASURE, KEY -> true;
        };
    }

    public boolean addItem(Item item) {
        if (item == null) return false;
        switch (item.getType()) {
            case WEAPON:
                if (weapons.size() < MAX_ITEMS_PER_TYPE) {
                    weapons.add(item);
                    return true;
                }
                return false;
            case FOOD:
                if (foods.size() < MAX_ITEMS_PER_TYPE) {
                    foods.add(item);
                    return true;
                }
                return false;
            case ELIXIR:
                if (elixirs.size() < MAX_ITEMS_PER_TYPE) {
                    elixirs.add(item);
                    return true;
                }
                return false;
            case SCROLL:
                if (scrolls.size() < MAX_ITEMS_PER_TYPE) {
                    scrolls.add(item);
                    return true;
                }
                return false;
            case TREASURE:
                treasureGold += item.getValue();
                return true;
            case KEY:
                if (item.getKeyColor() != null) {
                    keys.add(item.getKeyColor());
                }
                return true;
        }
        return false;
    }

    public void addTreasure(int amount) {
        if (amount > 0) {
            this.treasureGold += amount;
        }
    }

    public boolean hasKey(KeyColor color) {
        return keys.contains(color);
    }

    public void addKey(KeyColor color) {
        keys.add(color);
    }

    public List<Item> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Item> weapons) {
        this.weapons = weapons;
    }

    public List<Item> getFoods() {
        return foods;
    }

    public void setFoods(List<Item> foods) {
        this.foods = foods;
    }

    public List<Item> getElixirs() {
        return elixirs;
    }

    public void setElixirs(List<Item> elixirs) {
        this.elixirs = elixirs;
    }

    public List<Item> getScrolls() {
        return scrolls;
    }

    public void setScrolls(List<Item> scrolls) {
        this.scrolls = scrolls;
    }

    public int getTreasureGold() {
        return treasureGold;
    }

    public void setTreasureGold(int treasureGold) {
        this.treasureGold = treasureGold;
    }

    public Set<KeyColor> getKeys() {
        return keys;
    }

    public void setKeys(Set<KeyColor> keys) {
        this.keys = keys;
    }

    public Item removeWeapon(int index) {
        if (index >= 0 && index < weapons.size()) {
            return weapons.remove(index);
        }
        return null;
    }

    public Item removeFood(int index) {
        if (index >= 0 && index < foods.size()) {
            return foods.remove(index);
        }
        return null;
    }

    public Item removeElixir(int index) {
        if (index >= 0 && index < elixirs.size()) {
            return elixirs.remove(index);
        }
        return null;
    }

    public Item removeScroll(int index) {
        if (index >= 0 && index < scrolls.size()) {
            return scrolls.remove(index);
        }
        return null;
    }
}

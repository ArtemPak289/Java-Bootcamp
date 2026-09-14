package school21.rogue.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int id;
    private int sector;
    private Position topLeft;
    private Position bottomRight;
    private List<Door> doors;
    private List<Item> items;
    private List<Enemy> enemies;
    private boolean explored;

    public Room() {
        this.doors = new ArrayList<>();
        this.items = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.explored = false;
    }

    public Room(int id, int sector, Position topLeft, Position bottomRight) {
        this();
        this.id = id;
        this.sector = sector;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public boolean isInsideFloor(Position pos) {
        if (pos == null || topLeft == null || bottomRight == null) return false;
        return pos.getX() > topLeft.getX() && pos.getX() < bottomRight.getX()
                && pos.getY() > topLeft.getY() && pos.getY() < bottomRight.getY();
    }

    public boolean isWall(Position pos) {
        if (pos == null || topLeft == null || bottomRight == null) return false;
        int x = pos.getX();
        int y = pos.getY();
        boolean horizontal = (y == topLeft.getY() || y == bottomRight.getY()) && (x >= topLeft.getX() && x <= bottomRight.getX());
        boolean vertical = (x == topLeft.getX() || x == bottomRight.getX()) && (y >= topLeft.getY() && y <= bottomRight.getY());
        return horizontal || vertical;
    }

    public boolean contains(Position pos) {
        if (pos == null || topLeft == null || bottomRight == null) return false;
        return pos.getX() >= topLeft.getX() && pos.getX() <= bottomRight.getX()
                && pos.getY() >= topLeft.getY() && pos.getY() <= bottomRight.getY();
    }

    public int getWidth() {
        return bottomRight.getX() - topLeft.getX() + 1;
    }

    public int getHeight() {
        return bottomRight.getY() - topLeft.getY() + 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSector() {
        return sector;
    }

    public void setSector(int sector) {
        this.sector = sector;
    }

    public Position getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Position topLeft) {
        this.topLeft = topLeft;
    }

    public Position getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Position bottomRight) {
        this.bottomRight = bottomRight;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }
}

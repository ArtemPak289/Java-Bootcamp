package school21.rogue.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Level {
    public static final int DEFAULT_WIDTH = 80;
    public static final int DEFAULT_HEIGHT = 24;

    private int levelNumber;
    private int width;
    private int height;
    private List<Room> rooms;
    private List<Corridor> corridors;
    private int startRoomId;
    private int endRoomId;
    private Position exitPosition;

    public Level() {
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.rooms = new ArrayList<>();
        this.corridors = new ArrayList<>();
    }

    public Level(int levelNumber) {
        this();
        this.levelNumber = levelNumber;
    }

    public Room getRoomById(int id) {
        for (Room r : rooms) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    public Room getRoomContaining(Position pos) {
        if (pos == null) return null;
        for (Room r : rooms) {
            if (r.contains(pos)) return r;
        }
        return null;
    }

    public Room getRoomInsideFloor(Position pos) {
        if (pos == null) return null;
        for (Room r : rooms) {
            if (r.isInsideFloor(pos)) return r;
        }
        return null;
    }

    public Door getDoorAt(Position pos) {
        if (pos == null) return null;
        for (Room r : rooms) {
            for (Door d : r.getDoors()) {
                if (d.getPosition().equals(pos)) {
                    return d;
                }
            }
        }
        return null;
    }

    public boolean isCorridor(Position pos) {
        if (pos == null) return false;
        for (Corridor c : corridors) {
            if (c.contains(pos)) return true;
        }
        return false;
    }

    public boolean isWall(Position pos) {
        if (pos == null) return false;
        if (getDoorAt(pos) != null) return false;
        for (Room r : rooms) {
            if (r.isWall(pos)) return true;
        }
        return false;
    }

    public Item getItemAt(Position pos) {
        if (pos == null) return null;
        for (Room r : rooms) {
            for (Item item : r.getItems()) {
                if (item.getPosition() != null && item.getPosition().equals(pos)) {
                    return item;
                }
            }
        }
        return null;
    }

    public Enemy getEnemyAt(Position pos) {
        if (pos == null) return null;
        for (Room r : rooms) {
            for (Enemy enemy : r.getEnemies()) {
                if (enemy.isAlive() && enemy.getPosition() != null && enemy.getPosition().equals(pos)) {
                    return enemy;
                }
            }
        }
        return null;
    }

    public boolean isWalkable(Position pos) {
        if (pos == null) return false;
        if (pos.getX() < 0 || pos.getX() >= width || pos.getY() < 0 || pos.getY() >= height) {
            return false;
        }
        Door door = getDoorAt(pos);
        if (door != null) {
            return !door.isLocked();
        }
        if (isWall(pos)) {
            return false;
        }
        if (isCorridor(pos)) {
            return true;
        }
        Room r = getRoomInsideFloor(pos);
        return r != null;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Corridor> getCorridors() {
        return corridors;
    }

    public void setCorridors(List<Corridor> corridors) {
        this.corridors = corridors;
    }

    public int getStartRoomId() {
        return startRoomId;
    }

    public void setStartRoomId(int startRoomId) {
        this.startRoomId = startRoomId;
    }

    public int getEndRoomId() {
        return endRoomId;
    }

    public void setEndRoomId(int endRoomId) {
        this.endRoomId = endRoomId;
    }

    public Position getExitPosition() {
        return exitPosition;
    }

    public void setExitPosition(Position exitPosition) {
        this.exitPosition = exitPosition;
    }
}

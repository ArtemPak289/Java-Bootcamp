package school21.rogue.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Corridor {
    private int roomAId;
    private int roomBId;
    private List<Position> path;

    public Corridor() {
        this.path = new ArrayList<>();
    }

    public Corridor(int roomAId, int roomBId, List<Position> path) {
        this.roomAId = roomAId;
        this.roomBId = roomBId;
        this.path = path != null ? path : new ArrayList<>();
    }

    public int getRoomAId() {
        return roomAId;
    }

    public void setRoomAId(int roomAId) {
        this.roomAId = roomAId;
    }

    public int getRoomBId() {
        return roomBId;
    }

    public void setRoomBId(int roomBId) {
        this.roomBId = roomBId;
    }

    public List<Position> getPath() {
        return path;
    }

    public void setPath(List<Position> path) {
        this.path = path;
    }

    public boolean contains(Position pos) {
        return path.contains(pos);
    }
}

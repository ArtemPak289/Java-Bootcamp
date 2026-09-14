package school21.rogue.domain.model;

public class Door {
    private Position position;
    private KeyColor keyColor; // null if ordinary doorway, non-null if locked DOOM door
    private boolean open;

    public Door() {
    }

    public Door(Position position, KeyColor keyColor, boolean open) {
        this.position = position;
        this.keyColor = keyColor;
        this.open = open;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public KeyColor getKeyColor() {
        return keyColor;
    }

    public void setKeyColor(KeyColor keyColor) {
        this.keyColor = keyColor;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isLocked() {
        return keyColor != null && !open;
    }
}

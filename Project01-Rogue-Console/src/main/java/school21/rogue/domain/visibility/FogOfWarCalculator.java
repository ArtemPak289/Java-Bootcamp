package school21.rogue.domain.visibility;

import school21.rogue.domain.model.*;
import school21.rogue.domain.model.Character;

import java.util.*;

public class FogOfWarCalculator {

    public static class VisibilityMap {
        private final boolean[][] visibleNow;    // Visible in the current frame
        private final boolean[][] exploredEver;  // Explored in any frame

        public VisibilityMap(int width, int height) {
            this.visibleNow = new boolean[height][width];
            this.exploredEver = new boolean[height][width];
        }

        public boolean isVisibleNow(int x, int y) {
            if (y >= 0 && y < visibleNow.length && x >= 0 && x < visibleNow[0].length) {
                return visibleNow[y][x];
            }
            return false;
        }

        public boolean isExploredEver(int x, int y) {
            if (y >= 0 && y < exploredEver.length && x >= 0 && x < exploredEver[0].length) {
                return exploredEver[y][x];
            }
            return false;
        }

        public void setVisibleNow(int x, int y, boolean val) {
            if (y >= 0 && y < visibleNow.length && x >= 0 && x < visibleNow[0].length) {
                visibleNow[y][x] = val;
                if (val) {
                    exploredEver[y][x] = true;
                }
            }
        }

        public void setExploredEver(int x, int y, boolean val) {
            if (y >= 0 && y < exploredEver.length && x >= 0 && x < exploredEver[0].length) {
                exploredEver[y][x] = val;
            }
        }
    }

    public static void updateVisibility(Level level, Character player, VisibilityMap visMap) {
        if (level == null || player == null || visMap == null) return;

        // Clear current frame visibility
        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                visMap.visibleNow[y][x] = false;
            }
        }

        Position playerPos = player.getPosition();
        Room playerRoom = level.getRoomContaining(playerPos);

        // 1. If player is inside a room
        if (playerRoom != null) {
            playerRoom.setExplored(true);
            int x1 = playerRoom.getTopLeft().getX();
            int y1 = playerRoom.getTopLeft().getY();
            int x2 = playerRoom.getBottomRight().getX();
            int y2 = playerRoom.getBottomRight().getY();

            for (int y = y1; y <= y2; y++) {
                for (int x = x1; x <= x2; x++) {
                    visMap.setVisibleNow(x, y, true);
                }
            }
        }

        // 2. Corridors near player
        int px = playerPos.getX();
        int py = playerPos.getY();
        for (int dy = -2; dy <= 2; dy++) {
            for (int dx = -2; dx <= 2; dx++) {
                int cx = px + dx;
                int cy = py + dy;
                Position pos = new Position(cx, cy);
                if (level.isCorridor(pos) || level.getDoorAt(pos) != null) {
                    visMap.setVisibleNow(cx, cy, true);
                }
            }
        }

        // 3. Line of sight from corridor/door into adjacent rooms via Ray Casting & Bresenham's algorithm
        if (playerRoom == null || level.getDoorAt(playerPos) != null) {
            castRaysIntoRooms(level, playerPos, visMap);
        }
    }

    /**
     * Ray-casts using Bresenham lines to dissipate fog of war in line-of-sight cone
     */
    private static void castRaysIntoRooms(Level level, Position origin, VisibilityMap visMap) {
        int maxDist = 8;
        int ox = origin.getX();
        int oy = origin.getY();

        for (int angleDeg = 0; angleDeg < 360; angleDeg += 5) {
            double rad = Math.toRadians(angleDeg);
            int targetX = (int) Math.round(ox + Math.cos(rad) * maxDist);
            int targetY = (int) Math.round(oy + Math.sin(rad) * maxDist);

            List<Position> line = bresenhamLine(ox, oy, targetX, targetY);
            for (Position p : line) {
                if (p.getX() < 0 || p.getX() >= level.getWidth() || p.getY() < 0 || p.getY() >= level.getHeight()) {
                    break;
                }
                visMap.setVisibleNow(p.getX(), p.getY(), true);

                // Stop ray at walls or closed doors
                if (level.isWall(p)) {
                    break;
                }
                Door d = level.getDoorAt(p);
                if (d != null && d.isLocked()) {
                    break;
                }
            }
        }
    }

    public static List<Position> bresenhamLine(int x0, int y0, int x1, int y1) {
        List<Position> line = new ArrayList<>();
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        int currX = x0;
        int currY = y0;

        while (true) {
            line.add(new Position(currX, currY));
            if (currX == x1 && currY == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                currX += sx;
            }
            if (e2 < dx) {
                err += dx;
                currY += sy;
            }
        }

        return line;
    }
}

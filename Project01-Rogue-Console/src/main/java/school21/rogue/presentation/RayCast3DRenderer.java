package school21.rogue.presentation;

import school21.rogue.domain.model.Character;
import school21.rogue.domain.model.Level;
import school21.rogue.domain.model.Position;

import java.util.Arrays;
import java.util.Comparator;

public class RayCast3DRenderer {
    public static final int VIEW_WIDTH = 80;
    public static final int VIEW_HEIGHT = 22;

    private static final float RAY_STEP = 0.05f;
    private static final float SEPARATOR_EPS = 0.015f;

    // Characters matching task_samples/inc/walker_consts.h
    public static final char NEAR_WALL_CHAR = '█';     // 0x2588
    public static final char MEDIUM_WALL_CHAR = '▓';   // 0x2593
    public static final char FAR_WALL_CHAR = '▒';      // 0x2592
    public static final char FURTHEST_WALL_CHAR = '░'; // 0x2591
    public static final char SEPARATOR_CHAR = '|';

    public static final char NEAR_FLOOR_CHAR = '#';
    public static final char MEDIUM_FLOOR_CHAR = 'X';
    public static final char FAR_FLOOR_CHAR = '~';
    public static final char FURTHEST_FLOOR_CHAR = '-';
    public static final char SPACE_CHAR = ' ';

    public static class RenderBuffer {
        public char[][] chars = new char[VIEW_HEIGHT][VIEW_WIDTH];
        public String[][] colors = new String[VIEW_HEIGHT][VIEW_WIDTH];

        public RenderBuffer() {
            for (int y = 0; y < VIEW_HEIGHT; y++) {
                Arrays.fill(chars[y], SPACE_CHAR);
                Arrays.fill(colors[y], "WHITE");
            }
        }
    }

    public static RenderBuffer render3D(Level level, Character player) {
        RenderBuffer buffer = new RenderBuffer();

        double viewAngle = player.getViewAngle();
        double fov = player.getFov();
        double viewDist = player.getViewDistance();
        Position playerPos = player.getPosition();
        double posX = playerPos.getX() + 0.5;
        double posY = playerPos.getY() + 0.5;

        // Cast ray for each column of the screen
        for (int col = 0; col < VIEW_WIDTH; col++) {
            double rayAngle = (viewAngle + fov / 2.0) - ((double) col / (double) VIEW_WIDTH) * fov;
            double sinAngle = Math.sin(rayAngle);
            double cosAngle = Math.cos(rayAngle);

            double distanceToWall = 0.0;
            boolean wallHit = false;
            boolean separatorHit = false;
            String wallColor = "WHITE";

            while (!wallHit && distanceToWall < viewDist) {
                distanceToWall += RAY_STEP;

                int nextX = (int) (posX + sinAngle * distanceToWall);
                int nextY = (int) (posY + cosAngle * distanceToWall);

                if (nextX < 0 || nextX >= level.getWidth() || nextY < 0 || nextY >= level.getHeight()) {
                    wallHit = true;
                    distanceToWall = viewDist;
                } else {
                    Position cellPos = new Position(nextX, nextY);
                    if (level.isWall(cellPos) || (level.getDoorAt(cellPos) != null && level.getDoorAt(cellPos).isLocked())) {
                        wallHit = true;

                        if (level.getDoorAt(cellPos) != null && level.getDoorAt(cellPos).isLocked()) {
                            wallColor = level.getDoorAt(cellPos).getKeyColor().name();
                        }

                        // Check texture separator hit
                        if (checkTextureSeparator(nextX, nextY, posX, posY, distanceToWall, sinAngle, cosAngle)) {
                            separatorHit = true;
                        }
                    }
                }
            }

            // Correct fish-eye distortion
            double correctedDist = distanceToWall * Math.cos(rayAngle - viewAngle);
            if (correctedDist < 0.1) correctedDist = 0.1;

            int wallHeight = (int) (VIEW_HEIGHT / correctedDist);
            int wallTop = Math.max(0, (VIEW_HEIGHT / 2) - wallHeight);
            int wallBottom = Math.min(VIEW_HEIGHT - 1, (VIEW_HEIGHT / 2) + wallHeight);

            char wallChar = selectWallChar(correctedDist, viewDist, separatorHit);

            for (int y = 0; y < VIEW_HEIGHT; y++) {
                if (y < wallTop) {
                    buffer.chars[y][col] = SPACE_CHAR;
                } else if (y <= wallBottom) {
                    buffer.chars[y][col] = wallChar;
                    buffer.colors[y][col] = wallColor;
                } else {
                    // Floor gradient
                    float gradient = ((float) y - VIEW_HEIGHT / 2.0f) / (VIEW_HEIGHT / 2.0f);
                    char floorChar = FURTHEST_FLOOR_CHAR;
                    if (gradient > 0.85f) floorChar = NEAR_FLOOR_CHAR;
                    else if (gradient > 0.65f) floorChar = MEDIUM_FLOOR_CHAR;
                    else if (gradient > 0.45f) floorChar = FAR_FLOOR_CHAR;

                    buffer.chars[y][col] = floorChar;
                    buffer.colors[y][col] = "CYAN";
                }
            }
        }

        // Overlay 2D Minimap in top-right corner
        drawCornerMinimap(buffer, level, player);

        return buffer;
    }

    private static char selectWallChar(double dist, double maxDist, boolean separatorHit) {
        if (separatorHit) {
            return SEPARATOR_CHAR;
        }
        if (dist <= maxDist / 3.0) return NEAR_WALL_CHAR;
        if (dist <= maxDist / 2.0) return MEDIUM_WALL_CHAR;
        if (dist <= maxDist / 1.5) return FAR_WALL_CHAR;
        if (dist < maxDist) return FURTHEST_WALL_CHAR;
        return SPACE_CHAR;
    }

    private static boolean checkTextureSeparator(int nextX, int nextY, double posX, double posY,
                                                 double dist, double sinAngle, double cosAngle) {
        SeparatorVector[] vectors = new SeparatorVector[4];
        int idx = 0;
        for (int sx = 0; sx <= 1; sx++) {
            for (int sy = 0; sy <= 1; sy++) {
                double vx = nextX + sx - posX;
                double vy = nextY + sy - posY;
                double len = Math.sqrt(vx * vx + vy * vy);
                double dot = (sinAngle * vx / len) + (cosAngle * vy / len);
                vectors[idx++] = new SeparatorVector(len, dot);
            }
        }

        Arrays.sort(vectors, Comparator.comparingDouble(v -> v.length));

        double clampedDot0 = Math.clamp(vectors[0].dotProduct, -1.0, 1.0);
        double clampedDot1 = Math.clamp(vectors[1].dotProduct, -1.0, 1.0);

        return Math.acos(clampedDot0) < SEPARATOR_EPS || Math.acos(clampedDot1) < SEPARATOR_EPS;
    }

    private static void drawCornerMinimap(RenderBuffer buffer, Level level, Character player) {
        // Draw minimap in top right corner: width 24, height 10
        int mapW = 22;
        int mapH = 9;
        int startX = VIEW_WIDTH - mapW - 1;
        int startY = 1;

        Position pPos = player.getPosition();
        int centerPx = pPos.getX();
        int centerPy = pPos.getY();

        // Draw border
        for (int y = 0; y < mapH; y++) {
            for (int x = 0; x < mapW; x++) {
                int screenX = startX + x;
                int screenY = startY + y;

                if (y == 0 || y == mapH - 1 || x == 0 || x == mapW - 1) {
                    buffer.chars[screenY][screenX] = '+';
                    buffer.colors[screenY][screenX] = "YELLOW";
                } else {
                    int worldX = centerPx - (mapW / 2) + x;
                    int worldY = centerPy - (mapH / 2) + y;

                    if (worldX == centerPx && worldY == centerPy) {
                        buffer.chars[screenY][screenX] = '@';
                        buffer.colors[screenY][screenX] = "YELLOW";
                    } else if (worldX >= 0 && worldX < level.getWidth() && worldY >= 0 && worldY < level.getHeight()) {
                        Position pos = new Position(worldX, worldY);
                        if (level.isWall(pos)) {
                            buffer.chars[screenY][screenX] = '#';
                            buffer.colors[screenY][screenX] = "WHITE";
                        } else if (level.isCorridor(pos)) {
                            buffer.chars[screenY][screenX] = '.';
                            buffer.colors[screenY][screenX] = "GREEN";
                        } else if (level.getDoorAt(pos) != null) {
                            buffer.chars[screenY][screenX] = '+';
                            buffer.colors[screenY][screenX] = "RED";
                        } else if (level.getEnemyAt(pos) != null) {
                            buffer.chars[screenY][screenX] = level.getEnemyAt(pos).getType().getSymbol();
                            buffer.colors[screenY][screenX] = "RED";
                        } else if (level.getItemAt(pos) != null) {
                            buffer.chars[screenY][screenX] = level.getItemAt(pos).getType().getSymbol();
                            buffer.colors[screenY][screenX] = "BLUE";
                        } else if (pos.equals(level.getExitPosition())) {
                            buffer.chars[screenY][screenX] = 'E';
                            buffer.colors[screenY][screenX] = "CYAN";
                        } else {
                            buffer.chars[screenY][screenX] = ' ';
                        }
                    } else {
                        buffer.chars[screenY][screenX] = ' ';
                    }
                }
            }
        }
    }

    private static class SeparatorVector {
        double length;
        double dotProduct;

        SeparatorVector(double length, double dotProduct) {
            this.length = length;
            this.dotProduct = dotProduct;
        }
    }
}

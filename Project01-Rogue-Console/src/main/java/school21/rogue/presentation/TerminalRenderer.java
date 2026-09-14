package school21.rogue.presentation;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import school21.rogue.datalayer.ScoreEntry;
import school21.rogue.domain.model.*;
import school21.rogue.domain.model.Character;
import school21.rogue.domain.visibility.FogOfWarCalculator;

import java.io.IOException;
import java.util.List;

public class TerminalRenderer {
    public static final int TERM_WIDTH = 96;
    public static final int TERM_HEIGHT = 28;

    private final Terminal terminal;
    private final Screen screen;

    public TerminalRenderer() throws IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        factory.setInitialTerminalSize(new TerminalSize(TERM_WIDTH, TERM_HEIGHT));
        this.terminal = factory.createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.screen.startScreen();
        this.screen.setCursorPosition(null); // hide cursor
    }

    public void stop() {
        try {
            screen.stopScreen();
            terminal.close();
        } catch (IOException ignored) {
        }
    }

    public KeyStroke readInput() throws IOException {
        return screen.readInput();
    }

    public KeyStroke pollInput() throws IOException {
        return screen.pollInput();
    }

    public void clear() {
        screen.clear();
    }

    public void refresh() throws IOException {
        screen.refresh();
    }

    public void putString(int x, int y, String text, TextColor fg, TextColor bg) {
        if (text == null) return;
        for (int i = 0; i < text.length(); i++) {
            if (x + i < TERM_WIDTH && y >= 0 && y < TERM_HEIGHT) {
                screen.setCharacter(x + i, y, new com.googlecode.lanterna.TextCharacter(text.charAt(i), fg, bg));
            }
        }
    }

    public void putChar(int x, int y, char ch, TextColor fg, TextColor bg) {
        if (x >= 0 && x < TERM_WIDTH && y >= 0 && y < TERM_HEIGHT) {
            screen.setCharacter(x, y, new com.googlecode.lanterna.TextCharacter(ch, fg, bg));
        }
    }

    public void render2DGame(Level level, Character player, FogOfWarCalculator.VisibilityMap visMap,
                             List<String> messages, GameStats stats, int currentLevel) throws IOException {
        clear();

        int mapW = level.getWidth();
        int mapH = level.getHeight();
        Position pPos = player.getPosition();
        Room playerRoom = level.getRoomContaining(pPos);

        // Draw Dungeon Grid
        for (int y = 0; y < mapH; y++) {
            for (int x = 0; x < mapW; x++) {
                Position cellPos = new Position(x, y);
                boolean visibleNow = visMap != null && visMap.isVisibleNow(x, y);
                boolean exploredEver = visMap != null && visMap.isExploredEver(x, y);

                Room room = level.getRoomContaining(cellPos);
                Door door = level.getDoorAt(cellPos);

                if (!exploredEver && !visibleNow) {
                    // Unexplored: completely hidden
                    putChar(x, y, ' ', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK);
                    continue;
                }

                // If cell is part of an explored room, but player is not in that room
                boolean inDifferentRoom = (room != null && (playerRoom == null || playerRoom.getId() != room.getId()));

                if (door != null) {
                    if (door.isLocked()) {
                        TextColor dc = switch (door.getKeyColor()) {
                            case RED -> TextColor.ANSI.RED;
                            case BLUE -> TextColor.ANSI.BLUE;
                            case YELLOW -> TextColor.ANSI.YELLOW;
                        };
                        putChar(x, y, '+', dc, TextColor.ANSI.BLACK);
                    } else {
                        putChar(x, y, '+', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
                    }
                } else if (level.isWall(cellPos)) {
                    // Walls
                    char wallChar = (y == 0 || y == mapH - 1 || (room != null && (y == room.getTopLeft().getY() || y == room.getBottomRight().getY()))) ? '-' : '|';
                    putChar(x, y, wallChar, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
                } else if (level.isCorridor(cellPos)) {
                    // Corridor
                    if (visibleNow || exploredEver) {
                        putChar(x, y, '#', TextColor.ANSI.GREEN, TextColor.ANSI.BLACK);
                    }
                } else if (room != null && room.isInsideFloor(cellPos)) {
                    // Room Floor
                    if (inDifferentRoom && !visibleNow) {
                        // Explored room where player is not present -> floor/actors hidden
                        putChar(x, y, ' ', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK);
                    } else {
                        // Room floor visible
                        putChar(x, y, '.', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
                    }
                }

                // If currently visible to player, draw items and exit
                if (visibleNow) {
                    if (cellPos.equals(level.getExitPosition())) {
                        putChar(x, y, 'E', TextColor.ANSI.CYAN, TextColor.ANSI.BLACK);
                    } else {
                        Item item = level.getItemAt(cellPos);
                        if (item != null) {
                            TextColor ic = switch (item.getType()) {
                                case FOOD -> TextColor.ANSI.RED;
                                case ELIXIR -> TextColor.ANSI.CYAN;
                                case SCROLL -> TextColor.ANSI.YELLOW;
                                case WEAPON -> TextColor.ANSI.BLUE;
                                case TREASURE -> TextColor.ANSI.YELLOW;
                                case KEY -> item.getKeyColor() == KeyColor.RED ? TextColor.ANSI.RED :
                                        item.getKeyColor() == KeyColor.BLUE ? TextColor.ANSI.BLUE : TextColor.ANSI.YELLOW;
                            };
                            putChar(x, y, item.getType().getSymbol(), ic, TextColor.ANSI.BLACK);
                        }
                    }

                    // Enemies visible
                    Enemy enemy = level.getEnemyAt(cellPos);
                    if (enemy != null && enemy.isAlive()) {
                        if (enemy.getType() == EnemyType.MIMIC && !enemy.isAwakened()) {
                            // Mimic disguised as item!
                            putChar(x, y, enemy.getMimickedItemType().getSymbol(), TextColor.ANSI.RED, TextColor.ANSI.BLACK);
                        } else if (enemy.getType() == EnemyType.GHOST && enemy.isInvisible()) {
                            // Invisible ghost: floor shows instead
                        } else {
                            TextColor ec = switch (enemy.getType().getColor()) {
                                case "GREEN" -> TextColor.ANSI.GREEN;
                                case "RED" -> TextColor.ANSI.RED;
                                case "YELLOW" -> TextColor.ANSI.YELLOW;
                                default -> TextColor.ANSI.WHITE;
                            };
                            putChar(x, y, enemy.getType().getSymbol(), ec, TextColor.ANSI.BLACK);
                        }
                    }
                }
            }
        }

        // Draw Player '@'
        putChar(pPos.getX(), pPos.getY(), '@', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);

        // Status Line (HUD)
        int hudY = mapH;
        String weaponStr = player.getEquippedWeapon() != null ? "(+" + player.getEquippedWeapon().getValue() + ")" : "(+0)";
        StringBuilder buffsStr = new StringBuilder();
        for (Buff b : player.getActiveBuffs()) {
            buffsStr.append("[").append(b.getStatType()).append("+").append(b.getAmount()).append(":").append(b.getRemainingTurns()).append("] ");
        }

        String hud = String.format("Level: %-2d  Gold: %-5d  HP: %d/%-3d  Agility: %-2d  Strength: %d%-5s %s",
                currentLevel, player.getBackpack().getTreasureGold(), player.getCurrentHealth(), player.getEffectiveMaxHealth(),
                player.getEffectiveAgility(), player.getEffectiveStrength(), weaponStr, buffsStr);

        putString(0, hudY, "-".repeat(mapW), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
        putString(0, hudY + 1, hud, TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);

        // Controls bar
        putString(0, hudY + 2, "[WASD] Move/Attack | [H] Weapon | [J] Food | [K] Elixir | [E] Scroll | [V] 3D Mode | [ESC] Menu",
                TextColor.ANSI.CYAN, TextColor.ANSI.BLACK);

        // Recent Battle Messages (last 2)
        int msgY = hudY + 3;
        if (messages != null && !messages.isEmpty()) {
            int start = Math.max(0, messages.size() - 2);
            for (int i = start; i < messages.size(); i++) {
                putString(0, msgY + (i - start), "> " + messages.get(i), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
            }
        }

        refresh();
    }

    public void render3DGame(RayCast3DRenderer.RenderBuffer buffer, Character player, int currentLevel, List<String> messages) throws IOException {
        clear();

        for (int y = 0; y < RayCast3DRenderer.VIEW_HEIGHT; y++) {
            for (int x = 0; x < RayCast3DRenderer.VIEW_WIDTH; x++) {
                char ch = buffer.chars[y][x];
                String col = buffer.colors[y][x];
                TextColor tc = switch (col) {
                    case "GREEN" -> TextColor.ANSI.GREEN;
                    case "RED" -> TextColor.ANSI.RED;
                    case "YELLOW" -> TextColor.ANSI.YELLOW;
                    case "BLUE" -> TextColor.ANSI.BLUE;
                    case "CYAN" -> TextColor.ANSI.CYAN;
                    default -> TextColor.ANSI.WHITE;
                };
                putChar(x, y, ch, tc, TextColor.ANSI.BLACK);
            }
        }

        // HUD
        int hudY = RayCast3DRenderer.VIEW_HEIGHT;
        String hud = String.format("Level: %-2d  Gold: %-5d  HP: %d/%-3d  Agility: %-2d  Strength: %-2d  Angle: %.1f deg",
                currentLevel, player.getBackpack().getTreasureGold(), player.getCurrentHealth(), player.getEffectiveMaxHealth(),
                player.getEffectiveAgility(), player.getEffectiveStrength(), Math.toDegrees(player.getViewAngle()) % 360);

        putString(0, hudY, "-".repeat(RayCast3DRenderer.VIEW_WIDTH), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
        putString(0, hudY + 1, hud, TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);
        putString(0, hudY + 2, "[W/S] Move Fwd/Back | [A/D] Turn Left/Right | [V] Return to 2D | [ESC] Menu", TextColor.ANSI.CYAN, TextColor.ANSI.BLACK);

        if (messages != null && !messages.isEmpty()) {
            putString(0, hudY + 3, "> " + messages.get(messages.size() - 1), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
        }

        refresh();
    }

    public void renderMenu(int selectedIndex, boolean hasSave) throws IOException {
        clear();

        int startY = 3;
        for (int i = 0; i < Views.TITLE_BANNER.length; i++) {
            putString(15, startY + i, Views.TITLE_BANNER[i], TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);
        }

        int menuY = startY + Views.TITLE_BANNER.length + 3;
        putString(25, menuY, "================  GAME MENU  ================", TextColor.ANSI.CYAN, TextColor.ANSI.BLACK);

        for (int i = 0; i < Views.MENU_OPTIONS.length; i++) {
            String opt = Views.MENU_OPTIONS[i];
            if (i == 1 && !hasSave) {
                opt += " (No save found)";
            }

            if (i == selectedIndex) {
                putString(28, menuY + 2 + i * 2, ">>>  " + opt + "  <<<", TextColor.ANSI.GREEN, TextColor.ANSI.BLACK);
            } else {
                putString(33, menuY + 2 + i * 2, opt, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
            }
        }

        putString(20, menuY + 11, "Use [UP/DOWN/W/S] to navigate, [ENTER/SPACE] to select", TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);
        refresh();
    }

    public void renderScoreboard(List<ScoreEntry> scores) throws IOException {
        clear();
        putString(30, 2, "=== ROGUE 1980 - HIGH SCORES ===", TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);

        String table = Views.formatScoreboard(scores);
        String[] lines = table.split("\n");
        for (int i = 0; i < lines.length; i++) {
            putString(1, 4 + i, lines[i], TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
        }

        putString(25, 6 + lines.length, "Press [ENTER], [SPACE] or [ESC] to return to menu", TextColor.ANSI.CYAN, TextColor.ANSI.BLACK);
        refresh();
    }

    public void renderInventory(String title, List<Item> items, boolean canUnequip) throws IOException {
        // Draw centered modal box
        int boxW = 55;
        int boxH = Math.max(8, items.size() + 6);
        int startX = (TERM_WIDTH - boxW) / 2;
        int startY = (TERM_HEIGHT - boxH) / 2;

        for (int y = 0; y < boxH; y++) {
            for (int x = 0; x < boxW; x++) {
                if (y == 0 || y == boxH - 1 || x == 0 || x == boxW - 1) {
                    putChar(startX + x, startY + y, '#', TextColor.ANSI.CYAN, TextColor.ANSI.BLACK);
                } else {
                    putChar(startX + x, startY + y, ' ', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
                }
            }
        }

        putString(startX + 4, startY + 1, "=== " + title + " ===", TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);

        int lineIdx = startY + 3;
        if (canUnequip) {
            putString(startX + 4, lineIdx++, "[0] Unequip Weapon (Keep in backpack)", TextColor.ANSI.GREEN, TextColor.ANSI.BLACK);
        }

        if (items.isEmpty()) {
            putString(startX + 4, lineIdx++, "(No items in backpack)", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
        } else {
            for (int i = 0; i < items.size(); i++) {
                putString(startX + 4, lineIdx++, "[" + (i + 1) + "] " + items.get(i).toString(), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
            }
        }

        putString(startX + 4, startY + boxH - 2, "Press [0-9] to select, [ESC] to cancel", TextColor.ANSI.CYAN, TextColor.ANSI.BLACK);
        refresh();
    }

    public void renderGameOver(GameStats stats) throws IOException {
        clear();
        int startY = 3;
        for (int i = 0; i < Views.DEAD_BANNER.length; i++) {
            putString(15, startY + i, Views.DEAD_BANNER[i], TextColor.ANSI.RED, TextColor.ANSI.BLACK);
        }

        int infoY = startY + Views.DEAD_BANNER.length + 3;
        putString(25, infoY, "=== YOU DIED IN THE DUNGEON ===", TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
        putString(25, infoY + 2, "Treasures Collected: " + stats.getTreasures(), TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);
        putString(25, infoY + 3, "Dungeon Level Reached: " + stats.getLevelReached(), TextColor.ANSI.CYAN, TextColor.ANSI.BLACK);
        putString(25, infoY + 4, "Monsters Slain: " + stats.getEnemiesDefeated(), TextColor.ANSI.GREEN, TextColor.ANSI.BLACK);
        putString(25, infoY + 5, "Total Steps Taken: " + stats.getStepsTaken(), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);

        putString(20, infoY + 8, "Press [ENTER], [SPACE] or [ESC] to return to Main Menu", TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);
        refresh();
    }

    public void renderVictory(GameStats stats) throws IOException {
        clear();
        int startY = 3;
        for (int i = 0; i < Views.VICTORY_BANNER.length; i++) {
            putString(15, startY + i, Views.VICTORY_BANNER[i], TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);
        }

        int infoY = startY + Views.VICTORY_BANNER.length + 3;
        putString(20, infoY, "=== CONGRATULATIONS! YOU CONQUERED ALL 21 LEVELS! ===", TextColor.ANSI.GREEN, TextColor.ANSI.BLACK);
        putString(25, infoY + 2, "Total Treasures: " + stats.getTreasures(), TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);
        putString(25, infoY + 3, "Monsters Defeated: " + stats.getEnemiesDefeated(), TextColor.ANSI.CYAN, TextColor.ANSI.BLACK);
        putString(25, infoY + 4, "Food Consumed: " + stats.getFoodConsumed(), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
        putString(25, infoY + 5, "Elixirs Drunk: " + stats.getElixirsConsumed(), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
        putString(25, infoY + 6, "Scrolls Read: " + stats.getScrollsRead(), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);

        putString(20, infoY + 9, "Press [ENTER], [SPACE] or [ESC] to return to Main Menu", TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK);
        refresh();
    }
}

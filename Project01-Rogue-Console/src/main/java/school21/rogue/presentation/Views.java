package school21.rogue.presentation;

import school21.rogue.datalayer.ScoreEntry;
import school21.rogue.domain.model.Item;

import java.util.List;

public class Views {

    public static final String[] TITLE_BANNER = {
            "  ____   ____   ____ _   _ _____     _  ___   ___   ___  ",
            " |  _ \\ / __ \\ / ___| | | | ____|   / |/ _ \\ ( _ ) / _ \\ ",
            " | |_) | |  | | |  _| | | |  _|     | | (_) |/ _ \\| | | |",
            " |  _ <| |__| | |_| | |_| | |___    | |\\__, | (_) | |_| |",
            " |_| \\_\\\\____/ \\____|\\___/|_____|   |_|  /_/ \\___/ \\___/ "
    };

    public static final String[] MENU_OPTIONS = {
            "1. NEW GAME",
            "2. LOAD GAME",
            "3. SCOREBOARD",
            "4. EXIT"
    };

    public static final String[] DEAD_BANNER = {
            " __     ______  _    _   _____ _____ ______ _____  ",
            " \\ \\   / / __ \\| |  | | |  __ \\_   _|  ____|  __ \\ ",
            "  \\ \\_/ / |  | | |  | | | |  | || | | |__  | |  | |",
            "   \\   /| |  | | |  | | | |  | || | |  __| | |  | |",
            "    | | | |__| | |__| | | |__| || |_| |____| |__| |",
            "    |_|  \\____/ \\____/  |_____/_____|______|_____/ "
    };

    public static final String[] VICTORY_BANNER = {
            " __      _______ _____ _______ ____  _______     __",
            " \\ \\    / /_   _/ ____|__   __/ __ \\|  __ \\ \\   / /",
            "  \\ \\  / /  | || |       | | | |  | | |__) \\ \\_/ / ",
            "   \\ \\/ /   | || |       | | | |  | |  _  / \\   /  ",
            "    \\  /   _| || |____   | | | |__| | | \\ \\  | |   ",
            "     \\/   |_____\\_____|  |_|  \\____/|_|  \\_\\ |_|   "
    };

    public static String formatScoreboard(List<ScoreEntry> scores) {
        StringBuilder sb = new StringBuilder();
        int width = 94;
        String line = "+" + "-".repeat(width - 2) + "+";

        sb.append(line).append("\n");
        sb.append(String.format("| %-10s | %-5s | %-7s | %-6s | %-7s | %-7s | %-7s | %-6s | %-6s | %-9s |\n",
                "Treasures", "Level", "Enemies", "Food", "Elixirs", "Scrolls", "Attacks", "Missed", "Steps", "Date"));
        sb.append(line).append("\n");

        if (scores == null || scores.isEmpty()) {
            sb.append(String.format("| %-90s |\n", "No playthrough records found yet. Play a game to record your score!"));
        } else {
            int limit = Math.min(10, scores.size());
            for (int i = 0; i < limit; i++) {
                ScoreEntry s = scores.get(i);
                sb.append(String.format("| %10d | %5d | %7d | %6d | %7d | %7d | %7d | %6d | %6d | %-9s |\n",
                        s.getTreasures(), s.getLevelReached(), s.getEnemiesDefeated(), s.getFoodConsumed(),
                        s.getElixirsConsumed(), s.getScrollsRead(), s.getHitsDealt(), s.getHitsMissed(),
                        s.getStepsTaken(), s.getDate().length() > 9 ? s.getDate().substring(0, 9) : s.getDate()));
            }
        }
        sb.append(line).append("\n");
        return sb.toString();
    }

    public static String formatInventoryPrompt(String title, List<Item> items, boolean canUnequip) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(title).append(" ===\n");
        if (canUnequip) {
            sb.append(" [0] Unequip Current Weapon\n");
        }
        if (items.isEmpty()) {
            sb.append(" (Empty)\n");
        } else {
            for (int i = 0; i < items.size(); i++) {
                sb.append(String.format(" [%d] %s\n", i + 1, items.get(i).toString()));
            }
        }
        sb.append("Press [0-9] to select, or [ESC] / other key to cancel.\n");
        return sb.toString();
    }
}

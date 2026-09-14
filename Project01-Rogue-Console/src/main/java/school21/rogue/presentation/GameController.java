package school21.rogue.presentation;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import school21.rogue.datalayer.SaveLoadManager;
import school21.rogue.datalayer.ScoreEntry;
import school21.rogue.datalayer.ScoreboardManager;
import school21.rogue.domain.engine.GameEngine;
import school21.rogue.domain.model.Character;
import school21.rogue.domain.model.GameSession;
import school21.rogue.domain.model.GameStats;
import school21.rogue.domain.model.Item;

import java.io.IOException;
import java.util.List;

public class GameController {

    public enum UIState {
        MAIN_MENU,
        GAME_2D,
        GAME_3D,
        INVENTORY_WEAPON,
        INVENTORY_FOOD,
        INVENTORY_ELIXIR,
        INVENTORY_SCROLL,
        SCOREBOARD,
        GAME_OVER,
        VICTORY
    }

    private final GameEngine gameEngine;
    private final TerminalRenderer renderer;
    private final SaveLoadManager saveLoadManager;
    private final ScoreboardManager scoreboardManager;

    private UIState state;
    private int menuSelection;
    private boolean isRunning;
    private int lastRecordedLevel;

    public GameController(GameEngine engine, TerminalRenderer renderer) {
        this(engine, renderer, new SaveLoadManager(), new ScoreboardManager());
    }

    public GameController(GameEngine engine, TerminalRenderer renderer, SaveLoadManager saveLoad, ScoreboardManager scoreboard) {
        this.gameEngine = engine;
        this.renderer = renderer;
        this.saveLoadManager = saveLoad;
        this.scoreboardManager = scoreboard;
        this.state = UIState.MAIN_MENU;
        this.menuSelection = 0;
        this.isRunning = true;
        this.lastRecordedLevel = 1;
    }

    public void run() throws IOException {
        while (isRunning) {
            renderCurrentState();
            KeyStroke key = renderer.readInput();
            if (key != null) {
                handleInput(key);
            }
        }
        renderer.stop();
    }

    private void renderCurrentState() throws IOException {
        GameSession session = gameEngine.getSession();

        switch (state) {
            case MAIN_MENU -> renderer.renderMenu(menuSelection, saveLoadManager.hasSavedGame());
            case GAME_2D -> {
                if (session != null) {
                    checkLevelSave(session);
                    renderer.render2DGame(session.getCurrentLevel(), session.getPlayer(),
                            gameEngine.getVisibilityMap(), gameEngine.getRecentMessages(),
                            session.getStats(), session.getCurrentLevelNumber());
                }
            }
            case GAME_3D -> {
                if (session != null) {
                    checkLevelSave(session);
                    RayCast3DRenderer.RenderBuffer buffer = RayCast3DRenderer.render3D(session.getCurrentLevel(), session.getPlayer());
                    renderer.render3DGame(buffer, session.getPlayer(), session.getCurrentLevelNumber(), gameEngine.getRecentMessages());
                }
            }
            case INVENTORY_WEAPON -> {
                if (session != null) {
                    renderer.renderInventory("SELECT WEAPON", session.getPlayer().getBackpack().getWeapons(), true);
                }
            }
            case INVENTORY_FOOD -> {
                if (session != null) {
                    renderer.renderInventory("SELECT FOOD TO EAT", session.getPlayer().getBackpack().getFoods(), false);
                }
            }
            case INVENTORY_ELIXIR -> {
                if (session != null) {
                    renderer.renderInventory("SELECT ELIXIR TO DRINK", session.getPlayer().getBackpack().getElixirs(), false);
                }
            }
            case INVENTORY_SCROLL -> {
                if (session != null) {
                    renderer.renderInventory("SELECT SCROLL TO READ", session.getPlayer().getBackpack().getScrolls(), false);
                }
            }
            case SCOREBOARD -> renderer.renderScoreboard(scoreboardManager.getScores());
            case GAME_OVER -> {
                if (session != null) {
                    renderer.renderGameOver(session.getStats());
                }
            }
            case VICTORY -> {
                if (session != null) {
                    renderer.renderVictory(session.getStats());
                }
            }
        }
    }

    private void checkLevelSave(GameSession session) {
        if (session.getCurrentLevelNumber() > lastRecordedLevel) {
            lastRecordedLevel = session.getCurrentLevelNumber();
            try {
                saveLoadManager.saveSession(session);
                gameEngine.addMessage("Auto-saved game checkpoint at Level " + lastRecordedLevel);
            } catch (IOException e) {
                gameEngine.addMessage("Failed to auto-save: " + e.getMessage());
            }
        }
    }

    private void handleInput(KeyStroke key) throws IOException {
        switch (state) {
            case MAIN_MENU -> handleMenuInput(key);
            case GAME_2D -> handleGame2DInput(key);
            case GAME_3D -> handleGame3DInput(key);
            case INVENTORY_WEAPON -> handleWeaponInput(key);
            case INVENTORY_FOOD -> handleFoodInput(key);
            case INVENTORY_ELIXIR -> handleElixirInput(key);
            case INVENTORY_SCROLL -> handleScrollInput(key);
            case SCOREBOARD -> {
                if (key.getKeyType() == KeyType.Escape || key.getKeyType() == KeyType.Enter
                        || key.getCharacter() != null && key.getCharacter() == ' ') {
                    state = UIState.MAIN_MENU;
                }
            }
            case GAME_OVER, VICTORY -> {
                if (key.getKeyType() == KeyType.Escape || key.getKeyType() == KeyType.Enter
                        || key.getCharacter() != null && key.getCharacter() == ' ') {
                    state = UIState.MAIN_MENU;
                }
            }
        }
    }

    private void handleMenuInput(KeyStroke key) throws IOException {
        if (key.getKeyType() == KeyType.ArrowUp || (key.getCharacter() != null && (key.getCharacter() == 'w' || key.getCharacter() == 'W'))) {
            menuSelection = (menuSelection - 1 + Views.MENU_OPTIONS.length) % Views.MENU_OPTIONS.length;
        } else if (key.getKeyType() == KeyType.ArrowDown || (key.getCharacter() != null && (key.getCharacter() == 's' || key.getCharacter() == 'S'))) {
            menuSelection = (menuSelection + 1) % Views.MENU_OPTIONS.length;
        } else if (key.getKeyType() == KeyType.Enter || (key.getCharacter() != null && key.getCharacter() == ' ')) {
            executeMenuAction();
        } else if (key.getCharacter() != null && java.lang.Character.isDigit(key.getCharacter())) {
            int digit = java.lang.Character.getNumericValue(key.getCharacter());
            if (digit >= 1 && digit <= Views.MENU_OPTIONS.length) {
                menuSelection = digit - 1;
                executeMenuAction();
            }
        }
    }

    private void executeMenuAction() throws IOException {
        switch (menuSelection) {
            case 0 -> { // NEW GAME
                gameEngine.startNewGame();
                lastRecordedLevel = 1;
                state = UIState.GAME_2D;
            }
            case 1 -> { // LOAD GAME
                if (saveLoadManager.hasSavedGame()) {
                    GameSession saved = saveLoadManager.loadSession();
                    if (saved != null) {
                        gameEngine.restoreSession(saved);
                        lastRecordedLevel = saved.getCurrentLevelNumber();
                        state = UIState.GAME_2D;
                    }
                }
            }
            case 2 -> { // SCOREBOARD
                state = UIState.SCOREBOARD;
            }
            case 3 -> { // EXIT
                isRunning = false;
            }
        }
    }

    private void handleGame2DInput(KeyStroke key) throws IOException {
        GameSession session = gameEngine.getSession();
        if (session == null) return;

        if (key.getKeyType() == KeyType.Escape) {
            // Save on exit to menu
            saveLoadManager.saveSession(session);
            state = UIState.MAIN_MENU;
            return;
        }

        char c = key.getCharacter() != null ? java.lang.Character.toLowerCase(key.getCharacter()) : ' ';

        if (key.getKeyType() == KeyType.ArrowUp || c == 'w') {
            gameEngine.processPlayerMove(0, -1);
        } else if (key.getKeyType() == KeyType.ArrowDown || c == 's') {
            gameEngine.processPlayerMove(0, 1);
        } else if (key.getKeyType() == KeyType.ArrowLeft || c == 'a') {
            gameEngine.processPlayerMove(-1, 0);
        } else if (key.getKeyType() == KeyType.ArrowRight || c == 'd') {
            gameEngine.processPlayerMove(1, 0);
        } else if (c == 'h') {
            state = UIState.INVENTORY_WEAPON;
        } else if (c == 'j') {
            state = UIState.INVENTORY_FOOD;
        } else if (c == 'k') {
            state = UIState.INVENTORY_ELIXIR;
        } else if (c == 'e') {
            state = UIState.INVENTORY_SCROLL;
        } else if (c == 'v' || c == 't' || c == '3') {
            state = UIState.GAME_3D;
        }

        checkGameOverOrVictory();
    }

    private void handleGame3DInput(KeyStroke key) throws IOException {
        GameSession session = gameEngine.getSession();
        if (session == null) return;

        if (key.getKeyType() == KeyType.Escape) {
            saveLoadManager.saveSession(session);
            state = UIState.MAIN_MENU;
            return;
        }

        char c = key.getCharacter() != null ? java.lang.Character.toLowerCase(key.getCharacter()) : ' ';
        Character player = session.getPlayer();

        if (c == 'w') {
            // Move forward along view direction
            double sin = Math.sin(player.getViewAngle());
            double cos = Math.cos(player.getViewAngle());
            int dx = (int) Math.round(sin);
            int dy = (int) Math.round(cos);
            if (dx == 0 && dy == 0) dy = 1;
            gameEngine.processPlayerMove(dx, dy);
        } else if (c == 's') {
            // Move backward
            double sin = Math.sin(player.getViewAngle());
            double cos = Math.cos(player.getViewAngle());
            int dx = (int) Math.round(-sin);
            int dy = (int) Math.round(-cos);
            if (dx == 0 && dy == 0) dy = -1;
            gameEngine.processPlayerMove(dx, dy);
        } else if (c == 'a') {
            // Turn left
            player.setViewAngle(player.getViewAngle() + 0.20);
        } else if (c == 'd') {
            // Turn right
            player.setViewAngle(player.getViewAngle() - 0.20);
        } else if (c == 'v' || c == 't' || c == '2') {
            state = UIState.GAME_2D;
        }

        checkGameOverOrVictory();
    }

    private void handleWeaponInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.Escape) {
            state = UIState.GAME_2D;
            return;
        }
        if (key.getCharacter() != null && java.lang.Character.isDigit(key.getCharacter())) {
            int num = java.lang.Character.getNumericValue(key.getCharacter());
            if (num == 0) {
                gameEngine.switchWeapon(-1); // unequip
                state = UIState.GAME_2D;
            } else if (num >= 1 && num <= 9) {
                gameEngine.switchWeapon(num - 1);
                state = UIState.GAME_2D;
            }
        }
        checkGameOverOrVictory();
    }

    private void handleFoodInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.Escape) {
            state = UIState.GAME_2D;
            return;
        }
        if (key.getCharacter() != null && java.lang.Character.isDigit(key.getCharacter())) {
            int num = java.lang.Character.getNumericValue(key.getCharacter());
            if (num >= 1 && num <= 9) {
                gameEngine.useFood(num - 1);
                state = UIState.GAME_2D;
            }
        }
        checkGameOverOrVictory();
    }

    private void handleElixirInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.Escape) {
            state = UIState.GAME_2D;
            return;
        }
        if (key.getCharacter() != null && java.lang.Character.isDigit(key.getCharacter())) {
            int num = java.lang.Character.getNumericValue(key.getCharacter());
            if (num >= 1 && num <= 9) {
                gameEngine.useElixir(num - 1);
                state = UIState.GAME_2D;
            }
        }
        checkGameOverOrVictory();
    }

    private void handleScrollInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.Escape) {
            state = UIState.GAME_2D;
            return;
        }
        if (key.getCharacter() != null && java.lang.Character.isDigit(key.getCharacter())) {
            int num = java.lang.Character.getNumericValue(key.getCharacter());
            if (num >= 1 && num <= 9) {
                gameEngine.useScroll(num - 1);
                state = UIState.GAME_2D;
            }
        }
        checkGameOverOrVictory();
    }

    private void checkGameOverOrVictory() {
        GameSession session = gameEngine.getSession();
        if (session == null) return;

        if (session.isGameOver()) {
            recordGameOutcome("DEFEAT");
            saveLoadManager.deleteSavedGame();
            state = UIState.GAME_OVER;
        } else if (session.isVictory()) {
            recordGameOutcome("VICTORY");
            saveLoadManager.deleteSavedGame();
            state = UIState.VICTORY;
        }
    }

    private void recordGameOutcome(String outcome) {
        GameSession session = gameEngine.getSession();
        if (session != null && session.getStats() != null) {
            GameStats s = session.getStats();
            ScoreEntry entry = new ScoreEntry(s.getTreasures(), s.getLevelReached(), s.getEnemiesDefeated(),
                    s.getFoodConsumed(), s.getElixirsConsumed(), s.getScrollsRead(),
                    s.getHitsDealt(), s.getHitsMissed(), s.getHitsTaken(), s.getStepsTaken(), outcome);
            scoreboardManager.recordScore(entry);
        }
    }

    public UIState getState() {
        return state;
    }

    public void setState(UIState state) {
        this.state = state;
    }
}

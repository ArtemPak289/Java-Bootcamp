package school21.rogue.domain.engine;

import school21.rogue.domain.generation.DungeonGenerator;
import school21.rogue.domain.model.*;
import school21.rogue.domain.model.Character;
import school21.rogue.domain.visibility.FogOfWarCalculator;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private final DungeonGenerator dungeonGenerator;
    private final CombatCalculator combatCalculator;
    private final EnemyAI enemyAI;

    private GameSession session;
    private FogOfWarCalculator.VisibilityMap visibilityMap;
    private List<String> recentMessages;

    public GameEngine() {
        this(new DungeonGenerator(), new CombatCalculator());
    }

    public GameEngine(DungeonGenerator generator, CombatCalculator combatCalc) {
        this.dungeonGenerator = generator;
        this.combatCalculator = combatCalc;
        this.enemyAI = new EnemyAI(combatCalc);
        this.recentMessages = new ArrayList<>();
    }

    public GameSession startNewGame() {
        this.session = new GameSession();
        this.recentMessages.clear();

        loadLevel(1);
        addMessage("Welcome to Rogue 1980! Explore the dungeon to find the exit.");
        return session;
    }

    public void loadLevel(int levelNumber) {
        session.setCurrentLevelNumber(levelNumber);
        Level level = dungeonGenerator.generateLevel(levelNumber, session.getPlayer(), session.getStats());
        session.setCurrentLevel(level);

        this.visibilityMap = new FogOfWarCalculator.VisibilityMap(level.getWidth(), level.getHeight());

        // Spawn player in start room
        Room startRoom = level.getRoomById(level.getStartRoomId());
        Position spawnPos = dungeonGenerator.getRandomFloorPosition(startRoom);
        session.getPlayer().setPosition(spawnPos);

        FogOfWarCalculator.updateVisibility(level, session.getPlayer(), visibilityMap);
    }

    public void restoreSession(GameSession savedSession) {
        this.session = savedSession;
        Level level = session.getCurrentLevel();
        this.visibilityMap = new FogOfWarCalculator.VisibilityMap(level.getWidth(), level.getHeight());
        FogOfWarCalculator.updateVisibility(level, session.getPlayer(), visibilityMap);
        this.recentMessages.clear();
        addMessage("Game successfully loaded! Level " + session.getCurrentLevelNumber());
    }

    public void processPlayerMove(int dx, int dy) {
        if (session == null || session.isGameOver() || session.isVictory()) return;

        Character player = session.getPlayer();
        Level level = session.getCurrentLevel();

        // Sleep check
        if (player.getSleepTurns() > 0) {
            addMessage("You are fast asleep and cannot move this turn!");
            endTurn();
            return;
        }

        Position currentPos = player.getPosition();
        Position targetPos = currentPos.translate(dx, dy);

        // 1. Check for enemy in target cell -> initiate attack
        Enemy targetEnemy = level.getEnemyAt(targetPos);
        if (targetEnemy != null) {
            CombatCalculator.AttackResult attack = combatCalculator.playerAttacksEnemy(player, targetEnemy, session.getStats());
            addMessage(attack.getMessage());

            if (attack.isTargetDied()) {
                int loot = targetEnemy.calculateDroppedTreasure();
                player.getBackpack().addTreasure(loot);
                session.getStats().addTreasures(loot);
                addMessage("Found " + loot + " gold on the fallen enemy! Total: " + player.getBackpack().getTreasureGold());

                // Remove enemy from room
                Room r = level.getRoomById(targetEnemy.getRoomId());
                if (r != null) {
                    r.getEnemies().remove(targetEnemy);
                }
            }

            endTurn();
            return;
        }

        // 2. Check for locked door
        Door door = level.getDoorAt(targetPos);
        if (door != null && door.isLocked()) {
            KeyColor required = door.getKeyColor();
            if (player.getBackpack().hasKey(required)) {
                door.setOpen(true);
                addMessage("You used your " + required.getDisplayName() + " key to unlock the door!");
            } else {
                addMessage("The door is locked! You need a " + required.getDisplayName() + " key.");
                return; // Do not advance turn if blocked
            }
        }

        // 3. Move player if walkable
        if (level.isWalkable(targetPos)) {
            player.setPosition(targetPos);
            session.getStats().incrementStepsTaken();

            // Check item pickup
            Item item = level.getItemAt(targetPos);
            if (item != null) {
                // If it's a disguised mimic, wake it up!
                for (Room r : level.getRooms()) {
                    for (Enemy e : r.getEnemies()) {
                        if (e.getType() == EnemyType.MIMIC && e.getPosition().equals(targetPos) && !e.isAwakened()) {
                            e.setAwakened(true);
                            addMessage("The item came alive! It's a MIMIC!");
                            endTurn();
                            return;
                        }
                    }
                }

                if (player.getBackpack().canAddItem(item)) {
                    player.getBackpack().addItem(item);
                    Room room = level.getRoomContaining(targetPos);
                    if (room != null) {
                        room.getItems().remove(item);
                    }
                    addMessage("Picked up " + item + "!");
                } else {
                    addMessage("Your backpack cannot carry any more items of type " + item.getType().getDisplayName() + "!");
                }
            }

            // Check level exit
            if (targetPos.equals(level.getExitPosition())) {
                if (session.getCurrentLevelNumber() >= GameSession.TOTAL_LEVELS) {
                    session.setVictory(true);
                    addMessage("CONGRATULATIONS! You have escaped the dungeon alive!");
                    return;
                } else {
                    int nextLvl = session.getCurrentLevelNumber() + 1;
                    addMessage("Descending deeper into the dungeon... Level " + nextLvl);
                    loadLevel(nextLvl);
                    return;
                }
            }

            endTurn();
        } else {
            addMessage("Ouch! A wall blocks your path.");
        }
    }

    public boolean useFood(int index) {
        Item food = session.getPlayer().getBackpack().removeFood(index);
        if (food != null) {
            session.getPlayer().heal(food.getValue());
            session.getStats().incrementFoodConsumed();
            addMessage("Consumed " + food.getName() + " and restored " + food.getValue() + " HP!");
            endTurn();
            return true;
        }
        return false;
    }

    public boolean useElixir(int index) {
        Item elixir = session.getPlayer().getBackpack().removeElixir(index);
        if (elixir != null) {
            session.getPlayer().applyBuff(new Buff(elixir.getStatType(), elixir.getValue(), elixir.getDuration()));
            session.getStats().incrementElixirsConsumed();
            addMessage("Drank " + elixir.getName() + "! +" + elixir.getValue() + " " + elixir.getStatType() + " for " + elixir.getDuration() + " turns.");
            endTurn();
            return true;
        }
        return false;
    }

    public boolean useScroll(int index) {
        Item scroll = session.getPlayer().getBackpack().removeScroll(index);
        if (scroll != null) {
            session.getPlayer().applyPermanentBoost(scroll.getStatType(), scroll.getValue());
            session.getStats().incrementScrollsRead();
            addMessage("Read " + scroll.getName() + "! Permanently gained +" + scroll.getValue() + " to " + scroll.getStatType() + "!");
            endTurn();
            return true;
        }
        return false;
    }

    public boolean switchWeapon(int index) {
        Character player = session.getPlayer();
        Level level = session.getCurrentLevel();

        if (index == -1) {
            // Unequip weapon without dropping
            if (player.getEquippedWeapon() != null) {
                Item weapon = player.getEquippedWeapon();
                player.setEquippedWeapon(null);
                player.getBackpack().addItem(weapon);
                addMessage("Unequipped " + weapon.getName() + ".");
                endTurn();
                return true;
            }
            return false;
        }

        Item newWeapon = player.getBackpack().removeWeapon(index);
        if (newWeapon != null) {
            Item oldWeapon = player.getEquippedWeapon();
            player.setEquippedWeapon(newWeapon);

            if (oldWeapon != null) {
                // Drop old weapon to adjacent cell on the ground
                Position dropPos = findAdjacentFreeFloor(level, player.getPosition());
                if (dropPos != null) {
                    oldWeapon.setPosition(dropPos);
                    Room room = level.getRoomContaining(dropPos);
                    if (room != null) {
                        room.getItems().add(oldWeapon);
                    }
                    addMessage("Dropped " + oldWeapon.getName() + " on the floor.");
                } else {
                    player.getBackpack().addItem(oldWeapon);
                }
            }

            addMessage("Equipped " + newWeapon.getName() + " (+" + newWeapon.getValue() + " STR)!");
            endTurn();
            return true;
        }
        return false;
    }

    private void endTurn() {
        Character player = session.getPlayer();
        Level level = session.getCurrentLevel();

        player.tickTurn();

        List<String> battleLog = new ArrayList<>();
        enemyAI.processEnemyTurns(level, player, session.getStats(), battleLog);
        for (String msg : battleLog) {
            addMessage(msg);
        }

        if (!player.isAlive()) {
            session.setGameOver(true);
            addMessage("GAME OVER! You were slain in the dungeon.");
        }

        FogOfWarCalculator.updateVisibility(level, player, visibilityMap);
    }

    private Position findAdjacentFreeFloor(Level level, Position center) {
        int[] dxs = {0, 0, -1, 1, -1, 1, -1, 1};
        int[] dys = {-1, 1, 0, 0, -1, -1, 1, 1};

        for (int i = 0; i < 8; i++) {
            Position adj = center.translate(dxs[i], dys[i]);
            if (level.isWalkable(adj) && level.getItemAt(adj) == null && level.getEnemyAt(adj) == null) {
                return adj;
            }
        }
        return center;
    }

    public void addMessage(String msg) {
        recentMessages.add(msg);
        if (recentMessages.size() > 5) {
            recentMessages.remove(0);
        }
        session.setStatusMessage(msg);
    }

    public GameSession getSession() {
        return session;
    }

    public FogOfWarCalculator.VisibilityMap getVisibilityMap() {
        return visibilityMap;
    }

    public List<String> getRecentMessages() {
        return recentMessages;
    }
}

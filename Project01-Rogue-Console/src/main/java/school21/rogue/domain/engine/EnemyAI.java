package school21.rogue.domain.engine;

import school21.rogue.domain.model.*;
import school21.rogue.domain.model.Character;

import java.util.*;

public class EnemyAI {
    private final Random random;
    private final CombatCalculator combatCalculator;

    public EnemyAI(CombatCalculator combatCalculator) {
        this(combatCalculator, new Random());
    }

    public EnemyAI(CombatCalculator combatCalculator, Random random) {
        this.combatCalculator = combatCalculator;
        this.random = random;
    }

    public void processEnemyTurns(Level level, Character player, GameStats stats, List<String> battleLog) {
        for (Room room : level.getRooms()) {
            List<Enemy> enemies = new ArrayList<>(room.getEnemies());
            for (Enemy enemy : enemies) {
                if (!enemy.isAlive()) continue;

                processSingleEnemy(enemy, room, level, player, stats, battleLog);
                if (!player.isAlive()) {
                    return; // Player defeated
                }
            }
        }
    }

    private void processSingleEnemy(Enemy enemy, Room room, Level level, Character player, GameStats stats, List<String> battleLog) {
        Position enemyPos = enemy.getPosition();
        Position playerPos = player.getPosition();

        // Mimic logic
        if (enemy.getType() == EnemyType.MIMIC && !enemy.isAwakened()) {
            if (enemyPos.manhattanDistanceTo(playerPos) <= 1) {
                enemy.setAwakened(true);
                battleLog.add("The suspicious item reveals itself as a MIMIC!");
            } else {
                return; // Stays still disguised
            }
        }

        // Ghost periodic invisibility toggle
        if (enemy.getType() == EnemyType.GHOST) {
            if (random.nextInt(4) == 0) {
                enemy.setInvisible(!enemy.isInvisible());
            }
        }

        // Ogre rest check: if resting, skips movement turn
        if (enemy.getType() == EnemyType.OGRE && enemy.isResting()) {
            enemy.setResting(false);
            enemy.setGuaranteedCounter(true);
            return;
        }

        double distance = enemyPos.distanceTo(playerPos);
        boolean shouldPursue = distance <= enemy.getHostility();

        if (shouldPursue) {
            // If already adjacent to player, attack directly
            if (enemyPos.manhattanDistanceTo(playerPos) == 1) {
                CombatCalculator.AttackResult res = combatCalculator.enemyAttacksPlayer(enemy, player, stats);
                battleLog.add(res.getMessage());
                return;
            }

            // Shortest path BFS toward player
            List<Position> path = findPath(level, enemyPos, playerPos);
            if (path != null && path.size() > 1) {
                Position nextStep = path.get(1);

                // Check if next step is player -> attack
                if (nextStep.equals(playerPos)) {
                    CombatCalculator.AttackResult res = combatCalculator.enemyAttacksPlayer(enemy, player, stats);
                    battleLog.add(res.getMessage());
                    return;
                }

                // If cell is free from other enemies, move
                if (isCellFreeForEnemy(level, nextStep)) {
                    enemy.setPosition(nextStep);

                    // Ogre moves 2 cells at a time!
                    if (enemy.getType() == EnemyType.OGRE && path.size() > 2) {
                        Position secondStep = path.get(2);
                        if (secondStep.equals(playerPos)) {
                            CombatCalculator.AttackResult res = combatCalculator.enemyAttacksPlayer(enemy, player, stats);
                            battleLog.add(res.getMessage());
                        } else if (isCellFreeForEnemy(level, secondStep)) {
                            enemy.setPosition(secondStep);
                        }
                    }
                    return;
                }
            }
        }

        // Not pursuing or no path available -> apply specific movement pattern
        applyPatternMovement(enemy, room, level);
    }

    private void applyPatternMovement(Enemy enemy, Room room, Level level) {
        switch (enemy.getType()) {
            case GHOST -> {
                // Constantly teleports around the room
                if (random.nextInt(3) == 0) {
                    Position freePos = findRandomRoomFloor(room, level);
                    if (freePos != null) {
                        enemy.setPosition(freePos);
                    }
                }
            }
            case OGRE -> {
                // Moves 2 cells in room
                int[] dxs = {0, 0, -2, 2};
                int[] dys = {-2, 2, 0, 0};
                int dir = random.nextInt(4);
                Position newPos = enemy.getPosition().translate(dxs[dir], dys[dir]);
                if (room.isInsideFloor(newPos) && isCellFreeForEnemy(level, newPos)) {
                    enemy.setPosition(newPos);
                }
            }
            case SNAKE_MAGE -> {
                // Moves diagonally, bouncing off walls
                Position cur = enemy.getPosition();
                Position next = cur.translate(enemy.getDiagonalDx(), enemy.getDiagonalDy());

                if (room.isInsideFloor(next) && isCellFreeForEnemy(level, next)) {
                    enemy.setPosition(next);
                } else {
                    // Bounce
                    if (random.nextBoolean()) {
                        enemy.setDiagonalDx(-enemy.getDiagonalDx());
                    } else {
                        enemy.setDiagonalDy(-enemy.getDiagonalDy());
                    }
                    Position bounced = cur.translate(enemy.getDiagonalDx(), enemy.getDiagonalDy());
                    if (room.isInsideFloor(bounced) && isCellFreeForEnemy(level, bounced)) {
                        enemy.setPosition(bounced);
                    }
                }
            }
            case ZOMBIE, VAMPIRE, MIMIC -> {
                // Random wander by 1 cell
                int[] dxs = {0, 0, -1, 1};
                int[] dys = {-1, 1, 0, 0};
                int dir = random.nextInt(4);
                Position newPos = enemy.getPosition().translate(dxs[dir], dys[dir]);
                if (room.isInsideFloor(newPos) && isCellFreeForEnemy(level, newPos)) {
                    enemy.setPosition(newPos);
                }
            }
        }
    }

    private boolean isCellFreeForEnemy(Level level, Position pos) {
        if (!level.isWalkable(pos)) return false;
        if (level.getEnemyAt(pos) != null) return false;
        return true;
    }

    private Position findRandomRoomFloor(Room room, Level level) {
        int x1 = room.getTopLeft().getX() + 1;
        int x2 = room.getBottomRight().getX() - 1;
        int y1 = room.getTopLeft().getY() + 1;
        int y2 = room.getBottomRight().getY() - 1;

        if (x1 > x2 || y1 > y2) return null;

        for (int i = 0; i < 20; i++) {
            int rx = x1 + random.nextInt(x2 - x1 + 1);
            int ry = y1 + random.nextInt(y2 - y1 + 1);
            Position p = new Position(rx, ry);
            if (isCellFreeForEnemy(level, p)) {
                return p;
            }
        }
        return null;
    }

    /**
     * BFS shortest path from start to target
     */
    public List<Position> findPath(Level level, Position start, Position target) {
        Queue<Position> queue = new LinkedList<>();
        Map<Position, Position> prev = new HashMap<>();
        Set<Position> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        boolean found = false;
        int[] dxs = {0, 0, -1, 1};
        int[] dys = {-1, 1, 0, 0};

        while (!queue.isEmpty()) {
            Position cur = queue.poll();
            if (cur.equals(target)) {
                found = true;
                break;
            }

            for (int i = 0; i < 4; i++) {
                Position next = cur.translate(dxs[i], dys[i]);
                if (visited.contains(next)) continue;

                // Walkable or target (player)
                if (next.equals(target) || (level.isWalkable(next) && level.getEnemyAt(next) == null)) {
                    visited.add(next);
                    prev.put(next, cur);
                    queue.add(next);
                }
            }
        }

        if (!found) return null;

        List<Position> path = new ArrayList<>();
        Position curr = target;
        while (curr != null) {
            path.add(0, curr);
            curr = prev.get(curr);
        }

        return path;
    }
}

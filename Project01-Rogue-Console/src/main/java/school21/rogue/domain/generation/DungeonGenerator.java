package school21.rogue.domain.generation;

import school21.rogue.domain.engine.DynamicBalancer;
import school21.rogue.domain.model.*;
import school21.rogue.domain.model.Character;

import java.util.*;

public class DungeonGenerator {
    private static final int MAP_WIDTH = 80;
    private static final int MAP_HEIGHT = 24;
    private static final int SECTOR_COLS = 3;
    private static final int SECTOR_ROWS = 3;

    private final Random random;

    public DungeonGenerator() {
        this(new Random());
    }

    public DungeonGenerator(Random random) {
        this.random = random;
    }

    public Level generateLevel(int levelNumber, Character player, GameStats stats) {
        Level level;
        do {
            level = attemptGenerateLevel(levelNumber, player, stats);
        } while (!ConnectivityChecker.isLevelConnected(level) || !DoorKeyPlacer.validateNoSoftLock(level));

        return level;
    }

    private Level attemptGenerateLevel(int levelNumber, Character player, GameStats stats) {
        Level level = new Level(levelNumber);
        level.setWidth(MAP_WIDTH);
        level.setHeight(MAP_HEIGHT);

        int sectorWidth = MAP_WIDTH / SECTOR_COLS;
        int sectorHeight = MAP_HEIGHT / SECTOR_ROWS;

        // 1. Generate 9 rooms (one in each of the 3x3 sectors)
        List<Room> rooms = new ArrayList<>();
        Room[][] gridRooms = new Room[SECTOR_ROWS][SECTOR_COLS];

        int roomId = 0;
        for (int r = 0; r < SECTOR_ROWS; r++) {
            for (int c = 0; c < SECTOR_COLS; c++) {
                int sectorX = c * sectorWidth;
                int sectorY = r * sectorHeight;

                // Margins inside sector
                int minW = 6;
                int maxW = sectorWidth - 4;
                int roomW = minW + random.nextInt(Math.max(1, maxW - minW + 1));

                int minH = 4;
                int maxH = sectorHeight - 2;
                int roomH = minH + random.nextInt(Math.max(1, maxH - minH + 1));

                int roomX = sectorX + 1 + random.nextInt(Math.max(1, sectorWidth - roomW - 1));
                int roomY = sectorY + 1 + random.nextInt(Math.max(1, sectorHeight - roomH - 1));

                Position topLeft = new Position(roomX, roomY);
                Position bottomRight = new Position(roomX + roomW - 1, roomY + roomH - 1);

                Room room = new Room(roomId, r * 3 + c, topLeft, bottomRight);
                rooms.add(room);
                gridRooms[r][c] = room;
                roomId++;
            }
        }
        level.setRooms(rooms);

        // 2. Generate Spanning Tree + random extra edges between adjacent sectors
        List<Edge> allPossibleEdges = new ArrayList<>();
        for (int r = 0; r < SECTOR_ROWS; r++) {
            for (int c = 0; c < SECTOR_COLS; c++) {
                if (c + 1 < SECTOR_COLS) {
                    allPossibleEdges.add(new Edge(gridRooms[r][c], gridRooms[r][c + 1]));
                }
                if (r + 1 < SECTOR_ROWS) {
                    allPossibleEdges.add(new Edge(gridRooms[r][c], gridRooms[r + 1][c]));
                }
            }
        }

        Collections.shuffle(allPossibleEdges, random);

        // Kruskal's algorithm to form a spanning tree connecting all 9 rooms
        DisjointSet ds = new DisjointSet(rooms.size());
        List<Edge> selectedEdges = new ArrayList<>();

        for (Edge edge : allPossibleEdges) {
            if (ds.union(edge.roomA.getId(), edge.roomB.getId())) {
                selectedEdges.add(edge);
            }
        }

        // Add 1-2 random additional cycles for more interesting dungeon layout
        for (Edge edge : allPossibleEdges) {
            if (!selectedEdges.contains(edge) && random.nextDouble() < 0.35) {
                selectedEdges.add(edge);
            }
        }

        // 3. Build geometry of corridors and place doors
        List<Corridor> corridors = new ArrayList<>();
        for (Edge edge : selectedEdges) {
            Corridor corridor = createCorridorBetween(edge.roomA, edge.roomB);
            corridors.add(corridor);
        }
        level.setCorridors(corridors);

        // 4. Designate Start Room and End Room
        int startId = random.nextInt(rooms.size());
        int endId;
        do {
            endId = random.nextInt(rooms.size());
        } while (endId == startId);

        level.setStartRoomId(startId);
        level.setEndRoomId(endId);

        Room startRoom = level.getRoomById(startId);
        Room endRoom = level.getRoomById(endId);

        // Exit block 'E' inside end room
        Position exitPos = getRandomFloorPosition(endRoom);
        level.setExitPosition(exitPos);

        // 5. Populate rooms with Enemies and Items
        double diffMult = DynamicBalancer.getEffectiveDifficultyMultiplier(levelNumber, player, stats);
        int bonusHealing = DynamicBalancer.getBonusHealingItems(player, stats);

        populateLevelEntities(level, startId, diffMult, bonusHealing, levelNumber);

        // 6. Place DOOM-style colored doors and keys with zero-softlock verification (Task 6)
        DoorKeyPlacer.placeKeysAndDoors(level, random);

        return level;
    }

    private void populateLevelEntities(Level level, int startRoomId, double diffMult, int bonusHealing, int levelNumber) {
        for (Room room : level.getRooms()) {
            if (room.getId() == startRoomId) {
                // Guaranteed NO enemies in start room
                continue;
            }

            // Number of enemies scales with level
            int enemyCount = 1 + (levelNumber > 7 ? 1 : 0) + (levelNumber > 14 ? 1 : 0);
            if (random.nextDouble() < 0.4) {
                enemyCount++;
            }

            for (int e = 0; e < enemyCount; e++) {
                Position enemyPos = getRandomFloorPosition(room);
                if (enemyPos != null && !enemyPos.equals(level.getExitPosition()) && level.getEnemyAt(enemyPos) == null) {
                    Enemy enemy = createRandomEnemy(enemyPos, room.getId(), levelNumber, diffMult);
                    room.getEnemies().add(enemy);
                }
            }

            // Items in room
            int itemCount = (random.nextDouble() < (0.8 - Math.min(0.5, levelNumber * 0.02))) ? 1 : 0;
            for (int i = 0; i < itemCount; i++) {
                Position itemPos = getRandomFloorPosition(room);
                if (itemPos != null && !itemPos.equals(level.getExitPosition()) && level.getItemAt(itemPos) == null) {
                    Item item = createRandomItem(itemPos, levelNumber);
                    room.getItems().add(item);
                }
            }
        }

        // Add bonus healing items if player is struggling (Task 7)
        for (int b = 0; b < bonusHealing; b++) {
            List<Room> nonStartRooms = new ArrayList<>(level.getRooms());
            nonStartRooms.removeIf(r -> r.getId() == startRoomId);
            Room r = nonStartRooms.get(random.nextInt(nonStartRooms.size()));
            Position pos = getRandomFloorPosition(r);
            if (pos != null && level.getItemAt(pos) == null) {
                r.getItems().add(Item.createFood("Emergency Rations", 15, pos));
            }
        }
    }

    private Enemy createRandomEnemy(Position pos, int roomId, int levelNumber, double diffMult) {
        // As level increases, stronger enemies appear
        List<EnemyType> pool = new ArrayList<>();
        pool.add(EnemyType.ZOMBIE);
        if (levelNumber >= 2) pool.add(EnemyType.GHOST);
        if (levelNumber >= 4) pool.add(EnemyType.VAMPIRE);
        if (levelNumber >= 6) pool.add(EnemyType.OGRE);
        if (levelNumber >= 8) pool.add(EnemyType.SNAKE_MAGE);
        if (levelNumber >= 3) pool.add(EnemyType.MIMIC); // Task 8

        EnemyType type = pool.get(random.nextInt(pool.size()));
        return switch (type) {
            case ZOMBIE -> Enemy.createZombie(pos, roomId, diffMult);
            case VAMPIRE -> Enemy.createVampire(pos, roomId, diffMult);
            case GHOST -> Enemy.createGhost(pos, roomId, diffMult);
            case OGRE -> Enemy.createOgre(pos, roomId, diffMult);
            case SNAKE_MAGE -> Enemy.createSnakeMage(pos, roomId, diffMult);
            case MIMIC -> {
                ItemType disguise = random.nextBoolean() ? ItemType.FOOD : ItemType.WEAPON;
                yield Enemy.createMimic(pos, roomId, disguise, diffMult);
            }
        };
    }

    private Item createRandomItem(Position pos, int levelNumber) {
        int roll = random.nextInt(100);
        if (roll < 35) {
            // Food
            int heal = 10 + random.nextInt(15);
            return Item.createFood("Rations", heal, pos);
        } else if (roll < 60) {
            // Elixir (temporary)
            StatType[] stats = {StatType.HEALTH, StatType.AGILITY, StatType.STRENGTH, StatType.MAX_HEALTH};
            StatType stat = stats[random.nextInt(stats.length)];
            int boost = 3 + random.nextInt(5);
            int duration = 15 + random.nextInt(15);
            return Item.createElixir("Elixir of " + stat, stat, boost, duration, pos);
        } else if (roll < 80) {
            // Scroll (permanent)
            StatType[] stats = {StatType.MAX_HEALTH, StatType.AGILITY, StatType.STRENGTH};
            StatType stat = stats[random.nextInt(stats.length)];
            int boost = 2 + random.nextInt(4);
            return Item.createScroll("Scroll of " + stat, stat, boost, pos);
        } else {
            // Weapon
            int str = 2 + (levelNumber / 3) + random.nextInt(4);
            String[] names = {"Dagger", "Short Sword", "Broadsword", "Mace", "Battleaxe"};
            String name = names[Math.min(names.length - 1, levelNumber / 4)] + " (+" + str + ")";
            return Item.createWeapon(name, str, pos);
        }
    }

    public Position getRandomFloorPosition(Room room) {
        int x1 = room.getTopLeft().getX() + 1;
        int x2 = room.getBottomRight().getX() - 1;
        int y1 = room.getTopLeft().getY() + 1;
        int y2 = room.getBottomRight().getY() - 1;

        if (x1 > x2 || y1 > y2) return null;

        int rx = x1 + random.nextInt(x2 - x1 + 1);
        int ry = y1 + random.nextInt(y2 - y1 + 1);
        return new Position(rx, ry);
    }

    private Corridor createCorridorBetween(Room rA, Room rB) {
        // Choose doors on facing sides
        Position doorA;
        Position doorB;

        boolean horizontal = Math.abs(rA.getSector() % 3 - rB.getSector() % 3) > 0;

        if (horizontal) {
            // A is left, B is right (or vice versa)
            Room left = rA.getTopLeft().getX() < rB.getTopLeft().getX() ? rA : rB;
            Room right = left == rA ? rB : rA;

            int doorY1 = left.getTopLeft().getY() + 1 + random.nextInt(Math.max(1, left.getHeight() - 2));
            int doorY2 = right.getTopLeft().getY() + 1 + random.nextInt(Math.max(1, right.getHeight() - 2));

            doorA = new Position(left.getBottomRight().getX(), doorY1);
            doorB = new Position(right.getTopLeft().getX(), doorY2);

            left.getDoors().add(new Door(doorA, null, true));
            right.getDoors().add(new Door(doorB, null, true));
        } else {
            // A is above, B is below
            Room top = rA.getTopLeft().getY() < rB.getTopLeft().getY() ? rA : rB;
            Room bot = top == rA ? rB : rA;

            int doorX1 = top.getTopLeft().getX() + 1 + random.nextInt(Math.max(1, top.getWidth() - 2));
            int doorX2 = bot.getTopLeft().getX() + 1 + random.nextInt(Math.max(1, bot.getWidth() - 2));

            doorA = new Position(doorX1, top.getBottomRight().getY());
            doorB = new Position(doorX2, bot.getTopLeft().getY());

            top.getDoors().add(new Door(doorA, null, true));
            bot.getDoors().add(new Door(doorB, null, true));
        }

        // Generate orthogonal path between doorA and doorB
        List<Position> path = new ArrayList<>();
        path.add(doorA);

        int midX = (doorA.getX() + doorB.getX()) / 2;
        int midY = (doorA.getY() + doorB.getY()) / 2;

        if (horizontal) {
            // Move X to midX, then Y to doorB.y, then X to doorB.x
            for (int x = Math.min(doorA.getX(), midX); x <= Math.max(doorA.getX(), midX); x++) {
                path.add(new Position(x, doorA.getY()));
            }
            for (int y = Math.min(doorA.getY(), doorB.getY()); y <= Math.max(doorA.getY(), doorB.getY()); y++) {
                path.add(new Position(midX, y));
            }
            for (int x = Math.min(midX, doorB.getX()); x <= Math.max(midX, doorB.getX()); x++) {
                path.add(new Position(x, doorB.getY()));
            }
        } else {
            // Move Y to midY, then X to doorB.x, then Y to doorB.y
            for (int y = Math.min(doorA.getY(), midY); y <= Math.max(doorA.getY(), midY); y++) {
                path.add(new Position(doorA.getX(), y));
            }
            for (int x = Math.min(doorA.getX(), doorB.getX()); x <= Math.max(doorA.getX(), doorB.getX()); x++) {
                path.add(new Position(x, midY));
            }
            for (int y = Math.min(midY, doorB.getY()); y <= Math.max(midY, doorB.getY()); y++) {
                path.add(new Position(doorB.getX(), y));
            }
        }

        path.add(doorB);
        return new Corridor(rA.getId(), rB.getId(), path);
    }

    private static class Edge {
        Room roomA;
        Room roomB;

        Edge(Room roomA, Room roomB) {
            this.roomA = roomA;
            this.roomB = roomB;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return (roomA.getId() == edge.roomA.getId() && roomB.getId() == edge.roomB.getId()) ||
                    (roomA.getId() == edge.roomB.getId() && roomB.getId() == edge.roomA.getId());
        }

        @Override
        public int hashCode() {
            return roomA.getId() + roomB.getId();
        }
    }

    private static class DisjointSet {
        int[] parent;

        DisjointSet(int size) {
            parent = new int[size];
            for (int i = 0; i < size; i++) parent[i] = i;
        }

        int find(int i) {
            if (parent[i] == i) return i;
            return parent[i] = find(parent[i]);
        }

        boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                parent[rootI] = rootJ;
                return true;
            }
            return false;
        }
    }
}

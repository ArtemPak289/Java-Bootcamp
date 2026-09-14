package school21.rogue.domain.generation;

import school21.rogue.domain.model.*;

import java.util.*;

public class DoorKeyPlacer {

    /**
     * Attempts to place colored keys and locks on doors while guaranteeing through
     * modified BFS that the player can never be soft-locked.
     */
    public static void placeKeysAndDoors(Level level, Random random) {
        if (level.getLevelNumber() < 2) {
            // Level 1 has standard unlocked doors
            return;
        }

        // We can place 1 or 2 colored doors depending on level depth
        int numKeys = Math.min(2, 1 + (level.getLevelNumber() > 5 ? 1 : 0));
        KeyColor[] availableColors = KeyColor.values();

        for (int k = 0; k < numKeys && k < availableColors.length; k++) {
            KeyColor color = availableColors[k];
            tryPlaceDoorAndKey(level, color, random);
        }
    }

    private static boolean tryPlaceDoorAndKey(Level level, KeyColor color, Random random) {
        // Find all corridor connections and their doors
        List<DoorCandidate> candidates = new ArrayList<>();

        for (Room room : level.getRooms()) {
            for (Door door : room.getDoors()) {
                if (!door.isLocked()) {
                    candidates.add(new DoorCandidate(room.getId(), door));
                }
            }
        }

        Collections.shuffle(candidates, random);

        for (DoorCandidate candidate : candidates) {
            Door door = candidate.door;
            // Tentatively lock this door with the key color
            door.setKeyColor(color);
            door.setOpen(false);

            // Find rooms reachable from start room without opening this locked door
            Set<Integer> reachableRoomsBeforeLock = getReachableRoomsWithoutKey(level, level.getStartRoomId(), color);

            // Key must be placed in a room that is reachable before unlocking, and not in the end room
            List<Room> validKeyRooms = new ArrayList<>();
            for (int rId : reachableRoomsBeforeLock) {
                Room r = level.getRoomById(rId);
                if (r != null && rId != level.getEndRoomId()) {
                    validKeyRooms.add(r);
                }
            }

            if (!validKeyRooms.isEmpty()) {
                Room keyRoom = validKeyRooms.get(random.nextInt(validKeyRooms.size()));
                Position keyPos = findFreeFloorPosition(keyRoom, level, random);
                if (keyPos != null) {
                    Item keyItem = Item.createKey(color, keyPos);
                    keyRoom.getItems().add(keyItem);

                    // Validate that the exit can be reached with full key progression
                    if (validateNoSoftLock(level)) {
                        return true; // Successfully placed and verified
                    }

                    // Revert key placement if validation fails
                    keyRoom.getItems().remove(keyItem);
                }
            }

            // Revert door lock
            door.setKeyColor(null);
            door.setOpen(true);
        }

        return false;
    }

    /**
     * Modified BFS to verify player can reach the exit room collecting keys along the way.
     */
    public static boolean validateNoSoftLock(Level level) {
        int startId = level.getStartRoomId();
        int endId = level.getEndRoomId();

        Set<Integer> visitedRooms = new HashSet<>();
        Set<KeyColor> collectedKeys = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        // Corridors blocked waiting for a key: Map of Corridor -> KeyColor required
        Map<Corridor, KeyColor> blockedCorridors = new HashMap<>();

        queue.add(startId);
        visitedRooms.add(startId);

        boolean progress = true;
        while (progress) {
            progress = false;

            // Process room exploration
            while (!queue.isEmpty()) {
                int currentId = queue.poll();
                Room room = level.getRoomById(currentId);

                // Collect any keys present in this room
                if (room != null) {
                    for (Item item : room.getItems()) {
                        if (item.getType() == ItemType.KEY && item.getKeyColor() != null) {
                            if (collectedKeys.add(item.getKeyColor())) {
                                progress = true; // Newly acquired key
                            }
                        }
                    }
                }

                // Check neighbors through corridors
                for (Corridor corridor : level.getCorridors()) {
                    int neighborId = -1;
                    if (corridor.getRoomAId() == currentId) neighborId = corridor.getRoomBId();
                    else if (corridor.getRoomBId() == currentId) neighborId = corridor.getRoomAId();

                    if (neighborId != -1 && !visitedRooms.contains(neighborId)) {
                        // Check if any door along this connection is locked
                        KeyColor requiredKey = getRequiredKeyForConnection(level, currentId, neighborId);
                        if (requiredKey == null || collectedKeys.contains(requiredKey)) {
                            visitedRooms.add(neighborId);
                            queue.add(neighborId);
                            progress = true;
                        } else {
                            blockedCorridors.put(corridor, requiredKey);
                        }
                    }
                }
            }

            // Check if any blocked corridors can now be unlocked with newly collected keys
            Iterator<Map.Entry<Corridor, KeyColor>> it = blockedCorridors.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Corridor, KeyColor> entry = it.next();
                Corridor corridor = entry.getKey();
                KeyColor requiredKey = entry.getValue();

                if (collectedKeys.contains(requiredKey)) {
                    int targetA = corridor.getRoomAId();
                    int targetB = corridor.getRoomBId();

                    if (visitedRooms.contains(targetA) && !visitedRooms.contains(targetB)) {
                        visitedRooms.add(targetB);
                        queue.add(targetB);
                        it.remove();
                        progress = true;
                    } else if (visitedRooms.contains(targetB) && !visitedRooms.contains(targetA)) {
                        visitedRooms.add(targetA);
                        queue.add(targetA);
                        it.remove();
                        progress = true;
                    }
                }
            }
        }

        return visitedRooms.contains(endId);
    }

    private static Set<Integer> getReachableRoomsWithoutKey(Level level, int startId, KeyColor lockedColor) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.add(startId);
        visited.add(startId);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (Corridor corridor : level.getCorridors()) {
                int neighbor = -1;
                if (corridor.getRoomAId() == current) neighbor = corridor.getRoomBId();
                else if (corridor.getRoomBId() == current) neighbor = corridor.getRoomAId();

                if (neighbor != -1 && !visited.contains(neighbor)) {
                    KeyColor req = getRequiredKeyForConnection(level, current, neighbor);
                    if (req == null || req != lockedColor) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        return visited;
    }

    private static KeyColor getRequiredKeyForConnection(Level level, int roomAId, int roomBId) {
        Room rA = level.getRoomById(roomAId);
        Room rB = level.getRoomById(roomBId);
        if (rA == null || rB == null) return null;

        for (Door d : rA.getDoors()) {
            if (d.isLocked()) return d.getKeyColor();
        }
        for (Door d : rB.getDoors()) {
            if (d.isLocked()) return d.getKeyColor();
        }
        return null;
    }

    private static Position findFreeFloorPosition(Room room, Level level, Random random) {
        int x1 = room.getTopLeft().getX() + 1;
        int x2 = room.getBottomRight().getX() - 1;
        int y1 = room.getTopLeft().getY() + 1;
        int y2 = room.getBottomRight().getY() - 1;

        if (x1 > x2 || y1 > y2) return null;

        for (int attempts = 0; attempts < 30; attempts++) {
            int rx = x1 + random.nextInt(x2 - x1 + 1);
            int ry = y1 + random.nextInt(y2 - y1 + 1);
            Position pos = new Position(rx, ry);
            if (level.getItemAt(pos) == null && level.getEnemyAt(pos) == null
                    && !pos.equals(level.getExitPosition())) {
                return pos;
            }
        }
        return null;
    }

    private static class DoorCandidate {
        int roomId;
        Door door;

        DoorCandidate(int roomId, Door door) {
            this.roomId = roomId;
            this.door = door;
        }
    }
}

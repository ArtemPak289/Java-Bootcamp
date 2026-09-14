package school21.rogue.domain.generation;

import school21.rogue.domain.model.Corridor;
import school21.rogue.domain.model.Level;
import school21.rogue.domain.model.Room;

import java.util.*;

public class ConnectivityChecker {

    /**
     * Verifies that all rooms in the level are connected via corridors.
     */
    public static boolean isLevelConnected(Level level) {
        if (level == null || level.getRooms() == null || level.getRooms().isEmpty()) {
            return false;
        }

        List<Room> rooms = level.getRooms();
        int roomCount = rooms.size();

        // Build adjacency list based on corridors
        Map<Integer, Set<Integer>> adj = new HashMap<>();
        for (Room r : rooms) {
            adj.put(r.getId(), new HashSet<>());
        }

        for (Corridor c : level.getCorridors()) {
            adj.get(c.getRoomAId()).add(c.getRoomBId());
            adj.get(c.getRoomBId()).add(c.getRoomAId());
        }

        // BFS traversal from room 0
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        int startRoomId = rooms.get(0).getId();
        queue.add(startRoomId);
        visited.add(startRoomId);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int neighbor : adj.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visited.size() == roomCount;
    }
}

package school21.rogue;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import school21.rogue.domain.generation.ConnectivityChecker;
import school21.rogue.domain.generation.DungeonGenerator;
import school21.rogue.domain.model.Character;
import school21.rogue.domain.model.GameStats;
import school21.rogue.domain.model.Level;
import school21.rogue.domain.model.Room;

import static org.junit.jupiter.api.Assertions.*;

public class DungeonGenerationTest {

    @RepeatedTest(10)
    void testLevelGenerationStructure() {
        DungeonGenerator generator = new DungeonGenerator();
        Character player = new Character();
        GameStats stats = new GameStats();

        Level level = generator.generateLevel(1, player, stats);

        assertNotNull(level);
        assertEquals(9, level.getRooms().size(), "Each level must contain exactly 9 rooms");
        assertFalse(level.getCorridors().isEmpty(), "Level must have connecting corridors");

        // Connectivity check
        assertTrue(ConnectivityChecker.isLevelConnected(level), "Level graph must be fully connected");

        // Start and end room checks
        assertNotEquals(level.getStartRoomId(), level.getEndRoomId(), "Start and end rooms must be distinct");
        Room startRoom = level.getRoomById(level.getStartRoomId());
        assertNotNull(startRoom);
        assertTrue(startRoom.getEnemies().isEmpty(), "Start room must not have enemies");

        Room endRoom = level.getRoomById(level.getEndRoomId());
        assertNotNull(endRoom);
        assertNotNull(level.getExitPosition(), "Level must have an exit block position");
        assertTrue(endRoom.isInsideFloor(level.getExitPosition()) || endRoom.contains(level.getExitPosition()),
                "Exit block must be inside end room");
    }
}

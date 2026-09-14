package school21.rogue;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import school21.rogue.domain.generation.DoorKeyPlacer;
import school21.rogue.domain.generation.DungeonGenerator;
import school21.rogue.domain.model.Character;
import school21.rogue.domain.model.GameStats;
import school21.rogue.domain.model.Level;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DoorKeyReachabilityTest {

    @RepeatedTest(15)
    void testNoSoftLocksInLevelsWithDoorsAndKeys() {
        DungeonGenerator generator = new DungeonGenerator();
        Character player = new Character();
        GameStats stats = new GameStats();

        // Level 5 typically generates locked doors and keys
        Level level = generator.generateLevel(5, player, stats);

        // Verify with modified BFS reachability algorithm that player can reach exit without softlock
        boolean valid = DoorKeyPlacer.validateNoSoftLock(level);
        assertTrue(valid, "Modified BFS must guarantee that keys are accessible before doors (no softlocks)");
    }
}

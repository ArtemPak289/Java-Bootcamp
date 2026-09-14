package school21.rogue;

import org.junit.jupiter.api.Test;
import school21.rogue.domain.engine.CombatCalculator;
import school21.rogue.domain.engine.EnemyAI;
import school21.rogue.domain.model.*;
import school21.rogue.domain.model.Character;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyAITest {

    @Test
    void testPursuitWhenPlayerWithinHostility() {
        CombatCalculator combat = new CombatCalculator();
        EnemyAI ai = new EnemyAI(combat);

        Level level = new Level(1);
        Room room = new Room(0, 0, new Position(0, 0), new Position(10, 10));
        level.getRooms().add(room);

        Character player = new Character(30, 30, 10, 10, new Position(5, 5));
        Enemy zombie = Enemy.createZombie(new Position(3, 5), 0, 1.0);
        zombie.setHostility(10); // can easily see player
        room.getEnemies().add(zombie);

        GameStats stats = new GameStats();
        List<String> log = new ArrayList<>();

        Position initialPos = zombie.getPosition();
        ai.processEnemyTurns(level, player, stats, log);

        // Zombie should have moved towards player (x increased from 3 towards 5)
        assertTrue(zombie.getPosition().getX() > initialPos.getX(),
                "Zombie should step towards player when within hostility range");
    }

    @Test
    void testMimicAwakensWhenPlayerAdjacent() {
        CombatCalculator combat = new CombatCalculator();
        EnemyAI ai = new EnemyAI(combat);

        Level level = new Level(1);
        Room room = new Room(0, 0, new Position(0, 0), new Position(10, 10));
        level.getRooms().add(room);

        Character player = new Character(30, 30, 10, 10, new Position(5, 5));
        Enemy mimic = Enemy.createMimic(new Position(4, 5), 0, ItemType.FOOD, 1.0); // adjacent
        assertFalse(mimic.isAwakened());
        room.getEnemies().add(mimic);

        GameStats stats = new GameStats();
        List<String> log = new ArrayList<>();

        ai.processEnemyTurns(level, player, stats, log);
        assertTrue(mimic.isAwakened(), "Mimic must awaken when player is adjacent");
    }
}

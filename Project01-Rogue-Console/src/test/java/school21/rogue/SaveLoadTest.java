package school21.rogue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import school21.rogue.datalayer.SaveLoadManager;
import school21.rogue.datalayer.ScoreEntry;
import school21.rogue.datalayer.ScoreboardManager;
import school21.rogue.domain.engine.GameEngine;
import school21.rogue.domain.model.*;
import school21.rogue.domain.model.Character;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaveLoadTest {

    @Test
    void testSaveAndLoadSession(@TempDir Path tempDir) throws IOException {
        File saveFile = tempDir.resolve("savegame_test.json").toFile();
        SaveLoadManager manager = new SaveLoadManager(saveFile.getAbsolutePath());

        GameEngine engine = new GameEngine();
        GameSession original = engine.startNewGame();
        original.getPlayer().takeDamage(5);
        original.getPlayer().applyBuff(new Buff(StatType.STRENGTH, 5, 10));
        original.getStats().addTreasures(150);
        original.getStats().incrementEnemiesDefeated();

        manager.saveSession(original);
        assertTrue(manager.hasSavedGame());

        GameSession loaded = manager.loadSession();
        assertNotNull(loaded);
        assertEquals(original.getCurrentLevelNumber(), loaded.getCurrentLevelNumber());
        assertEquals(original.getPlayer().getCurrentHealth(), loaded.getPlayer().getCurrentHealth());
        assertEquals(original.getPlayer().getEffectiveStrength(), loaded.getPlayer().getEffectiveStrength());
        assertEquals(original.getStats().getTreasures(), loaded.getStats().getTreasures());
        assertEquals(original.getStats().getEnemiesDefeated(), loaded.getStats().getEnemiesDefeated());
        assertEquals(9, loaded.getCurrentLevel().getRooms().size());

        manager.deleteSavedGame();
        assertFalse(manager.hasSavedGame());
    }

    @Test
    void testScoreboardPersistenceAndSorting(@TempDir Path tempDir) {
        File scoreFile = tempDir.resolve("scoreboard_test.json").toFile();
        ScoreboardManager manager = new ScoreboardManager(scoreFile.getAbsolutePath());

        manager.recordScore(new ScoreEntry(50, 3, 4, 1, 0, 1, 10, 2, 8, 120, "DEFEAT"));
        manager.recordScore(new ScoreEntry(200, 10, 15, 5, 2, 3, 45, 5, 20, 450, "DEFEAT"));
        manager.recordScore(new ScoreEntry(500, 21, 35, 10, 8, 7, 90, 8, 40, 990, "VICTORY"));

        List<ScoreEntry> scores = manager.getScores();
        assertEquals(3, scores.size());
        assertEquals(500, scores.get(0).getTreasures(), "Scores must be sorted descending by treasures");
        assertEquals(200, scores.get(1).getTreasures());
        assertEquals(50, scores.get(2).getTreasures());
    }
}

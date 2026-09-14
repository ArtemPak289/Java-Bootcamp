package school21.rogue;

import org.junit.jupiter.api.Test;
import school21.rogue.domain.engine.DynamicBalancer;
import school21.rogue.domain.model.Character;
import school21.rogue.domain.model.GameStats;
import school21.rogue.domain.model.Position;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicBalancerTest {

    @Test
    void testDifficultyIncreasesWhenPlayerDominates() {
        Character player = new Character(30, 30, 15, 15, new Position(0, 0));
        GameStats stats = new GameStats();
        stats.setHitsDealt(20);
        stats.setHitsTaken(2);

        double factor = DynamicBalancer.calculateBalanceFactor(player, stats);
        assertTrue(factor > 1.0, "Balance factor should increase when player dominates");
    }

    @Test
    void testDifficultyDecreasesAndHealsWhenPlayerStruggles() {
        Character player = new Character(30, 5, 5, 5, new Position(0, 0)); // low health
        GameStats stats = new GameStats();
        stats.setHitsDealt(3);
        stats.setHitsTaken(15);

        double factor = DynamicBalancer.calculateBalanceFactor(player, stats);
        assertTrue(factor < 1.0, "Balance factor should decrease when player is struggling");

        int bonusHealing = DynamicBalancer.getBonusHealingItems(player, stats);
        assertTrue(bonusHealing > 0, "Struggling player should receive bonus healing items");
    }
}

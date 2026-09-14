package school21.rogue.domain.engine;

import school21.rogue.domain.model.Character;
import school21.rogue.domain.model.GameStats;

public class DynamicBalancer {

    /**
     * Calculates adaptive balance multiplier based on player's current health and combat history.
     * > 1.0 means player is doing well -> make it more challenging.
     * < 1.0 means player is struggling -> ease difficulty and offer more healing.
     */
    public static double calculateBalanceFactor(Character player, GameStats stats) {
        if (player == null || stats == null) {
            return 1.0;
        }

        double healthRatio = (double) player.getCurrentHealth() / Math.max(1, player.getEffectiveMaxHealth());
        double combatRatio = stats.getHitsTaken() == 0 ? 2.0 : (double) stats.getHitsDealt() / stats.getHitsTaken();

        double factor = 1.0;

        if (healthRatio > 0.8 && combatRatio > 1.5) {
            // Player is easily dominating
            factor = 1.15;
        } else if (healthRatio > 0.6 && combatRatio > 1.0) {
            factor = 1.05;
        } else if (healthRatio < 0.35 || combatRatio < 0.7) {
            // Player is heavily struggling
            factor = 0.8;
        } else if (healthRatio < 0.5) {
            factor = 0.9;
        }

        return factor;
    }

    public static double getEffectiveDifficultyMultiplier(int levelNumber, Character player, GameStats stats) {
        double levelBase = 1.0 + (levelNumber - 1) * 0.07;
        double balance = calculateBalanceFactor(player, stats);
        return Math.max(0.7, levelBase * balance);
    }

    public static int getBonusHealingItems(Character player, GameStats stats) {
        double balance = calculateBalanceFactor(player, stats);
        if (balance <= 0.8) {
            return 2; // Extra 2 food items for struggling player
        } else if (balance <= 0.9) {
            return 1;
        }
        return 0;
    }
}

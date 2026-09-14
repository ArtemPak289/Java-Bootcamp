package school21.rogue.domain.engine;

import school21.rogue.domain.model.Character;
import school21.rogue.domain.model.Enemy;
import school21.rogue.domain.model.EnemyType;
import school21.rogue.domain.model.GameStats;

import java.util.Random;

public class CombatCalculator {
    private final Random random;

    public CombatCalculator() {
        this(new Random());
    }

    public CombatCalculator(Random random) {
        this.random = random;
    }

    public static class AttackResult {
        private final boolean hit;
        private final int damage;
        private final boolean targetDied;
        private final String message;

        public AttackResult(boolean hit, int damage, boolean targetDied, String message) {
            this.hit = hit;
            this.damage = damage;
            this.targetDied = targetDied;
            this.message = message;
        }

        public boolean isHit() {
            return hit;
        }

        public int getDamage() {
            return damage;
        }

        public boolean isTargetDied() {
            return targetDied;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * Player attacks an enemy
     */
    public AttackResult playerAttacksEnemy(Character player, Enemy enemy, GameStats stats) {
        // Special case: Vampire - first strike on vampire ALWAYS misses!
        if (enemy.getType() == EnemyType.VAMPIRE && !enemy.isWasFirstAttacked()) {
            enemy.setWasFirstAttacked(true);
            if (stats != null) {
                stats.incrementHitsMissed();
            }
            return new AttackResult(false, 0, false, "You swing at the Vampire, but it vanishes in a blur! (First attack always misses)");
        }

        // Stage 1: Hit calculation based on agility of attacker vs defender
        int attackerAgility = player.getEffectiveAgility();
        int defenderAgility = enemy.getAgility();
        boolean hit = calculateHit(attackerAgility, defenderAgility);

        if (!hit) {
            if (stats != null) {
                stats.incrementHitsMissed();
            }
            return new AttackResult(false, 0, false, "You attacked the " + enemy.getType().getDisplayName() + " but missed!");
        }

        // Stage 2: Damage calculation
        int baseStrength = player.getEffectiveStrength();
        int damage = calculateDamage(baseStrength);

        // Stage 3: Apply damage
        enemy.takeDamage(damage);
        if (stats != null) {
            stats.incrementHitsDealt();
        }

        boolean died = !enemy.isAlive();
        if (died && stats != null) {
            stats.incrementEnemiesDefeated();
        }

        String msg = "You struck the " + enemy.getType().getDisplayName() + " for " + damage + " damage!";
        if (died) {
            msg += " The " + enemy.getType().getDisplayName() + " was slain!";
        }

        return new AttackResult(true, damage, died, msg);
    }

    /**
     * Enemy attacks the player
     */
    public AttackResult enemyAttacksPlayer(Enemy enemy, Character player, GameStats stats) {
        // Special case: Ogre rest cycle
        if (enemy.getType() == EnemyType.OGRE) {
            if (enemy.isResting()) {
                enemy.setResting(false);
                enemy.setGuaranteedCounter(true);
                return new AttackResult(false, 0, false, "The Ogre is catching its breath and preparing a counterattack!");
            }
        }

        // Stage 1: Hit calculation
        boolean hit;
        if (enemy.getType() == EnemyType.OGRE && enemy.isGuaranteedCounter()) {
            hit = true; // Guaranteed counterattack after resting!
            enemy.setGuaranteedCounter(false);
        } else {
            hit = calculateHit(enemy.getAgility(), player.getEffectiveAgility());
        }

        if (!hit) {
            return new AttackResult(false, 0, false, "The " + enemy.getType().getDisplayName() + " attacked you but missed!");
        }

        // Stage 2: Damage calculation
        int damage = calculateDamage(enemy.getStrength());

        // Stage 3: Apply damage
        player.takeDamage(damage);
        if (stats != null) {
            stats.incrementHitsTaken();
        }

        // Special enemy on-hit effects
        StringBuilder effectMsg = new StringBuilder();

        // Vampire drains player's max health!
        if (enemy.getType() == EnemyType.VAMPIRE) {
            int drain = 1 + random.nextInt(2);
            player.reduceMaxHealth(drain);
            effectMsg.append(" (Vampire drained ").append(drain).append(" from your Max HP!)");
        }

        // Snake Mage puts player to sleep
        if (enemy.getType() == EnemyType.SNAKE_MAGE) {
            if (random.nextDouble() < 0.40) {
                player.setSleepTurns(1);
                effectMsg.append(" (The Snake Mage casts a sleep hex! You are asleep for 1 turn!)");
            }
        }

        // Ogre rests for a turn after attacking
        if (enemy.getType() == EnemyType.OGRE) {
            enemy.setResting(true);
        }

        boolean playerDied = !player.isAlive();
        String msg = "The " + enemy.getType().getDisplayName() + " hits you for " + damage + " damage!" + effectMsg;
        if (playerDied) {
            msg += " You have succumbed to your injuries...";
        }

        return new AttackResult(true, damage, playerDied, msg);
    }

    private boolean calculateHit(int attackerAgility, int defenderAgility) {
        // Probability formula based on agility difference
        // Base 70% chance modified by (attacker - defender) * 3%
        double chance = 0.70 + (attackerAgility - defenderAgility) * 0.03;
        chance = Math.clamp(chance, 0.20, 0.95);
        return random.nextDouble() < chance;
    }

    private int calculateDamage(int strength) {
        // Base damage = strength / 2 + random variation
        int minDmg = Math.max(1, strength / 2);
        int variation = Math.max(1, strength / 2);
        return minDmg + random.nextInt(variation + 1);
    }
}

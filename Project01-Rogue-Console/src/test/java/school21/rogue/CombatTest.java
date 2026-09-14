package school21.rogue;

import org.junit.jupiter.api.Test;
import school21.rogue.domain.engine.CombatCalculator;
import school21.rogue.domain.model.*;
import school21.rogue.domain.model.Character;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CombatTest {

    @Test
    void testVampireFirstAttackAlwaysMisses() {
        CombatCalculator calc = new CombatCalculator(new Random(42));
        Character player = new Character();
        Enemy vampire = Enemy.createVampire(new Position(5, 5), 0, 1.0);
        GameStats stats = new GameStats();

        assertFalse(vampire.isWasFirstAttacked());

        // First attack must miss!
        CombatCalculator.AttackResult first = calc.playerAttacksEnemy(player, vampire, stats);
        assertFalse(first.isHit(), "First attack on Vampire must always miss");
        assertTrue(vampire.isWasFirstAttacked());

        // Subsequent attack has standard hit check
        boolean hitOccurred = false;
        for (int i = 0; i < 20; i++) {
            CombatCalculator.AttackResult next = calc.playerAttacksEnemy(player, vampire, stats);
            if (next.isHit()) {
                hitOccurred = true;
                break;
            }
        }
        assertTrue(hitOccurred, "Subsequent attacks on Vampire should be able to hit");
    }

    @Test
    void testVampireDrainsMaxHealth() {
        CombatCalculator calc = new CombatCalculator(new Random(100));
        Character player = new Character(30, 30, 2, 10, new Position(0, 0)); // low agility to get hit
        Enemy vampire = Enemy.createVampire(new Position(1, 0), 0, 1.0);
        vampire.setAgility(50); // high agility to guarantee hit
        GameStats stats = new GameStats();

        int originalMaxHp = player.getMaxHealth();

        // Let vampire attack
        calc.enemyAttacksPlayer(vampire, player, stats);

        assertTrue(player.getMaxHealth() < originalMaxHp, "Vampire hit must reduce player's max health");
    }

    @Test
    void testOgreRestAndCounterattackCycle() {
        CombatCalculator calc = new CombatCalculator(new Random(1));
        Character player = new Character(50, 50, 20, 10, new Position(0, 0));
        Enemy ogre = Enemy.createOgre(new Position(1, 0), 0, 1.0);
        ogre.setStrength(10);
        GameStats stats = new GameStats();

        // 1. First attack: hits and triggers resting state
        CombatCalculator.AttackResult r1 = calc.enemyAttacksPlayer(ogre, player, stats);
        if (r1.isHit()) {
            assertTrue(ogre.isResting(), "Ogre must rest after attack");

            // 2. Next turn: rests and prepares guaranteed counter
            CombatCalculator.AttackResult r2 = calc.enemyAttacksPlayer(ogre, player, stats);
            assertFalse(r2.isHit(), "Ogre does not hit while resting");
            assertTrue(ogre.isGuaranteedCounter(), "Ogre prepares guaranteed counter");

            // 3. Third turn: guaranteed counterattack hits
            CombatCalculator.AttackResult r3 = calc.enemyAttacksPlayer(ogre, player, stats);
            assertTrue(r3.isHit(), "Guaranteed counterattack must hit");
        }
    }

    @Test
    void testWeaponBonusDamage() {
        CombatCalculator calc = new CombatCalculator(new Random(42));
        Character player = new Character(50, 50, 50, 10, new Position(0, 0));
        Item sword = Item.createWeapon("Excalibur", 20, null);
        player.setEquippedWeapon(sword);

        assertEquals(30, player.getEffectiveStrength(), "Equipped weapon should add to effective strength");
    }

    @Test
    void testTreasureCalculationOnEnemyDefeat() {
        Enemy zombie = Enemy.createZombie(new Position(0, 0), 0, 1.0);
        int treasure = zombie.calculateDroppedTreasure();
        assertTrue(treasure > 0, "Defeated enemy must drop treasures");
    }
}

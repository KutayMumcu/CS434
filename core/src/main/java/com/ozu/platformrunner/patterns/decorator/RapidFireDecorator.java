package com.ozu.platformrunner.patterns.decorator;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.GameConstants;
import com.ozu.platformrunner.entities.Bullet;
import com.ozu.platformrunner.entities.Enemy;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.patterns.strategy.AttackStrategy;

/**
 * RapidFireDecorator - Increases attack speed by reducing cooldown
 *
 * This decorator can be stacked with DoubleShotDecorator:
 * new RapidFireDecorator(new DoubleShotDecorator(new BowStrategy()))
 * Result: Fast double shots!
 */
public class RapidFireDecorator extends StrategyDecorator {

    private float cooldownTimer = 0f;
    private final float cooldownDuration = GameConstants.RAPID_FIRE_COOLDOWN;

    public RapidFireDecorator(AttackStrategy strategyToWrap) {
        super(strategyToWrap);
    }

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        // Check cooldown - only attack if cooldown expired
        if (cooldownTimer <= 0) {
            // Execute wrapped strategy's attack
            super.attack(player, enemies, bullets);

            System.out.println(">>> RAPID FIRE ACTIVE! Next shot ready in " + cooldownDuration + "s <<<");

            // Reset cooldown
            cooldownTimer = cooldownDuration;
        }
    }

    /**
     * Update cooldown timer - must be called every frame
     * Call this from Player or GameScreen update loop
     */
    public void update(float delta) {
        if (cooldownTimer > 0) {
            cooldownTimer -= delta;
        }
    }

    public float getCooldownTimer() {
        return cooldownTimer;
    }

    public boolean isReady() {
        return cooldownTimer <= 0;
    }
}

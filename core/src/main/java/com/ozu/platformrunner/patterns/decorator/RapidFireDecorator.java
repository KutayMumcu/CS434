package com.ozu.platformrunner.patterns.decorator;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.patterns.strategy.AttackStrategy;
import com.ozu.platformrunner.utils.GameConstants;

public class RapidFireDecorator extends StrategyDecorator {
    private float cooldownTimer = 0f;
    private final float cooldownDuration = GameConstants.RAPID_FIRE_COOLDOWN;

    public RapidFireDecorator(AttackStrategy strategyToWrap) {
        super(strategyToWrap);
    }

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        if (cooldownTimer <= 0) {
            super.attack(player, enemies, bullets);
            cooldownTimer = cooldownDuration;
        }
    }

    public void update(float delta) {
        if (cooldownTimer > 0) cooldownTimer -= delta;
    }

    public boolean isReady() { return cooldownTimer <= 0; }
}

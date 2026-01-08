package com.ozu.platformrunner.patterns.decorator;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.managers.PoolManager;
import com.ozu.platformrunner.patterns.strategy.AttackStrategy;
import com.ozu.platformrunner.utils.GameConstants;

public class DoubleShotDecorator extends StrategyDecorator {

    public DoubleShotDecorator(AttackStrategy strategyToWrap) {
        super(strategyToWrap);
    }

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        // Execute original strategy attack
        super.attack(player, enemies, bullets);

        // Add extra projectile(s) based on constants
        for (int i = 1; i < GameConstants.DOUBLE_SHOT_COUNT; i++) {
            Bullet b = PoolManager.getInstance().bulletPool.obtain();
            int dir = player.getFacingDirection();

            float startX = (dir == 1)
                ? player.getBounds().x + GameConstants.BOW_OFFSET_X_RIGHT
                : player.getBounds().x - GameConstants.BOW_OFFSET_X_LEFT;

            // Offset the extra bullet vertically
            float startY = player.getBounds().y + GameConstants.BOW_OFFSET_Y + (i * GameConstants.DOUBLE_SHOT_OFFSET);

            b.init(startX, startY, dir);
            bullets.add(b);
        }
    }
}

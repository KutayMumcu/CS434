package com.ozu.platformrunner.patterns.decorator;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.managers.PoolManager;
import com.ozu.platformrunner.patterns.strategy.AttackStrategy;

public class DoubleShotDecorator extends StrategyDecorator {

    public DoubleShotDecorator(AttackStrategy strategyToWrap) {
        super(strategyToWrap);
    }

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        // Original attack
        super.attack(player, enemies, bullets);

        // Extra Bullet (Higher position)
        Bullet b = PoolManager.getInstance().bulletPool.obtain();
        int dir = player.getFacingDirection();
        float startX = (dir == 1) ? player.getBounds().x + 25 : player.getBounds().x - 10;

        b.init(startX, player.getBounds().y + 30, dir);
        bullets.add(b);
    }
}

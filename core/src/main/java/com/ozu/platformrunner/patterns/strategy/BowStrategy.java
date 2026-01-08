package com.ozu.platformrunner.patterns.strategy;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.managers.PoolManager;

public class BowStrategy implements AttackStrategy {

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        Bullet b = PoolManager.getInstance().bulletPool.obtain();

        int dir = player.getFacingDirection();
        float startX = (dir == 1) ? player.getBounds().x + 25 : player.getBounds().x - 10;

        b.init(startX, player.getBounds().y + 10, dir);
        bullets.add(b);
    }

    @Override
    public WeaponType getWeaponType() {
        return WeaponType.RANGED;
    }
}

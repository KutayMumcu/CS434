package com.ozu.platformrunner.patterns.strategy;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.managers.PoolManager;
import com.ozu.platformrunner.utils.GameConstants;

public class BowStrategy implements AttackStrategy {

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        Bullet b = PoolManager.getInstance().bulletPool.obtain();

        int dir = player.getFacingDirection();

        float startX = (dir == 1)
            ? player.getBounds().x + GameConstants.BOW_OFFSET_X_RIGHT
            : player.getBounds().x - GameConstants.BOW_OFFSET_X_LEFT;

        float startY = player.getBounds().y + GameConstants.BOW_OFFSET_Y;

        b.init(startX, startY, dir);
        bullets.add(b);
    }

    @Override
    public WeaponType getWeaponType() {
        return WeaponType.RANGED;
    }
}

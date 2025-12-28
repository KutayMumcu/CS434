package com.ozu.platformrunner.patterns.strategy;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Bullet;
import com.ozu.platformrunner.entities.Enemy;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.managers.PoolManager; // Import ekle

public class BowStrategy implements AttackStrategy {

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        Bullet b = PoolManager.getInstance().bulletPool.obtain();

        int dir = player.getFacingDirection();

        // Merminin çıkış noktasını yöne göre ayarla (Sağdaysa sağdan, soldaysa soldan çıksın)
        float startX = (dir == 1) ? player.getBounds().x + 25 : player.getBounds().x - 10;

        b.init(startX, player.getBounds().y + 10, dir);
        bullets.add(b);
    }
}

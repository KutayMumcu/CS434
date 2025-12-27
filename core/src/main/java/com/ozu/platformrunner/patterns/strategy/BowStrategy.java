package com.ozu.platformrunner.patterns.strategy;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Bullet;
import com.ozu.platformrunner.entities.Enemy;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.managers.PoolManager; // Import ekle

public class BowStrategy implements AttackStrategy {

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        // 1. Havuzdan bir mermi al (Obtain)
        Bullet b = PoolManager.getInstance().bulletPool.obtain();

        // 2. Mermiyi hazırla (Init)
        // Yönü oyuncunun son hareket yönüne göre yapmak lazım ama şimdilik sağ (1)
        b.init(player.getBounds().x + 20, player.getBounds().y + 10, 1);

        // 3. Aktif listeye ekle (Ekranda çizilmesi için)
        bullets.add(b);

        System.out.println("Havuzdan mermi çekildi. Aktif mermi sayısı: " + bullets.size);
    }
}

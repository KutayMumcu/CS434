package com.ozu.platformrunner.patterns.strategy;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.utils.GameConstants;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.Player;

public class SwordStrategy implements AttackStrategy {
    private float range = 80f;

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        System.out.println("Kılıç sallandı!");

        for (Enemy enemy : enemies) {
            // Mesafe hesapla
            float distance = Math.abs(player.getBounds().x - enemy.getBounds().x);
            float yDistance = Math.abs(player.getBounds().y - enemy.getBounds().y);

            // Eğer menzildeyse VE aynı yükseklikteyse (yDistance kontrolü önemli)
            if (distance < range && yDistance < 50) {

                // Hasar ver
                enemy.takeDamage(GameConstants.SWORD_DAMAGE);

                // Vurulan düşmanı biraz geri it (Geri tepme efekti)
                if (enemy.getBounds().x > player.getBounds().x) {
                    enemy.getBounds().x += 30;
                } else {
                    enemy.getBounds().x -= 30;
                }

                System.out.println("Düşmana hasar verildi! Kalan can: " + enemy.getCurrentHealth());
            }
        }
    }
}

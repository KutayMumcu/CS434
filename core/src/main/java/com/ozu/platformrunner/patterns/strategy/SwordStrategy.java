package com.ozu.platformrunner.patterns.strategy;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Bullet;
import com.ozu.platformrunner.entities.Enemy;
import com.ozu.platformrunner.entities.Player;

public class SwordStrategy implements AttackStrategy {
    private float range = 50f; // Kılıç menzili

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        System.out.println("Kılıç sallandı!"); // Ses efekti burada çalınabilir

        for (Enemy enemy : enemies) {
            // Basit mesafe kontrolü (Hitbox çakışması veya mesafe)
            float distance = Math.abs(player.getBounds().x - enemy.getBounds().x);

            // Eğer düşman menzildeyse ve Y ekseninde yakınsa
            if (distance < range && Math.abs(player.getBounds().y - enemy.getBounds().y) < 50) {
                System.out.println("Düşmana kılıç isabet etti!");
                // İleride: enemy.takeDamage(10);
                // Şimdilik görsel olarak düşmanı uzağa atalım ki vurduğumuzu anlayalım
                enemy.getBounds().x += 100;
            }
        }
    }
}

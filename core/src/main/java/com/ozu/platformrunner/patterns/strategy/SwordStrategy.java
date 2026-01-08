package com.ozu.platformrunner.patterns.strategy;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.utils.GameConstants;

public class SwordStrategy implements AttackStrategy {
    private float range = 80f;

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        for (Enemy enemy : enemies) {
            float distance = Math.abs(player.getBounds().x - enemy.getBounds().x);
            float yDistance = Math.abs(player.getBounds().y - enemy.getBounds().y);

            if (distance < range && yDistance < 50) {
                enemy.takeDamage(GameConstants.SWORD_DAMAGE);

                // Apply simple knockback
                if (enemy.getBounds().x > player.getBounds().x) {
                    enemy.getBounds().x += 30;
                } else {
                    enemy.getBounds().x -= 30;
                }
            }
        }
    }

    @Override
    public WeaponType getWeaponType() {
        return WeaponType.MELEE;
    }
}

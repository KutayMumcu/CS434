package com.ozu.platformrunner.patterns.strategy;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.Player;

public interface AttackStrategy {
    void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets);
}

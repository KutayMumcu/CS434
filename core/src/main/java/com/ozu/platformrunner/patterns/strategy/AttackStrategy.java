package com.ozu.platformrunner.patterns.strategy;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Bullet;
import com.ozu.platformrunner.entities.Enemy;
import com.ozu.platformrunner.entities.Player;

public interface AttackStrategy {
    // Saldırıyı gerçekleştir
    void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets);
}

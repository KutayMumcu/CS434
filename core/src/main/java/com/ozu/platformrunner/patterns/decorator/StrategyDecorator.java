package com.ozu.platformrunner.patterns.decorator;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.patterns.strategy.AttackStrategy;

public abstract class StrategyDecorator implements AttackStrategy {
    protected AttackStrategy wrappedStrategy;

    public StrategyDecorator(AttackStrategy strategyToWrap) {
        this.wrappedStrategy = strategyToWrap;
    }

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        wrappedStrategy.attack(player, enemies, bullets);
    }
}

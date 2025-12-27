package com.ozu.platformrunner.patterns.decorator;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Bullet;
import com.ozu.platformrunner.entities.Enemy;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.managers.PoolManager;
import com.ozu.platformrunner.patterns.strategy.AttackStrategy;

public class DoubleShotDecorator extends StrategyDecorator {

    public DoubleShotDecorator(AttackStrategy strategyToWrap) {
        super(strategyToWrap);
    }

    @Override
    public void attack(Player player, Array<Enemy> enemies, Array<Bullet> bullets) {
        // 1. Önce normal saldırıyı yap (Tek ok at veya kılıç salla)
        super.attack(player, enemies, bullets);

        System.out.println(">>> ÇİFT ATIŞ GÜCÜ DEVREDE! <<<");

        // 2. Ekstra bir mermi daha oluştur (Hafif yukarıdan)
        // Not: Burada stratejinin tipini kontrol etmeden direkt mermi ekliyoruz.
        // Kılıç kullansa bile büyülü bir mermi çıkması "PowerUp" mantığına uyar.
        Bullet b = PoolManager.getInstance().bulletPool.obtain();

        // Oyuncunun biraz yukarısından ikinci mermiyi at
        b.init(player.getBounds().x + 20, player.getBounds().y + 30, 1);
        bullets.add(b);
    }
}

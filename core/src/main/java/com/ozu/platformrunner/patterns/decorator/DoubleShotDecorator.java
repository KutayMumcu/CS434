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
        // 1. Önce normal saldırıyı yap (Asıl strateji yönü kendi içinde hallediyor olmalı)
        super.attack(player, enemies, bullets);

        System.out.println(">>> ÇİFT ATIŞ GÜCÜ DEVREDE! <<<");

        // 2. Ekstra bir mermi daha oluştur (Hafif yukarıdan)
        Bullet b = PoolManager.getInstance().bulletPool.obtain();

        // Oyuncunun yönünü al
        int dir = player.getFacingDirection();

        // Merminin çıkış noktasını yöne göre ayarla
        // Yöne göre biraz ileri veya geri alıyoruz ki karakterin içinden çıkmasın
        float startX = (dir == 1) ? player.getBounds().x + 25 : player.getBounds().x - 10;

        // Mermiyi oyuncunun biraz yukarısından (y + 30) fırlat
        b.init(startX, player.getBounds().y + 30, dir);

        bullets.add(b);
    }
}

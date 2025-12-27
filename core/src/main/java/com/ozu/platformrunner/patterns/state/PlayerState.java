package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.entities.Player;

public interface PlayerState {
    // Duruma girerken çalışacak (Örn: Zıplama sesi çal)
    void enter(Player player);

    // Her karede çalışacak mantık (Örn: Durum geçişlerini kontrol et)
    void update(Player player, float delta);

    // Durumdan çıkarken çalışacak (Gerekirse)
    void exit(Player player);
}

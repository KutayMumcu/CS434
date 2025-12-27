package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.entities.Player;

public class FallingState implements PlayerState {
    @Override
    public void enter(Player player) {
        // "Düşme Animasyonu" başlat
    }

    @Override
    public void update(Player player, float delta) {
        // Eğer yere değdiyse -> DURMAYA GEÇ (Veya hızı varsa koşmaya)
        if (player.isOnGround()) {
            if (Math.abs(player.getVelocity().x) > 0.1f) {
                player.changeState(new RunningState());
            } else {
                player.changeState(new IdleState());
            }
        }
    }

    @Override
    public void exit(Player player) {}
}

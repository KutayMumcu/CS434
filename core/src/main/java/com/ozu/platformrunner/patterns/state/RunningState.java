package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.entities.Player;

public class RunningState implements PlayerState {
    @Override
    public void enter(Player player) {
        // "Koşma Animasyonu"nu başlat
    }

    @Override
    public void update(Player player, float delta) {
        // Hız 0 olduysa -> DURMAYA GEÇ
        if (Math.abs(player.getVelocity().x) < 0.1f) {
            player.changeState(new IdleState());
        }

        // Zıpladıysa -> ZIPLAMAYA GEÇ
        if (player.getVelocity().y > 0) {
            player.changeState(new JumpingState());
        }

        // Platform bittiyse -> DÜŞMEYE GEÇ
        if (!player.isOnGround()) {
            player.changeState(new FallingState());
        }
    }

    @Override
    public void exit(Player player) {}
}

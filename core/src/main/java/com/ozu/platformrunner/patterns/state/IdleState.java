package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.entities.Player;

public class IdleState implements PlayerState {
    @Override
    public void enter(Player player) {
        // İleride burada "Durma Animasyonu"nu başlatacağız
        player.stopX(); // Emin olmak için hızı sıfırla
    }

    @Override
    public void update(Player player, float delta) {
        // Eğer hız (X ekseni) 0 değilse -> KOŞMAYA GEÇ
        if (Math.abs(player.getVelocity().x) > 0.1f) {
            player.changeState(new RunningState());
        }

        // Eğer Y hızı pozitifse (zıpladıysa) -> ZIPLAMAYA GEÇ
        if (player.getVelocity().y > 0) {
            player.changeState(new JumpingState());
        }

        // Eğer yer ayağının altından kaydıysa -> DÜŞMEYE GEÇ
        if (!player.isOnGround()) {
            player.changeState(new FallingState());
        }
    }

    @Override
    public void exit(Player player) {}
}

package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.entities.Player;

public class JumpingState implements PlayerState {
    @Override
    public void enter(Player player) {
        // "Zıplama Animasyonu" başlat
        player.setOnGround(false); // Artık havada
    }

    @Override
    public void update(Player player, float delta) {
        // Yerçekimi karakteri aşağı çekmeye başladığında (Hız negatif olunca) -> DÜŞMEYE GEÇ
        if (player.getVelocity().y < 0) {
            player.changeState(new FallingState());
        }
    }

    @Override
    public void exit(Player player) {}
}

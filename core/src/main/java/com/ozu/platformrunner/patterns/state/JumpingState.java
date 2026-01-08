package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.entities.Player;

public class JumpingState implements PlayerState {
    @Override
    public void enter(Player player) {
        player.setOnGround(false);
    }

    @Override
    public void update(Player player, float delta) {
        if (player.getVelocity().y < 0) {
            player.changeState(new FallingState());
        }
    }

    @Override
    public void exit(Player player) {}
}

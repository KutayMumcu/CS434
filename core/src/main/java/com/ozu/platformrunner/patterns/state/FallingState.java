package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.entities.Player;

public class FallingState implements PlayerState {
    @Override
    public void enter(Player player) {}

    @Override
    public void update(Player player, float delta) {
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

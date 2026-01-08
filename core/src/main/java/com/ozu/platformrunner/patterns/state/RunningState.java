package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.utils.GameConstants;

public class RunningState implements PlayerState {
    @Override
    public void enter(Player player) {}

    @Override
    public void update(Player player, float delta) {
        if (Math.abs(player.getVelocity().x) < GameConstants.VELOCITY_EPSILON) {
            player.changeState(new IdleState());
        } else if (player.getVelocity().y > 0) {
            player.changeState(new JumpingState());
        } else if (!player.isOnGround()) {
            player.changeState(new FallingState());
        }
    }

    @Override
    public void exit(Player player) {}
}

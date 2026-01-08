package com.ozu.platformrunner.entities.enemies;

import com.ozu.platformrunner.entities.Player;

public class ChasingEnemy extends Enemy {

    public ChasingEnemy(float x, float y) {
        super(x, y, 32, 32);
        this.speed = 90f;
    }

    @Override
    protected void moveBehavior(float delta, Player player) {
        int chaseDirection = (player.getBounds().x > bounds.x) ? 1 : -1;

        if (isEdgeAhead(chaseDirection, getPlatforms())) {
            if (onGround && jumpCooldownTimer <= 0) {
                jump();
                velocity.x = speed * chaseDirection;
            } else {
                velocity.x = 0;
                if (shouldJumpToPlayer(player, 150f, 30f)) {
                    jump();
                }
            }
        } else {
            velocity.x = speed * chaseDirection;

            float distanceToPlayer = Math.abs(player.getBounds().x - bounds.x);
            if (distanceToPlayer < 100f) {
                velocity.x *= 1.5f;
            }

            if (shouldJumpToPlayer(player, 150f, 30f)) {
                jump();
            }
        }
    }
}

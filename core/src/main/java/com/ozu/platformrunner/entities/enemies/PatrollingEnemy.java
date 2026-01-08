package com.ozu.platformrunner.entities.enemies;

import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.utils.GameConstants;

public class PatrollingEnemy extends Enemy {
    private float movementRange = 200f;
    private float startX;
    private int direction = 1;
    private float aggroRange = 300f;

    public PatrollingEnemy(float x, float y) {
        super(x, y, 32, 32, GameConstants.ENEMY_DEFAULT_HEALTH);
        this.startX = x;
        this.speed = 80f;
    }

    @Override
    public void moveBehavior(float delta, Player player) {
        float distanceToPlayer = Math.abs(player.getBounds().x - bounds.x);

        if (distanceToPlayer < aggroRange) {
            // Aggressive Mode
            int chaseDirection = (player.getBounds().x > bounds.x) ? 1 : -1;

            if (isEdgeAhead(chaseDirection, getPlatforms())) {
                if (onGround && jumpCooldownTimer <= 0) {
                    jump();
                    velocity.x = speed * 1.5f * chaseDirection;
                } else {
                    velocity.x = 0; // Stop to avoid falling
                }
            } else {
                velocity.x = speed * 1.5f * chaseDirection;
            }

            if (shouldJumpToPlayer(player, 200f, 30f)) {
                jump();
            }
        } else {
            // Patrol Mode
            if (isEdgeAhead(direction, getPlatforms())) {
                if (onGround && jumpCooldownTimer <= 0) {
                    jump();
                    velocity.x = speed * direction;
                } else {
                    direction *= -1; // Turn around
                    velocity.x = speed * direction;
                }
            } else {
                velocity.x = speed * direction;
            }

            // Patrol Boundaries
            if (bounds.x > startX + movementRange) direction = -1;
            else if (bounds.x < startX) direction = 1;
        }
    }

    public float getStartX() { return startX; }
    public void setStartX(float startX) { this.startX = startX; }
    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }
}

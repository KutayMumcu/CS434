package com.ozu.platformrunner.entities;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.managers.PoolManager;

public class BossEnemy extends Enemy {
    private float shootCooldown = 1.5f;
    private float shootCooldownTimer = 0f;
    private float shootRange = 500f;
    private float minDistance = 200f;
    private float patrolRange = 250f;
    private float startX;
    private int patrolDirection = 1;
    private static final float ARRIVAL_THRESHOLD = 10f;

    public BossEnemy(float x, float y) {
        super(x, y, 32, 32);
        this.maxHealth = 60;
        this.currentHealth = 60;
        this.speed = 50f;
        this.startX = x;
    }

    @Override
    protected void moveBehavior(float delta, Player player) {
        // Calculate distance to player
        float distanceToPlayer = Math.abs(player.getBounds().x - bounds.x);

        // If too close, back away
        if (distanceToPlayer < minDistance) {
            int backAwayDirection = (player.getBounds().x < bounds.x) ? 1 : -1;

            // Check edge before backing away
            if (isEdgeAhead(backAwayDirection, getPlatforms())) {
                // Edge behind! Try to jump over it while backing away
                if (onGround && jumpCooldownTimer <= 0) {
                    jump();
                    velocity.x = 40f * backAwayDirection;
                } else {
                    // Can't jump, stand ground
                    velocity.x = 0;
                }
            } else {
                // Safe to back away
                velocity.x = 40f * backAwayDirection;
            }
        }
        // If player in range, focus on shooting (minimal movement)
        else if (distanceToPlayer < shootRange) {
            velocity.x = velocity.x * 0.5f; // Slow down

            // Jump if player is significantly above (to maintain shooting angle)
            if (shouldJumpToPlayer(player, 300f, 50f)) {
                jump();
            }
        }
        // Otherwise, patrol
        else {
            // Check edge before patrolling
            if (isEdgeAhead(patrolDirection, getPlatforms())) {
                // Edge ahead! Try to jump over it
                if (onGround && jumpCooldownTimer <= 0) {
                    jump();
                    velocity.x = speed * patrolDirection;
                } else {
                    // Can't jump, turn around
                    patrolDirection *= -1;
                    velocity.x = speed * patrolDirection;
                }
            } else {
                velocity.x = speed * patrolDirection;
            }

            // Check if reached patrol boundaries
            float distanceFromStart = bounds.x - startX;

            if (distanceFromStart >= patrolRange) {
                patrolDirection = -1;
            } else if (distanceFromStart <= -patrolRange) {
                patrolDirection = 1;
            }
        }
    }

    public void updateAndShoot(float delta, Player player, Array<Platform> platforms, Array<Arrow> arrows) {
        // Update cooldown timer
        if (shootCooldownTimer > 0) {
            shootCooldownTimer -= delta;
        }

        // Regular enemy update
        update(delta, player, platforms);

        // Try to shoot if player is in range
        float distanceToPlayer = Math.abs(player.getBounds().x - bounds.x);
        if (distanceToPlayer < shootRange && canShoot()) {
            shootArrow(arrows, player);
        }
    }

    private boolean canShoot() {
        return shootCooldownTimer <= 0;
    }

    private void shootArrow(Array<Arrow> arrows, Player player) {
        // Get arrow from pool
        Arrow arrow = PoolManager.getInstance().arrowPool.obtain();

        // Calculate direction toward player
        int direction = (player.getBounds().x > bounds.x) ? 1 : -1;

        // Initialize arrow at boss position (slightly offset for visual)
        float arrowX = bounds.x + (direction > 0 ? 20 : -8);
        float arrowY = bounds.y + 12; // Center height

        arrow.init(arrowX, arrowY, direction);
        arrows.add(arrow);

        // Reset cooldown
        shootCooldownTimer = shootCooldown;
    }
}

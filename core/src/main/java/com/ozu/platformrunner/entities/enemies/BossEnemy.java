package com.ozu.platformrunner.entities.enemies;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.projectiles.Arrow;
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.managers.PoolManager;

public class BossEnemy extends Enemy {
    private float shootCooldown = 1.5f;
    private float shootCooldownTimer = 0f;
    private float shootRange = 500f;
    private float minDistance = 200f;
    private float patrolRange = 250f;
    private float startX;
    private int patrolDirection = 1;

    public BossEnemy(float x, float y) {
        super(x, y, 32, 32);
        this.maxHealth = 60;
        this.currentHealth = 60;
        this.speed = 50f;
        this.startX = x;
    }

    @Override
    protected void moveBehavior(float delta, Player player) {
        float distanceToPlayer = Math.abs(player.getBounds().x - bounds.x);

        if (distanceToPlayer < minDistance) {
            // Retreat
            int backAwayDirection = (player.getBounds().x < bounds.x) ? 1 : -1;

            if (isEdgeAhead(backAwayDirection, getPlatforms())) {
                if (onGround && jumpCooldownTimer <= 0) {
                    jump();
                    velocity.x = 40f * backAwayDirection;
                } else {
                    velocity.x = 0;
                }
            } else {
                velocity.x = 40f * backAwayDirection;
            }
        } else if (distanceToPlayer < shootRange) {
            // Combat Stance
            velocity.x *= 0.5f;
            if (shouldJumpToPlayer(player, 300f, 50f)) {
                jump();
            }
        } else {
            // Patrol
            if (isEdgeAhead(patrolDirection, getPlatforms())) {
                if (onGround && jumpCooldownTimer <= 0) {
                    jump();
                    velocity.x = speed * patrolDirection;
                } else {
                    patrolDirection *= -1;
                    velocity.x = speed * patrolDirection;
                }
            } else {
                velocity.x = speed * patrolDirection;
            }

            float distanceFromStart = bounds.x - startX;
            if (distanceFromStart >= patrolRange) patrolDirection = -1;
            else if (distanceFromStart <= -patrolRange) patrolDirection = 1;
        }
    }

    public void updateAndShoot(float delta, Player player, Array<Platform> platforms, Array<Arrow> arrows) {
        if (shootCooldownTimer > 0) shootCooldownTimer -= delta;

        update(delta, player, platforms);

        float distanceToPlayer = Math.abs(player.getBounds().x - bounds.x);
        if (distanceToPlayer < shootRange && shootCooldownTimer <= 0) {
            shootArrow(arrows, player);
        }
    }

    private void shootArrow(Array<Arrow> arrows, Player player) {
        Arrow arrow = PoolManager.getInstance().arrowPool.obtain();
        int direction = (player.getBounds().x > bounds.x) ? 1 : -1;

        float arrowX = bounds.x + (direction > 0 ? 20 : -8);
        float arrowY = bounds.y + 12;

        arrow.init(arrowX, arrowY, direction);
        arrows.add(arrow);
        shootCooldownTimer = shootCooldown;
    }
}

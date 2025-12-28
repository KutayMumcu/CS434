package com.ozu.platformrunner.entities;

public class ChasingEnemy extends Enemy {

    public ChasingEnemy(float x, float y) {
        super(x, y, 32, 32);
        this.speed = 90f; // Daha agresif ve hızlı
    }

    @Override
    protected void moveBehavior(float delta, Player player) {
        // Determine chase direction
        int chaseDirection = (player.getBounds().x > bounds.x) ? 1 : -1;

        // Check for edge before chasing
        if (isEdgeAhead(chaseDirection, getPlatforms())) {
            // Edge ahead! Try to jump over it while chasing
            if (onGround && jumpCooldownTimer <= 0) {
                jump();
                // Keep chasing aggressively while jumping
                velocity.x = speed * chaseDirection;
            } else {
                // Can't jump right now, but still try to jump if player is reachable
                velocity.x = 0;
                if (shouldJumpToPlayer(player, 150f, 30f)) {
                    jump();
                }
            }
        } else {
            // Safe to chase - go for it!
            velocity.x = speed * chaseDirection;

            // Oyuncu çok yakınsa daha da hızlan
            float distanceToPlayer = Math.abs(player.getBounds().x - bounds.x);
            if (distanceToPlayer < 100f) {
                velocity.x *= 1.5f; // %50 daha hızlı
            }

            // Jump if player is above and nearby (aggressive jumping)
            if (shouldJumpToPlayer(player, 150f, 30f)) {
                jump();
            }
        }
    }
}

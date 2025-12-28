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
            // Edge ahead! Stop to avoid falling
            velocity.x = 0;
            // But still try to jump if player is reachable
            if (shouldJumpToPlayer(player, 150f, 30f)) {
                jump();
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

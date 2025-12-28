package com.ozu.platformrunner.entities;

public class PatrollingEnemy extends Enemy {
    private float movementRange = 200f; // Ne kadar gidip dönecek
    private float startX;
    private int direction = 1; // 1: Sağ, -1: Sol
    private float aggroRange = 300f; // Player bu mesafeye girerse kovala

    public PatrollingEnemy(float x, float y) {
        super(x, y, 32, 32);
        this.startX = x;
        this.speed = 80f;
    }

    @Override
    public void moveBehavior(float delta, Player player) {
        // Oyuncuya olan mesafe
        float distanceToPlayer = Math.abs(player.getBounds().x - bounds.x);

        // AGRESIF MOD: Oyuncu yakınsa kovala
        if (distanceToPlayer < aggroRange) {
            int chaseDirection = (player.getBounds().x > bounds.x) ? 1 : -1;

            // Check for edge before chasing
            if (isEdgeAhead(chaseDirection, getPlatforms())) {
                // Edge ahead! Stop moving to avoid falling
                velocity.x = 0;
            } else {
                // Safe to chase
                velocity.x = speed * 1.5f * chaseDirection;
            }

            // Jump when chasing if player is above
            if (shouldJumpToPlayer(player, 200f, 30f)) {
                jump();
            }
        } else {
            // Normal devriye - check for edges
            if (isEdgeAhead(direction, getPlatforms())) {
                // Edge ahead! Turn around
                direction *= -1;
            }

            velocity.x = speed * direction;

            // Sınırlara geldiyse yön değiştir
            if (bounds.x > startX + movementRange) {
                direction = -1;
            } else if (bounds.x < startX) {
                direction = 1;
            }
        }
    }

    // Getters/setters for save/load
    public float getStartX() { return startX; }
    public void setStartX(float startX) { this.startX = startX; }
    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }
}

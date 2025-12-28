package com.ozu.platformrunner.entities;

public class ChasingEnemy extends Enemy {

    public ChasingEnemy(float x, float y) {
        super(x, y, 32, 32);
        this.speed = 90f; // Daha agresif ve hızlı
    }

    @Override
    protected void moveBehavior(float delta, Player player) {
        // Her zaman oyuncuyu kovala (agresif)
        if (player.getBounds().x > bounds.x) {
            velocity.x = speed;
        } else {
            velocity.x = -speed;
        }

        // Oyuncu çok yakınsa daha da hızlan
        float distanceToPlayer = Math.abs(player.getBounds().x - bounds.x);
        if (distanceToPlayer < 100f) {
            velocity.x *= 1.5f; // %50 daha hızlı
        }
    }
}

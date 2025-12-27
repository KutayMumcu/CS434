package com.ozu.platformrunner.entities;

public class ChasingEnemy extends Enemy {

    public ChasingEnemy(float x, float y) {
        super(x, y, 32, 32);
        this.speed = 60f; // Oyuncudan yavaş olsun ki kaçabilsin
    }

    @Override
    public void update(float delta, Player player) {
        // Oyuncu nerede? Ona doğru git.
        if (player.getBounds().x > bounds.x) {
            bounds.x += speed * delta; // Sağa git
        } else {
            bounds.x -= speed * delta; // Sola git
        }
    }
}

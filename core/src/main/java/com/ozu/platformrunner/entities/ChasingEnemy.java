package com.ozu.platformrunner.entities;

public class ChasingEnemy extends Enemy {

    public ChasingEnemy(float x, float y) {
        super(x, y, 32, 32);
        this.speed = 60f; // Oyuncudan yavaş olsun ki kaçabilsin
    }

    @Override
    protected void moveBehavior(float delta, Player player) {
        if (player.getBounds().x > bounds.x) {
            velocity.x = speed; // Sağa hız ver
        } else {
            velocity.x = -speed; // Sola hız ver
        }
    }
}

package com.ozu.platformrunner.entities;

public class PatrollingEnemy extends Enemy {
    private float movementRange = 200f; // Ne kadar gidip dönecek
    private float startX;
    private int direction = 1; // 1: Sağ, -1: Sol

    public PatrollingEnemy(float x, float y) {
        super(x, y, 32, 32);
        this.startX = x;
        this.speed = 80f; // Biraz daha yavaş
    }

    @Override
    public void moveBehavior(float delta, Player player) {
        // Basit devriye mantığı
        bounds.x += speed * direction * delta;

        // Sınırlara geldiyse yön değiştir
        if (bounds.x > startX + movementRange) {
            direction = -1;
        } else if (bounds.x < startX) {
            direction = 1;
        }
    }
}

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
            if (player.getBounds().x > bounds.x) {
                velocity.x = speed * 1.5f; // Kovalama modunda daha hızlı
            } else {
                velocity.x = -speed * 1.5f;
            }
        } else {
            // Normal devriye
            velocity.x = speed * direction;

            // Sınırlara geldiyse yön değiştir
            if (bounds.x > startX + movementRange) {
                direction = -1;
            } else if (bounds.x < startX) {
                direction = 1;
            }
        }
    }
}

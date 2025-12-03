package com.ozu.platformrunner;

// Player.java
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    // Fizik ve Hız Ayarları
    private static final float MOVE_SPEED = 200f; // Yatay hareket hızı
    private static final float JUMP_VELOCITY = 400f; // Zıplama gücü
    private static final float GRAVITY = -800f; // Yerçekimi ivmesi

    private final Rectangle bounds; // Karakterin çarpışma alanı
    private final Vector2 velocity; // Karakterin hızı (x ve y)
    private boolean onGround; // Karakter yerde mi?

    public Player(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        velocity = new Vector2(0, 0);
        onGround = false;
    }

    public void update(float delta) {
        // 1. Yerçekimini Uygula (Sadece havadaysa)
        if (!onGround) {
            velocity.y += GRAVITY * delta;
        }

        // 2. Pozisyonu Güncelle (Hız * Zaman)
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        // 3. Yere Çarpma Kontrolü (Hareketsiz sahnede basit bir sınır)
        if (bounds.y < 0) {
            bounds.y = 0;
            velocity.y = 0;
            onGround = true;
        }
    }

    // --- Hareket Metotları ---

    public void moveX(int direction) { // direction: -1 (sol), 1 (sağ)
        velocity.x = direction * MOVE_SPEED;
    }

    public void stopX() {
        velocity.x = 0;
    }

    public void jump() {
        if (onGround) {
            velocity.y = JUMP_VELOCITY;
            onGround = false; // Zıpladıktan sonra havadayız
        }
    }

    // --- Erişim Metotları ---
    public Rectangle getBounds() { return bounds; }
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }
    public Vector2 getVelocity() { return velocity; }
}

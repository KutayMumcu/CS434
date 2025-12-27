package com.ozu.platformrunner.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ozu.platformrunner.patterns.state.IdleState;
import com.ozu.platformrunner.patterns.state.PlayerState;

public class Player {
    // Fizik Sabitleri
    private static final float MOVE_SPEED = 200f;
    private static final float JUMP_VELOCITY = 400f;
    private static final float GRAVITY = -800f;

    private final Rectangle bounds;
    private final Vector2 velocity;
    private boolean onGround;

    // --- YENİ: STATE PATTERN DEĞİŞKENİ ---
    private PlayerState currentState;

    public Player(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        velocity = new Vector2(0, 0);
        onGround = false;

        // Başlangıç durumu: Idle (Duruyor)
        currentState = new IdleState();
        currentState.enter(this);
    }

    public void update(float delta) {
        // 1. Duruma özgü mantığı çalıştır (State Update)
        currentState.update(this, delta);

        // 2. Fizik Motoru (Burası ortak kalabilir)
        if (!onGround) {
            velocity.y += GRAVITY * delta;
        }

        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        // Alt sınır kontrolü (Geçici zemin)
        if (bounds.y < 0) {
            bounds.y = 0;
            velocity.y = 0;
            onGround = true; // State bunu algılayıp Falling -> Idle yapacak
        }
    }

    // --- STATE GEÇİŞ METODU ---
    public void changeState(PlayerState newState) {
        // Önceki durumdan çık
        currentState.exit(this);

        // Durumu değiştir
        this.currentState = newState;

        // Yeni duruma gir
        currentState.enter(this);

        // Debug için konsola yazdıralım
        System.out.println("Durum değişti: " + newState.getClass().getSimpleName());
    }

    // --- Hareket Metotları (Command Pattern bunları kullanıyor) ---
    public void moveX(int direction) {
        velocity.x = direction * MOVE_SPEED;
    }

    public void stopX() {
        velocity.x = 0;
    }

    public void jump() {
        // Sadece yerdeysek zıpla (Bunu ilerde State içine de alabiliriz ama şimdilik burada kalsın)
        if (onGround) {
            velocity.y = JUMP_VELOCITY;
            onGround = false;
        }
    }

    // Getter / Setter
    public Rectangle getBounds() { return bounds; }
    public Vector2 getVelocity() { return velocity; }
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }
}

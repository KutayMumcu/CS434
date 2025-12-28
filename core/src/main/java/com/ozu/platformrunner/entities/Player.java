package com.ozu.platformrunner.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ozu.platformrunner.patterns.observer.GameEvent;
import com.ozu.platformrunner.patterns.observer.GameObserver;
import com.ozu.platformrunner.patterns.state.IdleState;
import com.ozu.platformrunner.patterns.state.PlayerState;
import com.ozu.platformrunner.patterns.strategy.AttackStrategy;
import com.ozu.platformrunner.patterns.strategy.SwordStrategy;
import com.ozu.platformrunner.patterns.decorator.StrategyDecorator;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class Player {
    // Fizik Sabitleri
    private static final float MOVE_SPEED = 200f;
    private static final float JUMP_VELOCITY = 450f; // Zıplamayı biraz güçlendirdim
    private static final float GRAVITY = -900f;      // Yerçekimini biraz artırdım (daha tok hissiyat)

    private final Rectangle bounds;
    private final Vector2 velocity;
    private boolean onGround;

    private PlayerState currentState;
    private AttackStrategy attackStrategy;

    // Can Sistemi
    private int health = 100;
    private final List<GameObserver> observers;

    // Harita Sınırı
    private static final float MAP_WIDTH = 2000f; // Harita ne kadar uzunsa buraya yaz

    public Player(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        velocity = new Vector2(0, 0);
        onGround = false;

        observers = new ArrayList<>();
        currentState = new IdleState();
        currentState.enter(this);

        this.attackStrategy = new SwordStrategy();
    }

    public void update(float delta) {
        currentState.update(this, delta);

        // Fizik Uygula
        if (!onGround) {
            velocity.y += GRAVITY * delta;
        }

        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        // --- DÜZELTME 1: HARİTA SINIRLARI ---
        // Sol Sınır
        if (bounds.x < 0) {
            bounds.x = 0;
            velocity.x = 0;
        }
        // Sağ Sınır
        if (bounds.x > MAP_WIDTH - bounds.width) {
            bounds.x = MAP_WIDTH - bounds.width;
            velocity.x = 0;
        }

        // Aşağı Düşme Kontrolü (Ölüm)
        if (bounds.y < -50) {
            takeDamage(1000); // Direkt öldür
        }
    }

    public void performAttack(Array<Enemy> enemies, Array<Bullet> bullets) {
        attackStrategy.attack(this, enemies, bullets);
    }

    public void equipWeapon(AttackStrategy newStrategy) {
        this.attackStrategy = newStrategy;
        System.out.println("Silah değişti: " + newStrategy.getClass().getSimpleName());
    }

    public void addPowerUp(StrategyDecorator decorator) {
        this.attackStrategy = decorator;
    }

    public void changeState(PlayerState newState) {
        currentState.exit(this);
        this.currentState = newState;
        currentState.enter(this);
    }

    // --- Observer & Health ---
    public void addObserver(GameObserver observer) { observers.add(observer); }

    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health < 0) this.health = 0;
        notifyObservers(GameEvent.HEALTH_CHANGED);
        if (this.health == 0) notifyObservers(GameEvent.PLAYER_DIED);
    }

    public void setHealth(int health) {
        this.health = health;
        notifyObservers(GameEvent.HEALTH_CHANGED);
    }

    private void notifyObservers(GameEvent event) {
        for (GameObserver o : observers) o.onNotify(this, event);
    }

    // --- Hareket ---
    public void moveX(int direction) { velocity.x = direction * MOVE_SPEED; }
    public void stopX() { velocity.x = 0; }

    public void jump() {
        if (onGround) {
            velocity.y = JUMP_VELOCITY;
            onGround = false;
        }
    }

    // Getter
    public Rectangle getBounds() { return bounds; }
    public Vector2 getVelocity() { return velocity; }
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }
    public int getHealth() { return health; }
    public AttackStrategy getAttackStrategy() { return attackStrategy; }
}

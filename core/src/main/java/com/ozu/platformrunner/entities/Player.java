package com.ozu.platformrunner.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ozu.platformrunner.GameConstants;
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
    // Fizik Sabitleri - now using GameConstants
    private static final float MOVE_SPEED = GameConstants.PLAYER_MOVE_SPEED;
    private static final float JUMP_VELOCITY = GameConstants.PLAYER_JUMP_VELOCITY;
    private static final float GRAVITY = GameConstants.PLAYER_GRAVITY;

    private final Rectangle bounds;
    private final Vector2 velocity;
    private boolean onGround;

    private PlayerState currentState;
    private AttackStrategy attackStrategy;

    // Can Sistemi
    private int health = GameConstants.PLAYER_MAX_HEALTH;
    private final List<GameObserver> observers;

    // Harita Sınırı
    private static final float MAP_WIDTH = GameConstants.MAP_LIMIT_X;

    private int facingDirection = 1; // 1: Sağ, -1: Sol

    // Hurt animation
    private boolean isHurt = false;
    private float hurtTimer = 0f;
    private static final float HURT_DURATION = 0.5f;
    private Vector2 knockbackVelocity = new Vector2(0, 0);
    private float invulnerabilityTimer = 0f;
    private static final float INVULNERABILITY_DURATION = GameConstants.PLAYER_INVULNERABILITY_TIME;

    // Attack Cooldown System
    private float attackCooldown = 0.5f;
    private float attackCooldownTimer = 0f;

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

        // Update hurt state
        if (isHurt) {
            hurtTimer += delta;
            if (hurtTimer >= HURT_DURATION) {
                isHurt = false;
                hurtTimer = 0f;
            }
        }

        // Update invulnerability
        if (invulnerabilityTimer > 0) {
            invulnerabilityTimer -= delta;
        }

        // Update attack cooldown
        if (attackCooldownTimer > 0) {
            attackCooldownTimer -= delta;
        }

        // Apply knockback if hurt
        if (knockbackVelocity.len() > 0) {
            bounds.x += knockbackVelocity.x * delta;
            bounds.y += knockbackVelocity.y * delta;
            // Gradually reduce knockback
            knockbackVelocity.scl(0.9f);
            if (knockbackVelocity.len() < 10f) {
                knockbackVelocity.set(0, 0);
            }
        }

        // Fizik Uygula
        if (!onGround) {
            velocity.y += GRAVITY * delta;
        }

        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        // Harita Sınırları
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
        // Only attack if cooldown is ready
        if (attackCooldownTimer <= 0) {
            attackStrategy.attack(this, enemies, bullets);
            attackCooldownTimer = attackCooldown;
        }
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
        // Only take damage if not invulnerable
        if (invulnerabilityTimer <= 0) {
            this.health -= amount;
            if (this.health < 0) this.health = 0;
            notifyObservers(GameEvent.HEALTH_CHANGED);
            if (this.health == 0) {
                notifyObservers(GameEvent.PLAYER_DIED);
            } else {
                // Trigger hurt animation
                isHurt = true;
                hurtTimer = 0f;
                invulnerabilityTimer = INVULNERABILITY_DURATION;
            }
        }
    }

    public void applyKnockback(float directionX, float force) {
        knockbackVelocity.set(directionX * force, 100f);
    }

    public boolean isInvulnerable() {
        return invulnerabilityTimer > 0;
    }

    public boolean isHurt() {
        return isHurt;
    }

    public void setHealth(int health) {
        this.health = health;
        notifyObservers(GameEvent.HEALTH_CHANGED);
    }

    private void notifyObservers(GameEvent event) {
        for (GameObserver o : observers) o.onNotify(this, event);
    }

    // --- Hareket ---
    public void moveX(int direction) {
        velocity.x = direction * MOVE_SPEED;
        if (direction != 0) {
            this.facingDirection = direction; // Yönü kaydet
        }
    }

    // --- EKSİK METOTLAR EKLENDİ ---

    public void stopX() {
        velocity.x = 0;
    }

    public void jump() {
        if (onGround) {
            velocity.y = JUMP_VELOCITY;
            onGround = false;
        }
    }

    // Getter
    public int getFacingDirection() { return facingDirection; }
    public void setFacingDirection(int direction) { this.facingDirection = direction; }
    public Rectangle getBounds() { return bounds; }
    public Vector2 getVelocity() { return velocity; }
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }
    public int getHealth() { return health; }
    public AttackStrategy getAttackStrategy() { return attackStrategy; }

    // Attack Cooldown Getters
    public float getAttackCooldownPercent() {
        return Math.max(0, attackCooldownTimer / attackCooldown);
    }

    public boolean isAttackOnCooldown() {
        return attackCooldownTimer > 0;
    }
}

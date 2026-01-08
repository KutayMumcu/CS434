package com.ozu.platformrunner.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.managers.ResourceManager;
import com.ozu.platformrunner.patterns.decorator.StrategyDecorator;
import com.ozu.platformrunner.patterns.observer.GameEvent;
import com.ozu.platformrunner.patterns.observer.GameObserver;
import com.ozu.platformrunner.patterns.state.IdleState;
import com.ozu.platformrunner.patterns.state.PlayerState;
import com.ozu.platformrunner.patterns.strategy.AttackStrategy;
import com.ozu.platformrunner.patterns.strategy.SwordStrategy;

import java.util.ArrayList;
import java.util.List;

public class Player {
    // --- FİZİK SABİTLERİ ---
    private static final float MOVE_SPEED = 200f;
    private static final float JUMP_VELOCITY = 450f;
    private static final float GRAVITY = -900f;
    private static final float MAP_WIDTH = 2000f;

    // --- TEMEL BİLEŞENLER ---
    private final Rectangle bounds;
    private final Vector2 velocity;
    private boolean onGround;
    private int facingDirection = 1; // 1: Sağ, -1: Sol

    // --- GERİ TEPME (KNOCKBACK) FİZİĞİ ---
    private Vector2 knockbackVelocity;

    // --- DESENLER ---
    private PlayerState currentState;
    private AttackStrategy attackStrategy;
    private final List<GameObserver> observers;

    // --- GÖRSELLER ---
    private final Texture textureRight;
    private final Texture textureLeft;
    private Texture currentTexture;

    // --- CAN VE HASAR SİSTEMİ ---
    private int health = 100;
    private boolean isHurt = false;
    private float hurtTimer = 0f;
    private static final float HURT_DURATION = 0.2f;

    private float invulnerabilityTimer = 0f;
    private static final float INVULNERABILITY_DURATION = 1.0f;

    // --- SALDIRI SİSTEMİ ---
    private float attackCooldownTimer = 0f;
    private static final float ATTACK_COOLDOWN = 0.5f;

    public Player(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        velocity = new Vector2(0, 0);
        knockbackVelocity = new Vector2(0, 0); // Knockback vektörünü başlat
        onGround = false;

        observers = new ArrayList<>();

        currentState = new IdleState();
        currentState.enter(this);
        this.attackStrategy = new SwordStrategy();

        textureRight = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_CHAR_RIGHT);
        textureLeft = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_CHAR_LEFT);
        currentTexture = textureRight;
    }

    public void update(float delta) {
        currentState.update(this, delta);

        // Zamanlayıcılar
        if (invulnerabilityTimer > 0) invulnerabilityTimer -= delta;
        if (attackCooldownTimer > 0) attackCooldownTimer -= delta;

        if (isHurt) {
            hurtTimer += delta;
            if (hurtTimer >= HURT_DURATION) {
                isHurt = false;
                hurtTimer = 0f;
            }
        }

        // --- KNOCKBACK MANTIĞI ---
        if (knockbackVelocity.len() > 0) {
            // Geri tepme varsa karakteri it
            bounds.x += knockbackVelocity.x * delta;
            bounds.y += knockbackVelocity.y * delta;

            // Sürtünme ile yavaşlat
            knockbackVelocity.scl(0.9f);

            // Çok yavaşladıysa durdur
            if (knockbackVelocity.len() < 10f) {
                knockbackVelocity.set(0, 0);
            }
        }

        // --- NORMAL FİZİK ---
        if (!onGround) {
            velocity.y += GRAVITY * delta;
        }

        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        // Sınır Kontrolleri
        if (bounds.x < 0) {
            bounds.x = 0;
            velocity.x = 0;
        }
        if (bounds.x > MAP_WIDTH - bounds.width) {
            bounds.x = MAP_WIDTH - bounds.width;
            velocity.x = 0;
        }

        if (bounds.y < -50) {
            takeDamage(1000);
        }
    }

    public void draw(SpriteBatch batch) {
        if (facingDirection == 1) {
            currentTexture = textureRight;
        } else if (facingDirection == -1) {
            currentTexture = textureLeft;
        }

        if (isHurt) {
            batch.setColor(Color.RED);
        } else if (invulnerabilityTimer > 0) {
            batch.setColor(1, 1, 1, 0.5f);
        } else {
            batch.setColor(Color.WHITE);
        }

        batch.draw(currentTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
    }

    // --- EKSİK OLAN METOTLAR EKLENDİ ---

    // 1. GameScreen'in çağırdığı Geri Tepme Metodu
    public void applyKnockback(float directionX, float force) {
        // X yönünde force kadar, Y yönünde hafif yukarı (200f) zıplat
        knockbackVelocity.set(directionX * force, 200f);
    }

    // 2. GameScreen'in çağırdığı Dokunulmazlık Kontrolü
    public boolean isInvulnerable() {
        return invulnerabilityTimer > 0;
    }

    // 3. GameScreen'in çağırdığı Yaralanma Durumu Kontrolü
    public boolean isHurt() {
        return isHurt;
    }

    // 4. GameScreen'in çağırdığı Saldırı Bekleme Durumu
    public boolean isAttackOnCooldown() {
        return attackCooldownTimer > 0;
    }

    // 5. GameScreen'in çağırdığı Saldırı Yüzdesi (UI için olabilir)
    public float getAttackCooldownPercent() {
        if (attackCooldownTimer <= 0) return 0;
        return attackCooldownTimer / ATTACK_COOLDOWN;
    }

    // --- MEVCUT METOTLAR ---

    public void moveX(int direction) {
        // Knockback yiyorsa hareket edemesin
        if (knockbackVelocity.len() > 50f) return;

        velocity.x = direction * MOVE_SPEED;
        if (direction != 0) {
            this.facingDirection = direction;
        }
    }

    public void stopX() {
        if (knockbackVelocity.len() > 50f) return;
        velocity.x = 0;
    }

    public void jump() {
        if (onGround) {
            velocity.y = JUMP_VELOCITY;
            onGround = false;
        }
    }

    public void performAttack(Array<Enemy> enemies, Array<Bullet> bullets) {
        if (attackCooldownTimer <= 0) {
            attackStrategy.attack(this, enemies, bullets);
            attackCooldownTimer = ATTACK_COOLDOWN;
        }
    }

    public void takeDamage(int amount) {
        if (invulnerabilityTimer > 0) return;

        this.health -= amount;
        if (this.health < 0) this.health = 0;

        isHurt = true;
        invulnerabilityTimer = INVULNERABILITY_DURATION;

        notifyObservers(GameEvent.HEALTH_CHANGED);

        if (this.health == 0) {
            notifyObservers(GameEvent.PLAYER_DIED);
        }
    }

    public void equipWeapon(AttackStrategy newStrategy) {
        this.attackStrategy = newStrategy;
        System.out.println("Silah Kuşandı: " + newStrategy.getClass().getSimpleName());
    }

    public void addPowerUp(StrategyDecorator decorator) {
        this.attackStrategy = decorator;
    }

    public void changeState(PlayerState newState) {
        currentState.exit(this);
        this.currentState = newState;
        currentState.enter(this);
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(GameEvent event) {
        for (GameObserver o : observers) o.onNotify(this, event);
    }

    // --- GETTER & SETTER ---
    public Rectangle getBounds() { return bounds; }
    public Vector2 getVelocity() { return velocity; }
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }
    public int getHealth() { return health; }
    public void setHealth(int health) {
        this.health = health;
        notifyObservers(GameEvent.HEALTH_CHANGED);
    }
    public int getFacingDirection() { return facingDirection; }
    public void setFacingDirection(int direction) { this.facingDirection = direction; }
    public AttackStrategy getAttackStrategy() { return attackStrategy; }
}

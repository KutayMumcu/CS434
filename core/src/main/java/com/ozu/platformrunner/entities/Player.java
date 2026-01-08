package com.ozu.platformrunner.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.ozu.platformrunner.patterns.strategy.WeaponType;
import com.ozu.platformrunner.utils.GameConstants;

import java.util.ArrayList;
import java.util.List;

public class Player {
    // visual dimensions (slightly larger than hitbox for aesthetics)
    private static final float DRAW_WIDTH = 64f;
    private static final float DRAW_HEIGHT = 64f;

    // Alignment offsets to center the visual sprite on the logical hitbox
    private final float drawOffsetX;
    private final float drawOffsetY = 0f;

    // Physics
    private final Rectangle bounds;
    private final Vector2 velocity;
    private final Vector2 knockbackVelocity;
    private boolean onGround;
    private int facingDirection = 1;

    // Animations
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> runAnim;
    private Animation<TextureRegion> attackAnim;
    private Animation<TextureRegion> rangeAnim;
    private Animation<TextureRegion> jumpAnim;
    private float stateTime = 0f;

    // Combat State
    private boolean isAttacking = false;
    private float attackAnimTimer = 0f;
    private int health = GameConstants.PLAYER_MAX_HEALTH;
    private boolean isHurt = false;
    private float hurtTimer = 0f;
    private float invulnerabilityTimer = 0f;
    private float attackCooldownTimer = 0f;

    // Patterns
    private PlayerState currentState;
    private AttackStrategy attackStrategy;
    private final List<GameObserver> observers;

    public Player(float x, float y) {
        bounds = new Rectangle(x, y, GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);
        velocity = new Vector2(0, 0);
        knockbackVelocity = new Vector2(0, 0);
        onGround = false;

        // Calculate offset to center the sprite
        drawOffsetX = (DRAW_WIDTH - GameConstants.PLAYER_WIDTH) / 2;

        observers = new ArrayList<>();
        currentState = new IdleState();
        currentState.enter(this);
        this.attackStrategy = new SwordStrategy();

        loadAnimations();
    }

    private void loadAnimations() {
        idleAnim = createAnimation(ResourceManager.TEXTURE_IDLE, 7, 0.15f);
        runAnim = createAnimation(ResourceManager.TEXTURE_RUN, 8, 0.1f);
        jumpAnim = createAnimation(ResourceManager.TEXTURE_JUMP, 8, 0.1f);
        attackAnim = createAnimation(ResourceManager.TEXTURE_ATTACK, 10, 0.08f);
        rangeAnim = createAnimation(ResourceManager.TEXTURE_LIGHTBALL, 7, 0.08f);
    }

    private Animation<TextureRegion> createAnimation(String textureKey, int frameCols, float frameDuration) {
        Texture sheet = ResourceManager.getInstance().getTexture(textureKey);
        if (sheet == null) return null;

        TextureRegion[][] tmp = TextureRegion.split(sheet,
            sheet.getWidth() / frameCols,
            sheet.getHeight() / 1);

        TextureRegion[] frames = new TextureRegion[frameCols];
        System.arraycopy(tmp[0], 0, frames, 0, frameCols);
        return new Animation<>(frameDuration, frames);
    }

    public void update(float delta) {
        stateTime += delta;
        currentState.update(this, delta);

        updateTimers(delta);
        updatePhysics(delta);
        checkBoundaries();
    }

    private void updateTimers(float delta) {
        if (invulnerabilityTimer > 0) invulnerabilityTimer -= delta;
        if (attackCooldownTimer > 0) attackCooldownTimer -= delta;

        if (isAttacking) {
            attackAnimTimer -= delta;
            if (attackAnimTimer <= 0) {
                isAttacking = false;
            }
        }

        if (isHurt) {
            hurtTimer += delta;
            if (hurtTimer >= 0.2f) {
                isHurt = false;
                hurtTimer = 0f;
            }
        }
    }

    private void updatePhysics(float delta) {
        // Knockback decay
        if (knockbackVelocity.len() > 0) {
            bounds.x += knockbackVelocity.x * delta;
            bounds.y += knockbackVelocity.y * delta;
            knockbackVelocity.scl(0.9f);
            if (knockbackVelocity.len() < 10f) knockbackVelocity.set(0, 0);
        }

        // Gravity & Movement
        if (!onGround) velocity.y += GameConstants.PLAYER_GRAVITY * delta;
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;
    }

    private void checkBoundaries() {
        if (bounds.x < 0) {
            bounds.x = 0;
            velocity.x = 0;
        }
        if (bounds.x > GameConstants.MAP_LIMIT_X - bounds.width) {
            bounds.x = GameConstants.MAP_LIMIT_X - bounds.width;
            velocity.x = 0;
        }
        // Fall check
        if (bounds.y < -50) takeDamage(GameConstants.PLAYER_MAX_HEALTH);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = getCurrentFrame();

        if (currentFrame == null) return;

        // Handle Flipping
        if (facingDirection == -1 && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (facingDirection == 1 && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        // Handle Visual Effects (Damage, Invulnerability)
        if (isHurt) batch.setColor(Color.RED);
        else if (invulnerabilityTimer > 0) batch.setColor(1, 1, 1, 0.5f);
        else batch.setColor(Color.WHITE);

        batch.draw(currentFrame,
            bounds.x - drawOffsetX,
            bounds.y - drawOffsetY,
            DRAW_WIDTH,
            DRAW_HEIGHT);

        batch.setColor(Color.WHITE);
    }

    private TextureRegion getCurrentFrame() {
        if (isAttacking) {
            if (attackStrategy.getWeaponType() == WeaponType.RANGED) {
                rangeAnim.setPlayMode(Animation.PlayMode.NORMAL);
                return rangeAnim.getKeyFrame(stateTime);
            } else {
                attackAnim.setPlayMode(Animation.PlayMode.NORMAL);
                return attackAnim.getKeyFrame(stateTime);
            }
        } else if (!onGround) {
            jumpAnim.setPlayMode(Animation.PlayMode.LOOP);
            return jumpAnim.getKeyFrame(stateTime, true);
        } else if (Math.abs(velocity.x) > 10) {
            runAnim.setPlayMode(Animation.PlayMode.LOOP);
            return runAnim.getKeyFrame(stateTime, true);
        } else {
            idleAnim.setPlayMode(Animation.PlayMode.LOOP);
            return idleAnim.getKeyFrame(stateTime, true);
        }
    }

    public void performAttack(Array<Enemy> enemies, Array<Bullet> bullets) {
        if (attackCooldownTimer <= 0) {
            attackStrategy.attack(this, enemies, bullets);
            isAttacking = true;

            if (attackStrategy.getWeaponType() == WeaponType.RANGED) {
                attackAnimTimer = rangeAnim.getAnimationDuration();
            } else {
                attackAnimTimer = attackAnim.getAnimationDuration();
            }

            stateTime = 0;
            attackCooldownTimer = GameConstants.PLAYER_ATTACK_COOLDOWN;
        }
    }

    // --- Movement Helpers ---
    public void moveX(int direction) {
        if (knockbackVelocity.len() > 50f) return;
        velocity.x = direction * GameConstants.PLAYER_MOVE_SPEED;
        if (direction != 0) facingDirection = direction;
    }

    public void stopX() {
        if (knockbackVelocity.len() > 50f) return;
        velocity.x = 0;
    }

    public void jump() {
        if (onGround) {
            velocity.y = GameConstants.PLAYER_JUMP_VELOCITY;
            onGround = false;
        }
    }

    // --- Combat Helpers ---
    public void equipWeapon(AttackStrategy newStrategy) {
        this.attackStrategy = newStrategy;
    }

    public void addPowerUp(StrategyDecorator decorator) {
        this.attackStrategy = decorator;
    }

    public void takeDamage(int amount) {
        if (invulnerabilityTimer > 0) return;

        this.health -= amount;
        if (this.health < 0) health = 0;

        isHurt = true;
        invulnerabilityTimer = GameConstants.PLAYER_INVULNERABILITY_TIME;

        notifyObservers(GameEvent.HEALTH_CHANGED);
        if (health == 0) notifyObservers(GameEvent.PLAYER_DIED);
    }

    public void applyKnockback(float dirX, float force) {
        knockbackVelocity.set(dirX * force, 200f);
    }

    // --- Getters & Setters ---
    public boolean isInvulnerable() { return invulnerabilityTimer > 0; }
    public boolean isHurt() { return isHurt; }
    public boolean isAttackOnCooldown() { return attackCooldownTimer > 0; }

    public float getAttackCooldownPercent() {
        return (attackCooldownTimer <= 0) ? 0 : attackCooldownTimer / GameConstants.PLAYER_ATTACK_COOLDOWN;
    }

    public void addObserver(GameObserver observer) { observers.add(observer); }
    private void notifyObservers(GameEvent event) { for (GameObserver o : observers) o.onNotify(this, event); }

    public Rectangle getBounds() { return bounds; }
    public Vector2 getVelocity() { return velocity; }
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; notifyObservers(GameEvent.HEALTH_CHANGED); }
    public int getFacingDirection() { return facingDirection; }
    public void setFacingDirection(int direction) { this.facingDirection = direction; }
    public AttackStrategy getAttackStrategy() { return attackStrategy; }

    public void changeState(PlayerState newState) {
        currentState.exit(this);
        this.currentState = newState;
        currentState.enter(this);
    }
}

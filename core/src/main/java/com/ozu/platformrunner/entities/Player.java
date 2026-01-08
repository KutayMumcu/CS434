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
import com.ozu.platformrunner.patterns.strategy.BowStrategy;
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
    private Vector2 knockbackVelocity;

    // --- ANİMASYONLAR ---
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> runAnim;
    private Animation<TextureRegion> attackAnim;    // Kılıç
    private Animation<TextureRegion> rangeAnim;     // LightBall Atışı
    private Animation<TextureRegion> jumpAnim;

    private float stateTime = 0f; // Genel animasyon süresi

    // --- DURUMLAR (State Logic) ---
    private boolean isAttacking = false;
    private float attackAnimTimer = 0f; // Saldırı animasyonunun ne kadar süreceği

    // --- DİĞER SİSTEMLER ---
    private PlayerState currentState;
    private AttackStrategy attackStrategy;
    private final List<GameObserver> observers;

    private int health = 100;
    private boolean isHurt = false;
    private float hurtTimer = 0f;
    private float invulnerabilityTimer = 0f;
    private float attackCooldownTimer = 0f;

    public Player(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        velocity = new Vector2(0, 0);
        knockbackVelocity = new Vector2(0, 0);
        onGround = false;

        observers = new ArrayList<>();
        currentState = new IdleState();
        currentState.enter(this);
        this.attackStrategy = new SwordStrategy(); // Varsayılan silah

        // --- ANİMASYONLARI YÜKLE ---
        // createAnimation(DosyaAdı, SütunSayısı, Hız)
        // NOT: İndirdiğin görsellerin kaç kare olduğunu buradaki rakamlarla güncelle!
        idleAnim = createAnimation(ResourceManager.TEXTURE_IDLE, 7, 0.15f);
        runAnim = createAnimation(ResourceManager.TEXTURE_RUN, 8, 0.1f);
        jumpAnim = createAnimation(ResourceManager.TEXTURE_JUMP, 8, 0.1f);

        // Saldırı animasyonları genelde daha hızlı olur (0.05f - 0.08f gibi)
        attackAnim = createAnimation(ResourceManager.TEXTURE_ATTACK, 10, 0.08f);
        rangeAnim = createAnimation(ResourceManager.TEXTURE_LIGHTBALL, 7, 0.08f);
    }

    /**
     * Yardımcı Metot: Sprite Sheet'i parçalayıp animasyon oluşturur.
     */
    private Animation<TextureRegion> createAnimation(String textureName, int frameCols, float frameDuration) {
        Texture sheet = ResourceManager.getInstance().getTexture(textureName);
        TextureRegion[][] tmp = TextureRegion.split(sheet,
            sheet.getWidth() / frameCols,
            sheet.getHeight() / 1); // Tek satır varsayıyoruz

        TextureRegion[] frames = new TextureRegion[frameCols];
        int index = 0;
        for (int i = 0; i < frameCols; i++) {
            frames[index++] = tmp[0][i];
        }
        return new Animation<>(frameDuration, frames);
    }

    public void update(float delta) {
        stateTime += delta;
        currentState.update(this, delta);

        // Zamanlayıcılar
        if (invulnerabilityTimer > 0) invulnerabilityTimer -= delta;
        if (attackCooldownTimer > 0) attackCooldownTimer -= delta;

        // Saldırı animasyonu kontrolü
        if (isAttacking) {
            attackAnimTimer -= delta;
            if (attackAnimTimer <= 0) {
                isAttacking = false; // Animasyon bitti
            }
        }

        if (isHurt) {
            hurtTimer += delta;
            if (hurtTimer >= 0.2f) { isHurt = false; hurtTimer = 0f; }
        }

        // --- FİZİK GÜNCELLEMELERİ ---
        // Knockback
        if (knockbackVelocity.len() > 0) {
            bounds.x += knockbackVelocity.x * delta;
            bounds.y += knockbackVelocity.y * delta;
            knockbackVelocity.scl(0.9f);
            if (knockbackVelocity.len() < 10f) knockbackVelocity.set(0, 0);
        }

        // Yerçekimi ve Hareket
        if (!onGround) velocity.y += GRAVITY * delta;
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        // Sınırlar
        if (bounds.x < 0) { bounds.x = 0; velocity.x = 0; }
        if (bounds.x > MAP_WIDTH - bounds.width) { bounds.x = MAP_WIDTH - bounds.width; velocity.x = 0; }
        if (bounds.y < -50) takeDamage(1000);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = null;

        // --- ANİMASYON ÖNCELİK MANTIĞI ---

        // 1. ÖNCELİK: Saldırıyorsa -> Attack
        if (isAttacking) {
            String strategyName = attackStrategy.getClass().getSimpleName();
            if (strategyName.contains("Bow")) {
                rangeAnim.setPlayMode(Animation.PlayMode.NORMAL);
                currentFrame = rangeAnim.getKeyFrame(stateTime);
            } else {
                attackAnim.setPlayMode(Animation.PlayMode.NORMAL);
                currentFrame = attackAnim.getKeyFrame(stateTime);
            }
        }
        // 2. ÖNCELİK: Yerde Değilse (Havadaysa) -> JUMP (YENİ EKLENEN KISIM)
        else if (!onGround) {
            // Zıplama genelde döngüye girmez, son karede kalması daha doğal durur
            // Ama basit olsun diye şimdilik LOOP yapıyoruz.
            jumpAnim.setPlayMode(Animation.PlayMode.LOOP);
            currentFrame = jumpAnim.getKeyFrame(stateTime, true);
        }
        // 3. ÖNCELİK: Hareket ediyorsa -> Run
        else if (Math.abs(velocity.x) > 10) {
            runAnim.setPlayMode(Animation.PlayMode.LOOP);
            currentFrame = runAnim.getKeyFrame(stateTime, true);
        }
        // 4. ÖNCELİK: Duruyorsa -> Idle
        else {
            idleAnim.setPlayMode(Animation.PlayMode.LOOP);
            currentFrame = idleAnim.getKeyFrame(stateTime, true);
        }

        // --- FLIP VE RENK KODLARI AYNEN KALIYOR ---
        if (facingDirection == -1 && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (facingDirection == 1 && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        if (isHurt) batch.setColor(Color.RED);
        else if (invulnerabilityTimer > 0) batch.setColor(1, 1, 1, 0.5f);
        else batch.setColor(Color.WHITE);

        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
    }

    public void performAttack(Array<Enemy> enemies, Array<Bullet> bullets) {
        // Cooldown kontrolü
        if (attackCooldownTimer <= 0) {
            // 1. Saldırı işlemini yap (Hasar ver / Mermi oluştur)
            attackStrategy.attack(this, enemies, bullets);

            // 2. Animasyonu başlat
            isAttacking = true;

            // Animasyon süresini ayarla (stateTime'ı o ana senkronize etmek için biraz hile yapabiliriz
            // ama basitçe animasyon süresi kadar kilitliyoruz)

            String strategyName = attackStrategy.getClass().getSimpleName();
            if (strategyName.contains("Bow")) {
                attackAnimTimer = rangeAnim.getAnimationDuration();
            } else {
                attackAnimTimer = attackAnim.getAnimationDuration();
            }

            // Animasyonun baştan başlaması için stateTime'ı sıfırlamak yerine
            // Animation class'ının stateTime mantığını kullanıyoruz.
            // Ancak basit yöntem: Saldırı başladığında global stateTime'ı sıfırlarsak diğer animasyonlar titreyebilir.
            // En temizi: draw metodunda attack için (stateTime - attackStartTime) kullanmaktır.
            // Ama şimdilik basit tutmak adına stateTime kullanıyoruz, sadece görsel akışı devam eder.
            // Daha doğru görünmesi için:
            stateTime = 0;

            attackCooldownTimer = 0.5f; // Cooldown
        }
    }

    // --- MEVCUT METOTLAR (Getter/Setter/Helper) ---
    public void moveX(int direction) {
        if (knockbackVelocity.len() > 50f) return;
        velocity.x = direction * MOVE_SPEED;
        if (direction != 0) facingDirection = direction;
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
        invulnerabilityTimer = 1.0f;
        notifyObservers(GameEvent.HEALTH_CHANGED);
        if (health == 0) notifyObservers(GameEvent.PLAYER_DIED);
    }

    public void applyKnockback(float dirX, float force) {
        knockbackVelocity.set(dirX * force, 200f);
    }

    public boolean isInvulnerable() { return invulnerabilityTimer > 0; }
    public boolean isHurt() { return isHurt; }
    public boolean isAttackOnCooldown() { return attackCooldownTimer > 0; }
    public float getAttackCooldownPercent() { return (attackCooldownTimer <= 0) ? 0 : attackCooldownTimer / 0.5f; }

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

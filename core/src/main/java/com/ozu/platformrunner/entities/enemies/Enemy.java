package com.ozu.platformrunner.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.managers.ResourceManager;

public abstract class Enemy {
    protected Rectangle bounds;
    protected float speed = 100f;

    // Fizik
    protected Vector2 velocity;
    protected boolean onGround = false;
    protected static final float GRAVITY = -800f;
    protected static final float JUMP_VELOCITY = 400f; // Boss zıplaması için gerekli

    // Zıplama ve AI Sistemi (BossEnemy için gerekli)
    protected float jumpCooldown = 1.0f;
    protected float jumpCooldownTimer = 0f;
    private Array<Platform> currentPlatforms; // AI kontrolleri için platformları tutar

    // Can Sistemi
    protected int maxHealth = 30;
    protected int currentHealth;

    // Görseller
    protected Texture textureRight;
    protected Texture textureLeft;
    protected Texture currentTexture;

    // Can Barı
    private static Texture healthBarTexture;

    public Enemy(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
        this.velocity = new Vector2(0, 0);
        this.currentHealth = maxHealth;

        // Textureları Yükle
        textureRight = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_ENEMY_RIGHT);
        textureLeft = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_ENEMY_LEFT);
        currentTexture = textureRight; // Varsayılan

        if (healthBarTexture == null) {
            createHealthBarTexture();
        }
    }

    private void createHealthBarTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        healthBarTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void update(float delta, Player player, Array<Platform> platforms) {
        // Platformları kaydet (AI metotları için gerekli)
        this.currentPlatforms = platforms;

        // Zıplama süresini güncelle
        if (jumpCooldownTimer > 0) {
            jumpCooldownTimer -= delta;
        }

        // 1. Yerçekimi
        if (!onGround) {
            velocity.y += GRAVITY * delta;
        }

        // 2. Alt Sınıf Hareket Mantığı
        moveBehavior(delta, player);

        // 3. Pozisyon Güncelleme
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        // 4. Platform Çarpışma Kontrolü
        onGround = false;
        for (Platform platform : platforms) {
            if (Intersector.overlaps(bounds, platform.getBounds())) {
                if (velocity.y < 0 && bounds.y > platform.getBounds().y) {
                    bounds.y = platform.getBounds().y + platform.getBounds().height;
                    velocity.y = 0;
                    onGround = true;
                }
            }
        }

        // Zemin kontrolü
        if (bounds.y < -50) {
            currentHealth = 0;
        }
    }

    protected abstract void moveBehavior(float delta, Player player);

    // --- HELPER METOTLAR (BossEnemy ve Akıllı Düşmanlar İçin) ---

    protected void jump() {
        if (onGround && jumpCooldownTimer <= 0) {
            velocity.y = JUMP_VELOCITY;
            onGround = false;
            jumpCooldownTimer = jumpCooldown;
        }
    }

    protected boolean shouldJumpToPlayer(Player player, float horizontalRange, float verticalThreshold) {
        float horizontalDistance = Math.abs(player.getBounds().x - bounds.x);
        float verticalDistance = player.getBounds().y - bounds.y;

        return horizontalDistance < horizontalRange &&
            verticalDistance > verticalThreshold &&
            verticalDistance < 200f &&
            onGround &&
            jumpCooldownTimer <= 0;
    }

    protected boolean isEdgeAhead(int direction, Array<Platform> platforms) {
        if (!onGround) return false;

        float checkDistance = bounds.width * 0.6f;
        float checkX = bounds.x + (direction > 0 ? bounds.width + checkDistance : -checkDistance);
        float checkY = bounds.y - 10f;

        for (Platform platform : platforms) {
            Rectangle pBounds = platform.getBounds();
            if (checkX >= pBounds.x && checkX <= pBounds.x + pBounds.width &&
                checkY >= pBounds.y - 20 && checkY <= pBounds.y + pBounds.height) {
                return false; // Zemin var, kenar değil
            }
        }
        return true; // Kenar var (Düşülecek yer)
    }

    protected Array<Platform> getPlatforms() {
        return currentPlatforms;
    }

    // --- ÇİZİM VE DİĞERLERİ ---

    public void draw(SpriteBatch batch) {
        if (velocity.x > 0) {
            currentTexture = textureRight;
        } else if (velocity.x < 0) {
            currentTexture = textureLeft;
        }

        batch.draw(currentTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        drawHealthBar(batch);
    }

    private void drawHealthBar(SpriteBatch batch) {
        float barWidth = bounds.width;
        float barHeight = 5;
        float barX = bounds.x;
        float barY = bounds.y + bounds.height + 5;

        batch.setColor(Color.RED);
        batch.draw(healthBarTexture, barX, barY, barWidth, barHeight);

        float healthPercentage = (float) currentHealth / maxHealth;
        batch.setColor(Color.GREEN);
        batch.draw(healthBarTexture, barX, barY, barWidth * healthPercentage, barHeight);

        batch.setColor(Color.WHITE);
    }

    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth < 0) currentHealth = 0;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public Rectangle getBounds() { return bounds; }

    public static void disposeStaticResources() {
        if (healthBarTexture != null) {
            healthBarTexture.dispose();
            healthBarTexture = null;
        }
    }

    // --- SaveManager İçin Gerekli Metotlar ---

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setHealth(int health) {
        this.currentHealth = health;
    }
}

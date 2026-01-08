package com.ozu.platformrunner.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.managers.ResourceManager;
import com.badlogic.gdx.math.Intersector;

public abstract class Enemy {
    protected Rectangle bounds;
    protected Texture texture;
    protected float speed = 100f;

    // YENİ: Fizik
    protected Vector2 velocity;
    protected boolean onGround = false;
    protected static final float GRAVITY = -800f;
    protected static final float JUMP_VELOCITY = 400f;

    // YENİ: Jump System
    protected float jumpCooldown = 1.0f;
    protected float jumpCooldownTimer = 0f;

    // YENİ: Can Sistemi
    protected int maxHealth = 30;
    protected int currentHealth;

    // YENİ: Can Barı Görseli (Programatik oluşturacağız)
    private static Texture healthBarTexture;

    public Enemy(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
        this.texture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_ENEMY);
        this.velocity = new Vector2(0, 0);
        this.currentHealth = maxHealth;

        // Can barı için basit beyaz bir piksel oluştur (Sadece bir kere)
        if (healthBarTexture == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            healthBarTexture = new Texture(pixmap);
            pixmap.dispose();
        }
    }

    // Platformları da parametre olarak alıyoruz ki düşmeyelim
    public void update(float delta, Player player, Array<Platform> platforms) {
        // Store platforms for edge detection
        this.currentPlatforms = platforms;

        // 1. Update jump cooldown
        if (jumpCooldownTimer > 0) {
            jumpCooldownTimer -= delta;
        }

        // 2. Yerçekimi
        if (!onGround) {
            velocity.y += GRAVITY * delta;
        }

        // 3. Alt Sınıfın Hareket Mantığı (Abstract metodu çağır)
        moveBehavior(delta, player);

        // 4. Pozisyon Güncelleme
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        // 4. Platform Çarpışma Kontrolü (Fizik)
        onGround = false;
        for (Platform platform : platforms) {
            if (Intersector.overlaps(bounds, platform.getBounds())) {
                // Sadece düşüyorsak ve platformun üstündeysek
                if (velocity.y < 0 && bounds.y > platform.getBounds().y) {
                    bounds.y = platform.getBounds().y + platform.getBounds().height;
                    velocity.y = 0;
                    onGround = true;
                }
            }
        }

        // Death by falling into pit (same as player)
        if (bounds.y < -50) {
            currentHealth = 0; // Enemy dies from falling
        }
    }

    // Her düşman nasıl yürüyeceğine kendi karar verir
    protected abstract void moveBehavior(float delta, Player player);

    // Jump method that can be called by subclasses
    protected void jump() {
        if (onGround && jumpCooldownTimer <= 0) {
            velocity.y = JUMP_VELOCITY;
            onGround = false;
            jumpCooldownTimer = jumpCooldown;
        }
    }

    // Helper method to check if player is above and nearby
    protected boolean shouldJumpToPlayer(Player player, float horizontalRange, float verticalThreshold) {
        float horizontalDistance = Math.abs(player.getBounds().x - bounds.x);
        float verticalDistance = player.getBounds().y - bounds.y;

        return horizontalDistance < horizontalRange &&
               verticalDistance > verticalThreshold &&
               verticalDistance < 200f &&
               onGround &&
               jumpCooldownTimer <= 0;
    }

    // Edge detection - checks if there's ground ahead in the direction of movement
    protected boolean isEdgeAhead(int direction, Array<Platform> platforms) {
        if (!onGround) return false; // Only check edges when on ground

        // Check point slightly ahead and below the enemy
        float checkDistance = bounds.width * 0.6f; // Check a bit ahead
        float checkX = bounds.x + (direction > 0 ? bounds.width + checkDistance : -checkDistance);
        float checkY = bounds.y - 10f; // Check slightly below current position

        // Check if there's a platform at this position
        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBounds();
            // Check if the check point is above and within the platform
            if (checkX >= platformBounds.x &&
                checkX <= platformBounds.x + platformBounds.width &&
                checkY >= platformBounds.y - 20 &&
                checkY <= platformBounds.y + platformBounds.height) {
                return false; // Ground exists, no edge
            }
        }

        return true; // Edge detected! No ground ahead
    }

    // Helper method to get platforms from update method
    private Array<Platform> currentPlatforms;

    protected Array<Platform> getPlatforms() {
        return currentPlatforms;
    }

    // Hasar Alma
    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth < 0) currentHealth = 0;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public void draw(SpriteBatch batch) {
        // Düşmanı çiz
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);

        // CAN BARI ÇİZİMİ
        float barWidth = bounds.width;
        float barHeight = 5;
        float barX = bounds.x;
        float barY = bounds.y + bounds.height + 5;

        // Arka plan (Kırmızı)
        batch.setColor(Color.RED);
        batch.draw(healthBarTexture, barX, barY, barWidth, barHeight);

        // Kalan can (Yeşil)
        float healthPercentage = (float) currentHealth / maxHealth;
        batch.setColor(Color.GREEN);
        batch.draw(healthBarTexture, barX, barY, barWidth * healthPercentage, barHeight);

        // Rengi normale döndür (Yoksa her şey yeşil olur)
        batch.setColor(Color.WHITE);
    }

    public Rectangle getBounds() { return bounds; }
    public int getCurrentHealth() { return currentHealth; }
    public void setHealth(int health) { this.currentHealth = health; }
    public Vector2 getVelocity() { return velocity; }

    /**
     * Dispose static resources - call this when game exits
     * Prevents memory leaks from static textures
     */
    public static void disposeStaticResources() {
        if (healthBarTexture != null) {
            healthBarTexture.dispose();
            healthBarTexture = null;
        }
    }
}

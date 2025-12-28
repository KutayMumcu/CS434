package com.ozu.platformrunner.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
        // 1. Yerçekimi
        if (!onGround) {
            velocity.y += GRAVITY * delta;
        }

        // 2. Alt Sınıfın Hareket Mantığı (Abstract metodu çağır)
        moveBehavior(delta, player);

        // 3. Pozisyon Güncelleme
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

        // Zemin kontrolü (Basit zemin)
        if (bounds.y < 0) {
            bounds.y = 0;
            velocity.y = 0;
            onGround = true;
        }
    }

    // Her düşman nasıl yürüyeceğine kendi karar verir
    protected abstract void moveBehavior(float delta, Player player);

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
}

package com.ozu.platformrunner.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.ozu.platformrunner.managers.ResourceManager;

public abstract class Enemy {
    protected Rectangle bounds;
    protected Texture texture;
    protected float speed = 100f; // Varsayılan hız

    public Enemy(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
        // Düşman görselini alıyoruz
        this.texture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_ENEMY);
    }

    // Her düşman farklı hareket eder, o yüzden abstract
    public abstract void update(float delta, Player player);

    // Çizim işlemi genelde aynıdır
    public void draw(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

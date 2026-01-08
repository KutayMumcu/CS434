package com.ozu.platformrunner.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.ozu.platformrunner.GameConstants;
import com.ozu.platformrunner.managers.ResourceManager;

public class Arrow implements Pool.Poolable {
    private final Rectangle bounds;
    private float speed = GameConstants.ARROW_SPEED;
    private int direction;
    private int damage = GameConstants.BOW_DAMAGE;
    private Texture texture;
    public boolean active;

    public Arrow() {
        this.bounds = new Rectangle(0, 0, 12, 6);
        this.active = false;
        this.texture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_PLATFORM);
    }

    @Override
    public void reset() {
        this.bounds.setPosition(0, 0);
        this.direction = 0;
        this.active = false;
    }

    public void init(float x, float y, int direction) {
        this.bounds.setPosition(x, y);
        this.direction = direction;
        this.active = true;
    }

    public void update(float delta) {
        if (!active) return;

        bounds.x += speed * direction * delta;

        // Deactivate when far off-screen (increased range to fix shooting issues at map edges)
        if (bounds.x < -100 || bounds.x > 2500) {
            active = false;
        }
    }

    public void draw(SpriteBatch batch) {
        if (active) {
            // Draw with yellow tint to distinguish from bullets
            batch.setColor(1f, 1f, 0.6f, 1f);
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
            batch.setColor(Color.WHITE);
        }
    }

    public Rectangle getBounds() { return bounds; }
    public int getDamage() { return damage; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }
    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }
}

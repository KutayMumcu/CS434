package com.ozu.platformrunner.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.ozu.platformrunner.utils.GameConstants;
import com.ozu.platformrunner.managers.ResourceManager;

public class Bullet implements Pool.Poolable {
    private final Rectangle bounds;
    private float speed = GameConstants.BULLET_SPEED;
    private int direction;
    private Texture texture;
    public boolean active;

    public Bullet() {
        this.bounds = new Rectangle(0, 0, 10, 5);
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

        // Despawn logic
        if (bounds.x < -GameConstants.PROJECTILE_DESPAWN_MARGIN ||
            bounds.x > GameConstants.MAP_LIMIT_X + GameConstants.PROJECTILE_DESPAWN_MARGIN) {
            active = false;
        }
    }

    public void draw(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public Rectangle getBounds() { return bounds; }
    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }
}

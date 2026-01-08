package com.ozu.platformrunner.entities.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ozu.platformrunner.managers.ResourceManager;

public class SwordSlash {
    private float x, y;
    private float width, height;
    private float duration;
    private float elapsedTime;
    private int direction;
    private Texture texture;

    public SwordSlash(float x, float y, int direction) {
        this.x = x;
        this.y = y;
        this.width = 60;
        this.height = 40;
        this.direction = direction;
        this.duration = 0.2f;
        this.elapsedTime = 0;
        this.texture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_WHITE_PIXEL);
    }

    public void update(float delta) {
        elapsedTime += delta;
        x += direction * 200 * delta;
    }

    public boolean isFinished() {
        return elapsedTime >= duration;
    }

    public void draw(SpriteBatch batch) {
        float alpha = 1.0f - (elapsedTime / duration);
        batch.setColor(1, 1, 1, alpha);
        batch.draw(texture, x, y, width, height);
        batch.setColor(Color.WHITE);
    }
}

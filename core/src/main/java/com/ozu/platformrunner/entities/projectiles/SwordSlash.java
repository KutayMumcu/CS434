package com.ozu.platformrunner.entities.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SwordSlash {
    private float x, y;
    private float width, height;
    private float duration;
    private float elapsedTime;
    private int direction;
    private static Texture slashTexture;

    public SwordSlash(float x, float y, int direction) {
        this.x = x;
        this.y = y;
        this.width = 60;
        this.height = 40;
        this.direction = direction;
        this.duration = 0.2f;
        this.elapsedTime = 0;

        // Generate texture if not exists
        if (slashTexture == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            slashTexture = new Texture(pixmap);
            pixmap.dispose();
        }
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
        batch.draw(slashTexture, x, y, width, height);
        batch.setColor(Color.WHITE);
    }

    public static void disposeStaticResources() {
        if (slashTexture != null) {
            slashTexture.dispose();
            slashTexture = null;
        }
    }
}

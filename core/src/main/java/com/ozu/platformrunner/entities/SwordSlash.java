package com.ozu.platformrunner.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SwordSlash {
    private float x, y;
    private float width, height;
    private float duration;
    private float elapsedTime;
    private int direction; // 1 for right, -1 for left
    private static Texture slashTexture;

    public SwordSlash(float x, float y, int direction) {
        this.x = x;
        this.y = y;
        this.width = 60;
        this.height = 40;
        this.direction = direction;
        this.duration = 0.2f; // Slash lasts 0.2 seconds
        this.elapsedTime = 0;

        // Create slash texture if not already created
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
        // Animate the slash moving forward
        x += direction * 200 * delta;
    }

    public boolean isFinished() {
        return elapsedTime >= duration;
    }

    public void draw(SpriteBatch batch) {
        // Calculate alpha based on time (fade out)
        float alpha = 1.0f - (elapsedTime / duration);

        // Draw as a white slash effect
        batch.setColor(1, 1, 1, alpha);

        // Simple rectangular slash
        batch.draw(slashTexture, x, y, width, height);

        // Reset color
        batch.setColor(Color.WHITE);
    }
}

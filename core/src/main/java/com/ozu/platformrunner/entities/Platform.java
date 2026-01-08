package com.ozu.platformrunner.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
    private final Rectangle bounds;
    private static Texture platformTexture;

    public Platform(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);

        // Generate texture if not exists
        if (platformTexture == null) {
            Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
            pixmap.setColor(new Color(0.4f, 0.3f, 0.2f, 1f));
            pixmap.fill();

            // Add border details
            pixmap.setColor(new Color(0.3f, 0.2f, 0.1f, 1f));
            pixmap.drawRectangle(0, 0, 64, 64);
            pixmap.drawRectangle(1, 1, 62, 62);

            platformTexture = new Texture(pixmap);
            pixmap.dispose();
        }
    }

    public void draw(SpriteBatch batch) {
        float tileSize = 64f;
        int tilesX = (int) Math.ceil(bounds.width / tileSize);
        int tilesY = (int) Math.ceil(bounds.height / tileSize);

        for (int i = 0; i < tilesX; i++) {
            for (int j = 0; j < tilesY; j++) {
                float x = bounds.x + i * tileSize;
                float y = bounds.y + j * tileSize;
                float w = Math.min(tileSize, bounds.width - i * tileSize);
                float h = Math.min(tileSize, bounds.height - j * tileSize);
                batch.draw(platformTexture, x, y, w, h);
            }
        }
    }

    public Rectangle getBounds() { return bounds; }

    public static void disposeStaticResources() {
        if (platformTexture != null) {
            platformTexture.dispose();
            platformTexture = null;
        }
    }
}

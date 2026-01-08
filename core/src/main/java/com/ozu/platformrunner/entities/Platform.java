package com.ozu.platformrunner.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.ozu.platformrunner.managers.ResourceManager;

public class Platform {
    private final Rectangle bounds;
    private Texture platformTexture;

    public Platform(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);

        // Get texture from the central Resource Manager
        platformTexture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_PLATFORM);
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
}

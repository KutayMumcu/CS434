package com.ozu.platformrunner.entities;

// Platform.java
import com.badlogic.gdx.math.Rectangle;

public class Platform {
    private final Rectangle bounds;

    public Platform(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

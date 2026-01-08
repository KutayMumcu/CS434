package com.ozu.platformrunner.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;

public class ResourceManager {
    private static ResourceManager instance;

    // Texture File Names (Also used as Keys)
    public static final String TEXTURE_IDLE = "Idle.png";
    public static final String TEXTURE_RUN = "Run.png";
    public static final String TEXTURE_JUMP = "Jump.png";
    public static final String TEXTURE_ATTACK = "Attack.png";
    public static final String TEXTURE_ENEMY_RIGHT = "enemyRight.png";
    public static final String TEXTURE_ENEMY_LEFT = "enemyLeft.png";
    public static final String TEXTURE_LIGHTBALL = "LightBall.png";

    // Procedural (Generated) Texture Keys
    public static final String TEXTURE_PLATFORM = "platform_proc";
    public static final String TEXTURE_WHITE_PIXEL = "white_pixel_proc";

    private HashMap<String, Texture> textures;

    private ResourceManager() {
        textures = new HashMap<>();
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public void loadAssets() {
        // 1. Load from Files
        loadTexture(TEXTURE_IDLE, TEXTURE_IDLE);
        loadTexture(TEXTURE_RUN, TEXTURE_RUN);
        loadTexture(TEXTURE_JUMP, TEXTURE_JUMP);
        loadTexture(TEXTURE_ATTACK, TEXTURE_ATTACK);
        loadTexture(TEXTURE_ENEMY_RIGHT, TEXTURE_ENEMY_RIGHT);
        loadTexture(TEXTURE_ENEMY_LEFT, TEXTURE_ENEMY_LEFT);
        loadTexture(TEXTURE_LIGHTBALL, TEXTURE_LIGHTBALL);

        // 2. Generate Procedural Textures
        generateProceduralTextures();
    }

    private void generateProceduralTextures() {
        // A) WHITE PIXEL
        if (!textures.containsKey(TEXTURE_WHITE_PIXEL)) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            textures.put(TEXTURE_WHITE_PIXEL, new Texture(pixmap));
            pixmap.dispose();
        }

        // B) PLATFORM TEXTURE
        if (!textures.containsKey(TEXTURE_PLATFORM)) {
            Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
            // Brown background
            pixmap.setColor(new Color(0.4f, 0.3f, 0.2f, 1f));
            pixmap.fill();

            // Border
            pixmap.setColor(new Color(0.3f, 0.2f, 0.1f, 1f));
            pixmap.drawRectangle(0, 0, 64, 64);
            pixmap.drawRectangle(1, 1, 62, 62);

            textures.put(TEXTURE_PLATFORM, new Texture(pixmap));
            pixmap.dispose();
        }
    }

    private void loadTexture(String key, String path) {
        if (Gdx.files.internal(path).exists()) {
            textures.put(key, new Texture(Gdx.files.internal(path)));
        } else {
            System.err.println("ERROR: File not found! -> " + path);
        }
    }

    public Texture getTexture(String key) {
        return textures.get(key);
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
    }
}

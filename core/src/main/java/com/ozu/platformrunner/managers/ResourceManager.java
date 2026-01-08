package com.ozu.platformrunner.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class ResourceManager {
    private static ResourceManager instance;
    public final AssetManager assetManager;

    // Player Textures
    public static final String TEXTURE_IDLE = "Idle.png";
    public static final String TEXTURE_RUN = "Run.png";
    public static final String TEXTURE_ATTACK = "Attack.png";
    public static final String TEXTURE_LIGHTBALL = "LightBall.png";
    public static final String TEXTURE_JUMP = "Jump.png";

    // Enemy Textures
    public static final String TEXTURE_ENEMY_RIGHT = "enemyRight.png";
    public static final String TEXTURE_ENEMY_LEFT = "enemyLeft.png";

    // Environment Textures
    public static final String TEXTURE_PLATFORM = "platform.png";

    private ResourceManager() {
        assetManager = new AssetManager();
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public void loadAllResources() {
        // Load Player
        assetManager.load(TEXTURE_IDLE, Texture.class);
        assetManager.load(TEXTURE_RUN, Texture.class);
        assetManager.load(TEXTURE_ATTACK, Texture.class);
        assetManager.load(TEXTURE_LIGHTBALL, Texture.class);
        assetManager.load(TEXTURE_JUMP, Texture.class);

        // Load Enemies
        assetManager.load(TEXTURE_ENEMY_RIGHT, Texture.class);
        assetManager.load(TEXTURE_ENEMY_LEFT, Texture.class);

        // Load Environment
        assetManager.load(TEXTURE_PLATFORM, Texture.class);

        assetManager.finishLoading();
    }

    public Texture getTexture(String name) {
        return assetManager.get(name, Texture.class);
    }

    public void dispose() {
        assetManager.dispose();
    }
}

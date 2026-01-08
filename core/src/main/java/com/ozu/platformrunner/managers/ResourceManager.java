package com.ozu.platformrunner.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class ResourceManager {
    private static ResourceManager instance;
    public final AssetManager assetManager;

    // --- MEVCUT SABİTLER ---
    public static final String TEXTURE_PLATFORM = "platform.png";
    // --- YENİ EKLENEN GÖRSELLER ---
    public static final String TEXTURE_CHAR_RIGHT = "charRight.png";
    public static final String TEXTURE_CHAR_LEFT = "charLeft.png";
    public static final String TEXTURE_ENEMY_RIGHT = "enemyRight.png";
    public static final String TEXTURE_ENEMY_LEFT = "enemyLeft.png";

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
        // Eskileri yükle
        assetManager.load(TEXTURE_PLATFORM, Texture.class);
        // assetManager.load(TEXTURE_ENEMY, Texture.class); // Zaten char.png ile aynı

        // Yenileri yükle
        assetManager.load(TEXTURE_CHAR_RIGHT, Texture.class);
        assetManager.load(TEXTURE_CHAR_LEFT, Texture.class);
        assetManager.load(TEXTURE_ENEMY_RIGHT, Texture.class);
        assetManager.load(TEXTURE_ENEMY_LEFT, Texture.class);

        assetManager.finishLoading();
    }

    public Texture getTexture(String name) {
        return assetManager.get(name, Texture.class);
    }

    public void dispose() {
        assetManager.dispose();
    }
}

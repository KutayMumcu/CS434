package com.ozu.platformrunner.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class ResourceManager {
    private static ResourceManager instance;
    public final AssetManager assetManager;

    // --- PLAYER ANİMASYON DOSYALARI ---
    public static final String TEXTURE_IDLE = "Idle.png";
    public static final String TEXTURE_RUN = "Run.png";
    public static final String TEXTURE_ATTACK = "Attack.png";
    public static final String TEXTURE_LIGHTBALL = "LightBall.png";
    public static final String TEXTURE_JUMP = "Jump.png";

    // --- DÜŞMAN GÖRSELLERİ ---
    public static final String TEXTURE_ENEMY_RIGHT = "enemyRight.png";
    public static final String TEXTURE_ENEMY_LEFT = "enemyLeft.png";

    // --- DİĞERLERİ ---
    public static final String TEXTURE_PLATFORM = "platform.png";

    // NOT: char.png ARTIK YOK. SİLDİK.

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
        // Player Animasyonları
        assetManager.load(TEXTURE_IDLE, Texture.class);
        assetManager.load(TEXTURE_RUN, Texture.class);
        assetManager.load(TEXTURE_ATTACK, Texture.class);
        assetManager.load(TEXTURE_LIGHTBALL, Texture.class);
        assetManager.load(TEXTURE_JUMP, Texture.class);

        // Düşman Görselleri
        assetManager.load(TEXTURE_ENEMY_RIGHT, Texture.class);
        assetManager.load(TEXTURE_ENEMY_LEFT, Texture.class);

        // Diğerleri
        assetManager.load(TEXTURE_PLATFORM, Texture.class);

        // char.png YÜKLEME SATIRINI TAMAMEN KALDIRDIK.
        // assetManager.load("char.png", Texture.class); <--- BU SATIR ARTIK YOK

        assetManager.finishLoading();
    }

    public Texture getTexture(String name) {
        return assetManager.get(name, Texture.class);
    }

    public void dispose() {
        assetManager.dispose();
    }
}

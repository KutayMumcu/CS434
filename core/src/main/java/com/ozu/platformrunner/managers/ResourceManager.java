package com.ozu.platformrunner.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

/**
 * Facade Pattern:
 * LibGDX'in karmaşık AssetManager yapısını basitleştirerek
 * dış dünyaya (istemciye) basit metotlar sunar.
 */
public class ResourceManager {
    private static ResourceManager instance; // Buna da Singleton diyebiliriz ama asıl olay Facade olması.
    public final AssetManager assetManager;

    // Dosya yolları (Hardcoded stringlerden kurtulmak için)
    public static final String TEXTURE_CHAR = "char.png";
    public static final String TEXTURE_PLATFORM = "platform.png";
    public static final String TEXTURE_ENEMY = "char.png";
    // İleride eklenecekler:
    // public static final String TEXTURE_ENEMY = "enemy.png";
    // public static final String SOUND_JUMP = "jump.wav";

    private ResourceManager() {
        assetManager = new AssetManager();
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    // 1. Tüm varlıkları yükle
    public void loadAllResources() {
        assetManager.load(TEXTURE_CHAR, Texture.class);
        assetManager.load(TEXTURE_PLATFORM, Texture.class);

        // Yükleme bitene kadar bekle (Senkronize)
        assetManager.finishLoading();
    }

    // 2. Basitleştirilmiş Erişim Metotları
    public Texture getTexture(String name) {
        return assetManager.get(name, Texture.class);
    }

    // 3. Temizlik
    public void dispose() {
        assetManager.dispose();
    }
}

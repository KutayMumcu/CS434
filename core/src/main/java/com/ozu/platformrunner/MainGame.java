package com.ozu.platformrunner;

import com.badlogic.gdx.Game;
import com.ozu.platformrunner.managers.GameManager;
import com.ozu.platformrunner.managers.ResourceManager;

public class MainGame extends Game {

    @Override
    public void create() {
        // 1. Kaynakları Yükle (Facade Kullanımı)
        ResourceManager.getInstance().loadAllResources();

        // 2. Oyun Durumunu Başlat (Singleton Kullanımı)
        GameManager.getInstance().setCurrentState(GameManager.GameState.PLAYING);

        // 3. Ekrana Geç
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        // Kaynakları temizle
        ResourceManager.getInstance().dispose();
    }
}

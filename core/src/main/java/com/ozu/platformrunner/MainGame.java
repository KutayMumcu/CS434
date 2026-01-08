package com.ozu.platformrunner;

import com.badlogic.gdx.Game;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.projectiles.SwordSlash;
import com.ozu.platformrunner.managers.GameManager;
import com.ozu.platformrunner.managers.ResourceManager;
import com.ozu.platformrunner.screens.MainMenuScreen;

public class MainGame extends Game {

    @Override
    public void create() {
        // Kaynakları yükle
        ResourceManager.getInstance().loadAllResources();

        // Oyun Durumunu Başlat
        GameManager.getInstance().setCurrentState(GameManager.GameState.MENU);

        // DİKKAT: Artık Menü ile başlıyoruz!
        // this parametresini geçiyoruz ki menü diğer ekranlara geçiş yapabilsin
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        // Kaynakları temizle
        ResourceManager.getInstance().dispose();

        // Dispose static textures to prevent memory leaks
        Enemy.disposeStaticResources();
        com.ozu.platformrunner.entities.Platform.disposeStaticResources();
        SwordSlash.disposeStaticResources();
    }
}

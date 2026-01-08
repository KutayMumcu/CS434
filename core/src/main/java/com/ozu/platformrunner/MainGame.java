package com.ozu.platformrunner;

import com.badlogic.gdx.Game;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.projectiles.SwordSlash;
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.managers.GameManager;
import com.ozu.platformrunner.managers.ResourceManager;
import com.ozu.platformrunner.screens.MainMenuScreen;

public class MainGame extends Game {

    @Override
    public void create() {
        ResourceManager.getInstance().loadAllResources();
        GameManager.getInstance().setCurrentState(GameManager.GameState.MENU);
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        ResourceManager.getInstance().dispose();

        // Static cleanup
        Enemy.disposeStaticResources();
        Platform.disposeStaticResources();
        SwordSlash.disposeStaticResources();
    }
}

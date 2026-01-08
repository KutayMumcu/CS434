package com.ozu.platformrunner;

import com.badlogic.gdx.Game;
import com.ozu.platformrunner.managers.GameManager;
import com.ozu.platformrunner.managers.ResourceManager;
import com.ozu.platformrunner.screens.MainMenuScreen;

public class MainGame extends Game {

    @Override
    public void create() {
        ResourceManager.getInstance().loadAssets();

        GameManager.getInstance().setCurrentState(GameManager.GameState.MENU);
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        ResourceManager.getInstance().dispose();
    }
}

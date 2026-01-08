package com.ozu.platformrunner.patterns.state;

import com.badlogic.gdx.Gdx;
import com.ozu.platformrunner.screens.GameScreen;
import com.ozu.platformrunner.ui.PauseMenuOverlay;

public class PausedState implements GameScreenState {
    private PauseMenuOverlay pauseMenu;

    @Override
    public void enter(GameScreen screen) {
        pauseMenu = new PauseMenuOverlay(screen);
        Gdx.input.setInputProcessor(pauseMenu.getStage());
    }

    @Override
    public void update(GameScreen screen, float delta) {
        // No updates during pause
    }

    @Override
    public void handleInput(GameScreen screen) {
        // Input handled by pause menu stage
    }

    @Override
    public void exit(GameScreen screen) {
        if (pauseMenu != null) {
            pauseMenu.dispose();
            pauseMenu = null;
        }
    }

    public PauseMenuOverlay getPauseMenu() { return pauseMenu; }
}

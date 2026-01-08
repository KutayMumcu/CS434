package com.ozu.platformrunner.patterns.state;

import com.badlogic.gdx.Gdx;
import com.ozu.platformrunner.screens.GameScreen;
import com.ozu.platformrunner.ui.PauseMenuOverlay;

public class PausedState implements GameScreenState {
    private PauseMenuOverlay pauseMenu;

    @Override
    public void enter(GameScreen screen) {
        System.out.println("PausedState: Oyun duraklatıldı");
        pauseMenu = new PauseMenuOverlay(screen);
        // Input processor'ı pause menü stage'ine ayarla
        Gdx.input.setInputProcessor(pauseMenu.getStage());
    }

    @Override
    public void update(GameScreen screen, float delta) {
        // Oyun güncellemelerini DURDUR
        // Sadece pause menüyü render et
    }

    @Override
    public void handleInput(GameScreen screen) {
        // Input pause menü tarafından işleniyor
    }

    @Override
    public void exit(GameScreen screen) {
        System.out.println("PausedState: Oyun devam ediyor");
        pauseMenu.dispose();
        pauseMenu = null;
    }

    public PauseMenuOverlay getPauseMenu() {
        return pauseMenu;
    }
}

package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.screens.GameScreen;

public interface GameScreenState {
    void enter(GameScreen screen);
    void update(GameScreen screen, float delta);
    void handleInput(GameScreen screen);
    void exit(GameScreen screen);
}

package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.screens.GameScreen;

public class PlayingState implements GameScreenState {

    @Override
    public void enter(GameScreen screen) {}

    @Override
    public void update(GameScreen screen, float delta) {
        // Update logic handled by GameScreen while in this state
    }

    @Override
    public void handleInput(GameScreen screen) {
        // Input logic handled by GameScreen while in this state
    }

    @Override
    public void exit(GameScreen screen) {}
}

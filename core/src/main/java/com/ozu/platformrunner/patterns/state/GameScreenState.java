package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.GameScreen;

public interface GameScreenState {
    // Duruma girerken çalışacak
    void enter(GameScreen screen);

    // Her karede çalışacak mantık
    void update(GameScreen screen, float delta);

    // Girdi işleme
    void handleInput(GameScreen screen);

    // Durumdan çıkarken çalışacak
    void exit(GameScreen screen);
}

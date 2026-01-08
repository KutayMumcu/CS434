package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.screens.GameScreen;

public class PlayingState implements GameScreenState {

    @Override
    public void enter(GameScreen screen) {
        // Oyun normal halde başladı
        System.out.println("PlayingState: Oyun devam ediyor");
    }

    @Override
    public void update(GameScreen screen, float delta) {
        // Normal oyun güncellemesi yapılacak
        // GameScreen'in render metodu bunu çağırır
    }

    @Override
    public void handleInput(GameScreen screen) {
        // GameScreen'in handleInput() metodu çağrılacak
    }

    @Override
    public void exit(GameScreen screen) {
        // Oyundan çıkış
    }
}

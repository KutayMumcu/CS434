package com.ozu.platformrunner.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.patterns.observer.GameEvent;
import com.ozu.platformrunner.patterns.observer.GameObserver;

public class HUD implements GameObserver {
    private Label healthLabel;
    private Label scoreLabel;
    private Label levelLabel;

    public HUD(Stage stage) { // Stage parametresi alıyor
        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        // Health label - top left
        healthLabel = new Label("Health: 100", style);
        healthLabel.setFontScale(2);
        healthLabel.setPosition(20, 440);
        stage.addActor(healthLabel);

        // Score label - top left, below health
        scoreLabel = new Label("Score: 0", style);
        scoreLabel.setFontScale(1.5f);
        scoreLabel.setPosition(20, 410);
        scoreLabel.setColor(Color.YELLOW);
        stage.addActor(scoreLabel);

        // Level label - top right
        levelLabel = new Label("Level: 1", style);
        levelLabel.setFontScale(1.5f);
        levelLabel.setPosition(650, 440);
        stage.addActor(levelLabel);
    }

    @Override
    public void onNotify(Player player, GameEvent event) {
        switch (event) {
            case HEALTH_CHANGED:
                healthLabel.setText("Health: " + player.getHealth());
                break;
            case PLAYER_DIED:
                healthLabel.setText("GAME OVER");
                healthLabel.setColor(Color.RED);
                break;
        }
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void updateLevel(int level) {
        levelLabel.setText("Level: " + level);
    }

    // dispose metoduna gerek kalmadı, Stage fontu temizler.
}

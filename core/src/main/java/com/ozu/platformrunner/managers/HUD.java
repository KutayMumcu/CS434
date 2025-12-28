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

    public HUD(Stage stage) { // Stage parametresi alıyor
        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        healthLabel = new Label("Health: 100", style);
        healthLabel.setFontScale(2);
        healthLabel.setPosition(20, 440); // Sol üst köşe (Ekran koordinatı)

        stage.addActor(healthLabel); // Label'ı Stage'e ekle
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

    // dispose metoduna gerek kalmadı, Stage fontu temizler.
}

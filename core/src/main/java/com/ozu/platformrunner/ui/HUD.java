package com.ozu.platformrunner.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.patterns.observer.GameEvent;
import com.ozu.platformrunner.patterns.observer.GameObserver;

public class HUD implements GameObserver {
    private Label healthLabel;
    private Label scoreLabel;
    private Label levelLabel;

    public HUD(Stage stage) {
        // --- Tablo Kurulumu ---
        Table table = new Table();
        table.setFillParent(true);
        table.top(); // Tabloyu en tepeye yasla (UI aşağı kaymasın)
        stage.addActor(table);

        // --- Stiller ---
        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        healthLabel = new Label("Health: 100", style);
        healthLabel.setFontScale(2);

        scoreLabel = new Label("Score: 0", style);
        scoreLabel.setFontScale(1.5f);
        scoreLabel.setColor(Color.YELLOW);

        levelLabel = new Label("Level: 1", style);
        levelLabel.setFontScale(1.5f);

        // --- DÜZENLEME ---

        // 1. SATIR: Sol tarafta Health, Sağ taraf BOŞ (Menu butonu için)
        table.add(healthLabel).left().padLeft(20).padTop(20).expandX();
        table.add().expandX(); // Sağ üst köşeyi Menu butonuna rezerve et

        table.row(); // Alt satıra geç

        // 2. SATIR: Solda Score, Sağda Level
        table.add(scoreLabel).left().padLeft(20).padTop(10);
        table.add(levelLabel).right().padRight(20).padTop(10);
    }

    @Override
    public void onNotify(Player player, GameEvent event) {
        // ARTIK DOĞRUDAN Player ALIYORUZ (Hata buradaydı)
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
}

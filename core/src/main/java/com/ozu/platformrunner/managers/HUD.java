package com.ozu.platformrunner.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.patterns.observer.GameEvent;
import com.ozu.platformrunner.patterns.observer.GameObserver;

public class HUD implements GameObserver {
    private final BitmapFont font;
    private String healthText;

    public HUD() {
        // LibGDX'in varsayılan fontunu kullan (Arial benzeri)
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2); // Yazıyı biraz büyüt
        healthText = "Health: 100";
    }

    // Observer Metodu: Haber gelince burası çalışır
    @Override
    public void onNotify(Player player, GameEvent event) {
        switch (event) {
            case HEALTH_CHANGED:
                healthText = "Health: " + player.getHealth();
                break;
            case PLAYER_DIED:
                healthText = "GAME OVER";
                font.setColor(Color.RED);
                break;
        }
    }

    // Ekrana çizme metodu
    public void render(SpriteBatch batch) {
        // Sol üst köşeye yaz (Koordinatlar değişebilir)
        font.draw(batch, healthText, 20, 460);
    }

    public void dispose() {
        font.dispose();
    }
}

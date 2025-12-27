package com.ozu.platformrunner.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.patterns.memento.GameStateMemento;
import com.ozu.platformrunner.patterns.strategy.BowStrategy;
import com.ozu.platformrunner.patterns.strategy.SwordStrategy;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.json";
    private final Json json;

    public SaveManager() {
        json = new Json();
    }

    // KAYDETME (SAVE)
    public void saveGame(Player player) {
        // 1. Memento nesnesini oluştur ve verileri doldur
        GameStateMemento memento = new GameStateMemento();

        memento.playerX = player.getBounds().x;
        memento.playerY = player.getBounds().y;
        memento.playerHealth = player.getHealth();

        // GameManager'dan gelen veriler
        memento.score = GameManager.getInstance().getScore();
        memento.level = GameManager.getInstance().getCurrentLevel();

        // Silah bilgisini dinamik al
        if (player.getAttackStrategy() instanceof BowStrategy) {
            memento.currentWeapon = "BOW";
        } else {
            memento.currentWeapon = "SWORD";
        }

        // 2. JSON olarak dosyaya yaz
        FileHandle file = Gdx.files.local(SAVE_FILE);
        file.writeString(json.toJson(memento), false);

        System.out.println("Oyun Kaydedildi! Dosya: " + file.path());
    }

    // YÜKLEME (LOAD)
    public void loadGame(Player player) {
        FileHandle file = Gdx.files.local(SAVE_FILE);

        if (!file.exists()) {
            System.out.println("Kayıt dosyası bulunamadı!");
            return;
        }

        // 1. Dosyadan oku ve Memento'ya çevir
        GameStateMemento memento = json.fromJson(GameStateMemento.class, file.readString());

        // 2. Verileri oyuna geri yükle
        player.getBounds().setPosition(memento.playerX, memento.playerY);
        // Canı yüklemek için (takeDamage yerine setHealth lazım veya hileyle):
        // Player'a setHealth metodu ekleyeceğiz.
        player.setHealth(memento.playerHealth);

        GameManager.getInstance().resetGame(); // Önce sıfırla
        GameManager.getInstance().addScore(memento.score);
        // Level set etme metodu GameManager'da olmalı

        if ("BOW".equals(memento.currentWeapon)) {
            player.equipWeapon(new BowStrategy());
        } else {
            player.equipWeapon(new SwordStrategy());
        }

        System.out.println("Oyun Yüklendi! Puan: " + memento.score);
    }
}

package com.ozu.platformrunner.patterns.memento;

import com.badlogic.gdx.utils.Array;

// Kaydedilecek veriler - TAM oyun durumu
public class GameStateMemento {
    // Player temel verileri
    public float playerX;
    public float playerY;
    public int playerHealth;
    public int score;
    public int level;
    public String currentWeapon; // "SWORD" veya "BOW"

    // YENİ: Player tam durumu
    public float playerVelocityX;
    public float playerVelocityY;
    public int playerFacingDirection;
    public boolean playerOnGround;

    // YENİ: Tam oyun durumu
    public Array<EnemyData> enemies;
    public Array<BulletData> bullets;
    public Array<PlatformData> platforms;

    // JSON serileştirme için boş constructor şarttır
    public GameStateMemento() {
        enemies = new Array<>();
        bullets = new Array<>();
        platforms = new Array<>();
    }
}

package com.ozu.platformrunner.patterns.memento;

// Kaydedilecek veriler
public class GameStateMemento {
    public float playerX;
    public float playerY;
    public int playerHealth;
    public int score;
    public int level;
    public String currentWeapon; // "SWORD" veya "BOW"

    // JSON serileştirme için boş constructor şarttır
    public GameStateMemento() {}
}

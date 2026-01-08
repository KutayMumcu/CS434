package com.ozu.platformrunner.patterns.memento;

import com.badlogic.gdx.utils.Array;

public class GameStateMemento {
    // Player Physics & Stats
    public float playerX, playerY;
    public float playerVelocityX, playerVelocityY;
    public int playerHealth, playerFacingDirection;
    public boolean playerOnGround;
    public String currentWeapon;

    // Global Stats
    public int score, level;

    // Entity Lists
    public Array<EnemyData> enemies;
    public Array<BulletData> bullets;
    public Array<ArrowData> arrows;
    public Array<PlatformData> platforms;

    public GameStateMemento() {
        enemies = new Array<>();
        bullets = new Array<>();
        arrows = new Array<>();
        platforms = new Array<>();
    }
}

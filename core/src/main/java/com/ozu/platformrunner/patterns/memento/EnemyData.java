package com.ozu.platformrunner.patterns.memento;

public class EnemyData {
    public String type;
    public float x, y;
    public float velocityX, velocityY;
    public int currentHealth;

    // PatrollingEnemy specific
    public float startX;
    public int direction;

    public EnemyData() {}
}

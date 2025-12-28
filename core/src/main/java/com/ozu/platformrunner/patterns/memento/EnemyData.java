package com.ozu.platformrunner.patterns.memento;

public class EnemyData {
    public String type;  // "PatrollingEnemy" or "ChasingEnemy"
    public float x;
    public float y;
    public int currentHealth;
    public float velocityX;
    public float velocityY;

    // PatrollingEnemy specific fields
    public float startX;
    public int direction;

    // JSON serialization için boş constructor
    public EnemyData() {}
}

package com.ozu.platformrunner.utils;

public class GameConstants {

    // Player
    public static final int PLAYER_MAX_HEALTH = 100;
    public static final float PLAYER_MOVE_SPEED = 200f;
    public static final float PLAYER_JUMP_VELOCITY = 450f;
    public static final float PLAYER_GRAVITY = -900f;
    public static final float PLAYER_INVULNERABILITY_TIME = 1.0f;

    // Damage
    public static final int ENEMY_COLLISION_DAMAGE = 12;
    public static final int ARROW_DAMAGE = 15;
    public static final int SWORD_DAMAGE = 10;
    public static final int BOW_DAMAGE = 8;

    // Knockback
    public static final float ENEMY_COLLISION_KNOCKBACK = 300f;
    public static final float ARROW_HIT_KNOCKBACK = 250f;

    // Score
    public static final int SCORE_KILL_PATROLLING_ENEMY = 100;
    public static final int SCORE_KILL_CHASING_ENEMY = 150;
    public static final int SCORE_KILL_BOSS_ENEMY = 500;
    public static final int SCORE_COMPLETE_LEVEL = 1000;

    // World
    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = 480f;
    public static final float MAP_LIMIT_X = 2000f;
    public static final float GRAVITY = -900f;

    // Collision & Projectiles
    public static final float PLATFORM_TOP_THRESHOLD = 2f;
    public static final float BULLET_SPEED = 400f;
    public static final float ARROW_SPEED = 300f;
    public static final float PROJECTILE_DESPAWN_MARGIN = 100f;

    // Decorator
    public static final int DOUBLE_SHOT_COUNT = 2;
    public static final float DOUBLE_SHOT_OFFSET = 10f;
    public static final float RAPID_FIRE_COOLDOWN = 0.15f;

    // Enemy
    public static final int ENEMY_DEFAULT_HEALTH = 30;
    public static final int BOSS_ENEMY_HEALTH = 100;

    private GameConstants() {}
}

package com.ozu.platformrunner.utils;

public class GameConstants {

    // --- Player Stats ---
    public static final int PLAYER_MAX_HEALTH = 100;
    public static final float PLAYER_MOVE_SPEED = 200f;
    public static final float PLAYER_JUMP_VELOCITY = 450f;
    public static final float PLAYER_GRAVITY = -900f;
    public static final float PLAYER_INVULNERABILITY_TIME = 1.0f;
    public static final float PLAYER_ATTACK_COOLDOWN = 0.5f;

    // --- Player Dimensions ---
    public static final float PLAYER_WIDTH = 30f;
    public static final float PLAYER_HEIGHT = 40f;

    // --- Weapon & Combat (Sword) ---
    public static final float SWORD_RANGE_X = 80f;
    public static final float SWORD_RANGE_Y = 50f;
    public static final float SWORD_HIT_KNOCKBACK = 30f;

    // --- Weapon & Combat (Bow) ---
    public static final float BOW_OFFSET_X_RIGHT = 25f;
    public static final float BOW_OFFSET_X_LEFT = 10f;
    public static final float BOW_OFFSET_Y = 10f;

    // --- Damage Values ---
    public static final int ENEMY_COLLISION_DAMAGE = 12;
    public static final int ARROW_DAMAGE = 15;
    public static final int SWORD_DAMAGE = 10;
    public static final int BOW_DAMAGE = 8;

    // --- General Knockback ---
    public static final float ENEMY_COLLISION_KNOCKBACK = 300f;
    public static final float ARROW_HIT_KNOCKBACK = 250f;

    // --- World & Physics ---
    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = 480f;
    public static final float MAP_LIMIT_X = 2000f;
    public static final float GRAVITY = -900f;

    // --- Physics Thresholds ---
    public static final float VELOCITY_EPSILON = 0.1f;
    public static final float PLATFORM_TOP_THRESHOLD = 2f;

    // --- Projectiles ---
    public static final float BULLET_SPEED = 400f;
    public static final float ARROW_SPEED = 300f;
    public static final float PROJECTILE_DESPAWN_MARGIN = 100f;

    // --- Decorator Power-ups ---
    public static final int DOUBLE_SHOT_COUNT = 2;
    public static final float DOUBLE_SHOT_OFFSET = 10f;
    public static final float RAPID_FIRE_COOLDOWN = 0.15f;

    // --- Enemy Stats ---
    public static final int ENEMY_DEFAULT_HEALTH = 30;
    public static final int BOSS_ENEMY_HEALTH = 100;

    // --- Scoring ---
    public static final int SCORE_KILL_PATROLLING_ENEMY = 100;
    public static final int SCORE_KILL_CHASING_ENEMY = 150;
    public static final int SCORE_KILL_BOSS_ENEMY = 500;
    public static final int SCORE_COMPLETE_LEVEL = 1000;

    private GameConstants() {}
}

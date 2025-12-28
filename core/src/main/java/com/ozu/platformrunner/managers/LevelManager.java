package com.ozu.platformrunner.managers;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Enemy;
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.patterns.factory.EnemyFactory;

public class LevelManager {

    // Seviye ID'sine göre platformları ve düşmanları doldurur
    public static void loadLevel(int levelId, Array<Platform> platforms, Array<Enemy> enemies) {
        platforms.clear();
        enemies.clear();

        // Varsayılan Zemin (Her seviyede olsun)
        platforms.add(new Platform(0, 0, 2000, 30));

        switch (levelId) {
            case 1:
                setupLevel1(platforms, enemies);
                break;
            case 2:
                setupLevel2(platforms, enemies);
                break;
            case 3:
                setupLevel3(platforms, enemies);
                break;
            default:
                setupLevel1(platforms, enemies); // Hata olursa Level 1 aç
                break;
        }
    }

    private static void setupLevel1(Array<Platform> platforms, Array<Enemy> enemies) {
        // Kolay Seviye
        platforms.add(new Platform(300, 150, 200, 30));
        platforms.add(new Platform(600, 250, 200, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 350, 180));
    }

    private static void setupLevel2(Array<Platform> platforms, Array<Enemy> enemies) {
        // Orta Seviye
        platforms.add(new Platform(200, 120, 150, 30));
        platforms.add(new Platform(450, 200, 150, 30));
        platforms.add(new Platform(700, 300, 150, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 500, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 720, 330));
    }

    private static void setupLevel3(Array<Platform> platforms, Array<Enemy> enemies) {
        // Zor Seviye
        platforms.add(new Platform(150, 100, 100, 30));
        platforms.add(new Platform(350, 200, 100, 30));
        platforms.add(new Platform(550, 300, 100, 30));
        platforms.add(new Platform(750, 400, 100, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 200, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 600, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 360, 230));
    }
}

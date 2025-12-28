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
            case 4:
                setupLevel4(platforms, enemies);
                break;
            case 5:
                setupLevel5(platforms, enemies);
                break;
            case 6:
                setupLevel6(platforms, enemies);
                break;
            case 7:
                setupLevel7(platforms, enemies);
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

    private static void setupLevel4(Array<Platform> platforms, Array<Enemy> enemies) {
        // Level 4: "Vertical Challenge" - Ascending platformer
        // Focus on vertical movement and precision jumping

        // Ascending staircase pattern with increasing difficulty
        platforms.add(new Platform(100, 80, 150, 30));
        platforms.add(new Platform(300, 140, 130, 30));
        platforms.add(new Platform(500, 210, 120, 30));
        platforms.add(new Platform(700, 290, 110, 30));
        platforms.add(new Platform(900, 380, 100, 30));

        // Additional mid-air platforms for lateral movement
        platforms.add(new Platform(250, 240, 100, 30));
        platforms.add(new Platform(600, 170, 90, 30));

        // Two patrolling enemies on mid-height platforms to add pressure
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 520, 240));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 920, 410));
    }

    private static void setupLevel5(Array<Platform> platforms, Array<Enemy> enemies) {
        // Level 5: "Enemy Gauntlet" - Combat intensive
        // Many enemies with moderate platform complexity

        // Platform layout - creates combat arenas
        platforms.add(new Platform(200, 100, 180, 30));
        platforms.add(new Platform(450, 180, 160, 30));
        platforms.add(new Platform(700, 120, 180, 30));
        platforms.add(new Platform(950, 200, 160, 30));
        platforms.add(new Platform(350, 280, 140, 30));

        // Enemy gauntlet - 5 enemies (mix of types)
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 150, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 220, 130));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 500, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 720, 150));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 970, 230));
    }

    private static void setupLevel6(Array<Platform> platforms, Array<Enemy> enemies) {
        // Level 6: "The Maze" - Complex navigation
        // Many platforms creating maze-like structure with multiple paths

        // Lower maze section
        platforms.add(new Platform(150, 80, 120, 30));
        platforms.add(new Platform(320, 80, 120, 30));
        platforms.add(new Platform(490, 80, 120, 30));

        // Middle maze section - creates corridors
        platforms.add(new Platform(100, 160, 100, 30));
        platforms.add(new Platform(250, 160, 100, 30));
        platforms.add(new Platform(400, 160, 100, 30));
        platforms.add(new Platform(550, 160, 100, 30));
        platforms.add(new Platform(700, 160, 100, 30));

        // Upper maze section
        platforms.add(new Platform(180, 250, 110, 30));
        platforms.add(new Platform(350, 250, 110, 30));
        platforms.add(new Platform(520, 250, 110, 30));

        // Top platforms
        platforms.add(new Platform(270, 340, 120, 30));
        platforms.add(new Platform(600, 340, 120, 30));

        // Strategically placed enemies in choke points
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 270, 190));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 200, 280));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 540, 280));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 290, 370));
    }

    private static void setupLevel7(Array<Platform> platforms, Array<Enemy> enemies) {
        // Level 7: "Final Showdown" - Ultimate challenge
        // Complex platforming + many aggressive enemies

        // Complex platform layout with difficult jumps
        platforms.add(new Platform(120, 90, 100, 30));
        platforms.add(new Platform(280, 170, 90, 30));
        platforms.add(new Platform(450, 110, 85, 30));
        platforms.add(new Platform(600, 200, 95, 30));
        platforms.add(new Platform(780, 130, 90, 30));
        platforms.add(new Platform(920, 240, 100, 30));

        // Additional floating platforms for escape routes
        platforms.add(new Platform(350, 280, 80, 30));
        platforms.add(new Platform(650, 320, 85, 30));
        platforms.add(new Platform(200, 350, 90, 30));

        // Boss-level enemy count - 6 enemies (mostly chasing for aggression)
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 150, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 300, 200));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 470, 140));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 620, 230));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 800, 160));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 940, 270));
    }
}

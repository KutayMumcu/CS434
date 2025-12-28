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

        // NO DEFAULT GROUND - Each level now has segmented ground with deadly holes!

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
        // Easy Level - Wide ground segments with small gaps
        // Ground platforms with gaps
        platforms.add(new Platform(0, 0, 300, 30));      // Starting platform
        platforms.add(new Platform(380, 0, 250, 30));    // Gap: 80 units
        platforms.add(new Platform(710, 0, 300, 30));    // Gap: 80 units
        platforms.add(new Platform(1090, 0, 400, 30));   // Gap: 80 units
        platforms.add(new Platform(1570, 0, 430, 30));   // Gap: 80 units - extends to end

        // Elevated platforms
        platforms.add(new Platform(300, 150, 200, 30));
        platforms.add(new Platform(600, 250, 200, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 350, 180));
    }

    private static void setupLevel2(Array<Platform> platforms, Array<Enemy> enemies) {
        // Medium Level - Moderate gaps, requires careful jumping
        // Ground platforms with larger gaps
        platforms.add(new Platform(0, 0, 280, 30));      // Starting platform
        platforms.add(new Platform(400, 0, 220, 30));    // Gap: 120 units
        platforms.add(new Platform(740, 0, 280, 30));    // Gap: 120 units
        platforms.add(new Platform(1140, 0, 300, 30));   // Gap: 120 units
        platforms.add(new Platform(1560, 0, 440, 30));   // Gap: 120 units

        // Elevated platforms
        platforms.add(new Platform(200, 120, 150, 30));
        platforms.add(new Platform(450, 200, 150, 30));
        platforms.add(new Platform(700, 300, 150, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 500, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 720, 330));
    }

    private static void setupLevel3(Array<Platform> platforms, Array<Enemy> enemies) {
        // Hard Level - 1 Boss, wider gaps
        // Ground platforms with challenging gaps
        platforms.add(new Platform(0, 0, 250, 30));      // Starting platform
        platforms.add(new Platform(410, 0, 200, 30));    // Gap: 160 units
        platforms.add(new Platform(770, 0, 180, 30));    // Gap: 160 units
        platforms.add(new Platform(1110, 0, 250, 30));   // Gap: 160 units
        platforms.add(new Platform(1520, 0, 480, 30));   // Gap: 160 units

        // Elevated platforms
        platforms.add(new Platform(150, 100, 100, 30));
        platforms.add(new Platform(350, 200, 100, 30));
        platforms.add(new Platform(550, 300, 100, 30));
        platforms.add(new Platform(750, 400, 100, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 600, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 200, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 360, 230));
    }

    private static void setupLevel4(Array<Platform> platforms, Array<Enemy> enemies) {
        // Level 4: "Vertical Challenge" - Ascending platformer with 1 Boss
        // Focus on vertical movement and precision jumping with deadly gaps

        // Sparse ground - very dangerous to fall!
        platforms.add(new Platform(0, 0, 200, 30));      // Starting platform
        platforms.add(new Platform(420, 0, 180, 30));    // Gap: 220 units - WIDE!
        platforms.add(new Platform(820, 0, 200, 30));    // Gap: 220 units
        platforms.add(new Platform(1240, 0, 300, 30));   // Gap: 220 units
        platforms.add(new Platform(1760, 0, 240, 30));   // Gap: 220 units

        // Ascending staircase pattern with increasing difficulty
        platforms.add(new Platform(100, 80, 150, 30));
        platforms.add(new Platform(300, 140, 130, 30));
        platforms.add(new Platform(500, 210, 120, 30));
        platforms.add(new Platform(700, 290, 110, 30));
        platforms.add(new Platform(900, 380, 100, 30));

        // Additional mid-air platforms for lateral movement
        platforms.add(new Platform(250, 240, 100, 30));
        platforms.add(new Platform(600, 170, 90, 30));

        // Boss on high platform + one patrolling enemy
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 850, 410));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 520, 240));
    }

    private static void setupLevel5(Array<Platform> platforms, Array<Enemy> enemies) {
        // Level 5: "Enemy Gauntlet" - Combat intensive with 2 Bosses
        // Many enemies with moderate platform complexity + deadly pits

        // Fragmented ground - combat arenas separated by death pits
        platforms.add(new Platform(0, 0, 220, 30));      // Arena 1
        platforms.add(new Platform(380, 0, 200, 30));    // Gap: 160 units
        platforms.add(new Platform(740, 0, 220, 30));    // Gap: 160 units
        platforms.add(new Platform(1120, 0, 250, 30));   // Gap: 160 units
        platforms.add(new Platform(1530, 0, 470, 30));   // Gap: 160 units

        // Platform layout - creates combat arenas
        platforms.add(new Platform(200, 100, 180, 30));
        platforms.add(new Platform(450, 180, 160, 30));
        platforms.add(new Platform(700, 120, 180, 30));
        platforms.add(new Platform(950, 200, 160, 30));
        platforms.add(new Platform(350, 280, 140, 30));

        // 2 Bosses + 3 regular enemies
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 300, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 950, 230));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 150, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 500, 210));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 720, 150));
    }

    private static void setupLevel6(Array<Platform> platforms, Array<Enemy> enemies) {
        // Level 6: "The Maze" - Complex navigation with 3 Bosses
        // Many platforms creating maze-like structure with deadly drops everywhere!

        // Very fragmented ground - extreme danger!
        platforms.add(new Platform(0, 0, 180, 30));      // Starting
        platforms.add(new Platform(340, 0, 160, 30));    // Gap: 160 units
        platforms.add(new Platform(660, 0, 150, 30));    // Gap: 160 units
        platforms.add(new Platform(970, 0, 180, 30));    // Gap: 160 units
        platforms.add(new Platform(1310, 0, 200, 30));   // Gap: 160 units
        platforms.add(new Platform(1670, 0, 330, 30));   // Gap: 160 units

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

        // 3 Bosses strategically placed + 2 regular enemies
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 270, 190));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 370, 280));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 620, 370));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 200, 280));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 540, 280));
    }

    private static void setupLevel7(Array<Platform> platforms, Array<Enemy> enemies) {
        // Level 7: "Final Showdown" - Ultimate challenge
        // Complex platforming + many aggressive enemies + MAXIMUM DANGER GAPS!

        // NIGHTMARE GROUND - Extremely fragmented with huge gaps!
        platforms.add(new Platform(0, 0, 150, 30));      // Starting - very small!
        platforms.add(new Platform(400, 0, 140, 30));    // Gap: 250 units - HUGE!
        platforms.add(new Platform(790, 0, 130, 30));    // Gap: 250 units - HUGE!
        platforms.add(new Platform(1170, 0, 150, 30));   // Gap: 250 units - HUGE!
        platforms.add(new Platform(1570, 0, 200, 30));   // Gap: 250 units - HUGE!
        platforms.add(new Platform(1920, 0, 80, 30));    // Gap: 150 units - Final tiny platform!

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

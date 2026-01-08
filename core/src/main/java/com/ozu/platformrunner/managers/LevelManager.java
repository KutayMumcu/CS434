package com.ozu.platformrunner.managers;

import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.patterns.factory.EnemyFactory;

public class LevelManager {

    public static void loadLevel(int levelId, Array<Platform> platforms, Array<Enemy> enemies) {
        platforms.clear();
        enemies.clear();

        switch (levelId) {
            case 1: setupLevel1(platforms, enemies); break;
            case 2: setupLevel2(platforms, enemies); break;
            case 3: setupLevel3(platforms, enemies); break;
            case 4: setupLevel4(platforms, enemies); break;
            case 5: setupLevel5(platforms, enemies); break;
            case 6: setupLevel6(platforms, enemies); break;
            case 7: setupLevel7(platforms, enemies); break;
            default: setupLevel1(platforms, enemies); break;
        }
    }

    private static void setupLevel1(Array<Platform> platforms, Array<Enemy> enemies) {
        platforms.add(new Platform(0, 0, 300, 30));
        platforms.add(new Platform(380, 0, 250, 30));
        platforms.add(new Platform(710, 0, 300, 30));
        platforms.add(new Platform(1090, 0, 400, 30));
        platforms.add(new Platform(1570, 0, 430, 30));

        platforms.add(new Platform(300, 150, 200, 30));
        platforms.add(new Platform(600, 250, 200, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 350, 180));
    }

    private static void setupLevel2(Array<Platform> platforms, Array<Enemy> enemies) {
        platforms.add(new Platform(0, 0, 280, 30));
        platforms.add(new Platform(400, 0, 220, 30));
        platforms.add(new Platform(740, 0, 280, 30));
        platforms.add(new Platform(1140, 0, 300, 30));
        platforms.add(new Platform(1560, 0, 440, 30));

        platforms.add(new Platform(200, 120, 150, 30));
        platforms.add(new Platform(450, 200, 150, 30));
        platforms.add(new Platform(700, 300, 150, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 500, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 720, 330));
    }

    private static void setupLevel3(Array<Platform> platforms, Array<Enemy> enemies) {
        platforms.add(new Platform(0, 0, 250, 30));
        platforms.add(new Platform(410, 0, 200, 30));
        platforms.add(new Platform(770, 0, 180, 30));
        platforms.add(new Platform(1110, 0, 250, 30));
        platforms.add(new Platform(1520, 0, 480, 30));

        platforms.add(new Platform(150, 100, 100, 30));
        platforms.add(new Platform(350, 200, 100, 30));
        platforms.add(new Platform(550, 300, 100, 30));
        platforms.add(new Platform(750, 400, 100, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 600, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 200, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 360, 230));
    }

    private static void setupLevel4(Array<Platform> platforms, Array<Enemy> enemies) {
        platforms.add(new Platform(0, 0, 200, 30));
        platforms.add(new Platform(420, 0, 180, 30));
        platforms.add(new Platform(820, 0, 200, 30));
        platforms.add(new Platform(1240, 0, 300, 30));
        platforms.add(new Platform(1760, 0, 240, 30));

        platforms.add(new Platform(100, 80, 150, 30));
        platforms.add(new Platform(300, 140, 130, 30));
        platforms.add(new Platform(500, 210, 120, 30));
        platforms.add(new Platform(700, 290, 110, 30));
        platforms.add(new Platform(900, 380, 100, 30));

        platforms.add(new Platform(250, 240, 100, 30));
        platforms.add(new Platform(600, 170, 90, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 850, 410));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 520, 240));
    }

    private static void setupLevel5(Array<Platform> platforms, Array<Enemy> enemies) {
        platforms.add(new Platform(0, 0, 220, 30));
        platforms.add(new Platform(380, 0, 200, 30));
        platforms.add(new Platform(740, 0, 220, 30));
        platforms.add(new Platform(1120, 0, 250, 30));
        platforms.add(new Platform(1530, 0, 470, 30));

        platforms.add(new Platform(200, 100, 180, 30));
        platforms.add(new Platform(450, 180, 160, 30));
        platforms.add(new Platform(700, 120, 180, 30));
        platforms.add(new Platform(950, 200, 160, 30));
        platforms.add(new Platform(350, 280, 140, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 300, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 950, 230));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 150, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 500, 210));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 720, 150));
    }

    private static void setupLevel6(Array<Platform> platforms, Array<Enemy> enemies) {
        platforms.add(new Platform(0, 0, 180, 30));
        platforms.add(new Platform(340, 0, 160, 30));
        platforms.add(new Platform(660, 0, 150, 30));
        platforms.add(new Platform(970, 0, 180, 30));
        platforms.add(new Platform(1310, 0, 200, 30));
        platforms.add(new Platform(1670, 0, 330, 30));

        platforms.add(new Platform(150, 80, 120, 30));
        platforms.add(new Platform(320, 80, 120, 30));
        platforms.add(new Platform(490, 80, 120, 30));

        platforms.add(new Platform(100, 160, 100, 30));
        platforms.add(new Platform(250, 160, 100, 30));
        platforms.add(new Platform(400, 160, 100, 30));
        platforms.add(new Platform(550, 160, 100, 30));
        platforms.add(new Platform(700, 160, 100, 30));

        platforms.add(new Platform(180, 250, 110, 30));
        platforms.add(new Platform(350, 250, 110, 30));
        platforms.add(new Platform(520, 250, 110, 30));

        platforms.add(new Platform(270, 340, 120, 30));
        platforms.add(new Platform(600, 340, 120, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 270, 190));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 370, 280));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 620, 370));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 200, 280));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 540, 280));
    }

    private static void setupLevel7(Array<Platform> platforms, Array<Enemy> enemies) {
        platforms.add(new Platform(0, 0, 150, 30));
        platforms.add(new Platform(400, 0, 140, 30));
        platforms.add(new Platform(790, 0, 130, 30));
        platforms.add(new Platform(1170, 0, 150, 30));
        platforms.add(new Platform(1570, 0, 200, 30));
        platforms.add(new Platform(1920, 0, 80, 30));

        platforms.add(new Platform(120, 90, 100, 30));
        platforms.add(new Platform(280, 170, 90, 30));
        platforms.add(new Platform(450, 110, 85, 30));
        platforms.add(new Platform(600, 200, 95, 30));
        platforms.add(new Platform(780, 130, 90, 30));
        platforms.add(new Platform(920, 240, 100, 30));

        platforms.add(new Platform(350, 280, 80, 30));
        platforms.add(new Platform(650, 320, 85, 30));
        platforms.add(new Platform(200, 350, 90, 30));

        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 150, 30));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 300, 200));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 470, 140));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 620, 230));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 800, 160));
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 940, 270));
    }
}

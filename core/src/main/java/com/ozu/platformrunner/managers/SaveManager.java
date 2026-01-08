package com.ozu.platformrunner.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.ozu.platformrunner.entities.*;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.enemies.PatrollingEnemy;
import com.ozu.platformrunner.entities.projectiles.Arrow;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.patterns.factory.EnemyFactory;
import com.ozu.platformrunner.patterns.factory.EnemyFactory.EnemyType;
import com.ozu.platformrunner.patterns.memento.*;
import com.ozu.platformrunner.patterns.strategy.BowStrategy;
import com.ozu.platformrunner.patterns.strategy.SwordStrategy;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.json";
    private final Json json;

    public SaveManager() {
        json = new Json();
    }

    public GameStateMemento createMemento(Player player, Array<Enemy> enemies, Array<Bullet> bullets, Array<Arrow> arrows, Array<Platform> platforms) {
        GameStateMemento memento = new GameStateMemento();

        // Capture Player State
        memento.playerX = player.getBounds().x;
        memento.playerY = player.getBounds().y;
        memento.playerHealth = player.getHealth();
        memento.playerVelocityX = player.getVelocity().x;
        memento.playerVelocityY = player.getVelocity().y;
        memento.playerFacingDirection = player.getFacingDirection();
        memento.playerOnGround = player.isOnGround();
        memento.currentWeapon = (player.getAttackStrategy() instanceof BowStrategy) ? "BOW" : "SWORD";

        // Capture Global State
        memento.score = GameManager.getInstance().getScore();
        memento.level = GameManager.getInstance().getCurrentLevel();

        // Capture Enemies
        for (Enemy e : enemies) {
            EnemyData data = new EnemyData();
            data.type = e.getClass().getSimpleName();
            data.x = e.getBounds().x;
            data.y = e.getBounds().y;
            data.currentHealth = e.getCurrentHealth();
            data.velocityX = e.getVelocity().x;
            data.velocityY = e.getVelocity().y;

            if (e instanceof PatrollingEnemy) {
                PatrollingEnemy pe = (PatrollingEnemy) e;
                data.startX = pe.getStartX();
                data.direction = pe.getDirection();
            }
            memento.enemies.add(data);
        }

        // Capture Projectiles & Platforms
        for (Bullet b : bullets) {
            BulletData data = new BulletData();
            data.x = b.getBounds().x; data.y = b.getBounds().y;
            data.direction = b.getDirection(); data.active = b.active;
            memento.bullets.add(data);
        }
        for (Arrow a : arrows) {
            ArrowData data = new ArrowData();
            data.x = a.getBounds().x; data.y = a.getBounds().y;
            data.direction = a.getDirection(); data.active = a.active;
            memento.arrows.add(data);
        }
        for (Platform p : platforms) {
            PlatformData data = new PlatformData();
            data.x = p.getBounds().x; data.y = p.getBounds().y;
            data.width = p.getBounds().width; data.height = p.getBounds().height;
            memento.platforms.add(data);
        }

        return memento;
    }

    public void saveMemento(GameStateMemento memento) {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        file.writeString(json.toJson(memento), false);
        System.out.println("Game Saved: " + file.path());
    }

    public GameStateMemento loadMemento() {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        if (!file.exists()) return null;
        return json.fromJson(GameStateMemento.class, file.readString());
    }

    public void applyMemento(GameStateMemento memento, Player player, Array<Enemy> enemies, Array<Bullet> bullets, Array<Arrow> arrows, Array<Platform> platforms) {
        if (memento == null) return;

        // Restore Player
        player.getBounds().setPosition(memento.playerX, memento.playerY);
        player.setHealth(memento.playerHealth);
        player.getVelocity().set(memento.playerVelocityX, memento.playerVelocityY);
        player.setFacingDirection(memento.playerFacingDirection);
        player.setOnGround(memento.playerOnGround);
        player.equipWeapon("BOW".equals(memento.currentWeapon) ? new BowStrategy() : new SwordStrategy());

        // Restore Global State
        GameManager.getInstance().setLevel(memento.level);
        GameManager.getInstance().setScore(memento.score);
        GameManager.getInstance().setCurrentState(GameManager.GameState.PLAYING);

        // Restore Enemies
        enemies.clear();
        for (EnemyData data : memento.enemies) {
            EnemyType type = EnemyType.CHASING; // Default fallback
            if (data.type.equals("PatrollingEnemy")) type = EnemyType.PATROLLING;
            else if (data.type.equals("BossEnemy")) type = EnemyType.BOSS;

            Enemy enemy = EnemyFactory.createEnemy(type, data.x, data.y);
            enemy.setHealth(data.currentHealth);
            enemy.getVelocity().set(data.velocityX, data.velocityY);

            if (enemy instanceof PatrollingEnemy) {
                ((PatrollingEnemy) enemy).setStartX(data.startX);
                ((PatrollingEnemy) enemy).setDirection(data.direction);
            }
            enemies.add(enemy);
        }

        // Restore Projectiles
        bullets.clear();
        for (BulletData data : memento.bullets) {
            if (data.active) {
                Bullet b = PoolManager.getInstance().bulletPool.obtain();
                b.getBounds().setPosition(data.x, data.y);
                b.setDirection(data.direction);
                b.active = data.active;
                bullets.add(b);
            }
        }

        arrows.clear();
        for (ArrowData data : memento.arrows) {
            if (data.active) {
                Arrow a = PoolManager.getInstance().arrowPool.obtain();
                a.getBounds().setPosition(data.x, data.y);
                a.setDirection(data.direction);
                a.active = data.active;
                arrows.add(a);
            }
        }

        // Restore Platforms
        platforms.clear();
        for (PlatformData data : memento.platforms) {
            platforms.add(new Platform(data.x, data.y, data.width, data.height));
        }

        System.out.println("Game State Restored. Score: " + memento.score);
    }
}

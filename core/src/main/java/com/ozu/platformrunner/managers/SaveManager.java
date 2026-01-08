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

    // YENİ: TAM oyun durumu yakalama
    public GameStateMemento createMemento(Player player, Array<Enemy> enemies,
                                          Array<Bullet> bullets, Array<Arrow> arrows, Array<Platform> platforms) {
        GameStateMemento memento = new GameStateMemento();

        // Player temel veriler
        memento.playerX = player.getBounds().x;
        memento.playerY = player.getBounds().y;
        memento.playerHealth = player.getHealth();

        // Player tam durum
        memento.playerVelocityX = player.getVelocity().x;
        memento.playerVelocityY = player.getVelocity().y;
        memento.playerFacingDirection = player.getFacingDirection();
        memento.playerOnGround = player.isOnGround();

        // Oyun durumu
        memento.score = GameManager.getInstance().getScore();
        memento.level = GameManager.getInstance().getCurrentLevel();

        // Silah
        if (player.getAttackStrategy() instanceof BowStrategy) {
            memento.currentWeapon = "BOW";
        } else {
            memento.currentWeapon = "SWORD";
        }

        // Düşmanlar
        for (Enemy e : enemies) {
            EnemyData data = new EnemyData();
            data.type = e.getClass().getSimpleName();
            data.x = e.getBounds().x;
            data.y = e.getBounds().y;
            data.currentHealth = e.getCurrentHealth();
            data.velocityX = e.getVelocity().x;
            data.velocityY = e.getVelocity().y;

            // PatrollingEnemy özel verileri
            if (e instanceof PatrollingEnemy) {
                PatrollingEnemy pe = (PatrollingEnemy) e;
                data.startX = pe.getStartX();
                data.direction = pe.getDirection();
            }

            memento.enemies.add(data);
        }

        // Mermiler
        for (Bullet b : bullets) {
            BulletData data = new BulletData();
            data.x = b.getBounds().x;
            data.y = b.getBounds().y;
            data.direction = b.getDirection();
            data.active = b.active;
            memento.bullets.add(data);
        }

        // Oklar
        for (Arrow a : arrows) {
            ArrowData data = new ArrowData();
            data.x = a.getBounds().x;
            data.y = a.getBounds().y;
            data.direction = a.getDirection();
            data.active = a.active;
            memento.arrows.add(data);
        }

        // Platformlar
        for (Platform p : platforms) {
            PlatformData data = new PlatformData();
            data.x = p.getBounds().x;
            data.y = p.getBounds().y;
            data.width = p.getBounds().width;
            data.height = p.getBounds().height;
            memento.platforms.add(data);
        }

        return memento;
    }

    // Memento'yu dosyaya yaz
    public void saveMemento(GameStateMemento memento) {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        file.writeString(json.toJson(memento), false);
        System.out.println("Oyun Kaydedildi! Dosya: " + file.path());
    }

    // Dosyadan memento yükle
    public GameStateMemento loadMemento() {
        FileHandle file = Gdx.files.local(SAVE_FILE);

        if (!file.exists()) {
            System.out.println("Kayıt dosyası bulunamadı!");
            return null;
        }

        GameStateMemento memento = json.fromJson(GameStateMemento.class, file.readString());
        System.out.println("Kayıt yüklendi!");
        return memento;
    }

    // Memento'dan oyun durumunu geri yükle
    public void applyMemento(GameStateMemento memento, Player player,
                              Array<Enemy> enemies, Array<Bullet> bullets, Array<Arrow> arrows, Array<Platform> platforms) {
        if (memento == null) return;

        // Player geri yükle
        player.getBounds().setPosition(memento.playerX, memento.playerY);
        player.setHealth(memento.playerHealth);
        player.getVelocity().set(memento.playerVelocityX, memento.playerVelocityY);
        player.setFacingDirection(memento.playerFacingDirection);
        player.setOnGround(memento.playerOnGround);

        // Silah
        if ("BOW".equals(memento.currentWeapon)) {
            player.equipWeapon(new BowStrategy());
        } else {
            player.equipWeapon(new SwordStrategy());
        }

        // Oyun durumu - directly restore from memento
        GameManager.getInstance().setLevel(memento.level);
        GameManager.getInstance().setScore(memento.score);
        GameManager.getInstance().setCurrentState(GameManager.GameState.PLAYING);

        // Düşmanları geri yükle
        enemies.clear();
        for (EnemyData data : memento.enemies) {
            EnemyType type;
            if (data.type.equals("PatrollingEnemy")) {
                type = EnemyType.PATROLLING;
            } else if (data.type.equals("BossEnemy")) {
                type = EnemyType.BOSS;
            } else {
                type = EnemyType.CHASING;
            }
            Enemy enemy = EnemyFactory.createEnemy(type, data.x, data.y);
            enemy.setHealth(data.currentHealth);
            enemy.getVelocity().set(data.velocityX, data.velocityY);

            // PatrollingEnemy özel verileri
            if (enemy instanceof PatrollingEnemy) {
                PatrollingEnemy pe = (PatrollingEnemy) enemy;
                pe.setStartX(data.startX);
                pe.setDirection(data.direction);
            }

            enemies.add(enemy);
        }

        // Mermileri geri yükle
        bullets.clear();
        for (BulletData data : memento.bullets) {
            if (data.active) {
                Bullet bullet = PoolManager.getInstance().bulletPool.obtain();
                bullet.getBounds().setPosition(data.x, data.y);
                bullet.setDirection(data.direction);
                bullet.active = data.active;
                bullets.add(bullet);
            }
        }

        // Okları geri yükle
        arrows.clear();
        for (ArrowData data : memento.arrows) {
            if (data.active) {
                Arrow arrow = PoolManager.getInstance().arrowPool.obtain();
                arrow.getBounds().setPosition(data.x, data.y);
                arrow.setDirection(data.direction);
                arrow.active = data.active;
                arrows.add(arrow);
            }
        }

        // Platformları geri yükle
        platforms.clear();
        for (PlatformData data : memento.platforms) {
            platforms.add(new Platform(data.x, data.y, data.width, data.height));
        }

        System.out.println("Oyun durumu geri yüklendi! Puan: " + memento.score);
    }

    // ESKİ API (geriye dönük uyumluluk için - kullanmayın)
    @Deprecated
    public void saveGame(Player player) {
        System.out.println("UYARI: saveGame() artık kullanılmıyor. createMemento() + saveMemento() kullanın.");
    }

    @Deprecated
    public void loadGame(Player player) {
        System.out.println("UYARI: loadGame() artık kullanılmıyor. loadMemento() + applyMemento() kullanın.");
    }
}

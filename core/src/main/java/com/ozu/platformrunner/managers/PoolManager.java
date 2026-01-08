package com.ozu.platformrunner.managers;

import com.badlogic.gdx.utils.Pool;
import com.ozu.platformrunner.entities.projectiles.Arrow;
import com.ozu.platformrunner.entities.projectiles.Bullet;

public class PoolManager {
    private static PoolManager instance;

    public final Pool<Bullet> bulletPool;
    public final Pool<Arrow> arrowPool;

    private PoolManager() {
        bulletPool = new Pool<Bullet>() {
            @Override
            protected Bullet newObject() {
                return new Bullet();
            }
        };

        arrowPool = new Pool<Arrow>() {
            @Override
            protected Arrow newObject() {
                return new Arrow();
            }
        };
    }

    public static PoolManager getInstance() {
        if (instance == null) {
            instance = new PoolManager();
        }
        return instance;
    }
}

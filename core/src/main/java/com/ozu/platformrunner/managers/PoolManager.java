package com.ozu.platformrunner.managers;

import com.badlogic.gdx.utils.Pool;
import com.ozu.platformrunner.entities.Bullet;

public class PoolManager {
    private static PoolManager instance;

    // LibGDX'in generic Pool sınıfını kullanıyoruz
    public final Pool<Bullet> bulletPool;

    private PoolManager() {
        // Havuzun nasıl nesne üreteceğini tanımlıyoruz (newObject metodu)
        bulletPool = new Pool<Bullet>() {
            @Override
            protected Bullet newObject() {
                return new Bullet(); // Havuz boşsa yeni yarat
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

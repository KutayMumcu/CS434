package com.ozu.platformrunner.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool; // Poolable importu
import com.ozu.platformrunner.managers.ResourceManager;

// Pool.Poolable implemente ediyoruz
public class Bullet implements Pool.Poolable {
    private final Rectangle bounds;
    private float speed = 400f;
    private int direction;
    private Texture texture;
    public boolean active; // Public yaptık ki dışarıdan erişip kontrol edelim

    // Constructor artık parametre almamalı (veya varsayılan değerler almalı)
    // Çünkü nesne bir kere üretilecek, sonra init() ile ayarlanacak.
    public Bullet() {
        this.bounds = new Rectangle(0, 0, 10, 5);
        this.active = false;
        // Texture'ı burada alabiliriz, değişmiyor sonuçta
        this.texture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_PLATFORM);
    }

    // Mermiyi havuza geri koymadan önce temizleyen metot
    @Override
    public void reset() {
        this.bounds.setPosition(0, 0);
        this.direction = 0;
        this.active = false;
    }

    // Mermiyi havuzdan aldığımızda özelliklerini atamak için (Constructor yerine bunu kullanacağız)
    public void init(float x, float y, int direction) {
        this.bounds.setPosition(x, y);
        this.direction = direction;
        this.active = true;
    }

    public void update(float delta) {
        if (!active) return; // Aktif değilse işlem yapma

        bounds.x += speed * direction * delta;

        // Ekrandan çıkınca pasife çek (Pool'a iade edilmeye hazır)
        if (bounds.x < 0 || bounds.x > 1000) { // 1000: Dünya genişliği
            active = false;
        }
    }

    public void draw(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public Rectangle getBounds() { return bounds; }
}

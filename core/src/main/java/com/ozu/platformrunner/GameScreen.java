package com.ozu.platformrunner;

// GameScreen.java
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Player player;
    private final Array<Platform> platforms;

    // Varlıklar
    private final Texture charTexture;
    private final Texture platformTexture;

    // Dünya boyutu
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;

    public GameScreen() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        // Varlıkları yükle
        charTexture = new Texture("char.png"); // assets/char.png
        platformTexture = new Texture("platform.png"); // assets/platform.png

        // Karakteri ve Platformları oluştur
        player = new Player(50, 50, 32, 32);

        platforms = new Array<>();
        // 1. Zemin (Geniş bir platform)
        platforms.add(new Platform(0, 0, WORLD_WIDTH, 30));
        // 2. Ortadaki platform
        platforms.add(new Platform(300, 150, 200, 30));
    }

    // --- Girdi İşleme ---
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveX(-1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveX(1);
        } else {
            player.stopX();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.jump();
        }
    }

    // --- Çarpışma Tespiti ve Yanıtı ---
    private void checkCollisions() {
        player.setOnGround(false); // Her güncellemede yere değmediğini varsay

        for (Platform platform : platforms) {
            if (Intersector.overlaps(player.getBounds(), platform.getBounds())) {

                // Çarpışma sadece dikeyde mi gerçekleşti?
                Rectangle intersection = new Rectangle();
                Intersector.intersectRectangles(player.getBounds(), platform.getBounds(), intersection);

                // Yukarıdan çarpma (Karakterin altı platformun üstüne çarpıyor)
                if (intersection.width > intersection.height && player.getVelocity().y < 0) {
                    player.getBounds().y += platform.getBounds().y + platform.getBounds().height - player.getBounds().y; // Tam platformun üstüne hizala
                    player.getVelocity().y = 0;
                    player.setOnGround(true);
                }
                // Yanlardan çarpma mantığı buraya eklenebilir. (Basitlik için atlandı)
            }
        }
    }

    // --- Oyun Döngüsü: Güncelleme (Update) ---
    @Override
    public void render(float delta) {
        // Girdi ve Fizik
        handleInput();

        // Karakteri hareket ettir (Yerçekimi dahil)
        player.update(delta);

        // Çarpışmaları kontrol et ve pozisyonu düzelt
        checkCollisions();

        // Ekranı temizle
        ScreenUtils.clear(0.3f, 0.3f, 0.5f, 1); // Mavi tonlu arka plan

        // Kamerayı güncelle
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Çizim
        batch.begin();

        // Platformları Çiz
        for (Platform platform : platforms) {
            batch.draw(platformTexture, platform.getBounds().x, platform.getBounds().y, platform.getBounds().width, platform.getBounds().height);
        }

        // Karakteri Çiz
        batch.draw(charTexture, player.getBounds().x, player.getBounds().y, player.getBounds().width, player.getBounds().height);

        batch.end();
    }

    // Diğer Screen Metotları (Şimdilik boş bırakılabilir)
    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose();
        charTexture.dispose();
        platformTexture.dispose();
    }
}

package com.ozu.platformrunner;

// --- GEREKLİ LIBGDX IMPORTLARI ---
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen; // Hata veren kısım burasıydı
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

// --- KENDİ SINIFLARIMIZIN IMPORTLARI ---
// Eğer sınıfların yerleri farklıysa bu satırlar hata verir.
// Lütfen Player ve Platform'u 'entities', ResourceManager'ı 'managers' paketine taşıdığından emin ol.
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.managers.*;
import com.ozu.platformrunner.entities.Enemy;
import com.ozu.platformrunner.patterns.decorator.DoubleShotDecorator;
import com.ozu.platformrunner.patterns.factory.EnemyFactory;
import com.ozu.platformrunner.patterns.strategy.SwordStrategy;
import com.ozu.platformrunner.patterns.strategy.BowStrategy;
import com.ozu.platformrunner.entities.Bullet; // Mermiyi import et


public class GameScreen implements Screen {

    // Temel LibGDX bileşenleri
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final InputHandler inputHandler;

    // Oyun Nesneleri
    private final Player player;
    private final Array<Platform> platforms;

    // Görseller (Artık ResourceManager'dan geliyor)
    private final Texture charTexture;
    private final Texture platformTexture;

    private final Array<Enemy> enemies;
    private final Array<Bullet> bullets;

    // Sabitler (Bunları ileride Constants sınıfına taşıyacağız)
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;

    private final HUD hud;

    private final SaveManager saveManager;

    public GameScreen() {
        // 1. Kamera ve Çizim Aracı Hazırlığı
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        // 2. Varlıkları Yükle (Facade Kullanımı - Singleton üzerinden)
        // new Texture(...) yerine ResourceManager kullanıyoruz.
        charTexture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_CHAR);
        platformTexture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_PLATFORM);

        // 3. Oyun Nesnelerini Oluştur
        player = new Player(50, 50, 32, 32);

        platforms = new Array<>();
        // Zemin Platformu
        platforms.add(new Platform(0, 0, WORLD_WIDTH, 30));
        // Havadaki Platform
        platforms.add(new Platform(300, 150, 200, 30));

        inputHandler = new InputHandler();

        enemies = new Array<>();

        // Fabrikadan düşman sipariş ediyoruz:
        // 1. Devriye Gezen Düşman (Platformun üzerinde)
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.PATROLLING, 350, 180));

        // 2. Kovalayan Düşman (Zeminde)
        enemies.add(EnemyFactory.createEnemy(EnemyFactory.EnemyType.CHASING, 600, 30));

        bullets = new Array<>();

        hud = new HUD();
        player.addObserver(hud);

        saveManager = new SaveManager();
    }

    private void handleInput() {
        // Hareket kodları (inputHandler.handleInput) burada duruyor...
        inputHandler.handleInput(player);

        // --- YENİ: SALDIRI VE SİLAH DEĞİŞİMİ ---

        // 'Z' tuşu saldırı olsun
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player.performAttack(enemies, bullets);
        }

        // '1' tuşu Kılıç
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            player.equipWeapon(new SwordStrategy());
        }

        // '2' tuşu Yay
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            player.equipWeapon(new BowStrategy());
        }

        // TEST: Kendine zarar ver (Observer'ı denemek için)
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            player.takeDamage(10);
        }

        // KAYDET (F5)
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            saveManager.saveGame(player);
        }

        // YÜKLE (F9)
        if (Gdx.input.isKeyJustPressed(Input.Keys.F9)) {
            saveManager.loadGame(player);
        }

        // GÜÇLENDİRME AL (P Tuşu)
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            // Mevcut stratejiyi al, DoubleShot ile sar ve tekrar oyuncuya ver
            // Bu sayede kılıç varsa "Çift Vuran Kılıç", Yay varsa "Çift Atan Yay" olur.
            player.equipWeapon(new DoubleShotDecorator(player.getAttackStrategy()));
        }
    }

    // --- Çarpışma Mantığı ---
    private void checkCollisions() {
        player.setOnGround(false); // Her karede önce havada varsayıyoruz

        for (Platform platform : platforms) {
            if (Intersector.overlaps(player.getBounds(), platform.getBounds())) {
                Rectangle intersection = new Rectangle();
                Intersector.intersectRectangles(player.getBounds(), platform.getBounds(), intersection);

                // Eğer çarpışma dikeyde ve karakter aşağı düşüyorsa (platforma konuyorsa)
                if (intersection.width > intersection.height && player.getVelocity().y < 0) {
                    // Karakteri platformun tam üstüne hizala
                    player.getBounds().y = platform.getBounds().y + platform.getBounds().height;

                    // Düşme hızını sıfırla
                    player.getVelocity().y = 0;

                    // Yerde olduğunu işaretle
                    player.setOnGround(true);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        // 1. Mantıksal Güncellemeler
        handleInput();
        player.update(delta);
        checkCollisions();

        for (Enemy enemy : enemies) {
            enemy.update(delta, player);
        }

        // MERMİ GÜNCELLEME VE TEMİZLİK DÖNGÜSÜ
        // LibGDX Array kullanırken silme işlemi yapacaksak tersten döngü kurmak güvenlidir
        // veya Iterator kullanmak gerekir. Basit olsun diye tersten dönelim.
        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update(delta);

            if (!b.active) {
                // 1. Listeden çıkar
                bullets.removeIndex(i);

                // 2. Havuza iade et (Geri Dönüşüm)
                PoolManager.getInstance().bulletPool.free(b);

                System.out.println("Mermi havuza iade edildi.");
            }
        }

        // 2. Ekranı Temizle (Siyah yerine lacivert bir ton)
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 3. Kamerayı Güncelle
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // 4. Çizime Başla
        batch.begin();

        // Platformları Çiz
        for (Platform platform : platforms) {
            batch.draw(platformTexture,
                platform.getBounds().x, platform.getBounds().y,
                platform.getBounds().width, platform.getBounds().height);
        }

        // Karakteri Çiz
        batch.draw(charTexture,
            player.getBounds().x, player.getBounds().y,
            player.getBounds().width, player.getBounds().height);

        //düşmanları çiz
        for (Enemy enemy : enemies) {
            enemy.draw(batch);
        }

        //mermileri çiz
        for (Bullet b : bullets) {
            if (b.active) b.draw(batch);
        }

        //hud çiz
        hud.render(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Ekran boyutu değişirse kamera ayarları güncellenebilir
    }

    @Override
    public void show() {
        // Ekran ilk açıldığında çalışır
    }

    @Override
    public void pause() {
        // Oyun arka plana atıldığında (Mobilde önemli)
    }

    @Override
    public void resume() {
        // Oyun tekrar açıldığında
    }

    @Override
    public void hide() {
        // Ekran kapandığında
    }

    @Override
    public void dispose() {
        // Sadece bu sınıfa özel kaynakları temizle
        batch.dispose();

        // DİKKAT: texture'ları burada dispose etmiyoruz!
        // Onlar ResourceManager tarafından yönetiliyor ve MainGame kapanırken temizlenecek.
    }
}

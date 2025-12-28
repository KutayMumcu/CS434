package com.ozu.platformrunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.ozu.platformrunner.entities.Bullet;
import com.ozu.platformrunner.entities.Enemy;
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.managers.*;
import com.ozu.platformrunner.patterns.decorator.DoubleShotDecorator;
import com.ozu.platformrunner.patterns.strategy.BowStrategy;
import com.ozu.platformrunner.patterns.strategy.SwordStrategy;

public class GameScreen implements Screen {

    private final MainGame mainGame;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;

    private final Stage stage;
    private final HUD hud;

    private final Player player;
    private final Array<Platform> platforms;
    private final Array<Enemy> enemies;
    private final Array<Bullet> bullets;

    private final InputHandler inputHandler;
    private final SaveManager saveManager;

    private final Texture charTexture;
    private final Texture platformTexture;

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;
    private static final float MAP_LIMIT_X = 2000;

    public GameScreen(MainGame game, int levelId) {
        this.mainGame = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        charTexture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_CHAR);
        platformTexture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_PLATFORM);

        player = new Player(50, 200, 32, 32);
        platforms = new Array<>();
        enemies = new Array<>();
        bullets = new Array<>();

        // InputHandler'ı başlatıyoruz
        inputHandler = new InputHandler();
        saveManager = new SaveManager();

        if (levelId == -1) {
            LevelManager.loadLevel(1, platforms, enemies);
            saveManager.loadGame(player);
        } else {
            LevelManager.loadLevel(levelId, platforms, enemies);
            GameManager.getInstance().setLevel(levelId);
        }

        stage = new Stage(new ScreenViewport());
        setupUI();

        // YENİ: HUD'u stage ile başlat
        hud = new HUD(stage);
        player.addObserver(hud);

        // --- PAUSE BUTONU (UI) ---

        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.fontColor = Color.YELLOW;

        TextButton menuBtn = new TextButton("MENU", btnStyle);
        // Butonu ekranın sağ üstüne yerleştirelim
        // Gdx.graphics.getWidth() kullanarak dinamik konumlandırma daha güvenlidir
        menuBtn.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainGame.setScreen(new MainMenuScreen(mainGame));
            }
        });
        stage.addActor(menuBtn);

        // Girdi Yöneticisi: Hem UI (Stage) hem Oyun için
        // Stage'i input processor yapıyoruz ki butona tıklayabilelim
        Gdx.input.setInputProcessor(stage);
    }

    private void handleInput() {
        // Klavye girdilerini InputHandler kontrol ediyor (Polling)
        inputHandler.handleInput(player);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player.performAttack(enemies, bullets);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) player.equipWeapon(new SwordStrategy());
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) player.equipWeapon(new BowStrategy());
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) player.equipWeapon(new DoubleShotDecorator(player.getAttackStrategy()));
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) saveManager.saveGame(player);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F9)) saveManager.loadGame(player);
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) player.takeDamage(10);
    }

    private void checkCollisions() {
        // Varsayılan olarak havada kabul et
        player.setOnGround(false);

        for (Platform platform : platforms) {
            // 1. NORMAL ÇARPIŞMA (İç içe geçmeyi önle)
            if (Intersector.overlaps(player.getBounds(), platform.getBounds())) {
                Rectangle intersection = new Rectangle();
                Intersector.intersectRectangles(player.getBounds(), platform.getBounds(), intersection);

                // Yukarıdan düşerken platformun içine girdiyse yukarı taşı
                if (intersection.width > intersection.height && player.getVelocity().y <= 0) {
                    player.getBounds().y = platform.getBounds().y + platform.getBounds().height;
                    player.getVelocity().y = 0;
                }
            }

            // 2. "AYAK" SENSÖRÜ (Zıplama için yere basma kontrolü)
            // Karakterin 2 piksel altını kontrol eden sanal bir kutu
            Rectangle footSensor = new Rectangle(
                player.getBounds().x + 2,       // Biraz içeriden başla (kenarlara takılmasın)
                player.getBounds().y - 2,       // Karakterin 2 piksel altı
                player.getBounds().width - 4,   // Genişlik
                2                               // Yükseklik
            );

            // Eğer bu sensör platforma değiyorsa VE hızımız yukarı doğru değilse -> YERDEYİZ
            if (Intersector.overlaps(footSensor, platform.getBounds()) && player.getVelocity().y <= 0) {
                player.setOnGround(true);
            }
        }
    }

    private void setupUI() {
        // 1. Font ve Stil Hazırlığı
        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.fontColor = Color.YELLOW;

        // 2. MENU Butonu Oluşturma
        TextButton menuBtn = new TextButton("MENU", btnStyle);

        // Dinamik Konumlandırma: Ekranın sağ üst köşesi
        // (Genişlik - 100, Yükseklik - 50)
        menuBtn.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);

        // 3. Tıklama Olayı
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Ana Menüye Dön
                mainGame.setScreen(new MainMenuScreen(mainGame));
            }
        });

        // 4. Sahneye (Stage) Ekleme
        stage.addActor(menuBtn);
    }

    @Override
    public void render(float delta) {
        if (player.getHealth() <= 0) {
            mainGame.setScreen(new MainMenuScreen(mainGame));
            return;
        }

        if (enemies.size == 0) {
            mainGame.setScreen(new MainMenuScreen(mainGame));
            return;
        }

        handleInput();
        player.update(delta);
        checkCollisions();

        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update(delta);
            for (Enemy e : enemies) {
                if (b.active && Intersector.overlaps(b.getBounds(), e.getBounds())) {
                    e.takeDamage(10);
                    b.active = false;
                }
            }
            if (!b.active) {
                bullets.removeIndex(i);
                PoolManager.getInstance().bulletPool.free(b);
            }
        }

        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy e = enemies.get(i);
            e.update(delta, player, platforms);
            if (e.isDead()) enemies.removeIndex(i);
        }

        // Kamera güncelleme
        float camX = player.getBounds().x;
        if (camX < 400) camX = 400;
        if (camX > MAP_LIMIT_X - 400) camX = MAP_LIMIT_X - 400;
        camera.position.set(camX, 240, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Çizim
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for (Platform p : platforms) batch.draw(platformTexture, p.getBounds().x, p.getBounds().y, p.getBounds().width, p.getBounds().height);
        batch.draw(charTexture, player.getBounds().x, player.getBounds().y, player.getBounds().width, player.getBounds().height);
        for (Enemy e : enemies) e.draw(batch);
        for (Bullet b : bullets) if (b.active) b.draw(batch);
        batch.end();

        // --- DÜZELTME 4: UI ÇİZİMİ ---
        // UI çizimi batch.end()'den SONRA yapılmalı.
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}

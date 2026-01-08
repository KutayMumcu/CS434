package com.ozu.platformrunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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

import com.ozu.platformrunner.ui.HUD;
import com.ozu.platformrunner.utils.GameConstants;
import com.ozu.platformrunner.MainGame;
import com.ozu.platformrunner.entities.projectiles.Arrow;
import com.ozu.platformrunner.entities.enemies.BossEnemy;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.entities.enemies.ChasingEnemy;
import com.ozu.platformrunner.entities.enemies.PatrollingEnemy;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.entities.projectiles.SwordSlash;
import com.ozu.platformrunner.managers.*;
import com.ozu.platformrunner.patterns.decorator.DoubleShotDecorator;
import com.ozu.platformrunner.patterns.memento.GameStateMemento;
import com.ozu.platformrunner.patterns.state.GameScreenState;
import com.ozu.platformrunner.patterns.state.PausedState;
import com.ozu.platformrunner.patterns.state.PlayingState;
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
    private final Array<Arrow> arrows;
    private final Array<SwordSlash> swordSlashes;

    private final InputHandler inputHandler;
    private final SaveManager saveManager;

    private final Texture platformTexture;

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;
    private static final float MAP_LIMIT_X = 2000;

    private int currentLevelId;
    private float elapsedTime;

    // YENİ: State Pattern için
    private GameScreenState screenState;
    private boolean isPaused = false;

    public GameScreen(MainGame game, int levelId) {
        this.mainGame = game;
        this.currentLevelId = levelId;
        this.elapsedTime = 0f;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        platformTexture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_PLATFORM);

        player = new Player(50, 200, 96, 96);
        platforms = new Array<>();
        enemies = new Array<>();
        bullets = new Array<>();
        arrows = new Array<>();
        swordSlashes = new Array<>();

        // InputHandler'ı başlatıyoruz
        inputHandler = new InputHandler();
        saveManager = new SaveManager();

        if (levelId == -1) {
            // Load saved game
            GameStateMemento memento = saveManager.loadMemento();
            if (memento != null) {
                saveManager.applyMemento(memento, player, enemies, bullets, arrows, platforms);
                this.currentLevelId = memento.level;
            } else {
                // No save file exists, start from level 1
                LevelManager.loadLevel(1, platforms, enemies);
                this.currentLevelId = 1;
            }
        } else {
            LevelManager.loadLevel(levelId, platforms, enemies);
            GameManager.getInstance().setLevel(levelId);
        }

        stage = new Stage(new ScreenViewport());
        setupUI();

        // YENİ: HUD'u stage ile başlat
        hud = new HUD(stage);
        player.addObserver(hud);

        // Initialize HUD with current level and score
        hud.updateLevel(currentLevelId);
        hud.updateScore(GameManager.getInstance().getScore());

        // Girdi Yöneticisi: Hem UI (Stage) hem Oyun için
        // InputMultiplexer ile hem Stage hem keyboard inputlarını işle
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);  // UI önce
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                return handleKeyPress(keycode);
            }
        });
        Gdx.input.setInputProcessor(multiplexer);

        // State pattern başlat
        screenState = new PlayingState();
        screenState.enter(this);
    }

    private void handleInput() {
        // Klavye girdilerini InputHandler kontrol ediyor (Polling)
        inputHandler.handleInput(player);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player.performAttack(enemies, bullets);

            // Add sword slash animation if using sword
            if (player.getAttackStrategy().getClass().getSimpleName().contains("Sword")) {
                float slashX = player.getBounds().x + (player.getFacingDirection() > 0 ? player.getBounds().width : -40);
                float slashY = player.getBounds().y + 5;
                swordSlashes.add(new SwordSlash(slashX, slashY, player.getFacingDirection()));
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) player.equipWeapon(new SwordStrategy());
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) player.equipWeapon(new BowStrategy());
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) player.equipWeapon(new DoubleShotDecorator(player.getAttackStrategy()));
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) saveGame();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F9)) loadGame();
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) player.takeDamage(10);
    }

    public boolean handleKeyPress(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            togglePause();
            return true;
        }
        return false;
    }

    private void checkCollisions() {
        player.setOnGround(false);

        for (Platform platform : platforms) {
            // Check if player's bottom is at or very close to platform's top
            float playerBottom = player.getBounds().y;
            float platformTop = platform.getBounds().y + platform.getBounds().height;
            float verticalDistance = Math.abs(playerBottom - platformTop);

            // Check if player is horizontally aligned with platform
            boolean horizontallyAligned = player.getBounds().x + player.getBounds().width > platform.getBounds().x &&
                                         player.getBounds().x < platform.getBounds().x + platform.getBounds().width;

            // If player is standing on or very close to platform top (within 2 pixels)
            if (horizontallyAligned && verticalDistance < 2f && player.getVelocity().y <= 0) {
                player.getBounds().y = platformTop;
                player.getVelocity().y = 0;
                player.setOnGround(true);
            }

            if (Intersector.overlaps(player.getBounds(), platform.getBounds())) {
                Rectangle intersection = new Rectangle();
                Intersector.intersectRectangles(player.getBounds(), platform.getBounds(), intersection);

                // Determine which side we're colliding from
                if (intersection.height < intersection.width) {
                    // Vertical collision (top or bottom)
                    if (player.getVelocity().y <= 0) {
                        // Falling down or standing still, hit top of platform
                        player.getBounds().y = platform.getBounds().y + platform.getBounds().height;
                        player.getVelocity().y = 0;
                        player.setOnGround(true);
                    } else if (player.getVelocity().y > 0) {
                        // Jumping up, hit bottom of platform
                        player.getBounds().y = platform.getBounds().y - player.getBounds().height;
                        player.getVelocity().y = 0;
                    }
                } else {
                    // Horizontal collision (left or right)
                    if (player.getVelocity().x > 0) {
                        // Moving right, hit left side of platform
                        player.getBounds().x = platform.getBounds().x - player.getBounds().width;
                    } else if (player.getVelocity().x < 0) {
                        // Moving left, hit right side of platform
                        player.getBounds().x = platform.getBounds().x + platform.getBounds().width;
                    }
                    player.getVelocity().x = 0;
                }
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
        // Eğer pause edilmişse, sadece pause menüyü göster
        if (isPaused) {
            renderPausedState(delta);
            return;
        }

        // Track elapsed time
        elapsedTime += delta;

        // Death: Reset score and restart current level
        if (player.getHealth() <= 0) {
            GameManager.getInstance().setScore(0);
            System.out.println("Player died! Score reset to 0");
            mainGame.setScreen(new GameScreen(mainGame, currentLevelId));
            return;
        }

        // Victory: Show victory screen with stats
        if (enemies.size == 0) {
            // Award level completion bonus
            GameManager.getInstance().addScore(GameConstants.SCORE_COMPLETE_LEVEL);
            hud.updateScore(GameManager.getInstance().getScore());
            System.out.println("Level Complete! +" + GameConstants.SCORE_COMPLETE_LEVEL + " bonus points!");

            mainGame.setScreen(new VictoryScreen(
                mainGame,
                currentLevelId,
                GameManager.getInstance().getScore(),
                player.getHealth(),
                elapsedTime
            ));
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

        // Update arrows
        for (int i = arrows.size - 1; i >= 0; i--) {
            Arrow a = arrows.get(i);
            a.update(delta);

            // Arrow hits player
            if (a.active && Intersector.overlaps(a.getBounds(), player.getBounds())) {
                if (!player.isInvulnerable()) {
                    player.takeDamage(GameConstants.ARROW_DAMAGE);
                    float knockbackDir = (player.getBounds().x < a.getBounds().x) ? -1 : 1;
                    player.applyKnockback(knockbackDir, GameConstants.ARROW_HIT_KNOCKBACK);
                }
                a.deactivate();
            }

            // Remove inactive arrows
            if (!a.active) {
                arrows.removeIndex(i);
                PoolManager.getInstance().arrowPool.free(a);
            }
        }

        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy e = enemies.get(i);
            if (e instanceof BossEnemy) {
                ((BossEnemy) e).updateAndShoot(delta, player, platforms, arrows);
            } else {
                e.update(delta, player, platforms);
            }

            // Award score when enemy dies
            if (e.isDead()) {
                int scoreValue = getScoreForEnemy(e);
                GameManager.getInstance().addScore(scoreValue);
                hud.updateScore(GameManager.getInstance().getScore());
                System.out.println("Enemy killed! +" + scoreValue + " points. Total: " + GameManager.getInstance().getScore());
                enemies.removeIndex(i);
            }
        }

        // Update sword slashes
        for (int i = swordSlashes.size - 1; i >= 0; i--) {
            SwordSlash slash = swordSlashes.get(i);
            slash.update(delta);
            if (slash.isFinished()) {
                swordSlashes.removeIndex(i);
            }
        }

        // Enemy collision with player (damage player)
        for (Enemy enemy : enemies) {
            if (Intersector.overlaps(player.getBounds(), enemy.getBounds())) {
                if (!player.isInvulnerable()) {
                    player.takeDamage(GameConstants.ENEMY_COLLISION_DAMAGE);

                    // Apply smooth knockback
                    float knockbackDirection = (player.getBounds().x < enemy.getBounds().x) ? -1 : 1;
                    player.applyKnockback(knockbackDirection, GameConstants.ENEMY_COLLISION_KNOCKBACK);
                }
            }
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
        // Draw platforms using their own draw method
        for (Platform p : platforms) {
            p.draw(batch);
        }

        player.draw(batch);

        // Draw attack cooldown overlay
        if (player.isAttackOnCooldown()) {
            float cooldownPercent = player.getAttackCooldownPercent();
            float overlayHeight = player.getBounds().height * cooldownPercent;

            batch.setColor(0.2f, 0.2f, 0.2f, 0.5f);
            batch.draw(platformTexture,
                player.getBounds().x,
                player.getBounds().y,
                player.getBounds().width,
                overlayHeight);
            batch.setColor(Color.WHITE);
        }

        for (Enemy e : enemies) e.draw(batch);
        for (Bullet b : bullets) if (b.active) b.draw(batch);
        for (Arrow a : arrows) if (a.active) a.draw(batch);
        for (SwordSlash slash : swordSlashes) slash.draw(batch);
        batch.end();

        // --- DÜZELTME 4: UI ÇİZİMİ ---
        // UI çizimi batch.end()'den SONRA yapılmalı.
        stage.act();
        stage.draw();
    }

    private void renderPausedState(float delta) {
        // Arka planda oyunu çiz (donmuş halde)
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Platformlar
        for (Platform p : platforms) {
            p.draw(batch);
        }

        // --- DÜZELTME BURADA ---
        // Eski: batch.draw(charTexture, ...);
        // Yeni: Player kendi çizimini yapsın (Yön ve efektler korunsun)
        player.draw(batch);
        // -----------------------

        for (Enemy e : enemies) e.draw(batch);
        for (Bullet b : bullets) if (b.active) b.draw(batch);
        for (Arrow a : arrows) if (a.active) a.draw(batch);
        for (SwordSlash slash : swordSlashes) slash.draw(batch);

        batch.end();

        stage.act();
        stage.draw();

        // Pause menüyü çiz
        if (screenState instanceof PausedState) {
            PausedState pausedState = (PausedState) screenState;
            if (pausedState.getPauseMenu() != null) {
                pausedState.getPauseMenu().render(delta);
            }
        }
    }

    // YENİ: Pause/Resume metodları
    public void togglePause() {
        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    public void pauseGame() {
        screenState.exit(this);
        screenState = new PausedState();
        screenState.enter(this);
        isPaused = true;
    }

    public void resumeGame() {
        // Input processor'ı geri yükle
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                return handleKeyPress(keycode);
            }
        });
        Gdx.input.setInputProcessor(multiplexer);

        screenState.exit(this);
        screenState = new PlayingState();
        screenState.enter(this);
        isPaused = false;
    }

    public void saveGame() {
        GameStateMemento memento = saveManager.createMemento(player, enemies, bullets, arrows, platforms);
        saveManager.saveMemento(memento);
    }

    public void loadGame() {
        GameStateMemento memento = saveManager.loadMemento();
        if (memento != null) {
            saveManager.applyMemento(memento, player, enemies, bullets, arrows, platforms);
        }
    }

    public MainGame getMainGame() {
        return mainGame;
    }

    /**
     * Calculate score value based on enemy type
     * Demonstrates Strategy/Factory patterns - different enemies have different values
     */
    private int getScoreForEnemy(Enemy enemy) {
        if (enemy instanceof BossEnemy) {
            return GameConstants.SCORE_KILL_BOSS_ENEMY;
        } else if (enemy instanceof ChasingEnemy) {
            return GameConstants.SCORE_KILL_CHASING_ENEMY;
        } else if (enemy instanceof PatrollingEnemy) {
            return GameConstants.SCORE_KILL_PATROLLING_ENEMY;
        }
        return 50; // Default fallback
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

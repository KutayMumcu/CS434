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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.ozu.platformrunner.MainGame;
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.entities.enemies.BossEnemy;
import com.ozu.platformrunner.entities.enemies.ChasingEnemy;
import com.ozu.platformrunner.entities.enemies.Enemy;
import com.ozu.platformrunner.entities.enemies.PatrollingEnemy;
import com.ozu.platformrunner.entities.projectiles.Arrow;
import com.ozu.platformrunner.entities.projectiles.Bullet;
import com.ozu.platformrunner.entities.projectiles.SwordSlash;
import com.ozu.platformrunner.managers.*;
import com.ozu.platformrunner.patterns.decorator.DoubleShotDecorator;
import com.ozu.platformrunner.patterns.memento.GameStateMemento;
import com.ozu.platformrunner.patterns.state.GameScreenState;
import com.ozu.platformrunner.patterns.state.PausedState;
import com.ozu.platformrunner.patterns.state.PlayingState;
import com.ozu.platformrunner.patterns.strategy.BowStrategy;
import com.ozu.platformrunner.patterns.strategy.SwordStrategy;
import com.ozu.platformrunner.patterns.strategy.WeaponType;
import com.ozu.platformrunner.ui.HUD;
import com.ozu.platformrunner.utils.GameConstants;

public class GameScreen implements Screen {

    private static final float STEP_TIME = 1 / 60f;
    private static final float MAX_FRAME_TIME = 0.25f;
    private float accumulator = 0f;

    private ShapeRenderer shapeRenderer;
    private boolean debugMode = false;

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

    private int currentLevelId;
    private float elapsedTime;

    private GameScreenState screenState;
    private boolean isPaused = false;

    public GameScreen(MainGame game, int levelId) {
        shapeRenderer = new ShapeRenderer();
        this.mainGame = game;
        this.currentLevelId = levelId;
        this.elapsedTime = 0f;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();

        // Use Constants for Camera Dimensions
        camera.setToOrtho(false, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);

        platformTexture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_PLATFORM);

        player = new Player(50, 200);
        platforms = new Array<>();
        enemies = new Array<>();
        bullets = new Array<>();
        arrows = new Array<>();
        swordSlashes = new Array<>();

        inputHandler = new InputHandler();
        saveManager = new SaveManager();

        if (levelId == -1) {
            GameStateMemento memento = saveManager.loadMemento();
            if (memento != null) {
                saveManager.applyMemento(memento, player, enemies, bullets, arrows, platforms);
                this.currentLevelId = memento.level;
            } else {
                LevelManager.loadLevel(1, platforms, enemies);
                this.currentLevelId = 1;
            }
        } else {
            LevelManager.loadLevel(levelId, platforms, enemies);
            GameManager.getInstance().setLevel(levelId);
        }

        stage = new Stage(new ScreenViewport());
        setupUI();

        hud = new HUD(stage);
        player.addObserver(hud);
        hud.updateLevel(currentLevelId);
        hud.updateScore(GameManager.getInstance().getScore());

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                return handleKeyPress(keycode);
            }
        });
        Gdx.input.setInputProcessor(multiplexer);

        screenState = new PlayingState();
        screenState.enter(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (isPaused) {
            renderPausedState(delta);
            return;
        }

        handleInput();

        float frameTime = Math.min(delta, MAX_FRAME_TIME);
        accumulator += frameTime;

        while (accumulator >= STEP_TIME) {
            updatePhysics(STEP_TIME);
            accumulator -= STEP_TIME;
        }

        updateCamera();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Platform p : platforms) p.draw(batch);
        player.draw(batch);

        if (player.isAttackOnCooldown()) {
            float cooldownPercent = player.getAttackCooldownPercent();
            float overlayHeight = player.getBounds().height * cooldownPercent;
            batch.setColor(0.2f, 0.2f, 0.2f, 0.5f);
            batch.draw(platformTexture, player.getBounds().x, player.getBounds().y, player.getBounds().width, overlayHeight);
            batch.setColor(Color.WHITE);
        }

        for (Enemy e : enemies) e.draw(batch);
        for (Bullet b : bullets) if (b.active) b.draw(batch);
        for (Arrow a : arrows) if (a.active) a.draw(batch);
        for (SwordSlash slash : swordSlashes) slash.draw(batch);

        batch.end();

        stage.act();
        stage.draw();

        if (debugMode) renderDebug();
    }

    private void updatePhysics(float dt) {
        elapsedTime += dt;

        if (player.getHealth() <= 0) {
            GameManager.getInstance().setScore(0);
            mainGame.setScreen(new GameScreen(mainGame, currentLevelId));
            return;
        }

        if (enemies.size == 0) {
            GameManager.getInstance().addScore(GameConstants.SCORE_COMPLETE_LEVEL);
            hud.updateScore(GameManager.getInstance().getScore());
            mainGame.setScreen(new VictoryScreen(mainGame, currentLevelId, GameManager.getInstance().getScore(), player.getHealth(), elapsedTime));
            return;
        }

        player.update(dt);
        checkCollisions();

        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update(dt);
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

        for (int i = arrows.size - 1; i >= 0; i--) {
            Arrow a = arrows.get(i);
            a.update(dt);
            if (a.active && Intersector.overlaps(a.getBounds(), player.getBounds())) {
                if (!player.isInvulnerable()) {
                    player.takeDamage(GameConstants.ARROW_DAMAGE);
                    float knockbackDir = (player.getBounds().x < a.getBounds().x) ? -1 : 1;
                    player.applyKnockback(knockbackDir, GameConstants.ARROW_HIT_KNOCKBACK);
                }
                a.deactivate();
            }
            if (!a.active) {
                arrows.removeIndex(i);
                PoolManager.getInstance().arrowPool.free(a);
            }
        }

        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy e = enemies.get(i);
            if (e instanceof BossEnemy) {
                ((BossEnemy) e).updateAndShoot(dt, player, platforms, arrows);
            } else {
                e.update(dt, player, platforms);
            }

            if (e.isDead()) {
                int scoreValue = getScoreForEnemy(e);
                GameManager.getInstance().addScore(scoreValue);
                hud.updateScore(GameManager.getInstance().getScore());
                enemies.removeIndex(i);
            }
        }

        for (int i = swordSlashes.size - 1; i >= 0; i--) {
            SwordSlash slash = swordSlashes.get(i);
            slash.update(dt);
            if (slash.isFinished()) swordSlashes.removeIndex(i);
        }

        for (Enemy enemy : enemies) {
            if (Intersector.overlaps(player.getBounds(), enemy.getBounds())) {
                if (!player.isInvulnerable()) {
                    player.takeDamage(GameConstants.ENEMY_COLLISION_DAMAGE);
                    float knockbackDirection = (player.getBounds().x < enemy.getBounds().x) ? -1 : 1;
                    player.applyKnockback(knockbackDirection, GameConstants.ENEMY_COLLISION_KNOCKBACK);
                }
            }
        }
    }

    private void updateCamera() {
        float halfScreen = GameConstants.WORLD_WIDTH / 2;
        float camX = player.getBounds().x;

        // Clamp camera within map limits
        if (camX < halfScreen) camX = halfScreen;
        if (camX > GameConstants.MAP_LIMIT_X - halfScreen) camX = GameConstants.MAP_LIMIT_X - halfScreen;

        camera.position.set(camX, GameConstants.WORLD_HEIGHT / 2, 0);
        camera.update();
    }

    private void handleInput() {
        inputHandler.handleInput(player);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player.performAttack(enemies, bullets);

            if (player.getAttackStrategy().getWeaponType() == WeaponType.MELEE) {
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) debugMode = !debugMode;
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
            float playerBottom = player.getBounds().y;
            float platformTop = platform.getBounds().y + platform.getBounds().height;
            float verticalDistance = Math.abs(playerBottom - platformTop);
            boolean horizontallyAligned = player.getBounds().x + player.getBounds().width > platform.getBounds().x &&
                player.getBounds().x < platform.getBounds().x + platform.getBounds().width;

            // Use Constant for Threshold
            if (horizontallyAligned && verticalDistance < GameConstants.PLATFORM_TOP_THRESHOLD && player.getVelocity().y <= 0) {
                player.getBounds().y = platformTop;
                player.getVelocity().y = 0;
                player.setOnGround(true);
            }

            if (Intersector.overlaps(player.getBounds(), platform.getBounds())) {
                Rectangle intersection = new Rectangle();
                Intersector.intersectRectangles(player.getBounds(), platform.getBounds(), intersection);

                if (intersection.height < intersection.width) {
                    if (player.getVelocity().y <= 0) {
                        player.getBounds().y = platform.getBounds().y + platform.getBounds().height;
                        player.getVelocity().y = 0;
                        player.setOnGround(true);
                    } else if (player.getVelocity().y > 0) {
                        player.getBounds().y = platform.getBounds().y - player.getBounds().height;
                        player.getVelocity().y = 0;
                    }
                } else {
                    if (player.getVelocity().x > 0) {
                        player.getBounds().x = platform.getBounds().x - player.getBounds().width;
                    } else if (player.getVelocity().x < 0) {
                        player.getBounds().x = platform.getBounds().x + platform.getBounds().width;
                    }
                    player.getVelocity().x = 0;
                }
            }
        }
    }

    private void renderDebug() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(player.getBounds().x, player.getBounds().y, player.getBounds().width, player.getBounds().height);

        shapeRenderer.setColor(Color.GREEN);
        for (Platform p : platforms) {
            shapeRenderer.rect(p.getBounds().x, p.getBounds().y, p.getBounds().width, p.getBounds().height);
        }

        shapeRenderer.setColor(Color.ORANGE);
        for (Enemy e : enemies) {
            shapeRenderer.rect(e.getBounds().x, e.getBounds().y, e.getBounds().width, e.getBounds().height);
        }

        shapeRenderer.setColor(Color.YELLOW);
        for (Bullet b : bullets) {
            if (b.active) shapeRenderer.rect(b.getBounds().x, b.getBounds().y, b.getBounds().width, b.getBounds().height);
        }
        shapeRenderer.end();
    }

    private void setupUI() {
        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.fontColor = Color.YELLOW;

        TextButton menuBtn = new TextButton("MENU", btnStyle);
        menuBtn.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainGame.setScreen(new MainMenuScreen(mainGame));
            }
        });

        stage.addActor(menuBtn);
    }

    private void renderPausedState(float delta) {
        batch.begin();
        for (Platform p : platforms) p.draw(batch);
        player.draw(batch);
        for (Enemy e : enemies) e.draw(batch);
        for (Bullet b : bullets) if (b.active) b.draw(batch);
        for (Arrow a : arrows) if (a.active) a.draw(batch);
        for (SwordSlash slash : swordSlashes) slash.draw(batch);
        batch.end();

        stage.act();
        stage.draw();

        if (screenState instanceof PausedState) {
            PausedState pausedState = (PausedState) screenState;
            if (pausedState.getPauseMenu() != null) {
                pausedState.getPauseMenu().render(delta);
            }
        }
    }

    public void togglePause() {
        if (isPaused) resumeGame(); else pauseGame();
    }

    public void pauseGame() {
        screenState.exit(this);
        screenState = new PausedState();
        screenState.enter(this);
        isPaused = true;
    }

    public void resumeGame() {
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

    private int getScoreForEnemy(Enemy enemy) {
        if (enemy instanceof BossEnemy) return GameConstants.SCORE_KILL_BOSS_ENEMY;
        else if (enemy instanceof ChasingEnemy) return GameConstants.SCORE_KILL_CHASING_ENEMY;
        else if (enemy instanceof PatrollingEnemy) return GameConstants.SCORE_KILL_PATROLLING_ENEMY;
        return 50;
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
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}

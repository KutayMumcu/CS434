package com.ozu.platformrunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ozu.platformrunner.managers.GameManager;

public class VictoryScreen implements Screen {
    private final MainGame game;
    private final Stage stage;
    private final SpriteBatch batch;

    private final int currentLevel;
    private final int score;
    private final int healthRemaining;
    private final float timeTaken;

    public VictoryScreen(MainGame game, int currentLevel, int score, int healthRemaining, float timeTaken) {
        this.game = game;
        this.currentLevel = currentLevel;
        this.score = score;
        this.healthRemaining = healthRemaining;
        this.timeTaken = timeTaken;

        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        setupUI();
    }

    private void setupUI() {
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2f);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.YELLOW;

        // Button style
        BitmapFont buttonFont = new BitmapFont();
        buttonFont.getData().setScale(1.5f);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.YELLOW;

        // Create table for layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Victory title
        Label titleLabel = new Label("LEVEL COMPLETE!", labelStyle);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.GREEN);
        table.add(titleLabel).padBottom(40).row();

        // Stats
        Label levelLabel = new Label("Level: " + currentLevel, labelStyle);
        table.add(levelLabel).padBottom(20).row();

        Label scoreLabel = new Label("Score: " + score, labelStyle);
        table.add(scoreLabel).padBottom(20).row();

        Label healthLabel = new Label("Health Remaining: " + healthRemaining, labelStyle);
        table.add(healthLabel).padBottom(20).row();

        Label timeLabel = new Label(String.format("Time: %.1f seconds", timeTaken), labelStyle);
        table.add(timeLabel).padBottom(50).row();

        // Buttons
        TextButton nextLevelButton = new TextButton("NEXT LEVEL", buttonStyle);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int nextLevel = currentLevel + 1;
                GameManager.getInstance().setLevel(nextLevel);
                game.setScreen(new GameScreen(game, nextLevel));
            }
        });
        table.add(nextLevelButton).width(200).height(50).padBottom(20).row();

        TextButton menuButton = new TextButton("MAIN MENU", buttonStyle);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.add(menuButton).width(200).height(50).row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.3f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
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
        stage.dispose();
        batch.dispose();
    }
}

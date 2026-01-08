package com.ozu.platformrunner.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ozu.platformrunner.screens.GameScreen;
import com.ozu.platformrunner.screens.MainMenuScreen;

public class PauseMenuOverlay {
    private final Stage stage;
    private final Texture bgTexture;
    private final GameScreen gameScreen;

    public PauseMenuOverlay(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.stage = new Stage(new ScreenViewport());

        // Semi-transparan arka plan oluştur
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.7f);
        pixmap.fill();
        bgTexture = new Texture(pixmap);
        pixmap.dispose();

        setupUI();
    }

    private void setupUI() {
        // Font
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);

        // Button style
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.fontColor = Color.WHITE;
        btnStyle.downFontColor = Color.GRAY;

        // Table layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Butonlar
        TextButton resumeBtn = new TextButton("RESUME", btnStyle);
        TextButton saveBtn = new TextButton("SAVE GAME", btnStyle);
        TextButton loadBtn = new TextButton("LOAD GAME", btnStyle);
        TextButton quitBtn = new TextButton("QUIT TO MENU", btnStyle);

        // Resume butonu
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.resumeGame();
            }
        });

        // Save butonu
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.saveGame();
            }
        });

        // Load butonu
        loadBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.loadGame();
                gameScreen.resumeGame();
            }
        });

        // Quit butonu
        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.getMainGame().setScreen(new MainMenuScreen(gameScreen.getMainGame()));
            }
        });

        // Layout
        table.add(resumeBtn).pad(10).row();
        table.add(saveBtn).pad(10).row();
        table.add(loadBtn).pad(10).row();
        table.add(quitBtn).pad(10).row();
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {
        stage.dispose();
        bgTexture.dispose();
    }
}

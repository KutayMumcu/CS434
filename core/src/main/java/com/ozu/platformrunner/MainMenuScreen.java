package com.ozu.platformrunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private final MainGame game;
    private Stage stage;

    public MainMenuScreen(MainGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Tıklamaları algıla

        // Basit bir UI Stili oluştur (Skin dosyasıyla uğraşmamak için kodla yapıyoruz)
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.GRAY; // Tıklayınca gri olsun

        // Tablo ile düzenle
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Başlık
        Label titleLabel = new Label("PLATFORM RUNNER", labelStyle);
        table.add(titleLabel).padBottom(50);
        table.row();

        // PLAY Butonu
        TextButton playButton = new TextButton("PLAY", buttonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Level Seçme Ekranına Git
                game.setScreen(new LevelSelectScreen(game));
            }
        });
        table.add(playButton).padBottom(20);
        table.row();

        // LOAD Butonu
        TextButton loadButton = new TextButton("LOAD GAME", buttonStyle);
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Direkt Kayıtlı Oyunu Aç (-1 koduyla)
                game.setScreen(new GameScreen(game, -1));
            }
        });
        table.add(loadButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void dispose() { stage.dispose(); }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}

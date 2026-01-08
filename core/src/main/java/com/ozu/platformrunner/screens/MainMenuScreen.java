package com.ozu.platformrunner.screens;

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
import com.ozu.platformrunner.MainGame;
import com.ozu.platformrunner.ui.HelpWindow; // Yeni sınıfımızı import ettik

public class MainMenuScreen implements Screen {
    private final MainGame game;
    private Stage stage;

    // Stiller (Diğer sınıflara da geçebilmek için burada tanımlı)
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private TextButton.TextButtonStyle buttonStyle;

    public MainMenuScreen(MainGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        createStyles();
        setupMainMenu();
        setupHelpButton();
    }

    private void createStyles() {
        font = new BitmapFont();
        font.getData().setScale(2);
        labelStyle = new Label.LabelStyle(font, Color.WHITE);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.GRAY;
    }

    private void setupMainMenu() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("PLATFORM RUNNER", labelStyle);
        table.add(titleLabel).padBottom(50);
        table.row();

        TextButton playButton = new TextButton("PLAY", buttonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game));
            }
        });
        table.add(playButton).padBottom(20);
        table.row();

        TextButton loadButton = new TextButton("LOAD GAME", buttonStyle);
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, -1));
            }
        });
        table.add(loadButton);
    }

    private void setupHelpButton() {
        TextButton helpButton = new TextButton("?", buttonStyle);
        helpButton.setPosition(Gdx.graphics.getWidth() - 60, Gdx.graphics.getHeight() - 60);

        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // SADECE TEK SATIR!
                // Pencereyi oluştur ve sahneye ekle
                stage.addActor(new HelpWindow(labelStyle, buttonStyle));
            }
        });

        stage.addActor(helpButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void dispose() { stage.dispose(); font.dispose(); }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}

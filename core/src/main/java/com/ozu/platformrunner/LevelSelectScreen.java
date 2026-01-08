package com.ozu.platformrunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelSelectScreen implements Screen {
    private final MainGame game;
    private Stage stage;

    public LevelSelectScreen(MainGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.5f);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.YELLOW;
        buttonStyle.downFontColor = Color.ORANGE;

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // LEVEL 1
        TextButton lvl1 = new TextButton("LEVEL 1", buttonStyle);
        lvl1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 1));
            }
        });
        table.add(lvl1).width(200).height(50).pad(8);

        // LEVEL 2
        TextButton lvl2 = new TextButton("LEVEL 2", buttonStyle);
        lvl2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 2));
            }
        });
        table.add(lvl2).width(200).height(50).pad(8);
        table.row();

        // LEVEL 3
        TextButton lvl3 = new TextButton("LEVEL 3", buttonStyle);
        lvl3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 3));
            }
        });
        table.add(lvl3).width(200).height(50).pad(8);

        // LEVEL 4
        TextButton lvl4 = new TextButton("LEVEL 4", buttonStyle);
        lvl4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 4));
            }
        });
        table.add(lvl4).width(200).height(50).pad(8);
        table.row();

        // LEVEL 5
        TextButton lvl5 = new TextButton("LEVEL 5", buttonStyle);
        lvl5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 5));
            }
        });
        table.add(lvl5).width(200).height(50).pad(8);

        // LEVEL 6
        TextButton lvl6 = new TextButton("LEVEL 6", buttonStyle);
        lvl6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 6));
            }
        });
        table.add(lvl6).width(200).height(50).pad(8);
        table.row();

        // LEVEL 7
        TextButton lvl7 = new TextButton("LEVEL 7", buttonStyle);
        lvl7.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, 7));
            }
        });
        table.add(lvl7).width(200).height(50).pad(8).colspan(2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1); // Daha koyu arka plan
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

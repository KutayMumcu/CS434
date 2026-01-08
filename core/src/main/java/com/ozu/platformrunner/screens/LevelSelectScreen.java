package com.ozu.platformrunner.screens;

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
import com.ozu.platformrunner.MainGame;

public class LevelSelectScreen implements Screen {
    private final Stage stage;

    public LevelSelectScreen(final MainGame game) {
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

        // Create buttons for Levels 1-7 dynamically
        for (int i = 1; i <= 7; i++) {
            final int levelId = i;
            TextButton levelBtn = new TextButton("LEVEL " + levelId, buttonStyle);

            levelBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(game, levelId));
                }
            });

            // Layout logic: 2 buttons per row, Level 7 centered at bottom
            if (levelId == 7) {
                table.add(levelBtn).width(200).height(50).pad(8).colspan(2);
            } else {
                table.add(levelBtn).width(200).height(50).pad(8);
                if (levelId % 2 == 0) {
                    table.row();
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
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

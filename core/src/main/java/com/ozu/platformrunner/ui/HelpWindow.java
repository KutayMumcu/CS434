package com.ozu.platformrunner.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class HelpWindow extends Table {

    public HelpWindow(Label.LabelStyle labelStyle, TextButton.TextButtonStyle buttonStyle) {
        this.setFillParent(true);
        createBackground();

        // Title
        Label title = new Label("CONTROLS", labelStyle);
        title.setFontScale(2.5f);
        this.add(title).padBottom(30).colspan(2);
        this.row();

        // Control List
        addControlRow("Arrows", "Move Left / Right", labelStyle);
        addControlRow("Space", "Jump", labelStyle);
        addControlRow("Z", "Attack", labelStyle);
        addControlRow("1", "Equip Sword", labelStyle);
        addControlRow("2", "Equip Bow", labelStyle);
        addControlRow("P", "Power Up (Double Shot)", labelStyle);
        addControlRow("F5", "Quick Save", labelStyle);
        addControlRow("F9", "Quick Load", labelStyle);
        addControlRow("F1", "Debug Mode", labelStyle);
        addControlRow("ESC", "Pause Game", labelStyle);

        // Close Button
        TextButton closeButton = new TextButton("CLOSE", buttonStyle);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });

        this.add(closeButton).colspan(2).padTop(40);
    }

    private void createBackground() {
        // Generate semi-transparent black background programmatically
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.85f);
        pixmap.fill();
        Texture region = new Texture(pixmap);
        TextureRegionDrawable background = new TextureRegionDrawable(new TextureRegion(region));
        this.setBackground(background);
        pixmap.dispose();
    }

    private void addControlRow(String key, String action, Label.LabelStyle style) {
        Label keyLabel = new Label(key, style);
        keyLabel.setColor(Color.YELLOW);

        Label actionLabel = new Label(action, style);

        this.add(keyLabel).align(Align.right).padRight(20);
        this.add(actionLabel).align(Align.left);
        this.row().padBottom(10);
    }
}

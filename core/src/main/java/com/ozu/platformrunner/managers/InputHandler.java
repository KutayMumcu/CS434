package com.ozu.platformrunner.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.patterns.command.*;

public class InputHandler {
    private final Command moveLeft;
    private final Command moveRight;
    private final Command jump;
    private final Command stop;

    public InputHandler() {
        moveLeft = new MoveLeftCommand();
        moveRight = new MoveRightCommand();
        jump = new JumpCommand();
        stop = new StopCommand();
    }

    public void handleInput(Player player) {
        // Movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft.execute(player);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight.execute(player);
        } else {
            stop.execute(player);
        }

        // Jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jump.execute(player);
        }
    }
}

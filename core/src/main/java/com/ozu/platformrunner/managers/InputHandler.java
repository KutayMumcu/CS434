package com.ozu.platformrunner.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.patterns.command.*;

public class InputHandler {
    // Komut nesnelerini burada tutuyoruz
    private final Command moveLeft;
    private final Command moveRight;
    private final Command jump;
    private final Command stop;

    public InputHandler() {
        // Komutları oluşturuyoruz (İleride bunları bir config dosyasından da okuyabiliriz!)
        moveLeft = new MoveLeftCommand();
        moveRight = new MoveRightCommand();
        jump = new JumpCommand();
        stop = new StopCommand();
    }

    // GameScreen'den her karede çağrılacak metot
    public void handleInput(Player player) {
        // Hareket Kontrolü
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft.execute(player);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight.execute(player);
        } else {
            // Hiçbir şeye basılmıyorsa dur
            stop.execute(player);
        }

        // Zıplama Kontrolü
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jump.execute(player);
        }
    }
}

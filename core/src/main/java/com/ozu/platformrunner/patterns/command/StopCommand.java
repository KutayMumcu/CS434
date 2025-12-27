package com.ozu.platformrunner.patterns.command;

import com.ozu.platformrunner.entities.Player;

public class StopCommand implements Command {
    @Override
    public void execute(Player player) {
        player.stopX();
    }
}

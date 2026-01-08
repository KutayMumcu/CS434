package com.ozu.platformrunner.patterns.command;

import com.ozu.platformrunner.entities.Player;

public interface Command {
    void execute(Player player);
}

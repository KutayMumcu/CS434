package com.ozu.platformrunner.patterns.command;

import com.ozu.platformrunner.entities.Player;

public interface Command {
    // Komutun çalışması için bir "Aktör"e (Player) ihtiyacı var
    void execute(Player player);
}

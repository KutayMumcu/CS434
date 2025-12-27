package com.ozu.platformrunner.patterns.observer;

import com.ozu.platformrunner.entities.Player;

public interface GameObserver {
    void onNotify(Player player, GameEvent event);
}

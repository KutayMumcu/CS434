package com.ozu.platformrunner.patterns.state;

import com.ozu.platformrunner.entities.Player;

public interface PlayerState {
    void enter(Player player);
    void update(Player player, float delta);
    void exit(Player player);
}

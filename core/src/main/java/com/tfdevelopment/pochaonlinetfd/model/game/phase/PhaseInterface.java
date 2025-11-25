package com.tfdevelopment.pochaonlinetfd.model.game.phase;

import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;

public interface PhaseInterface {
    void render(ButtonEvent buttonEvent);
    void makeAnimation(int player);
}

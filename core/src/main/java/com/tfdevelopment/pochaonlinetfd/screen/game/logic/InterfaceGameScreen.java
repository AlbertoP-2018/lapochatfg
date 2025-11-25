package com.tfdevelopment.pochaonlinetfd.screen.game.logic;

import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;

public interface InterfaceGameScreen {
    /**
     * Permite controlar la gestión de los eventos de botón según
     *  el modo de juego (Online, Offline)
     * @param buttonEvent Evento de botón
     * @param input Para el modo Online:
     *    true: El evento viene desde el servidor
     *    false: El evento se envía al servidor
     */
    void controlButtonEvent(ButtonEvent buttonEvent, boolean input);
    void exitGame(boolean toMenu);
}

package com.tfdevelopment.pochaonlinetfd.model.thread;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;

import java.util.concurrent.Semaphore;

public class ThreadButtonEvent extends Thread{
    private static final String TAG = ThreadButtonEvent.class.getName();
    private GameLogic gameLogic;
    private Semaphore semaphoreIA;
    private ButtonEvent buttonEvent;

    public ThreadButtonEvent(GameLogic gameLogic, Semaphore semaphore, ButtonEvent buttonEvent){
        super();
        this.gameLogic = gameLogic;
        this.semaphoreIA = semaphore;
        this.buttonEvent = buttonEvent;
    }

    @Override
    public void run() {
        //super.run();
        try {
            semaphoreIA.acquire();
            gameLogic.doButtonEvent(this.buttonEvent); /***** Thread Button Event *****/
            semaphoreIA.release();
            gameLogic.setExistAI(false);
        } catch (InterruptedException e) {
            Gdx.app.error(TAG,e.getMessage());
        }
    }
}

package com.tfdevelopment.pochaonlinetfd.model.game.phase;

import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;

public class ContinuePhase implements PhaseInterface{
    private static final String TAG = ContinuePhase.class.getName();
    private GameLogic gameLogic;

    public ContinuePhase(GameLogic gameLogic){
        this.gameLogic = gameLogic;
    }

    @Override
    public void render(ButtonEvent buttonEvent) { /***** Thread Button Event *****/
        int player = buttonEvent.getPlayer();

        //Se pasa al siguiente
        if(gameLogic.getGameData().isLastPlayer(player)){
            gameLogic.initHand();
            gameLogic.getGameData().nextPhase();
        } else {
            gameLogic.getGameData().nextCurrentPlayer();
        }
    }

    @Override
    public void makeAnimation(int player) { //ThreadBE
        //Establcer con flag y hacer animación desde el render
    }
}

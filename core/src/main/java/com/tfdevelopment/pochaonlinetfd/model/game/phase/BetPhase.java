package com.tfdevelopment.pochaonlinetfd.model.game.phase;

import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;

public class BetPhase implements PhaseInterface {
    private static final String TAG = BetPhase.class.getName();
    private GameLogic gameLogic;

    public BetPhase(GameLogic gameLogic){
        this.gameLogic = gameLogic;
    }

    @Override
    public void render(ButtonEvent buttonEvent) { /***** Thread Button Event *****/
        int player = buttonEvent.getPlayer();
        //Se añade la apuesta al jugador
        gameLogic.getPlayers()[player].setBet(buttonEvent.getAnswer());
        //Se pasa al siguiente
        if(gameLogic.getGameData().isLastPlayer(player)){
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

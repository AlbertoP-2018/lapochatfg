package com.tfdevelopment.pochaonlinetfd.model.player.ai;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.DecisionB;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Phase;

public class AI_Main {
    private static final String TAG = AI_Main.class.getName();

    public static ButtonEvent getAnswer(GameLogic gameLogic){
        int currentPlayer = gameLogic.getGameData().getCurrentPlayer();
        ButtonEvent buttonEvent = null;
        switch (gameLogic.getGameData().getPhase()){
            case BET:
                buttonEvent = AI_BetAnswer.betAnswer(gameLogic, currentPlayer);
                break;
            case CARD:
                buttonEvent = AI_CardAnswer.cardAnswer(gameLogic, currentPlayer);
                break;
            case CONTINUE:
                buttonEvent = new ButtonEvent(DecisionB.CONTINUE, currentPlayer, -1);
                break;
        }

        if(buttonEvent!=null){
            Phase phase = gameLogic.getGameData().getPhase();
            if(phase!=Phase.CONTINUE && buttonEvent.getAnswer()==-1){
                String text = "";
                if (phase == Phase.BET) text = "AI - La apuesta seleccionada no existe: -1";
                if (phase == Phase.CARD) text = "AI - La carta seleccionada no existe: -1";

                gameLogic.showTest();

                Gdx.app.error(TAG, text);
                gameLogic.manageError(text);
                return null;
            } else return buttonEvent;
        } else {
            Gdx.app.error(TAG, "El objeto ButtonEvent es null");
            gameLogic.manageError("El botón seleccionado no existe");
            return null;
        }
    }
}

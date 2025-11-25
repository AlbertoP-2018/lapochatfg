package com.tfdevelopment.pochaonlinetfd.server.object.buttonevent;

import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;

public class ButtonEventServer {
    private static final String TAG = ButtonEventServer.class.getName();

    private int idEvent;
    private ButtonEvent buttonEvent;
    private int trump;
    private int[][] cards;

    public ButtonEventServer(int idEvent, ButtonEvent buttonEvent, int trump, int[][] cards){
        this.idEvent = idEvent;
        this.buttonEvent = buttonEvent;
        this.trump = trump;
        this.cards = cards;
    }

    public int getIdEvent(){ return idEvent; }
    public ButtonEvent getButtonEvent(){ return buttonEvent; }
    public int getTrump() { return trump; }
    public int[][] getCards() { return cards; }

    public static void runButtonEventServer(OnlineGS onlineGS, ButtonEventServer buttonEventServer){
        onlineGS.getGameLogic().setExistAI(true);
        onlineGS.getGUIBuilder().getGuiBuilderTimer().resetTimer();
        onlineGS.getServerGame().addButtonEvent(buttonEventServer.getIdEvent(), buttonEventServer);
        if(buttonEventServer.getCards()!=null)
            onlineGS.getServerGame().setAllCards(buttonEventServer.getTrump(), buttonEventServer.getCards());
        onlineGS.controlButtonEvent(buttonEventServer.getButtonEvent(), true);
    }
}

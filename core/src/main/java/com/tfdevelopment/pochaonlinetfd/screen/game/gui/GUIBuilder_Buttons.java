package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.model.game.GameData;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.DecisionB;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.PhasePlayer;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class GUIBuilder_Buttons {
    private static final String TAG = GUIBuilder_Buttons.class.getName();
    private static final float W = Constants.VIEWPORT_WIDTH;
    private static final float H = Constants.VIEWPORT_HEIGHT;
    public static final float[] SIZE_BUTTONS =       {W*0.28f,    H*0.25f};

    private final GUIBuilder guiBuilder;
    private Window wBet;
    private Label lNBet;
    private int nBet, banBet; //banBet=indica la apuesta que no se puede realizar

    private Window wContinue;

    public GUIBuilder_Buttons(GUIBuilder guiBuilder){
        this.guiBuilder = guiBuilder;
        this.nBet = -1;
        this.banBet = -1;
    }

    public void render(){
        GameLogic gameLogic = guiBuilder.getGameLogic();
        int currentPlayer = guiBuilder.getGameLogic().getGameData().getCurrentPlayer();
        int indexPlayer = guiBuilder.getGameScreen().getIndexPlayer();

        if(currentPlayer==indexPlayer){
            //Muestra la ventana de apuesta
            if(gameLogic.getPlayers()[indexPlayer].getPhasePlayer()==PhasePlayer.BET){
                if(!wBet.isVisible()) updateBet();
                wBet.setVisible(true);
            } else wBet.setVisible(false);

            //Muestra la ventana de continuar
            if(gameLogic.getPlayers()[indexPlayer].getPhasePlayer()==PhasePlayer.CONTINUE){
                wContinue.setVisible(true);
            } else wContinue.setVisible(false);
        } else {
            wBet.setVisible(false);
            wContinue.setVisible(false);
        }
    }

    public Table createBetLayer(){
        Label lBet = new Label(" Realizar predicción", StyleConfigurator.getLS_Maiandra65());
        lBet.setColor(Color.BLACK);
        lBet.setAlignment(Align.center);
        lNBet = new Label(" ? ", StyleConfigurator.getLS_Maiandra50());
        lNBet.setColor(Color.BLACK);
        lNBet.setAlignment(Align.center);
        TextButton tbLeft = new TextButton(" -", StyleConfigurator.getTBS_RoundButton50(guiBuilder.getSkin()));
        tbLeft.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listenerButton(true);
            }
        });
        TextButton tbRight = new TextButton(" +", StyleConfigurator.getTBS_RoundButton50(guiBuilder.getSkin()));
        tbRight.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listenerButton(false);
            }
        });
        TextButton tbBet = new TextButton(" Jugar ", StyleConfigurator.getTBS_RoundButton50(guiBuilder.getSkin()));
        tbBet.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                makeBet();
            }
        });

        wBet = new Window("", StyleConfigurator.getWS_GameButtons());
        wBet.getTitleLabel().setVisible(false);
        wBet.setVisible(false);
        wBet.setMovable(true);
        wBet.setResizable(false);
        wBet.add(lBet).padBottom(H*0.02f).colspan(3);
        wBet.row();
        wBet.add(tbLeft).size(W*0.1f,H*0.05f);
        wBet.add(lNBet);
        wBet.add(tbRight).size(W*0.1f,H*0.05f);
        wBet.row();
        wBet.add(tbBet).colspan(3).padTop(H*0.01f);

        Table table = new Table();
        table.add(wBet).size(W*0.5f,H*0.2f);
        return table;
    }

    public Table createContinueLayer(){
        Label lContinue = new Label("Pulsa para continuar", StyleConfigurator.getLS_Maiandra65());
        lContinue.setColor(Color.BLACK);
        lContinue.setAlignment(Align.center);

        TextButton tbContinue = new TextButton(" Continuar ", StyleConfigurator.getTBS_RoundButton50(guiBuilder.getSkin()));
        tbContinue.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int indexPlayer = guiBuilder.getGameScreen().getIndexPlayer();
                ButtonEvent buttonEvent = new ButtonEvent(DecisionB.CONTINUE,indexPlayer, -1);
                guiBuilder.getGameScreen().controlButtonEvent(buttonEvent, guiBuilder.getGameScreen().getGameMode()== PochaEnum.GameMode.OFFLINE);
            }
        });

        wContinue = new Window("", StyleConfigurator.getWS_GameButtons());
        wContinue.getTitleLabel().setVisible(false);
        wContinue.setVisible(false);
        wContinue.setMovable(true);
        wContinue.setResizable(false);
        wContinue.add(lContinue).padBottom(H*0.015f);
        wContinue.row();
        wContinue.add(tbContinue).padTop(H*0.01f);

        Table table = new Table();
        table.bottom();
        table.add(wContinue).size(W*0.5f,H*0.16f).padBottom(H*0.05f);
        return table;
    }

    private void makeBet(){
        GameLogic gameLogic = guiBuilder.getGameLogic();
        GameData gameData = gameLogic.getGameData();
        int currentPlayer = gameData.getCurrentPlayer();
        int indexPlayer = guiBuilder.getGameScreen().getIndexPlayer();
        int bet = Integer.parseInt(lNBet.getText().toString().replace(" ",""));

        if(currentPlayer==indexPlayer && gameLogic.getPlayers()[indexPlayer].getPhasePlayer()==PochaEnum.PhasePlayer.BET){
            ButtonEvent buttonEvent = new ButtonEvent(DecisionB.BET,indexPlayer, bet);
            //guiBuilder.getGameLogic().controlButtonEvent(buttonEvent);
            guiBuilder.getGameScreen().controlButtonEvent(buttonEvent, guiBuilder.getGameScreen().getGameMode()==PochaEnum.GameMode.OFFLINE);
        } else Gdx.app.error(TAG, "No se puede realizar la apuesta. El jugador no corresponde.");
    }

    public void updateBet(){
        banBet = -1;
        nBet = -1;
        int indexPlayer = guiBuilder.getGameScreen().getIndexPlayer();
        int handPlayer = guiBuilder.getGameLogic().getGameData().getHandPlayer();
        int lastPlayer = guiBuilder.getGameLogic().getGameData().getPreviousPlayer(handPlayer);
        int totalChances = guiBuilder.getGameLogic().getGameScore().getNumChancesHand();
        int totalBets = 0;

        for(int i = 0; i<guiBuilder.getGameScreen().getTOTAL_PLAYERS(); i++){
            int auxBet = guiBuilder.getGameLogic().getPlayers()[i].getBet();
            if(auxBet>=0) totalBets+=auxBet;
        }

        if(indexPlayer==lastPlayer) banBet = totalChances-totalBets;

        if(banBet==0) nBet = 1;
        else nBet = 0;
        /*Gdx.app.log(TAG,"TotalChances: "+totalChances);
        Gdx.app.log(TAG,"TotalBets: "+totalBets);
        Gdx.app.log(TAG,"BanBet: "+banBet);*/
        lNBet.setText(" "+nBet+" ");
    }

    private void listenerButton(boolean bLeft){
        if(bLeft){
            if(nBet!=0){
                nBet-=1;
                if(nBet==banBet) nBet-=1;
                if(nBet<0) nBet=1;
            }
        } else {
            int totalChances = guiBuilder.getGameLogic().getGameScore().getNumChancesHand();
            if(nBet<totalChances){
                nBet+=1;
                if(nBet==banBet) nBet+=1;
                if(nBet>totalChances) nBet-=2;
            }
        }
        lNBet.setText(" "+nBet+" ");
    }
}

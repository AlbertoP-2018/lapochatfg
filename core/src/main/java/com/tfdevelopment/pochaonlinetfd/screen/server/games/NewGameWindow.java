package com.tfdevelopment.pochaonlinetfd.screen.server.games;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class NewGameWindow {
    private static final String TAG = NewGameWindow.class.getName();
    private static final int NUM_PLAYERS_DEFAULT = 5;

    private ServerMainScreen serverMainScreen;
    private GameContainer gameContainer;

    private Window wNewGame;

    public NewGameWindow(ServerMainScreen serverMainScreen, GameContainer gameContainer) {
        this.serverMainScreen = serverMainScreen;
        this.gameContainer = gameContainer;
    }

    public void showNewGame(){
        wNewGame.setVisible(true);
    }

    public Table buildNewGameWindow(){
        wNewGame = new Window("¡Nueva partida!", StyleConfigurator.getWS_Maiandra80(serverMainScreen.getSkin()));
        wNewGame.setVisible(false);
        wNewGame.setMovable(false);
        wNewGame.setResizable(false);
        gameContainer.setStyleWindow(wNewGame);

        Label lNumBots = new Label("Bots: ", StyleConfigurator.getLS_Maiandra54());
        final SelectBox sbBots = new SelectBox(StyleConfigurator.getSBS_LevelIA(serverMainScreen.getSkin()));
        sbBots.getList().setAlignment(Align.center);

        Label lNumPlayers = new Label("Jugadores ", StyleConfigurator.getLS_Maiandra54());
        final SelectBox sbPlayers = new SelectBox(StyleConfigurator.getSBS_LevelIA(serverMainScreen.getSkin()));
        Array<Integer> numPlayers = new Array<>();
        numPlayers.add(4); numPlayers.add(5); numPlayers.add(6); numPlayers.add(7); numPlayers.add(8);
        sbPlayers.setItems(numPlayers);
        sbPlayers.getList().setAlignment(Align.center);
        sbPlayers.setSelected(NUM_PLAYERS_DEFAULT);
        updateNumBots(NUM_PLAYERS_DEFAULT, sbBots);
        sbPlayers.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int numPlayers = (int) sbPlayers.getSelected();
                updateNumBots(numPlayers, sbBots);
            }
        });

        Label lAILevel = new Label("Dificultad ", StyleConfigurator.getLS_Maiandra54());
        final SelectBox sbAILevel = new SelectBox(StyleConfigurator.getSBS_LevelIA(serverMainScreen.getSkin()));
        sbAILevel.setItems(new String[]{" Fácil "});
        sbAILevel.getList().setAlignment(Align.center);

        Label lTypeGame = new Label("Modo de juego ", StyleConfigurator.getLS_Maiandra54());
        final SelectBox slMode = new SelectBox(StyleConfigurator.getSBS_LevelIA(serverMainScreen.getSkin()));
        slMode.setItems(new String[]{" Subiendo "});
        slMode.getList().setAlignment(Align.center);

        TextButton tbNewGame = new TextButton(" Crear partida ", StyleConfigurator.getTBS_RoundButton60(serverMainScreen.getSkin()));
        tbNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int numPlayers = (int) sbPlayers.getSelected();
                int numBots = (int) sbBots.getSelected();
                String mode = slMode.getSelected().toString();

                Settings.playSoundClickButton();
                serverMainScreen.getNodeJS().getNewGameJS().eoNewGame(numPlayers, numBots, mode);
                wNewGame.setVisible(false);
            }
        });

        TextButton tbCancel = new TextButton(" Cancelar ", StyleConfigurator.getTBS_RoundButton60(serverMainScreen.getSkin()));
        tbCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wNewGame.setVisible(false);
            }
        });

        Table tNewGame = new Table();
        tNewGame.setFillParent(true);
        wNewGame.add(lNumPlayers);
        wNewGame.add(sbPlayers).padBottom(Constants.VIEWPORT_HEIGHT*0.005f);
        wNewGame.row();
        wNewGame.add(lNumBots);
        wNewGame.add(sbBots).padBottom(Constants.VIEWPORT_HEIGHT*0.005f);
        wNewGame.row();
        wNewGame.add(lAILevel);
        wNewGame.add(sbAILevel).padBottom(Constants.VIEWPORT_HEIGHT*0.005f);
        wNewGame.row();
        wNewGame.add(lTypeGame);
        wNewGame.add(slMode).padBottom(Constants.VIEWPORT_HEIGHT*0.008f);
        wNewGame.row();
        wNewGame.add(tbCancel).space(10f);
        wNewGame.add(tbNewGame).space(10f);
        wNewGame.row();
        tNewGame.add(wNewGame).size(Constants.VIEWPORT_WIDTH*0.8f, Constants.VIEWPORT_HEIGHT*0.35f);

        return tNewGame;

    }

    private void updateNumBots(int numPlayers, SelectBox selectBox) {
        Array<Integer> bots = new Array<>();
        for (int i=0; i<numPlayers; i++) {
            bots.add(i);
        }
        selectBox.setItems(bots);
        selectBox.setSelected(0);
    }

    /*** Getters And Setters ***/
    public Window getwNewGame(){ return wNewGame; }
}

package com.tfdevelopment.pochaonlinetfd.screen.server.games;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.object.game.NewGameDataServer;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class CurrentGameWindow {
    private static final String DELETE_GAME = " Eliminar partida ";
    private static final String LEAVE_GAME = " Abandonar partida ";

    private ServerMainScreen serverMainScreen;
    private GameContainer gameContainer;

    private Window wCurrentGameLayout;
    private Label lPlayers;
    private TextButton tbExit;

    public CurrentGameWindow(ServerMainScreen serverMainScreen, GameContainer gameContainer) {
        this.serverMainScreen = serverMainScreen;
        this.gameContainer = gameContainer;
    }

    public void updateCurrentGameWindow(NewGameDataServer newGameDataServer, boolean host){
        String mode = newGameDataServer.getMode();
        int totalPlayers = newGameDataServer.getTotalPlayers();
        int players = newGameDataServer.getNumPlayers();
        tbExit.setText(host ? DELETE_GAME : LEAVE_GAME);
        wCurrentGameLayout.getTitleLabel().setText(mode);
        lPlayers.setText(players + " / " +totalPlayers);
        wCurrentGameLayout.setVisible(true);
    }

    public Table builCurrentGameWindow(){
        wCurrentGameLayout = new Window("?", StyleConfigurator.getWS_Maiandra80(serverMainScreen.getSkin()));
        wCurrentGameLayout.setVisible(false);
        wCurrentGameLayout.setMovable(false);
        wCurrentGameLayout.setResizable(false);
        gameContainer.setStyleWindow(wCurrentGameLayout);

        Label lWaitingPlayers = new Label("Esperando jugadores...", StyleConfigurator.getLS_Maiandra54());
        lPlayers = new Label("-/-", StyleConfigurator.getLS_Maiandra54());

        tbExit = new TextButton("?", StyleConfigurator.getTBS_RoundButton60(serverMainScreen.getSkin()));
        tbExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                serverMainScreen.getNodeJS().getNewGameJS().eoLeaveGame();
            }
        });

        Table tCurrentGame = new Table();
        tCurrentGame.setFillParent(true);
        wCurrentGameLayout.add(lWaitingPlayers);
        wCurrentGameLayout.row();
        wCurrentGameLayout.add(lPlayers);
        wCurrentGameLayout.row();
        wCurrentGameLayout.add(tbExit).colspan(1).space(10f);
        wCurrentGameLayout.row();
        tCurrentGame.add(wCurrentGameLayout);

        return tCurrentGame;
    }

    /*** Getters And Setters ***/
    public Window getwCurrentGameLayout(){ return wCurrentGameLayout; }
}

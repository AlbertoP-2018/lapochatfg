package com.tfdevelopment.pochaonlinetfd.screen.server.window;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class HW_Invitation {
    private ServerMainScreen serverMainScreen;
    private HallWindows hallWindows;

    private Window wOKInvitationGame;
    private Label lOKInvitationGame;

    private Window wSendInvitationGame;
    private Label lSendInvitationGame;

    public HW_Invitation(ServerMainScreen serverMainScreen, HallWindows hallWindows){
        this.serverMainScreen = serverMainScreen;
        this.hallWindows = hallWindows;
    }

    public Table buildOKInvitationGame(){
        wOKInvitationGame = new Window(" Invitación a partida", StyleConfigurator.getWS_Maiandra80(serverMainScreen.getSkin()));
        wOKInvitationGame.setVisible(false);
        wOKInvitationGame.setMovable(false);
        wOKInvitationGame.setResizable(false);
        hallWindows.setStyleWindow(wOKInvitationGame);

        lOKInvitationGame = new Label(" La invitación ha sido enviada. ", StyleConfigurator.getLS_Maiandra65());
        lOKInvitationGame.setAlignment(Align.center);
        TextButton tbInvitationGame = new TextButton("Vale", StyleConfigurator.getTBS_RoundButton65(serverMainScreen.getSkin()));

        tbInvitationGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wOKInvitationGame.setVisible(false);
            }
        });

        Table layerInvitationGame = new Table();
        layerInvitationGame.setFillParent(true);
        wOKInvitationGame.add(lOKInvitationGame).space(10f);
        wOKInvitationGame.row();
        wOKInvitationGame.add(tbInvitationGame).space(10f);
        wOKInvitationGame.row();
        layerInvitationGame.add(wOKInvitationGame);

        return layerInvitationGame;
    }

    public Table buildSendInvitationGame(){
        wSendInvitationGame = new Window("Invitación a partida", StyleConfigurator.getWS_Maiandra80(serverMainScreen.getSkin()));
        wSendInvitationGame.setVisible(false);
        wSendInvitationGame.setMovable(false);
        wSendInvitationGame.setResizable(false);
        hallWindows.setStyleWindow(wSendInvitationGame);

        lSendInvitationGame = new Label("?", StyleConfigurator.getLS_Maiandra65());
        TextButton tbInvitationGame = new TextButton("Vale", StyleConfigurator.getTBS_RoundButton65(serverMainScreen.getSkin()));

        tbInvitationGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wSendInvitationGame.setVisible(false);
            }
        });

        Table layerInvitationGame = new Table();
        layerInvitationGame.setFillParent(true);
        wSendInvitationGame.add(lSendInvitationGame).space(10f);
        wSendInvitationGame.row();
        wSendInvitationGame.add(tbInvitationGame).space(10f);
        wSendInvitationGame.row();
        layerInvitationGame.add(wSendInvitationGame);

        return layerInvitationGame;
    }

    public void showOKInvitationGame(boolean ok){
        String text = "?";
        if(ok) text = " La invitación ha sido enviada. ";
        else text = " Para invitar a alguin debes \n estar en partida. ";
        lOKInvitationGame.setText(text);
        wOKInvitationGame.setVisible(true);
    }

    public void setInvitationGame(String playerName, String gameId, int remainingPlayers){
        String jugadores = "Faltan [GREEN]"+remainingPlayers+"[WHITE] jugadores.";
        if(remainingPlayers==1) jugadores="Falta [GREEN]"+remainingPlayers+"[WHITE] jugador.";

        String text = " "+playerName+" te ha invitado a jugar.\n Partida: [GREEN] "+(gameId)+"[WHITE]. \n\n"+jugadores;

        if(lSendInvitationGame!=null) {
            lSendInvitationGame.setText(text);
            lSendInvitationGame.setAlignment(Align.center);
        }
    }

    public void showSendInvitationGame(){
        wSendInvitationGame.setVisible(true);
    }
}

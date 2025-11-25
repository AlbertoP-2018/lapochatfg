package com.tfdevelopment.pochaonlinetfd.screen.game.gui.windows;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.GUIBuilder;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class GUIWindows_Invitation {
    private GUIBuilder guiBuilder;
    private GUIBuilder_Windows guiBuilderWindows;

    private Window wInvitation;
    private Label lInvitation;
    private String playerName, gameId;
    private int remainingPlayers;

    public GUIWindows_Invitation(GUIBuilder guiBuilder, GUIBuilder_Windows guiBuilderWindows) {
        this.guiBuilder = guiBuilder;
        this.guiBuilderWindows = guiBuilderWindows;
    }

    public void setInvitation(String playerName, String gameId, int remainingPlayers){
        this.playerName = playerName;
        this.gameId = gameId;
        this.remainingPlayers = remainingPlayers;
    }

    public void showNotice(){
        String jugadores = "Faltan [GREEN]"+remainingPlayers+"[WHITE] jugadores.";
        if(remainingPlayers==1) jugadores="Falta [GREEN]"+remainingPlayers+"[WHITE] jugador.";

        String text = " "+playerName+" te ha invitado a jugar.\n Partida: [GREEN] "+(gameId)+"[WHITE]. \n\n"+jugadores;

        lInvitation.setText(text);
        wInvitation.setVisible(true);
    }

    Table buildWindowInvitation(){
        wInvitation = new Window("Invitación a partida", StyleConfigurator.getWS_Default(guiBuilder.getSkin()));
        wInvitation.setVisible(false);
        wInvitation.setMovable(false);
        wInvitation.setResizable(false);
        guiBuilderWindows.setStyleWindow(wInvitation);

        lInvitation = new Label("???", StyleConfigurator.getLS_Maiandra65());
        lInvitation.setAlignment(Align.center);

        TextButton tbOk = new TextButton(" Vale ", StyleConfigurator.getTBS_RoundButton60(guiBuilder.getSkin()));
        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wInvitation.setVisible(false);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        wInvitation.add(lInvitation).space(10f).padLeft(10f).padRight(10f);
        wInvitation.row();
        wInvitation.add(tbOk).space(10f).padLeft(10f).padRight(10f);
        wInvitation.row();
        table.add(wInvitation);

        return table;
    }
}

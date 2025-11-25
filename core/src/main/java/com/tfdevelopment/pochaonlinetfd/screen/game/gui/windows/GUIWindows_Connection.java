package com.tfdevelopment.pochaonlinetfd.screen.game.gui.windows;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.GUIBuilder;
import com.tfdevelopment.pochaonlinetfd.server.object.playerdata.ServerPlayer;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class GUIWindows_Connection {
    private GUIBuilder guiBuilder;
    private GUIBuilder_Windows guiBuilderWindows;

    private Window wPlayerReconnected;
    private Label lPlayerReconnected;

    private Window wServerDisconnected;

    public GUIWindows_Connection(GUIBuilder guiBuilder, GUIBuilder_Windows guiBuilderWindows) {
        this.guiBuilder = guiBuilder;
        this.guiBuilderWindows = guiBuilderWindows;
    }

    public void playerReconnected(boolean playerOut, int indexPlayer){
        String title, text, namePlayer;
        String textOutput =  " ha perdido la conexión.";
        String textInput = " se ha reconectado.";

        if(playerOut) title = "Conexión perdida";
        else title = "Reconexión a partida";

        //PochaPlayer pochaPlayer = guiBuilder.getGameLogic().getPlayers()[indexPlayer];
        ServerPlayer serverPlayer = ((OnlineGS)guiBuilder.getGameScreen()).getServerGame().getGamePlayers().get(indexPlayer);
        namePlayer = serverPlayer.getName();
        if (playerOut) {
            text = namePlayer + textOutput;
            serverPlayer.setDisconnected(true);
        } else {
            text = namePlayer + textInput;
            serverPlayer.setDisconnected(false);
        }

        wPlayerReconnected.getTitleLabel().setText(title);
        lPlayerReconnected.setText(text);
        wPlayerReconnected.setVisible(true);
    }

    public Table buildPlayerReconnectedWindow(){
        wPlayerReconnected = new Window("Conexión perdida", StyleConfigurator.getWS_Maiandra80(guiBuilder.getSkin()));
        wPlayerReconnected.setVisible(false);
        wPlayerReconnected.setMovable(false);
        wPlayerReconnected.setResizable(false);
        guiBuilderWindows.setStyleWindow(wPlayerReconnected);

        lPlayerReconnected = new Label("-", StyleConfigurator.getLS_Maiandra65());
        lPlayerReconnected.setAlignment(Align.center);
        TextButton tbOk = new TextButton("Vale", StyleConfigurator.getTBS_Maiandra65(guiBuilder.getSkin()));

        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wPlayerReconnected.setVisible(false);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        wPlayerReconnected.add(lPlayerReconnected).space(10f).padLeft(10f).padRight(10f);
        wPlayerReconnected.row();
        wPlayerReconnected.add(tbOk).space(10f).padLeft(10f).padRight(10f);
        wPlayerReconnected.row();
        table.add(wPlayerReconnected);

        return table;
    }

    public Table buildServerDisconnetiondWindow(){
        wServerDisconnected = new Window("Error de conexión", StyleConfigurator.getWS_Default(guiBuilder.getSkin()));
        wServerDisconnected.setVisible(false);
        wServerDisconnected.setMovable(false);
        wServerDisconnected.setResizable(false);
        guiBuilderWindows.setStyleWindow(wServerDisconnected);

        String message =
                "  Se ha perdido la conexión con el servidor.  \n"+
                " Si no consigue reconectarse, abandone \n y vuelva a entrar. \n\n"+
                " Reconectando... ";
        Label lServerDisconnected = new Label(message, StyleConfigurator.getLS_Maiandra65());
        lServerDisconnected.setAlignment(Align.center);
        TextButton tbExit = new TextButton("Salir", StyleConfigurator.getTBS_Maiandra65(guiBuilder.getSkin()));

        tbExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wServerDisconnected.setVisible(false);
            }
        });

        Table layerReturnMenu = new Table();
        layerReturnMenu.setFillParent(true);
        wServerDisconnected.add(lServerDisconnected).space(10f);
        wServerDisconnected.row();
        layerReturnMenu.add(wServerDisconnected);

        return layerReturnMenu;
    }

    public Window getwServerDisconnected(){ return wServerDisconnected; }
}

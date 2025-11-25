package com.tfdevelopment.pochaonlinetfd.screen.game.gui.windows;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.GUIBuilder;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class GUIWindows_Reload {
    private GUIBuilder guiBuilder;
    private GUIBuilder_Windows guiBuilderWindows;

    private Window wLogin;

    public GUIWindows_Reload(GUIBuilder guiBuilder, GUIBuilder_Windows guiBuilderWindows) {
        this.guiBuilder = guiBuilder;
        this.guiBuilderWindows = guiBuilderWindows;
    }

    Table buildLoginReloadWindow(){
        wLogin = new Window("Cargando partida", StyleConfigurator.getWS_Default(guiBuilder.getSkin()));
        wLogin.setVisible(false);
        wLogin.setMovable(false);
        wLogin.setResizable(false);
        guiBuilderWindows.setStyleWindow(wLogin);

        Label lLoading = new Label("Cargando datos de partida...", StyleConfigurator.getLS_Maiandra65());
        lLoading.setAlignment(Align.center);

        Table tableLoadingGame = new Table();
        tableLoadingGame.setFillParent(true);
        wLogin.add(lLoading).space(10f);
        wLogin.row();
        tableLoadingGame.add(wLogin);

        return tableLoadingGame;
    }

    public Window getwLogin(){ return this.wLogin; }
}

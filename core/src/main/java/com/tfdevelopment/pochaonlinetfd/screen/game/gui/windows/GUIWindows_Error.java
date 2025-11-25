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

public class GUIWindows_Error {
    private GUIBuilder guiBuilder;
    private GUIBuilder_Windows guiBuilderWindows;

    private Window wError;
    private Label lError;

    public GUIWindows_Error(GUIBuilder guiBuilder, GUIBuilder_Windows guiBuilderWindows) {
        this.guiBuilder = guiBuilder;
        this.guiBuilderWindows = guiBuilderWindows;
    }

    public void setError(String noticeGame){
        lError.setText(noticeGame);
        wError.setVisible(true);
    }

    public void showErrorWindow(){
        wError.setVisible(true);
    }

    Table buildErrorWindow(){
        wError = new Window("Algo ha salido mal...", StyleConfigurator.getWS_Default(guiBuilder.getSkin()));
        wError.setVisible(false);
        wError.setMovable(false);
        wError.setResizable(false);
        guiBuilderWindows.setStyleWindow(wError);

        lError = new Label("???", StyleConfigurator.getLS_Maiandra65());
        lError.setAlignment(Align.center);
        TextButton tbOk = new TextButton(" Cerrar ", StyleConfigurator.getTBS_Maiandra65(guiBuilder.getSkin()));

        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wError.setVisible(false);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        wError.add(lError).space(10f).padLeft(10f).padRight(10f);
        wError.row();
        wError.add(tbOk).space(10f).padLeft(10f).padRight(10f);
        wError.row();
        table.add(wError);

        return table;
    }
}

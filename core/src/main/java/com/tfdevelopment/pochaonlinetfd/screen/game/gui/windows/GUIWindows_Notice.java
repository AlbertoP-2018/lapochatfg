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

public class GUIWindows_Notice {
    private GUIBuilder guiBuilder;
    private GUIBuilder_Windows guiBuilderWindows;

    private Window wNotice;
    private Label lNotice;

    public GUIWindows_Notice(GUIBuilder guiBuilder, GUIBuilder_Windows guiBuilderWindows) {
        this.guiBuilder = guiBuilder;
        this.guiBuilderWindows = guiBuilderWindows;
    }

    public void setNotice(String title, String body){
        wNotice.getTitleLabel().setText(title);
        lNotice.setText(body);
    }

    public void showNotice(){
        wNotice.setVisible(true);
    }

    Table buildWindowNotice(){
        wNotice = new Window("¡Aviso!", StyleConfigurator.getWS_Default(guiBuilder.getSkin()));
        wNotice.setVisible(false);
        wNotice.setMovable(false);
        wNotice.setResizable(false);
        guiBuilderWindows.setStyleWindow(wNotice);

        lNotice = new Label("???", StyleConfigurator.getLS_Maiandra65());
        lNotice.setAlignment(Align.center);
        TextButton tbOk = new TextButton("Vale", StyleConfigurator.getTBS_Maiandra65(guiBuilder.getSkin()));

        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wNotice.setVisible(false);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        wNotice.add(lNotice).space(10f).padLeft(10f).padRight(10f);
        wNotice.row();
        wNotice.add(tbOk).space(10f).padLeft(10f).padRight(10f);
        wNotice.row();
        table.add(wNotice);

        return table;
    }
}

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

public class HW_Notice {
    private ServerMainScreen serverMainScreen;
    private HallWindows hallWindows;

    private Window wNoticeServer;
    private Label lNoticeServer;

    private Window wNoticeTournament;
    private Label lNoticeTournament;

    public HW_Notice(ServerMainScreen serverMainScreen, HallWindows hallWindows){
        this.serverMainScreen = serverMainScreen;
        this.hallWindows = hallWindows;
    }

    public Table buildWindowNoticeServer() {
        wNoticeServer = new Window("¡Aviso!", StyleConfigurator.getWS_Maiandra80(serverMainScreen.getSkin()));
        wNoticeServer.setVisible(false);
        wNoticeServer.setMovable(false);
        wNoticeServer.setResizable(false);
        hallWindows.setStyleWindow(wNoticeServer);

        lNoticeServer = new Label("???", StyleConfigurator.getLS_Maiandra65());
        lNoticeServer.setAlignment(Align.center);
        TextButton tbOk = new TextButton("Vale", StyleConfigurator.getTBS_Maiandra65(serverMainScreen.getSkin()));

        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wNoticeServer.setVisible(false);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        wNoticeServer.add(lNoticeServer).space(10f).padLeft(10f).padRight(10f);
        wNoticeServer.row();
        wNoticeServer.add(tbOk).space(10f).padLeft(10f).padRight(10f);
        wNoticeServer.row();
        table.add(wNoticeServer);

        return table;
    }

    public Table buildWindowNoticeTournament() {
        wNoticeTournament = new Window("¡Aviso!", StyleConfigurator.getWS_Maiandra80(serverMainScreen.getSkin()));
        wNoticeTournament.setVisible(false);
        wNoticeTournament.setMovable(false);
        wNoticeTournament.setResizable(false);
        hallWindows.setStyleWindow(wNoticeTournament);

        lNoticeTournament = new Label("???", StyleConfigurator.getLS_Maiandra65());
        lNoticeTournament.setAlignment(Align.center);
        TextButton tbOk = new TextButton("Vale", StyleConfigurator.getTBS_Maiandra65(serverMainScreen.getSkin()));

        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wNoticeTournament.setVisible(false);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        wNoticeTournament.add(lNoticeTournament).space(10f).padLeft(10f).padRight(10f);
        wNoticeTournament.row();
        wNoticeTournament.add(tbOk).space(10f).padLeft(10f).padRight(10f);
        wNoticeTournament.row();
        table.add(wNoticeTournament);

        return table;
    }

    public void updateNoticeServer(String title, String notice){
        wNoticeServer.getTitleLabel().setText(title);
        lNoticeServer.setText(notice);
        wNoticeServer.setVisible(true);
    }

    public void updateNoticeTournament(String title, String notice){
        wNoticeTournament.getTitleLabel().setText(title);
        lNoticeTournament.setText(notice);
        wNoticeTournament.setVisible(true);
    }
}

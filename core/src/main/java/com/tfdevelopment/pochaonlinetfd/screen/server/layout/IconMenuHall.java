package com.tfdevelopment.pochaonlinetfd.screen.server.layout;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.screen.server.menu.HallMenuButtons;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class IconMenuHall extends Stack {
    private static final String TAG = IconMenuHall.class.getName();

    private static final float MENU_HEIGHT_BACKGROUND = Constants.VIEWPORT_HEIGHT *0.09f;
    private static final float MENU_WIDTH_ICON = Constants.VIEWPORT_WIDTH *0.15f;
    private static final float MENU_HEIGHT_ICON = MENU_HEIGHT_BACKGROUND*0.7f;

    private HallMenuButtons hallMenuButtons;
    private Table tInfo;
    private Label lInfo;
    private TextureRegionDrawable trdUp, trdDown;
    private Button bIcon;
    private TextButton tbInfo;
    private Table tButtonMenu;
    private Skin skin;

    private String nameButton;
    private boolean selected;

    public IconMenuHall(HallMenuButtons hallMenuButtons, String nameButton, TextureRegionDrawable trdUp, TextureRegionDrawable trdDown, Skin skin){
        super();
        this.hallMenuButtons = hallMenuButtons;
        this.nameButton = nameButton;
        this.trdDown = trdDown;
        this.trdUp = trdUp;
        this.selected = false;
        this.skin = skin;

        createButton();
    }

    private void createButton(){
        bIcon = new Button(trdUp);
        Table tButton = new Table();
        tButton.setDebug(false);
        tButton.setFillParent(true);
        tButton.add(bIcon).size(MENU_WIDTH_ICON, MENU_HEIGHT_ICON);

        tbInfo = new TextButton("?", StyleConfigurator.getTBS_MenuHall(skin));

        tbInfo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(nameButton.equals("bNews")) hallMenuButtons.listenerNews(false);
                if(nameButton.equals("bPlayers")) hallMenuButtons.listenerPlayers();
                if(nameButton.equals("bUser")) hallMenuButtons.listenerUser();
                if(nameButton.equals("bRanking")) hallMenuButtons.listenerRanking();
            }
        });
        tbInfo.getLabel().setFontScale(0.52f);

        tInfo = new Table();
        tInfo.add(tbInfo).size(Constants.VIEWPORT_WIDTH *0.060f, Constants.VIEWPORT_HEIGHT *0.030f)
                .padBottom(Constants.VIEWPORT_HEIGHT *0.040f).padLeft(Constants.VIEWPORT_WIDTH *0.13f);
        tInfo.setVisible(false);

        if(nameButton.equals("bPlayers")) tInfo.setVisible(true);
        if(nameButton.equals("bTournament0")) tInfo.setVisible(true);
        if(nameButton.equals("bTournament1")) tInfo.setVisible(true);

        this.setDebug(false);
        this.add(tButton);
        this.add(tInfo);
    }

    public void updateInfo(boolean show, String text){
        if(show){
            tbInfo.getLabel().setText(text);
            tInfo.setVisible(true);
        } else tInfo.setVisible(false);
    }

    public void drawButtonUp(){
        selected = true;
        bIcon.getStyle().up = trdUp;
    }

    public void drawButtonDown(){
        selected = false;
        bIcon.getStyle().up = trdDown;
    }

    /** Getters and Setters */
    public Button getbIcon(){ return bIcon; }
    public TextButton getTbInfo(){ return tbInfo; }
}

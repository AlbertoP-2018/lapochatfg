package com.tfdevelopment.pochaonlinetfd.screen.server.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.screen.server.layout.IconMenuHall;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.ServerObject;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class HallMenuButtons {
    private static final String TAG = HallMenuButtons.class.getName();

    private static final int NUM_BUTTONS = 4;
    private static final float MENU_WIDTH_BACKGROUND = Constants.VIEWPORT_WIDTH *0.9f;
    private static final float MENU_HEIGHT_BACKGROUND = Constants.VIEWPORT_HEIGHT *0.09f;
    private static final float MENU_WIDTH_ICON = Constants.VIEWPORT_WIDTH *0.15f;
    private static final float MENU_HEIGHT_ICON = MENU_HEIGHT_BACKGROUND*0.7f;
    private static final float PAD_BOTTOM_MENU = Constants.VIEWPORT_HEIGHT *0.005f;

    private ServerMainScreen serverMainScreen;
    private HallMenuPlayers hallMenuPlayers;
    private HallMenuNews hallMenuNews;
    private HallMenuRanking hallMenuRanking;
    private HallMenuUser hallMenuUser;

    private ScrollPane spMenu;
    private Table tMenuPlayers, tMenuNews, tMenuUser, tMenuRanking;
    private IconMenuHall imhExit, imhPlayers, imhNews, imhUser, imhRanking;

    private Button bArrowLeft, bArrowRight;
    private float buttonArrowY;

    private Actor[] buttonActors;
    private IconMenuHall[] imhActors;

    private static final Color COLOR_UP_INFO = Color.WHITE;
    private static final Color COLOR_DOWN_INFO = Color.WHITE;

    public HallMenuButtons(ServerMainScreen serverMainScreen){
        this.serverMainScreen = serverMainScreen;
        this.hallMenuPlayers = new HallMenuPlayers(this.serverMainScreen);
        this.hallMenuNews = new HallMenuNews(this.serverMainScreen);
        this.hallMenuUser = new HallMenuUser(this.serverMainScreen);
        this.hallMenuRanking = new HallMenuRanking(this.serverMainScreen);

        buttonActors = new Actor[NUM_BUTTONS];
        imhActors = new IconMenuHall[NUM_BUTTONS];
    }

    private static final float DURATION_ANIMATION = 0.7f;
    private float MOVEMENT_Y_ANIMATION = 2f;
    private float tAnimationRow = 3f, tAnimationLoad=3f;
    private float spPercentX_Left, spPercentX_Right;
    public void doAnimation(float delta){
        //Actualiza el movimiento de las flechas
        if(spMenu!=null) {
            if (spMenu.getScrollPercentX() >= spPercentX_Right) bArrowRight.setVisible(false);
            else bArrowRight.setVisible(true);

            if (spMenu.getScrollPercentX() <= spPercentX_Left) bArrowLeft.setVisible(false);
            else bArrowLeft.setVisible(true);

            tAnimationRow += delta;
            if (tAnimationRow >= DURATION_ANIMATION) {
                bArrowRight.addAction(Actions.sequence(Actions.moveBy(MOVEMENT_Y_ANIMATION, buttonArrowY, DURATION_ANIMATION)));
                bArrowLeft.addAction(Actions.sequence(Actions.moveBy(-MOVEMENT_Y_ANIMATION, buttonArrowY, DURATION_ANIMATION)));

                tAnimationRow = 0f;
                MOVEMENT_Y_ANIMATION = -MOVEMENT_Y_ANIMATION;
            }
        }

        //Actualiza el icono de Cargar
        hallMenuUser.updateLoadingAnimation();
        hallMenuPlayers.updateLoadingAnimation();
        hallMenuRanking.updateLoadingAnimation();
        hallMenuNews.updateLoadingAnimation();
    }

    public void updateTextButtonPlayers() {
        int numPlayers = 0;
        for (int i = 0; i < serverMainScreen.getAlUserDataServer().size(); i++) {
            if (!serverMainScreen.getAlUserDataServer().get(i).getName().contains("CPU"))
                numPlayers++;
        }

        imhPlayers.updateInfo(true, ""+numPlayers);
    }

    public boolean isMenuContentVisible(){
        return tMenuPlayers.isVisible() || tMenuUser.isVisible() || tMenuRanking.isVisible() || tMenuNews.isVisible();
    }

    public Stack buildMenuLayer(){
        tMenuPlayers = hallMenuPlayers.buildMenuPlayers();
        tMenuNews = hallMenuNews.buildMenuNews();
        tMenuUser = hallMenuUser.buildMenuUser();
        tMenuRanking = hallMenuRanking.buildMenuRanking();

        Stack stack = new Stack();
        stack.setDebug(false);
        stack.add(tMenuPlayers);
        stack.add(tMenuNews);
        stack.add(tMenuUser);
        stack.add(tMenuRanking);
        stack.add(buildIconsButtonsLayer());

        return stack;
    }

    float padRight = 15f;
    private Table buildIconsButtonsLayer(){
        //******** INI - BACKGROUND ************
        Button ibBackground = new Button(StaticsMethods.getNinePatchDrawable(AssetLoader.aLoader.aImage.getBackgroundMenuHall(), 5));
        ibBackground.setDisabled(true);
        Table tBackground = new Table();
        tBackground.add(ibBackground).size(MENU_WIDTH_BACKGROUND,MENU_HEIGHT_BACKGROUND);
        //******** FIN - BACKGROUND ************

        //******** INI - FLECHAS ************
        bArrowLeft = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getLeftArrow()));
        bArrowLeft.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                spMenu.setScrollPercentX(0f);
            }
        });
        bArrowRight = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getRightArrow()));
        bArrowRight.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                spMenu.setScrollPercentX(1f);
            }
        });

        Table tArrows = new Table();
        tArrows.add(bArrowLeft).size(MENU_WIDTH_ICON*0.3f, MENU_HEIGHT_ICON*0.5f);
        tArrows.add(new Actor()).width(MENU_WIDTH_BACKGROUND*0.99f);
        tArrows.add(bArrowRight).size(MENU_WIDTH_ICON*0.3f, MENU_HEIGHT_ICON*0.5f);
        //******** FIN - FLECHAS ************

        //******** INI - ICONS ************

        TextureRegionDrawable trdExit = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiExit());
        TextureRegionDrawable trdNewsUP = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiNewsUp());
        TextureRegionDrawable trdNewsDOWN = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiNewsDown());
        TextureRegionDrawable trdPlayersUP = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiPlayersUp());
        TextureRegionDrawable trdPlayersDOWN = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiPlayersDown());
        TextureRegionDrawable trdUserUP = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiUserUp());
        TextureRegionDrawable trdUserDOWN = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiUserDown());
        TextureRegionDrawable trdRankingUP = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiRankingUp());
        TextureRegionDrawable trdRankingDOWN = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiRankingDown());

        imhExit = new IconMenuHall(this,"bExit", trdExit, trdExit, serverMainScreen.getSkin());
        imhNews = new IconMenuHall(this,"bNews", trdNewsUP, trdNewsDOWN, serverMainScreen.getSkin());
        imhPlayers = new IconMenuHall(this,"bPlayers", trdPlayersUP, trdPlayersDOWN, serverMainScreen.getSkin());
        imhUser = new IconMenuHall(this,"bUser", trdUserUP, trdUserDOWN, serverMainScreen.getSkin());
        imhRanking = new IconMenuHall(this,"bRanking", trdRankingUP, trdRankingDOWN, serverMainScreen.getSkin());

        imhExit.getbIcon().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listenerExit();
            }
        });
        imhPlayers.getbIcon().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listenerPlayers();
            }
        });
        imhNews.getbIcon().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listenerNews(false);
            }
        });
        imhUser.getbIcon().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listenerUser();
            }
        });
        imhRanking.getbIcon().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listenerRanking();
            }
        });

        buttonActors[0] = tMenuPlayers; //Players
        imhActors[0] = imhPlayers;
        buttonActors[1] = tMenuNews; //News
        imhActors[1] = imhNews;
        buttonActors[2] = tMenuUser; //User
        imhActors[2] = imhUser;
        buttonActors[3] = tMenuRanking; //Ranking
        imhActors[3] = imhRanking;

        Table tIcons = new Table();
        tIcons.setVisible(true);
        tIcons.setDebug(false);
        tIcons.add(imhPlayers).size(MENU_WIDTH_ICON,MENU_HEIGHT_ICON).padRight(padRight);
        tIcons.add(imhRanking).size(MENU_WIDTH_ICON,MENU_HEIGHT_ICON).padRight(padRight/2);
        tIcons.add(imhUser).size(MENU_WIDTH_ICON,MENU_HEIGHT_ICON).padRight(padRight/2);
        tIcons.add(imhNews).size(MENU_WIDTH_ICON,MENU_HEIGHT_ICON).padRight(padRight/2);
        tIcons.add(imhExit).size(MENU_WIDTH_ICON,MENU_HEIGHT_ICON).padRight(padRight).padLeft(padRight/2);

        spMenu = new ScrollPane(tIcons, StyleConfigurator.getSPS_WithoutBackground());
        spMenu.setDebug(false);
        spMenu.scrollTo(0, 0, 0, 0);

        spPercentX_Left = 0.05f;
        spPercentX_Right = 0.95f;

        Table tScrollPane = new Table();
        tScrollPane.add(spMenu).size(MENU_WIDTH_BACKGROUND, MENU_HEIGHT_BACKGROUND);
        //******** FIN - ICONS ************

        Stack stack = new Stack();
        stack.add(tBackground);
        stack.add(tScrollPane);
        stack.add(tArrows);

        buttonArrowY = bArrowLeft.getY();

        Table tMenu = new Table();
        tMenu.setFillParent(true);
        tMenu.setVisible(true);
        tMenu.setDebug(false);
        tMenu.bottom().padBottom(PAD_BOTTOM_MENU);
        tMenu.add(stack);
        return tMenu;
    }

    private void startActionButtons(MoveToAction moveToAction, int typeActor, boolean toShow){
        Actor actorPressed = null;
        IconMenuHall buttonPressed = null;

        if(typeActor==0) {
            actorPressed = tMenuPlayers;
            buttonPressed = imhPlayers; }
        else if(typeActor==1) {
            actorPressed = tMenuNews;
            buttonPressed = imhNews; }
        else if(typeActor==2) {
            actorPressed = tMenuUser;
            buttonPressed = imhUser; }
        else if(typeActor==3) {
            actorPressed = tMenuRanking;
            buttonPressed = imhRanking; }

        if(toShow){
            for(int i=0; i<NUM_BUTTONS; i++) {
                buttonActors[i].setVisible(false);
                setDrawableButton(imhActors[i],false);
            }
            setDrawableButton(buttonPressed, true);
            moveToAction.setDuration(0f);
            actorPressed.setVisible(true);
            actorPressed.addAction(moveToAction);
            actorPressed.addAction(Actions.moveTo(0,0,0.25f));
        } else {
            setDrawableButton(buttonPressed, false);
            RunnableAction run = new RunnableAction();
            final Actor finalActorPressed = actorPressed;
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    finalActorPressed.setVisible(false);
                }
            });
            actorPressed.addAction(Actions.sequence(moveToAction, run));
        }
    }

    public void listenerExit(){
        Settings.playSoundClickButton();
        serverMainScreen.disconnectedServer(true);
    }

    public void listenerNews(boolean autoShow){
        Settings.playSoundClickButton();
        MoveToAction moveToAction = Actions.moveTo(0,Constants.VIEWPORT_HEIGHT,0.25f);
        startActionButtons(moveToAction, 1, !tMenuNews.isVisible());

        if(serverMainScreen.getAlNews().size()==0){
            if(serverMainScreen.getNodeJS()!=null)
                serverMainScreen.getNodeJS().getNewsJS().eoGetNews();
        }

        if(autoShow) spMenu.setScrollPercentX(0.65f);
        imhNews.updateInfo(false, "");
    }

    public void listenerPlayers(){
        if(!tMenuPlayers.isVisible()) imhPlayers.getTbInfo().getLabel().setColor(COLOR_DOWN_INFO);
        else imhPlayers.getTbInfo().getLabel().setColor(COLOR_UP_INFO);

        Settings.playSoundClickButton();
        MoveToAction moveToAction = Actions.moveTo(0, Constants.VIEWPORT_HEIGHT,0.25f);
        startActionButtons(moveToAction, 0, !tMenuPlayers.isVisible());
    }

    public void listenerRanking(){
        Settings.playSoundClickButton();
        MoveToAction moveToAction = Actions.moveTo(0, Constants.VIEWPORT_HEIGHT,0.25f);
        startActionButtons(moveToAction, 3, !tMenuRanking.isVisible());

        ServerObject so = serverMainScreen.getServerObject();
        if(tMenuRanking.isVisible() && so.getiRanking()==-1 && so.getiOrder()==-1){
            Server snJS = serverMainScreen.getNodeJS();
            if(snJS!=null) snJS.getRankingJS().eoRankingPlayers(so.getLengthRanking()-2, 0);
        }
    }

    public void listenerUser(){
        Settings.playSoundClickButton();
        MoveToAction moveToAction = Actions.moveTo(0, Constants.VIEWPORT_HEIGHT,0.25f);
        hallMenuUser.hideWindowSelectUserIcon();
        startActionButtons(moveToAction, 2, !tMenuUser.isVisible());
    }

    private void setDrawableButton(IconMenuHall imhButton, boolean pressed){
        if(pressed) imhButton.drawButtonDown();
        else imhButton.drawButtonUp();
    }

    /** Getters and Setters */
    public HallMenuPlayers getHallMenuPlayers(){ return hallMenuPlayers; }
    public HallMenuNews getHallMenuNews(){ return hallMenuNews; }
    public HallMenuUser getHallMenuUser(){ return hallMenuUser; }
    public HallMenuRanking getHallMenuRanking(){ return hallMenuRanking; }
    public IconMenuHall getIMHActor(int index){ return imhActors[index]; }
}

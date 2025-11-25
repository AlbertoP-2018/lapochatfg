package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Disposable;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.AbstractGameScreen;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.windows.GUIBuilder_Windows;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;

public class GUIBuilder {
    private static final String TAG = GUIBuilder.class.getName();
    private AbstractGameScreen gameScreen;
    private GameLogic gameLogic;

    private GUIBuilder_Cloth guiBuilderCloth;
    private GUIBuilder_Score guiBuilderScore;
    private GUIBuilder_Menu guiBuilderMenu;
    private GUIBuilder_Players guiBuilderPlayers;
    private GUIBuilder_Cards guiBuilderCards;
    private GUIBuilder_PlayersCardsTEST guiBuilderPlayersCardsTEST;
    private GUIBuilder_Table guiBuilderTable;
    private GUIBuilder_Deck guiBuilderDeck;
    private GUIBuilder_Buttons guiBuilderButtons;
    private GUIBuilder_SpeechBubble guiBuilderSpeechBubble;
    private GUIBuilder_Windows guiBuilderWindows;
    private GUIBuilder_Timer guiBuilderTimer;
    private GUIBuilder_Information guiBuilderGameInformation;

    private Skin skin;
    private Stack mainStack;

    public GUIBuilder(AbstractGameScreen gameScreen, GameLogic gameLogic){
        this.gameScreen = gameScreen;
        this.gameLogic = gameLogic;

        skin = new Skin(Gdx.files.internal(Constants.SKIN_UI), new TextureAtlas(Constants.TA_UI));

        this.guiBuilderCloth = new GUIBuilder_Cloth(this);
        this.guiBuilderScore = new GUIBuilder_Score(this);
        this.guiBuilderMenu = new GUIBuilder_Menu(this);
        this.guiBuilderPlayers = new GUIBuilder_Players(this);
        this.guiBuilderCards = new GUIBuilder_Cards(this);
        this.guiBuilderPlayersCardsTEST = new GUIBuilder_PlayersCardsTEST(this);
        this.guiBuilderTable = new GUIBuilder_Table(this);
        this.guiBuilderDeck = new GUIBuilder_Deck(this);
        this.guiBuilderButtons = new GUIBuilder_Buttons(this);
        this.guiBuilderSpeechBubble = new GUIBuilder_SpeechBubble(this);
        this.guiBuilderWindows = new GUIBuilder_Windows(this);
        this.guiBuilderTimer = new GUIBuilder_Timer(this);
        this.guiBuilderGameInformation = new GUIBuilder_Information(this);
    }

    public void initHand(){
        Gdx.app.log(TAG, "*Init HAND*");
        guiBuilderCards.initHand();
        getGuiBuilderPlayersCardsTEST().initHand();
        guiBuilderDeck.initHand();
        initChance();
    }

    public void initChance(){
        Gdx.app.log(TAG, "*Init CHANCE*");
        guiBuilderTable.initChance();
    }

    public void render(float delta){
        guiBuilderScore.render(delta);
        guiBuilderButtons.render();
        guiBuilderPlayers.render(delta);
        guiBuilderPlayersCardsTEST.render(delta);
        guiBuilderCards.render();
        guiBuilderDeck.render();
        guiBuilderTable.render(delta);
        guiBuilderSpeechBubble.render(delta);
        guiBuilderGameInformation.render(delta);
        guiBuilderWindows.render(delta);
        guiBuilderTimer.render(delta);
    }

    public Stack getMainStack(){
        mainStack = new Stack();
        mainStack.setDebug(false);
        mainStack.setSize(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
        //---> NO (Posibles problemas con el banner superior) mainStack.setFillParent(true);

        mainStack.add(guiBuilderCloth.createClothLayer());
        mainStack.add(guiBuilderScore.createScoreLayer());
        mainStack.add(guiBuilderDeck.createDeckLayer());
        mainStack.add(guiBuilderPlayers.createPlayersLayer());
        mainStack.add(guiBuilderCards.createCardsLayer());
        mainStack.add(guiBuilderTable.createTableLayer());
        mainStack.add(guiBuilderTimer.buildTimerLayer());
        mainStack.add(guiBuilderButtons.createBetLayer());
        mainStack.add(guiBuilderButtons.createContinueLayer());
        mainStack.add(guiBuilderSpeechBubble.createSpeechBubbleLayer());
        mainStack.add(guiBuilderPlayersCardsTEST.createCardsLayer());
        mainStack.add(guiBuilderGameInformation.createInformation());
        mainStack.add(guiBuilderWindows.createStackWindows());
        mainStack.add(guiBuilderMenu.createMenuLayer());

        return mainStack;
    }

    /*** Getters And Setters ***/
    public GUIBuilder_Timer getGuiBuilderTimer(){ return guiBuilderTimer; }
    public GUIBuilder_Players getGuiBuilderPlayers(){ return guiBuilderPlayers; }
    public GUIBuilder_Score getGuiBuilderScore(){ return guiBuilderScore; }
    public GUIBuilder_Windows getGuiBuilderWindows(){ return guiBuilderWindows; }
    public GUIBuilder_SpeechBubble getGuiBuilderSpeechBubble(){ return guiBuilderSpeechBubble; }
    public GUIBuilder_Buttons getGuiBuilderButtons(){ return guiBuilderButtons; }
    public GUIBuilder_Table getGuiBuilderTable(){ return guiBuilderTable; }
    public GUIBuilder_Cards getGuiBuilderCards(){ return guiBuilderCards; }
    public GUIBuilder_Information getGuiBuilderGameInformation(){ return guiBuilderGameInformation; }
    public GUIBuilder_PlayersCardsTEST getGuiBuilderPlayersCardsTEST(){ return guiBuilderPlayersCardsTEST; }
    public AbstractGameScreen getGameScreen(){ return gameScreen; }
    public GameLogic getGameLogic(){ return gameLogic; }
    public Skin getSkin(){ return skin; }
}

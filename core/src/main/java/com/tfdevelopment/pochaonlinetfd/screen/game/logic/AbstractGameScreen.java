package com.tfdevelopment.pochaonlinetfd.screen.game.logic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tfdevelopment.pochaonlinetfd.screen.AbstractScreen;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.GUIBuilder;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.windows.GUIWindows_Error;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.GameMode;
import com.tfdevelopment.pochaonlinetfd.utils.googleads.PochaGoogleAds;

public abstract class AbstractGameScreen extends AbstractScreen implements InterfaceGameScreen {
    private static final String TAG = AbstractGameScreen.class.getName();
    protected GUIBuilder guiBuilder;
    protected GameMode gameMode;
    private boolean gameInit; //Indica si se ha inicialiado to-do para empezar a recibir eventos de botón

    private int TOTAL_PLAYERS, TOTAL_CARDS, TOTAL_HANDS, MAX_CHANCES; //Número máximo de bazas en las últimas rondas

    //Offline --> Se pone a TRUE cuando se haya seleccionado la configuración de la partida
    //Online --> Se pone a TRUE cuando se reciban los datos correspondienes si se está uniendo a una partida empezada
    private boolean startGame; //Indica si la partida debe comenzar o esperar alguna configuración

    private int firstPlayer, indexPlayer;

    private boolean flagErrorWindow;

    AbstractGameScreen(Game game, GameMode gameMode, int totalPlayers, boolean previouslyStartedGame){
        super(game);
        this.gameMode = gameMode;

        calculateValues(totalPlayers);

        if(gameMode==GameMode.OFFLINE) this.startGame = true;
        else if(gameMode==GameMode.ONLINE) this.startGame = !previouslyStartedGame;

        this.firstPlayer = -1;
        this.indexPlayer = -1;
        this.flagErrorWindow = false;

        PochaGoogleAds.showBannerAd(true);
        PochaGoogleAds.loadInterstitialAd();
    }

    @Override
    public void show() {
        getStage().addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
    }

    @Override
    public void hide() {
        super.hide();
        StaticsMethods.showLogHide(TAG);
        PochaGoogleAds.showBannerAd(false);
    }

    public void initTimer(){
        guiBuilder.getGuiBuilderTimer().resetTimer();
    }

    protected void setConfig(int firstPlayer, int indexPlayer){
        this.firstPlayer = firstPlayer;
        this.indexPlayer = indexPlayer;
    }

    public void showErrorWindow(boolean flagErrorWindow, String error){
        if (guiBuilder != null) {
            GUIWindows_Error errorWindow = guiBuilder.getGuiBuilderWindows().getGuiW_Error();
            errorWindow.setError(error);
            this.flagErrorWindow = flagErrorWindow;
        }
    }

    private void calculateValues(int totalPlayers) {
        this.TOTAL_PLAYERS = totalPlayers;

        /**
         * NumPlayers               = X
         * MaxCards (MaxChances)    = Y = Truncate(40 / X)
         * TotalHands               = X + [(Y/X)-1)] + (X-1)
         */

        switch (TOTAL_PLAYERS) {
            case 3: // 39 Cartas, sin triunfo           // 36 Cartas, sin los doses
                this.TOTAL_HANDS = 17;                  // this.TOTAL_HANDS = 16;
                this.MAX_CHANCES = 13;                  // this.MAX_CHANCES = 12;
                break;
            case 4: // 40 Cartas
                this.TOTAL_HANDS = 16;
                this.MAX_CHANCES = 10;
                break;
            case 5: // 40 Cartas
                this.TOTAL_HANDS = 16;
                this.MAX_CHANCES = 8;
                break;
            case 6: // 36 Cartas, sin doses
                this.TOTAL_HANDS = 16;
                this.MAX_CHANCES = 6;
                break;
            case 7: // 35 Cartas, sin doses ni triunfo
                this.TOTAL_HANDS = 17;
                this.MAX_CHANCES = 5;
                break;
            case 8: // 40 Cartas
                this.TOTAL_HANDS = 19;
                this.MAX_CHANCES = 5;
                break;
        }

        this.TOTAL_CARDS = MAX_CHANCES*TOTAL_PLAYERS;
    }

    /*** Getters And Setters ***/
    public int getTOTAL_PLAYERS(){ return this.TOTAL_PLAYERS; }
    public int getTOTAL_CARDS(){ return this.TOTAL_CARDS; }
    public int getMAX_CHANCES(){ return MAX_CHANCES; }
    public int getTOTAL_HANDS(){ return this.TOTAL_HANDS; }

    public GUIBuilder getGUIBuilder() { return  this.guiBuilder; }
    public GameMode getGameMode(){ return this.gameMode; }
    public int getIndexPlayer(){ return this.indexPlayer; }
    public int getFirstPlayer(){ return this.firstPlayer; }
    public void setGameInit(boolean gameInit){ this.gameInit=gameInit; }
    public boolean isGameInit(){ return gameInit; }
    public boolean isStartGame(){ return startGame; }
    public void setStartGame(boolean startGame){ this.startGame = startGame; }
    public void setFlagErrorWindow(boolean error) { this.flagErrorWindow = error; }
    public boolean isFlagErrorWindow(){ return this.flagErrorWindow; }
}

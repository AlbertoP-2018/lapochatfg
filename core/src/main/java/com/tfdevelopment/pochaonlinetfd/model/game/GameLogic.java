package com.tfdevelopment.pochaonlinetfd.model.game;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.card.ShufflerCards;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.player.ai.AI_Main;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.model.player.PochaPlayer;
import com.tfdevelopment.pochaonlinetfd.model.thread.ThreadButtonEvent;
import com.tfdevelopment.pochaonlinetfd.model.game.phase.BetPhase;
import com.tfdevelopment.pochaonlinetfd.model.game.phase.CardPhase;
import com.tfdevelopment.pochaonlinetfd.model.game.phase.ContinuePhase;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.AbstractGameScreen;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.GUIBuilder;
import com.tfdevelopment.pochaonlinetfd.server.object.playerdata.ServerPlayer;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.GameMode;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.PhasePlayer;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Phase;
import com.tfdevelopment.pochaonlinetfd.utils.googleads.PochaGoogleAds;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;

import java.util.concurrent.Semaphore;

public class GameLogic {
    private static final String TAG = GameLogic.class.getName();
    private final int TOTAL_HANDS, TOTAL_PLAYERS;
    private AbstractGameScreen gameScreen;
    private GUIBuilder guiBuilder;
    private GameData gameData;
    private GameScore gameScore;
    private ShufflerCards shufflerCards;

    private PochaPlayer[] players;

    private BetPhase betPhase;
    private CardPhase cardPhase;
    private ContinuePhase continuePhase;

    private Semaphore semaphoreIA;
    private Thread threadIA;
    private boolean existAI; //Evita dos decisiones de IAs en paralelo

    private boolean finishedGame;

    public GameLogic(AbstractGameScreen gameScreen){
        this.gameScreen = gameScreen;
        this.TOTAL_PLAYERS = gameScreen.getTOTAL_PLAYERS();
        this.TOTAL_HANDS = gameScreen.getTOTAL_HANDS();

        this.players = new PochaPlayer[TOTAL_PLAYERS];
        if(gameScreen.getGameMode()==GameMode.OFFLINE){
            for(int i=0; i<players.length; i++) {
                if (i == 0) players[i] = new PochaPlayer("Jugador 1", false);
                //else players[i] = new PochaPlayer("CPU " + i, true);
                else players[i] = new PochaPlayer("BOT " + i, true);
            }
        }

        if(gameScreen.getGameMode()==GameMode.ONLINE){
            for(int i=0; i<players.length; i++) {
                ServerPlayer gamePlayer = ((OnlineGS) gameScreen).getServerGame().getGamePlayers().get(i);
                String name = gamePlayer.getName();
                players[i] = new PochaPlayer(name, gamePlayer.isAi());
            }
        }

        this.gameData = new GameData(this);
        this.gameScore = new GameScore(this);
        this.shufflerCards = new ShufflerCards(this, gameScreen);
        this.existAI = false;
        this.semaphoreIA = new Semaphore(1);
        this.betPhase = new BetPhase(this);
        this.cardPhase = new CardPhase(this);
        this.continuePhase = new ContinuePhase(this);

        this.finishedGame = false;
    }

    public void initHand(){
        Gdx.app.log(TAG, "*Init HAND*");
        for(int i=0; i<TOTAL_PLAYERS; i++){
            players[i].initHand();
        }

        //Antes de ShufflerCards (Para saber el número de ronda a la hora de repartir cartas)
        gameScore.initHand();

        shufflerCards.initHand();
        shufflerCards.dealCards();

        //Después de GameScore (Para saber el número de la mano)
        //Después de ShufflerCards (Para que los jugadores tengan ya las cartas
        gameData.initHand();

        guiBuilder.initHand();
        players[gameData.getCurrentPlayer()].setPhasePlayer(PhasePlayer.BET);
    }

    /***** Thread Button Event *****/
    public void initChance(){
        Gdx.app.log(TAG, "*Init CHANCE*");
        for(int i=0; i<TOTAL_PLAYERS; i++) players[i].initChance();
        gameScore.initChance();
        gameData.initChance();
        guiBuilder.initChance();
        players[gameData.getCurrentPlayer()].setPhasePlayer(PhasePlayer.CARD);
    }

    public void render(float delta){
        renderIA(delta);
        if(gameData.getPhase() == Phase.EXIT && !finishedGame) endGame();
    }

    private void renderIA(float delta){ //Hilo Principal
        int cP = gameData.getCurrentPlayer();

        if (players[cP].isAIPlayer() || Config.AUTOMATIC_RESPONSE) {
            boolean reloading = gameScreen.getGameMode() == GameMode.ONLINE && ((OnlineGS)gameScreen).isReloading();
            if (!reloading) {
                if(players[cP].getPhasePlayer().toString().equals(gameData.getPhase().toString())) {
                    if (!existAI) {
                        existAI = true;
                        ButtonEvent buttonEvent = AI_Main.getAnswer(this);
                        if (buttonEvent == null) {
                            Gdx.app.error(TAG, "La IA no puede realizar el evento. ButtonEvent null.");
                            existAI = false;
                        } else gameScreen.controlButtonEvent(buttonEvent, gameScreen.getGameMode()==GameMode.OFFLINE);
                    }
                }
            }
        }
    }

    //Controla los eventos y los ejecuta a través de un hilo
    //LLAMAR SOLO DESDE controlButtoEvent de la interface (OfflineGS y OnlineGS)
    public void controlButtonEvent(ButtonEvent buttonEvent){
        playersToWaiting();

        if(gameScreen.getGameMode()==GameMode.OFFLINE){
            threadIA = new ThreadButtonEvent(this, semaphoreIA, buttonEvent);
            threadIA.start();
        } else if(gameScreen.getGameMode()==GameMode.ONLINE){
            doButtonEvent(buttonEvent); /***** Thread NodeJS *****/
            existAI = false;
        }
    }

    /***** Thread Button Event OR NodeJS *****/
    public void doButtonEvent(ButtonEvent buttonEvent){
        int p = buttonEvent.getPlayer();
        int a = buttonEvent.getAnswer();

        if(players[buttonEvent.getPlayer()].isAIPlayer()) {
            try {
                long timeSleep = Config.TIME_IA;

                if (gameScreen.getGameMode()==GameMode.ONLINE) {
                    if (((OnlineGS)gameScreen).isReloading()) timeSleep = 0;
                }

                Thread.sleep(timeSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        switch (gameData.getPhase()){
            case BET:
                Gdx.app.log(TAG, "El jugador "+p+" apostó "+a);
                guiBuilder.getGuiBuilderSpeechBubble().setShowSpeechBubble(p,"Pido "+a);
                //betPhase.makeAnimation(p);
                betPhase.render(buttonEvent);
                break;
            case CARD:
                Card card = players[p].getCard(a);
                Gdx.app.log(TAG, "El jugador "+p+" seleccionó la carta "+a+": "+card.toString());
                gameData.addCardTable(card);
                if (gameScreen.getGameMode() == GameMode.OFFLINE || !((OnlineGS)gameScreen).isReloading()) {
                    cardPhase.makeAnimation(p);
                }
                cardPhase.render(buttonEvent);
                break;
            case CONTINUE:
                Gdx.app.log(TAG, "El jugador "+p+" pulsó Continuar");
                guiBuilder.getGuiBuilderSpeechBubble().setShowSpeechBubble(p,"Continuar");
                continuePhase.render(buttonEvent);
                break;
        }
    }

    public void manageError(String error) {
        gameScreen.showErrorWindow(true,  error);
    }

    public void endGame(){
        this.finishedGame = true;

        boolean win = gameScore.getWinnerPlayerIndex().contains(gameScreen.getIndexPlayer());

        if (win) Settings.playSoundWinGame();
        else Settings.playSoundLoseGame();

        if (gameScreen.getGameMode() == GameMode.OFFLINE) {

        }

        if (gameScreen.getGameMode() == GameMode.ONLINE) {
            ((OnlineGS)gameScreen).getServerNodeJS().getServerGameJS().eoFinishedGame(gameScore.getScoreboard());
        }

        PochaGoogleAds.showInterstitialAd();
    }

    private void playersToWaiting(){
        for(int i=0; i<TOTAL_PLAYERS; i++){
            players[i].setPhasePlayer(PochaEnum.PhasePlayer.WAITING);
        }
    }

    public void showTest(){
        Gdx.app.log(TAG, "********************************");
        Gdx.app.log(TAG, "*Num Hands: "+gameScore.getNumHands()+" ("+TOTAL_HANDS+")");
        Gdx.app.log(TAG, "*Num Max Chances: "+gameScreen.getMAX_CHANCES());
        Gdx.app.log(TAG, "*Num Chance: "+gameScore.getNumChance()+" ("+gameScore.getNumChancesHand()+")");
        Gdx.app.log(TAG, "*HandPlayer: "+gameData.getHandPlayer());
        Gdx.app.log(TAG, "*FirstPlayer: "+gameData.getFirstPlayer());
        Gdx.app.log(TAG, "*CurrentPlayer: "+gameData.getCurrentPlayer());
        Gdx.app.log(TAG, "*Trump: "+shufflerCards.getTrump().toString());
        Gdx.app.log(TAG, "*ALL Deck: "+shufflerCards.getAlAllCards().size());
        Gdx.app.log(TAG, "*Pile Deck: "+shufflerCards.getAlPile().size());
        for(int i=0; i<TOTAL_PLAYERS; i++){
            Gdx.app.log(TAG, "\t\t **** Player: "+players[i].getName()+" * Layer:"+players[i].getPhasePlayer().toString()+" ****");
            Gdx.app.log(TAG, "\t*Cards: "+players[i].getAlCards().toString()+" *");
            Gdx.app.log(TAG, "\t*Pedidas: "+players[i].getBet());
            Gdx.app.log(TAG, "\t*Ganadas: "+players[i].getWin());
        }
        Gdx.app.log(TAG, "*Table Cards: "+gameData.getAlCardsTable());
        Gdx.app.log(TAG, "*SCOREBOARD*");
//        Gdx.app.log(TAG, "  --     || *Player 0*  || *Player 1* || *Player 2* || *Player 3*");
//        for(int nR = 0; nR<gameScore.getNumHands(); nR++) {
//            int p0_0 = gameScore.getScoreboard()[nR][0][0];
//            int p0_1 = gameScore.getScoreboard()[nR][0][1];
//            int p1_0 = gameScore.getScoreboard()[nR][1][0];
//            int p1_1 = gameScore.getScoreboard()[nR][1][1];
//            int p2_0 = gameScore.getScoreboard()[nR][2][0];
//            int p2_1 = gameScore.getScoreboard()[nR][2][1];
//            int p3_0 = gameScore.getScoreboard()[nR][3][0];
//            int p3_1 = gameScore.getScoreboard()[nR][3][1];
//
//            Gdx.app.log(TAG,         "   "+(nR+1)+"     || "+p0_0+" * "+p0_1+"\t   || "+p1_0+" * "+p1_1+"\t     || "+p2_0+" * "+p2_1+"\t   || "+p3_0+" * "+p3_1+"  ");
//
//        }

        Gdx.app.log(TAG, "********************************");
    }

    /*** Getters And Setters ***/
    public ContinuePhase getContinuePhase(){ return continuePhase; }
    public BetPhase getBetPhase(){ return betPhase; }
    public CardPhase getCardPhase(){ return cardPhase; }
    public AbstractGameScreen getGameScreen(){ return gameScreen; }
    public ShufflerCards getShufflerCards(){ return shufflerCards; }
    public GameData getGameData(){ return gameData; }
    public GameScore getGameScore(){ return gameScore; }
    public PochaPlayer[] getPlayers(){ return players; }
    public Semaphore getSemaphoreIA(){ return semaphoreIA; }
    public void setBuilder(GUIBuilder guiBuilder) { this.guiBuilder = guiBuilder; }
    public void setExistAI(boolean existAI){ this.existAI = existAI; }
    public boolean isExistAI(){ return existAI; }
    public GUIBuilder getGuiBuilder(){ return this.guiBuilder; }
    public boolean isFinishedGame(){ return finishedGame; }
}

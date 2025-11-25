package com.tfdevelopment.pochaonlinetfd.model.game;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Phase;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.PhasePlayer;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Suit;

import java.util.ArrayList;

public class GameData {
    private static final String TAG = GameData.class.getName();
    private GameLogic gameLogic;

    private Phase phase;
    private int currentPlayer; //Jugador actual
    private int handPlayer; //Jugador mano (empieza a hablar en cada ronda)
    private int firstPlayer; //Primer jugador en hablar (el que gana la baza o la mano si es la primera)

    private ArrayList<Card> alCardsTable; //Cartas en la mesa (Se inserta en orden de selección)

    public GameData(GameLogic gameLogic){
        this.gameLogic = gameLogic;
        this.alCardsTable = new ArrayList<>();
        init();

        Gdx.app.log(TAG, "**Fase BET**");
    }

    public void init() {
        this.phase = Phase.BET;
        this.currentPlayer = -1;
        this.handPlayer = -1;
        this.firstPlayer = -1;
    }

    public void initHand(){
        Gdx.app.log(TAG, "*Init HAND*");
        alCardsTable.clear();
        if(gameLogic.getGameScore().getNumHands()==1) handPlayer = gameLogic.getGameScreen().getFirstPlayer();
        else handPlayer = getNextPlayer(handPlayer);

        firstPlayer = handPlayer;
        currentPlayer = handPlayer;

        //Evita que se llame antes de instanciar los objetos gráficos y así evitar que se cree dos
            //veces la misma tabla (en ronda 1)
        // if(gameLogic.getGameScreen().isGameInit()) gameLogic.getGuiBuilder().getGuiBuilderCards().updateTableCards();
        // if(gameLogic.getGameScreen().isGameInit()) gameLogic.getGuiBuilder().getGuiBuilderPlayersCardsTEST().updateTableCards();
        if(gameLogic.getGameScreen().isGameInit()) gameLogic.getGuiBuilder().getGuiBuilderCards().setFlagUpdateTableCards(true);
        if(gameLogic.getGameScreen().isGameInit()) gameLogic.getGuiBuilder().getGuiBuilderPlayersCardsTEST().setFlagUpdateTableCards(true);
    }

    public void initChance(){
        Gdx.app.log(TAG, "*Init CHANCE*");
        alCardsTable.clear();
        firstPlayer = gameLogic.getCardPhase().getPlayerWin();
        currentPlayer = firstPlayer;
    }

    public void reloadGame(Phase phase, int currentPlayer, int handPlayer, int firstPlayer) {
        this.phase = phase;
        this.currentPlayer = currentPlayer;
        this.handPlayer = handPlayer;
        this.firstPlayer = firstPlayer;
    }

    /***** Thread Button Event *****/
    public void nextPhase(){
        switch (phase){
            case BET:
                Gdx.app.log(TAG, "**Fase CARD**");
                phase = Phase.CARD;
                currentPlayer = handPlayer;
                gameLogic.getPlayers()[currentPlayer].setPhasePlayer(PhasePlayer.CARD);
                break;
            case CARD:
                if(gameLogic.getGameScore().getNumHands()!=gameLogic.getGameScreen().getTOTAL_HANDS()) {
                    Gdx.app.log(TAG, "**Fase CONTINUE**");
                    phase = Phase.CONTINUE;
                    currentPlayer = firstPlayer;
                    gameLogic.getPlayers()[currentPlayer].setPhasePlayer(PhasePlayer.CONTINUE);
                } else {
                    Gdx.app.log(TAG, "**Fase EXIT**");
                    phase = Phase.EXIT;
                }
                break;
            case CONTINUE:
                Gdx.app.log(TAG, "**Fase BET**");
                phase = Phase.BET;
                gameLogic.getPlayers()[currentPlayer].setPhasePlayer(PhasePlayer.BET);
                break;
        }
    }

    /***** Thread Button Event *****/
    public void nextCurrentPlayer(){
        switch (phase){
            case BET:
                currentPlayer = getNextPlayer(currentPlayer);
                gameLogic.getPlayers()[currentPlayer].setPhasePlayer(PhasePlayer.BET);
                break;
            case CARD:
                currentPlayer = getNextPlayer(currentPlayer);
                gameLogic.getPlayers()[currentPlayer].setPhasePlayer(PhasePlayer.CARD);
                break;
            case CONTINUE:
                currentPlayer = getNextPlayer(currentPlayer);
                gameLogic.getPlayers()[currentPlayer].setPhasePlayer(PhasePlayer.CONTINUE);
                break;
        }
    }

    //Devuelve la carta en la mesa del palo de salida con mayor valor
    //¡¡NO TOCAR O MODIFICAR MÉTODO TEST!!
    public Card getMaxCardFirst(){
        Card card = null;
        if(alCardsTable.size()>0) {
            Suit firstSuit = alCardsTable.get(0).getSuit();
            for (int i = 0; i < alCardsTable.size(); i++) {
                if (alCardsTable.get(i).getSuit() == firstSuit) {
                    if (card != null) {
                        if (alCardsTable.get(i).getCardType().ordinal() > card.getCardType().ordinal()) {
                            card = alCardsTable.get(i);
                        }
                    } else card = alCardsTable.get(i);
                }
            }
        }

        //Si devuelve null, no existe carta en mesa
        return card;
    }

    //Devuelve la carta en la mesa de la pinte con mayor valor
    //¡¡NO TOCAR O MODIFICAR MÉTODO TEST!!
    public Card getMaxCardTrump(){
        Suit trumpSuite = gameLogic.getShufflerCards().getTrump().getSuit();
        Card card = null;
        for(int i=0; i<alCardsTable.size(); i++){
            if(alCardsTable.get(i).getSuit()==trumpSuite){
                if(card!=null){
                    if(alCardsTable.get(i).getCardType().ordinal()>card.getCardType().ordinal()) {
                        card = alCardsTable.get(i);
                    }
                } else card = alCardsTable.get(i);
            }
        }

        //Si devuelve null, no existe carta en mesa
        return card;
    }

    /***** Thread Button Event *****/
    public void addCardTable(Card card){
        alCardsTable.add(card);
    }

    /***** Thread Button Event *****/
    public boolean isLastPlayer(int player){
        return player==getPreviousPlayer(firstPlayer);
    }

    public int getNextPlayer(int iPlayer){
        iPlayer+=1;
        if(iPlayer>=gameLogic.getGameScreen().getTOTAL_PLAYERS()) iPlayer=0;
        return iPlayer;
    }
    public int getPreviousPlayer(int iPlayer){
        iPlayer-=1;
        if(iPlayer<0) iPlayer=(gameLogic.getGameScreen().getTOTAL_PLAYERS()-1);
        return iPlayer;
    }

    /**
     * (Modo Online) Permite devolver la posición de un jugador según la GUI en base al valor del indexPlayer.
     *  Es decir, devuelve la posición gráfica del jugador a partir de la posición real en el array de jugadores.
     *    Ej: indexPlayer = 2 (siempre abajo)
     *        Custom          Posición natural                  Custom          Posición natural
     *          0                   2                             2                   2
     *      1       3   -->     3       1                      3     1    -->      3     1
     *          2                   0                             0                   0
     *    Sol: origin=1; indexPlayer=3; --> return: 2
     *     Siendo el indexPlayer=3 (estando abajo en la GUI) se quiere saber en qué posición quedaría
     *     el jugador origin=1. Este jugador estaría en arriba, por lo que se devolvería un 2
     *        CUSTOM          Posición natural
     *          1                   2
     *      2       0           3       1
     *          3                   0
     *
     * @param origin Posición del jugador que se quiere obtener
     * @return Posición en la GUI
     */
    public int getPlayerPositionAboutGUI(int origin){
        int diff = gameLogic.getGameScreen().getIndexPlayer();
        if (diff == 0) return origin;

        for(int i=0; i<diff; i++){
            origin-=1;
            if (origin==-1) origin = (gameLogic.getGameScreen().getTOTAL_PLAYERS()-1);
        }

        return origin;
    }

    /*** Getters And Setters ***/
    public int getFirstPlayer(){ return firstPlayer; }
    public Phase getPhase(){ return phase; }
    public int getCurrentPlayer(){ return currentPlayer; }
    public int getHandPlayer(){ return handPlayer; }
    public ArrayList<Card> getAlCardsTable(){ return alCardsTable; }
}

package com.tfdevelopment.pochaonlinetfd.model.card;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.AbstractGameScreen;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.server.object.game.ServerGameData;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.CardType;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Suit;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.GameMode;

import java.util.ArrayList;
import java.util.Random;

public class ShufflerCards {
    private static final String TAG = ShufflerCards.class.getName();
    private final int TOTAL_PLAYERS, MAX_CHANCES;
    private GameLogic gameLogic;
    private AbstractGameScreen gameScreen;
    private OnlineGS onlineGS;
    private ServerGameData dataServerGame;

    private ArrayList<Card> alAllCards;
    private ArrayList<Card> alPile; //Montón por repartir
    private Card trump; //Triunfo

    private Random random;

    public ShufflerCards(GameLogic gameLogic, AbstractGameScreen gameScreen){
        this.gameLogic = gameLogic;
        this.gameScreen = gameScreen;
        this.TOTAL_PLAYERS = gameLogic.getGameScreen().getTOTAL_PLAYERS();
        this.MAX_CHANCES = gameLogic.getGameScreen().getMAX_CHANCES();

        this.alAllCards = new ArrayList<>();
        this.alPile = new ArrayList<>();
        this.trump = null;


        if (gameScreen.getGameMode() == GameMode.ONLINE) {
            onlineGS = (OnlineGS)gameScreen;
            dataServerGame = onlineGS.getServerGame();
        } else random = new Random();

        initCards();
    }

    public void initHand(){
        Gdx.app.log(TAG, "*Init HAND*");

        for(int i=0; i<alPile.size(); i++)
            alPile.get(i).resetCard();
        alPile.clear();

        trump = null;
        alPile.addAll(alAllCards);
    }

    public void dealCards(){
        Gdx.app.log(TAG, "*Init HAND*");
        int numCards = gameLogic.getGameScore().getNumChancesHand();

        Card card = null;

        if(gameScreen.getGameMode()==GameMode.ONLINE){
            for(int i=0; i<TOTAL_PLAYERS; i++){
                for(int j=0; j<numCards; j++){
                    int iCard = dataServerGame.getGamePlayers().get(i).getCards()[j];
                    boolean selected = dataServerGame.getGamePlayers().get(i).getSelected()[j];
                    card = alPile.get(iCard);
                    card.setSelected(selected);
                    gameLogic.getPlayers()[i].addCard(card);
                }
            }

            int iTrump = dataServerGame.getTrump();
            if(gameLogic.getGameScore().isAllCardsDealt()) trump = card;
            else trump = alPile.get(iTrump);

        } else if(gameScreen.getGameMode()==GameMode.OFFLINE){
            for(int i=0; i<TOTAL_PLAYERS; i++){
                for(int j=0; j<numCards; j++){
                    int iCard = random.nextInt(alPile.size());
                    card = alPile.get(iCard);
                    card.setSelected(false);
                    gameLogic.getPlayers()[i].addCard(card);
                    alPile.remove(iCard);
                }
            }

            if (!gameLogic.getGameScore().isAllCardsDealt()) {
                int iTrump = random.nextInt(alPile.size());
                trump = alPile.get(iTrump);
                alPile.remove(iTrump);
            } else trump = card;
        }
    }

    private void initCards(){
        int totalCards = TOTAL_PLAYERS * MAX_CHANCES;
        boolean excludeTwos = totalCards == 35 || totalCards == 36;

        /** OROS **/
        alPile.add(new Card(0, CardType.AS, Suit.OROS));
        if (!excludeTwos) alPile.add(new Card(1, CardType.DOS, Suit.OROS));
        alPile.add(new Card(2, CardType.TRES, Suit.OROS));
        alPile.add(new Card(3, CardType.CUATRO, Suit.OROS));
        alPile.add(new Card(4, CardType.CINCO, Suit.OROS));
        alPile.add(new Card(5, CardType.SEIS, Suit.OROS));
        alPile.add(new Card(6, CardType.SIETE, Suit.OROS));
        alPile.add(new Card(7, CardType.J, Suit.OROS));
        alPile.add(new Card(8, CardType.Q, Suit.OROS));
        alPile.add(new Card(9, CardType.K, Suit.OROS));
        /** COPAS **/
        alPile.add(new Card(10, CardType.AS, Suit.COPAS));
        if (!excludeTwos) alPile.add(new Card(11, CardType.DOS, Suit.COPAS));
        alPile.add(new Card(12, CardType.TRES, Suit.COPAS));
        alPile.add(new Card(13, CardType.CUATRO, Suit.COPAS));
        alPile.add(new Card(14, CardType.CINCO, Suit.COPAS));
        alPile.add(new Card(15, CardType.SEIS, Suit.COPAS));
        alPile.add(new Card(16, CardType.SIETE, Suit.COPAS));
        alPile.add(new Card(17, CardType.J, Suit.COPAS));
        alPile.add(new Card(18, CardType.Q, Suit.COPAS));
        alPile.add(new Card(19, CardType.K, Suit.COPAS));
        /** ESPADAS **/
        alPile.add(new Card(20, CardType.AS, Suit.ESPADAS));
        if (!excludeTwos) alPile.add(new Card(21, CardType.DOS, Suit.ESPADAS));
        alPile.add(new Card(22, CardType.TRES, Suit.ESPADAS));
        alPile.add(new Card(23, CardType.CUATRO, Suit.ESPADAS));
        alPile.add(new Card(24, CardType.CINCO, Suit.ESPADAS));
        alPile.add(new Card(25, CardType.SEIS, Suit.ESPADAS));
        alPile.add(new Card(26, CardType.SIETE, Suit.ESPADAS));
        alPile.add(new Card(27, CardType.J, Suit.ESPADAS));
        alPile.add(new Card(28, CardType.Q, Suit.ESPADAS));
        alPile.add(new Card(29, CardType.K, Suit.ESPADAS));
        /**BASTOS **/
        alPile.add(new Card(30, CardType.AS, Suit.BASTOS));
        if (!excludeTwos) alPile.add(new Card(31, CardType.DOS, Suit.BASTOS));
        alPile.add(new Card(32, CardType.TRES, Suit.BASTOS));
        alPile.add(new Card(33, CardType.CUATRO, Suit.BASTOS));
        alPile.add(new Card(34, CardType.CINCO, Suit.BASTOS));
        alPile.add(new Card(35, CardType.SEIS, Suit.BASTOS));
        alPile.add(new Card(36, CardType.SIETE, Suit.BASTOS));
        alPile.add(new Card(37, CardType.J, Suit.BASTOS));
        alPile.add(new Card(38, CardType.Q, Suit.BASTOS));
        alPile.add(new Card(39, CardType.K, Suit.BASTOS));

        alAllCards.addAll(alPile);
    }

    /*** Getters And Setters ***/
    public Card getTrump(){ return trump; }
    public ArrayList<Card> getAlAllCards(){ return alAllCards; }
    public ArrayList<Card> getAlPile(){ return alPile; }
}

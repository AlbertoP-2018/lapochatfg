package com.tfdevelopment.pochaonlinetfd.model.game.phase;

import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.game.GameScore;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.player.PochaPlayer;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Suit;

public class CardPhase implements PhaseInterface {
    private static final String TAG = CardPhase.class.getName();

    private static final int POINTS_WIN = 10;
    private static final int POINTS_GUESS = 5;
    private static final int POINTS_FAIL = -5;

    private GameLogic gameLogic;
    private int playerWin = -1; //Indica el jugador ganador por baza

    public CardPhase(GameLogic gameLogic){
        this.gameLogic = gameLogic;
    }

    @Override
    public void render(ButtonEvent buttonEvent) { /***** Thread Button Event *****/
        GameScore gs = gameLogic.getGameScore();
        int totalPlayers = gameLogic.getGameScreen().getTOTAL_PLAYERS();
        int player = buttonEvent.getPlayer();
        PochaPlayer pp = gameLogic.getPlayers()[buttonEvent.getPlayer()];

        //Se añade la selección al jugador
        pp.setSelectedCards(buttonEvent.getAnswer());
        //Se marca la carta como seleccionada
        pp.getCard(buttonEvent.getAnswer()).setSelected(true);

        //Se oculta de la mano y se muestra en la mesa
        if(player==gameLogic.getGameScreen().getIndexPlayer()) {
            gameLogic.getGuiBuilder().getGuiBuilderCards().hideCard(buttonEvent.getAnswer());
        }
        gameLogic.getGuiBuilder().getGuiBuilderPlayersCardsTEST().selectCard(player, buttonEvent.getAnswer(), true);

        //Se pasa al siguiente
        if(gameLogic.getGameData().isLastPlayer(player)){
            //Se comprueba quien ha ganado
            PochaPlayer[] players = gameLogic.getPlayers();
            Suit trump = gameLogic.getShufflerCards().getTrump().getSuit();
            Card firstCard = players[gameLogic.getGameData().getFirstPlayer()].
                    getCard(players[gameLogic.getGameData().getFirstPlayer()].
                    getSelectedCard());

                //Comprobación por pinte
            playerWin = -1;
            Card cardWin = null;
            Card cAux = null;
            for(int i=0; i<totalPlayers; i++){
                cAux = players[i].getCard(players[i].getSelectedCard());
                if(cAux.getSuit()==trump){
                    if(cardWin!=null){
                        if(cAux.getCardType().ordinal()>cardWin.getCardType().ordinal()){
                            cardWin = cAux;
                            playerWin = i;
                        }
                    } else {
                        cardWin = cAux;
                        playerWin = i;
                    }
                }
            }
                //Comprobación por palo de salida
            if(cardWin==null){
                for(int i=0; i<totalPlayers; i++){
                    cAux = players[i].getCard(players[i].getSelectedCard());
                    if(cAux.getSuit()==firstCard.getSuit()){
                        if(cardWin!=null){
                            if(cAux.getCardType().ordinal()>cardWin.getCardType().ordinal()){
                                cardWin = cAux;
                                playerWin = i;
                            }
                        } else {
                            cardWin = cAux;
                            playerWin = i;
                        }
                    }
                }
            }

            //Se indica en la ventana quién ha ganado
            gameLogic.getGuiBuilder().getGuiBuilderGameInformation().showInformationWindow(players[playerWin].getName(), true);
            //Se actualiza la placa
            players[playerWin].setWin(players[playerWin].getWin()+1);

            //Se comprueba si es la última baza
            if(gs.getNumChance()==gs.getNumChancesHand()){
                //Se actualiza el marcador
                int numRound = gs.getNumHands();
                int pointsPlayer[] = new int[totalPlayers];
                for(int i=0; i<totalPlayers; i++){
                    int points = -9999;
                    if(players[i].getWin()==players[i].getBet()){
                        points = POINTS_WIN;
                        points+=(players[i].getWin()*POINTS_GUESS);
                    } else {
                        points = Math.abs(players[i].getWin()-players[i].getBet())*POINTS_FAIL;
                    }
                    pointsPlayer[i] = points;
                }

                gameLogic.getGameScore().updateScoreboard(numRound, pointsPlayer);
                //Se pasa a la siguente fase
                gameLogic.getGameData().nextPhase();
            } else {
                //Se pasa a la siguiente baza
                try {
                    Thread.sleep(Config.TIME_IA);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gameLogic.initChance();
            }
        } else {
            gameLogic.getGameData().nextCurrentPlayer();
        }
    }

    @Override
    public void makeAnimation(int player) { /***** Thread Button Event *****/
//        gameLogic.getGuiBuilder().getGuiBuilderTable().setAnimationPlayer(player);
        gameLogic.getGuiBuilder().getGuiBuilderTable().addAnimationPlayer(player);
    }

    /*** Getters And Setters ***/
    public int getPlayerWin(){ return playerWin; }
}

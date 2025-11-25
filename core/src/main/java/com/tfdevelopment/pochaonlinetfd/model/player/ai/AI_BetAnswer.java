package com.tfdevelopment.pochaonlinetfd.model.player.ai;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Suit;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.CardType;

import java.util.ArrayList;
import java.util.Random;

public class AI_BetAnswer {
    private static final String TAG = AI_CardAnswer.class.getName();
    private static final boolean showLogs = true;


    //private static GameLogic gameLogic;
    private static int numChancesHand, totalBets, mySuits;
    private static Suit trumpSuit;
    private static ArrayList<Card> alCard;

    private static boolean as, tres, rey, caballo, sota, siete, seis, cinco, cuatro, dos;
    private static int best; //As, Tres y Rey
    private static int high; //Caballo y sota
    private static int medium; //Siete y seis
    private static int low; //Cinco
    private static int worse; //Cuatro y dos
    private static int asNotTrump, tresNotTrump, reyNotTrump, caballoNotTrump, sotaNotTrump, seisNotTrump;
    private static int bestNotTrump; //As y Tres
    private static int highNotTrump; //Rey
    private static int mediumNotTrump; //Caballo y sota
    private static int remainingNotTrump; //Siete, seis, cinco, cuatro y dos

    public static void initData(int _numChancesHand, Suit _trumpSuit, int _totalBets,
                                 ArrayList<Card> _alCard){
        numChancesHand = _numChancesHand;
        trumpSuit = _trumpSuit;
        totalBets = _totalBets;
        alCard = _alCard;


        mySuits = 0;

        as=false; tres=false; rey=false; caballo=false; sota=false;
        siete=false; seis=false; cinco=false; cuatro=false; dos=false;
        best=0; high=0; medium=0; low=0; worse=0;

        asNotTrump=0; tresNotTrump=0; reyNotTrump=0; caballoNotTrump=0; sotaNotTrump=0;
        bestNotTrump=0; highNotTrump=0; mediumNotTrump=0; remainingNotTrump=0;

        initCardsData(alCard);
    }

    public static ButtonEvent betAnswer(GameLogic gameLogic, int currentPlayer){
        int numChancesHand = gameLogic.getGameScore().getNumChancesHand();
        Suit trumpSuit = gameLogic.getShufflerCards().getTrump().getSuit();
        boolean firstPlayer = gameLogic.getGameData().getFirstPlayer()==currentPlayer;
        boolean lastPlayer = gameLogic.getGameData().getPreviousPlayer(gameLogic.getGameData().getHandPlayer())==currentPlayer;
        int totalBets = getTotalBets(gameLogic); //Calcula el número total de apuestas
        int totalPlayers = gameLogic.getGameScreen().getTOTAL_PLAYERS();
        initData(numChancesHand, trumpSuit, totalBets,
                gameLogic.getPlayers()[currentPlayer].getAlCards());

        int bet = -1;
        boolean heuristicMethod = (totalPlayers == 3 && numChancesHand >= 4) || (totalPlayers == 4 && numChancesHand >= 5);

        showLogs("********** INICIO decisión IA: "+currentPlayer+" ********** Heuristic Method: "+heuristicMethod);
        showCards(alCard);
        showData();

        if (heuristicMethod) bet = heuristicMethod(lastPlayer, totalPlayers);
        else bet = basicMethod(gameLogic.getGameScreen().getMAX_CHANCES(), firstPlayer, lastPlayer);

        showLogs( "HE APOSTADO: "+bet);
        showLogs( "********** FIN decisión IA: "+currentPlayer+" **********");

        return new ButtonEvent(PochaEnum.DecisionB.BET, currentPlayer, bet);
    }

    private static void initCardsData(ArrayList<Card> alCard){
        mySuits = 0;
        for(Card card : alCard){
            if(card.getSuit()==trumpSuit){
                mySuits+=1;
                if(card.getCardType()== PochaEnum.CardType.AS){ as = true; best+=1; }
                if(card.getCardType()== PochaEnum.CardType.TRES){ tres = true; best+=1; }
                if(card.getCardType()== PochaEnum.CardType.K){ rey = true; best+=1; }
                if(card.getCardType()== PochaEnum.CardType.Q){ caballo = true; high+=1; }
                if(card.getCardType()== PochaEnum.CardType.J){ sota = true; high+=1; }
                if(card.getCardType()== PochaEnum.CardType.SIETE){ siete = true; medium+=1; }
                if(card.getCardType()== PochaEnum.CardType.SEIS){ seis = true; medium+=1; }
                if(card.getCardType()== PochaEnum.CardType.CINCO){ cinco = true; low+=1; }
                if(card.getCardType()== PochaEnum.CardType.CUATRO){ cuatro = true; worse+=1; }
                if(card.getCardType()== PochaEnum.CardType.DOS){ dos = true; worse+=1; }
            } else {
                if(card.getCardType()== PochaEnum.CardType.AS){ asNotTrump += 1; bestNotTrump+=1; }
                if(card.getCardType()== PochaEnum.CardType.TRES){ tresNotTrump += 1; bestNotTrump+=1; }
                if(card.getCardType()== PochaEnum.CardType.K){ reyNotTrump += 1; highNotTrump+=1; }
                if(card.getCardType()== PochaEnum.CardType.Q){ caballoNotTrump += 1; mediumNotTrump+=1; }
                if(card.getCardType()== PochaEnum.CardType.J){ sotaNotTrump += 1; mediumNotTrump+=1; }
                if(card.getCardType()== PochaEnum.CardType.SIETE){ remainingNotTrump+=1; }
                if(card.getCardType()== PochaEnum.CardType.SEIS){ remainingNotTrump+=1; }
                if(card.getCardType()== PochaEnum.CardType.CINCO){ remainingNotTrump+=1; }
                if(card.getCardType()== PochaEnum.CardType.CUATRO){ remainingNotTrump+=1; }
                if(card.getCardType()== PochaEnum.CardType.DOS){ remainingNotTrump+=1; }
            }
        }
    }

    private static int basicMethod(int maxChances, boolean firstPlayer, boolean lastPlayer) {
        int bet = -1;

        if(numChancesHand==1) bet = getBetChance1(firstPlayer, lastPlayer); //Una carta en mano
        else if(numChancesHand==2) bet = getBetChance2(firstPlayer, lastPlayer);
        else if(numChancesHand==3) bet = getBetChance3(firstPlayer, lastPlayer);
        else if(numChancesHand==4) bet = getBetChance4(firstPlayer, lastPlayer);
        else if(numChancesHand<maxChances) bet = getBetOtherChances(firstPlayer, lastPlayer);
        else if(numChancesHand==maxChances) bet = getBetLastChance(firstPlayer, lastPlayer);

        return bet;
    }

    private static int heuristicMethod(boolean lastPlayer, int totalPlayers) {
        int totalStrength = 0;
        for (Card card : alCard) {
            totalStrength += calculateStrength(card, trumpSuit, totalPlayers);
        }

        int bet = Math.max(Math.round(totalStrength / 10f), 0);

        if (lastPlayer) {
            if(numChancesHand == (totalBets+bet)) {
                if (bet == 1) {
                    if (as || tres || rey ) bet = 2;
                    else bet = 0;
                } else if (bet > 1) {
                    if (as || tres || rey || caballo || sota) bet = bet + 1;
                    else bet = bet - 1;
                } else bet = 1;
            }
        }

        return bet;
    }

    private static int calculateStrength(Card card, Suit trumpSuit, int players) {
        CardType cardType = card.getCardType();
        Suit suit = card.getSuit();

        int strength = 0;

        if (players == 3) {
            if (cardType == CardType.AS) strength = 8;
            if (cardType == CardType.TRES) strength = 7;
            if (cardType == CardType.K) strength = 5;
            if (cardType == CardType.Q) strength = 4;
            if (cardType == CardType.J) strength = 3;

            if (suit == trumpSuit) strength += 5;
        }

        if (players == 4) {
            if (cardType == CardType.AS) strength = 8;
            if (cardType == CardType.TRES) strength = 7;
            if (cardType == CardType.K) strength = 4;
            if (cardType == CardType.Q) strength = 3;
            if (cardType == CardType.J) strength = 2;

            if (suit == trumpSuit) strength += 2;
        }

        return strength;
    }

    private static int getBetChance1(boolean firstPlayer, boolean lastPlayer){
        showLogs("*** Tengo UNA carta en mano.... ***");

        //Número de fichas a pedir
        int myBets = 0;
        if(firstPlayer){
            showLogs("** Soy el primer jugador en pedir");
            if(mySuits==1){
                myBets = 1; showLogs("* Tengo una pinte. Apuesto: 1");
            } else if(mySuits==0 && asNotTrump>=1) {
                myBets = ((new Random().nextInt(4)==1) ? 1 : 0);
                showLogs("* No tengo pinte, pero tengo un AS. 25% de probabilidad de pedir una.");
            } else if(mySuits==0 && asNotTrump==0){
                myBets = 0;
                showLogs("* No tengo pinte ni carta alta. Apuesto: 0");
            } else {
                myBets = 0;
                showLogs("* Condición 'else'. Apuesto: 0");
            }
        } else {
            showLogs("** NO soy el primer jugador en pedir. Apuestas en mesa: "+totalBets);
            if(totalBets!=0){
                if(mySuits!=0){
                    if(dos || cuatro){ myBets = 0; showLogs("* Tengo el 2 o el 4 de la pinte. Pido 0");}
                    else { myBets = mySuits; showLogs("* Tengo pinte mayor al 4. Pido 1 ");}
                } else {
                    myBets = 0;
                    showLogs("* Hay apuestas en mesa. No tengo pintes. Pido: 0.");
                }
            } else {
                myBets = mySuits;
                showLogs("* No hay apuestas en mesa. Apuesto el número de pintes que tengo.");
            }
        }

        //Apuesta final
        int bet = -1;
        if (lastPlayer) {
            showLogs("*** Soy el último jugador... (TotalBets: " + totalBets + ") ***");
            if (totalBets == 1) bet = 1;
            else if (totalBets == 0) bet = 0;
            else bet = myBets;
        } else bet = myBets;

        showLogs("---> Triunfos: "+mySuits+" - MisApuestas: "+myBets+ " - Apuesta Final: "+bet +" <----");

        //Gestionar cartas ganadoras
        if(bet==1) setWinningCard(0);

        return bet;
    }

    private static int getBetChance2(boolean firstPlayer, boolean lastPlayer){
        showLogs("*** Tengo DOS cartas en mano.... ***");

        //Número de fichas a pedir
        int myBets = 0;
        if(firstPlayer){
            if(mySuits>0){
                showLogs("** Soy el primer jugador en pedir. Tengo pintes");
                if(mySuits==1){
                    if( (as||tres||rey||caballo) && (bestNotTrump+highNotTrump+mediumNotTrump==1) ) {
                        myBets = 2;
                        showLogs("* Tengo una pinte (AS-Caballo) y una carta alta (AS-J). Apuesto: 2");
                        setWinningCard(0); setWinningCard(1);
                    } else if( (sota && bestNotTrump+highNotTrump==1)) {
                        myBets = 2;
                        showLogs("* Tengo una pinte (J) y al una carta alta (AS-3). Apuesto: 2");
                        setWinningCard(0);
                        setWinningCard(1);
                    } else if( (cuatro||dos) && (asNotTrump==0 && tresNotTrump==0)) {
                        myBets = 0;
                        showLogs("* Tengo un 4,2 y la de no pinte NO es AS ni 3. Apuesto: 0");
                    } else {
                        myBets = 1;
                        showLogs("* Tengo una pinte. Apuesto: 1");
                        setWinningCardToSuit();
                    }
                } else if(mySuits==2){
                    //if( ((siete||seis||cinco) && (cuatro||dos)) || (seis||cinco)){
                    if( (medium+low>=1 && worse>=1) || (seis && (cinco||cuatro||dos) || (low+worse>=2)) ) {
                        myBets=1;
                        showLogs("* Tengo dos pintes, pero 7/6/5 y 4/2 ó ambas menor que 6. Apuesto: 1");
                        setWinningCardBetter(1);
                    } else {
                        myBets = 2;
                        showLogs("* Tengo dos pintes. Apuesto: 2");
                        setWinningCard(0); setWinningCard(1);
                    }
                }
            } else {
                showLogs("** Soy el primer jugador en pedir. NO tengo pintes");
                if(bestNotTrump==2) {
                    myBets = 1;
                    showLogs("* No tengo pintes. Pero tengo dos cartas altas (As/Tres). Apuesto: 1");
                    setHalfWinningCard(0); setHalfWinningCard(1);
                } else { myBets = 0; showLogs("* No tengo pintes. Apuesto 0");}
            }
        } else {
            if(mySuits>0){
                showLogs("** NO soy el primer jugador en pedir, pero tengo pintes");
                if(totalBets!=0){
                    showLogs("* Existen apuestas en mesa *");
                    if(mySuits==2){
                        showLogs("* Tengo dos pintes. Existen apuestas en mesa y no lo estoy gestionando... (TODO)");
                        if(best==2){
                            myBets=2;
                            showLogs("*Tengo dos pintes. Están entre AS-K. Apuesto: 2");
                            setWinningCard(0); setWinningCard(1);
                        } else if(best>=1 && (high>=1 || medium>=1 || low>=1 || worse>=1)){
                            myBets=2;
                            showLogs("*Tengo dos pintes. Una entre AS-K y la otra más baja. (TODO-¿Apuestas en mesa?). Apuesto: 2");
                            setWinningCard(0); setWinningCard(1);
                        } else if(high>=1 && medium>=1){
                            myBets=2;
                            showLogs("*Tengo dos pintes. Una entre Q-S y otra entre 7-6. (TODO-¿Apuestas en mesa?). Apuesto: 2");
                            setWinningCard(0); setWinningCard(1);
                        } else {
                            myBets=1;
                            showLogs("*Tengo dos pintes, pero no son lo suficientemente buenas. (TODO-¿Apuestas en mesa?). Apuesto: 1");
                            setHalfWinningCard(0); setHalfWinningCard(1);
                        }
                    } else if(mySuits==1){
                        showLogs("* Tengo una pinte.");
                        if(totalBets>1){
                            if(best>=1 || high>=1){
                                myBets=1;
                                showLogs(" * Existe más de una apuesta. Tengo  entre AS-J. Apuesto: 1");
                                setWinningCardToSuit();
                            } else {
                                myBets=0;
                                showLogs(" * Existe más de una apuesta. Tengo una entre 7-2. Apuesto: 0");
                            }
                        } else if(totalBets==1){
                            if(best>=1 || high>=1 || medium>=1 || low>=1){
                                myBets=1;
                                showLogs("* Existe una apuesta. Tengo una que mayor que 4. Apuesto: 1");
                                setWinningCardToSuit();
                            } else {
                                myBets=0;
                                showLogs("* Existe una apuesta. Tengo un 4/2. Apuesto: 0");
                            }
                        } else {
                            myBets=1;
                            showLogs("* (NO DEBERÍA LLEGAR AQUÍ) No existen apuestas. Apuesto mis pintes: "+mySuits);
                            setWinningCardToSuit();
                        }
                    }
                } else { //Jugador NO primero y SIN apuestas
                    if(mySuits==2){
                        showLogs("* Tengo 2 pintes y no hay apuestas ");
                        if(medium<2 && (medium+low+worse>=2)){
                            myBets = 1;
                            showLogs("* Tengo un 7 y la otra es 5,4,2. Apuesto: 1");
                            setWinningCardBetter(1);
                        } else {
                            myBets = mySuits;
                            showLogs("* Tengo dos pintes. Apuesto: 2");
                            setWinningCardToSuit();
                        }
                    } else if(mySuits==1){
                        showLogs("* Tengo 1 pinte y no hay apuestas ");
                        if ( (as||tres||rey) && (bestNotTrump>0 || highNotTrump>0)) {
                            myBets = 2;
                            showLogs("* Tengo un AS,3,K y la otra es carta alta (As,Tres,K). Apuesto: 2");
                            setWinningCard(0); setWinningCard(1);
                        } else if (caballo && bestNotTrump>=1) {
                            myBets = 2;
                            showLogs("* Tengo Q y la otra es carta alta (As,Tres). Apuesto: 2");
                            setWinningCard(0); setWinningCard(1);
                        } else if( (cuatro&&bestNotTrump>0) || (dos&&bestNotTrump>0) ) {
                            myBets = 1;
                            showLogs("* Tengo 4,2 y carta alta (As,Tres). Apuesto: 1");
                            setWinningCardToSuit();
                        } else if( (cuatro&&bestNotTrump==0) || (dos&&bestNotTrump==0) ) {
                            if(lastPlayer){
                                myBets = 1;
                                showLogs("* Tengo 4,2 y NO As,Tres pero soy el último jugador. Apuesto: 1");
                                setWinningCardBetter(1);
                            } else {
                                myBets = 0;
                                showLogs("* Tengo 4,2 y NO As,Tres. Apuesto: 0");
                            }
                        } else {
                            myBets = mySuits;
                            showLogs("* Tengo una pinte. Apuesto: 1");
                            setWinningCardToSuit();
                        }
                    }
                }
            } else {
                showLogs("** NO soy el primer jugador en pedir y NO tengo pintes");
                if(totalBets!=0){
                    myBets=0;
                    showLogs("* Ya existen apuestas en mesa. Apuesto: 0");
                } else {
                    if(bestNotTrump==2){
                        myBets=1;
                        showLogs("* No existen apuestas y tengo 2 cartas altas (A-3). Apuesto: 1");
                        setHalfWinningCard(0); setHalfWinningCard(1);
                    } else {
                        myBets = 0;
                        showLogs("* No existen apuestas en mesa. No tengo cartas alta. Apuesto: 0");
                    }
                }
            }
        }

        //Apuesta final
        int bet = -1;
        if (lastPlayer) {
            showLogs("*** Soy el último jugador... (TotalBets: " + totalBets + ") ***");
            if(totalBets==0){ //No puedo apostar dos
                if(myBets==2) bet = 1;
                else bet = myBets;
            } else if(totalBets == 1){
                if(myBets==1){ //No puedo apostar uno
                    if(as||tres||rey||caballo||sota) bet = 2;
                    else bet=0;
                } else bet = myBets;
            } else if (totalBets == 2) { //No puedo apostar cero
                if(myBets!=0) bet = myBets;
                else if(myBets==0) bet = 1; //No puedo apostar cero
            } else if(totalBets>2) bet = myBets; //Puedo apostar cualquiera
        } else bet = myBets;

        showLogs("---> Triunfos: "+mySuits+" - MisApuestas: "+myBets+ " - Apuesta Final: "+bet +" <----");

        return bet;
    }

    private static int getBetChance3(boolean firstPlayer, boolean lastPlayer){
        showLogs("*** Tengo TRES cartas en mano.... ***");

        //Número de fichas a pedir
        int myBets = 0;
        if(firstPlayer){
            showLogs("** Soy el primer jugador en hablar");
            if(mySuits>0){
                if(mySuits==3){
                    showLogs("*Tengo 3 triunfos...");
                    if(best>0){
                        if(best>=2){
                            myBets=3;
                            showLogs("* Tengo 2 o 3 triunos entre AS-K y uno más bajo. Apuesto: 3");
                            setWinningCardToSuit();
                        } else if(best==1){
                            if(high>=1){
                                myBets=3;
                                showLogs("* Tengo 1 triunos entre AS-K y al menos 1 entre Q-J. Apuesto: 3");
                                setWinningCardToSuit();
                            } else if(medium>=1){
                                myBets=3;
                                showLogs("* Tengo 1 triunos entre AS-K y al menos 1 entre 7-6. Apuesto: 3");
                                setWinningCardToSuit();
                            } else {
                                myBets=2;
                                showLogs("* Tengo 1 triunos entre AS-K y el resto entre 5-2. Apuesto: 2");
                                setWinningCardBetter(2);
                            }
                        }
                    } else if(high>0) {
                        if(high==2){
                            myBets=3;
                            showLogs("* Tengo Q y J y uno más bajo. Apuesto: 3");
                            setWinningCardToSuit();
                        } else {
                            myBets=2;
                            showLogs("* Tengo Q o J y el resto entre más abajo. Apuesto: 2");
                            setWinningCardBetter(2);
                        }
                    } else {
                        myBets=2;
                        showLogs("* Tengo 3 triunos entre 7-2. Apuesto: 2");
                        setWinningCardBetter(2);
                    }
                } else if(mySuits==2){
                    showLogs("*Tengo 2 triunfos...");
                    if(best>0){
                        if(best==2){
                            myBets=2;
                            showLogs("* Tengo 2 triunos entre A-K. Apuesto: 2");
                            setWinningCardToSuit();
                        } else if(best==1){
                            if(high>=1 || medium>=1){
                                myBets=2;
                                showLogs("* Tengo 1 triunfo entre A-K y otro entre Q-6. Apuesto: 2");
                                setWinningCardToSuit();
                            } else {
                                myBets=1;
                                showLogs("* Tengo 1 triunfo entre A-K y otro entre 5-2. Apuesto: 1");
                                setWinningCardBetter(1);
                            }
                        }
                    } else {
                        if(high>0){
                            if(high==2){
                                myBets=2;
                                showLogs("* Tengo 2 triunfos entre Q-J. Apuesto: 2");
                                setWinningCardToSuit();
                            } else if(high==1){
                                if(medium>=1){
                                    myBets=2;
                                    showLogs("* Tengo un triunfo entre Q-J y otro entre 7-6. Apuesto: 2");
                                    setWinningCardToSuit();
                                } else {
                                    myBets=1;
                                    showLogs("* Tengo un triunfo entre Q-J y otro menor que 6. Apuesto: 1");
                                    setWinningCardBetter(1);
                                }
                            }
                        } else {
                            myBets=1;
                            showLogs("* Tengo dos triunfos entre 7-2. Apuesto: 1");
                            setWinningCardBetter(1);
                        }
                    }
                } else if(mySuits==1){
                    myBets=1;
                    showLogs("*Tengo 1 triunfo... Apuesto: 1");
                    setWinningCardToSuit();
                }
            } else {
                myBets = 0;
                showLogs("* No tengo triunfos. Apuesto: 0");
            }
        } else {
            showLogs("** NO soy el primer jugador en hablar.");
            if(mySuits>0){
                if(totalBets!=0){
                    if(best>0) myBets+=best;
                    if(high>0) myBets+=high;
                    if(medium==2 && myBets>0) myBets+=medium;
                    if(medium==1 && myBets>0) myBets+=1;
                    if(cinco && myBets>0) myBets+=1;
                    if(worse>0 && myBets>1) myBets+=1;
                } else myBets = mySuits;
            } else myBets = 0;
        }

        //Apuesta final
        showLogs("--> MySuits: "+mySuits+" - MyBets: "+myBets);
        int bet = -1;
        if (lastPlayer) {
            showLogs("*** Soy el último jugador... (TotalBets: " + totalBets + ") ***");
            if(totalBets==0){
                if(myBets==3) bet=2; //No puedo apostar tres
                else bet=myBets;
            } else if(totalBets == 1){
                if(myBets==2){ //No puedo apostar dos
                    if(best>=2) bet=3;
                    else bet=1;
                } else bet=myBets;
            } else if (totalBets == 2) { //No puedo apostar uno
                if(myBets==1){
                    if(as||tres||rey) bet=2;
                    else bet=0;
                } else bet=myBets;
            } else if(totalBets==3) {
                if(myBets==0) bet=1; //No puedo apostar cero
                else bet = myBets;
            } else if(totalBets>3) bet = myBets; //Puedo apostar cualquiera
        } else bet = myBets;

        return bet;
    }

    private static int getBetChance4(boolean firstPlayer, boolean lastPlayer){
        showLogs("*** Tengo CUATRO cartas en mano.... ***");

        //Número de fichas a pedir
        int myBets = 0;
        if(firstPlayer){
            if(mySuits>0){
                myBets = mySuits;
            } else myBets = 0;
        } else {
            if(mySuits>0){
                if(totalBets!=0){
                    if(best>0) myBets+=best;
                    if(high>0) myBets+=high;
                    if(medium==2 && myBets>0) myBets+=medium;
                    if(medium==1 && myBets>0) myBets+=1;
                    if(cinco && myBets>0) myBets+=1;
                    if(worse>0 && myBets>1) myBets+=1;
                } else myBets = mySuits;
            } else myBets = 0;
        }

        //Apuesta final
        showLogs("--> MySuits: "+mySuits+" - MyBets: "+myBets);
        int bet = -1;
        if (lastPlayer) {
            showLogs("*** Soy el último jugador... (TotalBets: " + totalBets + ") ***");
            //No puedo apostar 0
            if(numChancesHand==totalBets){
                if(myBets==0) bet = 1;
                if(myBets>0) bet = myBets;
            }
            //No puedo apostar 1
            else if(numChancesHand==totalBets+1) {
                if (myBets == 1) {
                    if(as||tres||rey|caballo||sota||siete) bet=2;
                    else bet=0;
                } else bet = myBets;
            }
            //No puedo apostar las que yo quería (Serán mínimo 2)
            else if(numChancesHand==(totalBets+myBets)){
                bet = myBets-1;
            } else bet = myBets; //Apuesto las que yo quería
        } else bet = myBets;

        return bet;
    }

    private static int getBetOtherChances(boolean firstPlayer, boolean lastPlayer){
        //Número de fichas a pedir
        int myBets = mySuits;
        myBets = 0;

        if(as) myBets+=1;
        if(tres) myBets+=1;
        if(rey) myBets+=1;

        if(caballo && (as && (tres||rey))) myBets+=1;
        if(sota && (as && (tres||rey))) myBets+=1;
        if(siete && (as && (tres||rey))) myBets+=1;

        if(caballo && (sota||siete||seis||cinco||cuatro||dos)) myBets+=1;
        if(sota && (siete||seis||cinco||cuatro||dos)) myBets+=1;
        if(siete && (seis||cinco||cuatro||dos)) myBets+=1;
        if(seis && (cinco||cuatro||dos)) myBets+=1;

        if(siete && seis && cinco) myBets+=1;
        if(seis && cinco && cuatro) myBets+=1;
        if(cinco && cuatro && dos) myBets+=1;

        if(mySuits!=0 && bestNotTrump>=2) myBets+=1;

        //Apuesta final
        showLogs("*** Tengo más de una carta en mano, pero no todas... *** MySuits: "+mySuits+" - MyBets: "+myBets);
        int bet = -1;
        if(lastPlayer){
            showLogs("*** Soy el último jugador... (TotalBets: "+totalBets+" - Mybets: "+myBets+") ***");
            //No puedo apostar 0
            if(numChancesHand==totalBets){
                if(myBets==0) bet = 1;
                if(myBets>0) bet = myBets;
            }
            //No puedo apostar 1
            else if(numChancesHand==totalBets+1) {
                if (myBets == 1) {
                    if(as||tres||rey|caballo||sota||siete) bet=2;
                    else bet=0;
                } else bet = myBets;
            }
            //No puedo apostar las que yo quería (Serán mínimo 2)
            else if(numChancesHand==(totalBets+myBets)){
                bet = myBets-1;
            } else bet = myBets; //Apuesto las que yo quería

        } else bet = myBets;

        return bet;
    }

    private static int getBetLastChance(boolean firstPlayer, boolean lastPlayer){
        //Número de fichas a pedir  mySuits
        int myBets = 0;

        if(as) myBets+=1;
        if(tres) myBets+=1;
        if(rey) myBets+=1;

        if(caballo && (as && (tres||rey))) myBets+=1;
        if(sota && (as && (tres||rey))) myBets+=1;
        if(siete && (as && (tres||rey))) myBets+=1;

        if(caballo && (sota||siete||seis||cinco||cuatro||dos)) myBets+=1;
        if(sota && (siete||seis||cinco||cuatro||dos)) myBets+=1;
        if(siete && (seis||cinco||cuatro||dos)) myBets+=1;
        if(seis && (cinco||cuatro||dos)) myBets+=1;

        if(siete && seis && cinco) myBets+=1;
        if(seis && cinco && cuatro) myBets+=1;
        if(cinco && cuatro && dos) myBets+=1;

        if(mySuits!=0 && bestNotTrump>=2) myBets+=1;

        //Apuesta final
        showLogs("*** Tengo TODAS las cartas en mano... *** MySuits: "+mySuits+" - MyBets: "+myBets);
        int bet = -1;
        if(lastPlayer){
            showLogs("*** Soy el último jugador... (TotalBets: "+totalBets+" - MyBets: "+myBets+") ***");
            if(numChancesHand==(totalBets+myBets)){ //No puedo apostar las que yo quería
                showLogs("*** No puedo apostar las que yo quería... ***");
                if(myBets==1){
                    if(as || tres) bet=2;
                    else bet=0;
                } else if(myBets>1) bet=myBets-1;
                else if(myBets==0) bet=1;
            } else bet = myBets;
        } else bet = myBets;

        return bet;
    }

    public static int getTotalBets(GameLogic gameLogic){ //Calcula el número total de apuestas
        int totalBets = 0;
        for(int i = 0; i<gameLogic.getGameScreen().getTOTAL_PLAYERS(); i++){
            int auxBet = gameLogic.getPlayers()[i].getBet();
            if(auxBet>=0) totalBets+=auxBet;
        }
        return totalBets;
    }

    private static void setWinningCard(int indexCard){
        showLogs("**Card "+indexCard+" now is winningCard**");
        alCard.get(indexCard).setWinning(true);
    }

    private static void setHalfWinningCard(int indexCard){
        showLogs("**Card "+indexCard+" now is halfWinningCard**");
        alCard.get(indexCard).setHalfWinning(true);
    }

    //Establece como cartas ganadoras todas las de la pinte
    private static void setWinningCardToSuit(){
        for(int i=0; i<alCard.size(); i++){
            Card card = alCard.get(i);
            if(trumpSuit==card.getSuit()){
                showLogs("**Card "+i+" now is winningCard**");
                card.setWinning(true);
            }
        }
    }

    //Establece como cartas ganadoras las X cartas más altas de la pinte
    private static void setWinningCardBetter(int numCards){
        int counter = 0;

        breakForCardType:
        for(int i=CardType.values().length-1; i>=0; i--){
            if(counter==numCards) break;
            for(int j=0; j<alCard.size(); j++){
                Card card = alCard.get(j);
                if(trumpSuit==card.getSuit()){
                    if(card.getCardType().ordinal()==i){
                        counter+=1;
                        card.setWinning(true);
                        break breakForCardType;
                    }
                }
            }
        }

        showLogs("** PRUEBA: setWinningCardBetter() - NumCards: "+numCards);
        for(Card card : alCard){
            showLogs("*Carta: "+card.toString()+" es ganadora: "+card.isWinning());
        }
    }


    public static void showLogs(String text){
        if(showLogs) Gdx.app.log(TAG, text);
    }

    public static void showCards(ArrayList<Card> alCard){
        showLogs(alCard.toString());
    }

    public static void showData(){
        showLogs("Best: "+best+" || High: "+high+" || Medium: "+medium+" || Low: "+low+" || Worse: "+worse);
        showLogs("*NotTrump*");
        showLogs("AS("+asNotTrump+") - TRES("+tresNotTrump+") + REY("+reyNotTrump+") + CABALLO("+caballoNotTrump+") + SOTA("+sotaNotTrump+")");
        showLogs("Best("+bestNotTrump+") - High("+highNotTrump+") + Medium("+mediumNotTrump+") + Remaining("+remainingNotTrump+")");
        showLogs("CURRENT TOTAL BETS: "+totalBets);
    }

    public static int doTEST(int methodCode, Object... parameters){
        boolean firstPlayer = (boolean)parameters[0];
        boolean secondPlayer = (boolean)parameters[1];

        if(methodCode==1) return getBetChance1(firstPlayer, secondPlayer);
        if(methodCode==2) return getBetChance2(firstPlayer, secondPlayer);
        if(methodCode==3) return getBetChance3(firstPlayer, secondPlayer);
        if(methodCode==4) return getBetChance4(firstPlayer, secondPlayer);
        if(methodCode==5) return getBetOtherChances(firstPlayer, secondPlayer);
        if(methodCode==6) return getBetLastChance(firstPlayer, secondPlayer);

        return -1;
    }
}

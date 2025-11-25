package com.tfdevelopment.pochaonlinetfd.model.player.ai;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.player.PochaPlayer;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.DecisionB;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Suit;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.CardType;

import java.util.ArrayList;

public class AI_CardAnswer {
    private static final String TAG = AI_CardAnswer.class.getName();
    private static final boolean showLogs = true;

    private static int bet, win;
    private static Suit trumpSuit;
    private static ArrayList<Integer> alIndex;
    private static ArrayList<Card> alALLCards;
    private static ArrayList<Card> alTableCards;
    private static Card maxCardFirst, maxTrumpCard;
    private static boolean lastPlayer;

    public static void initData(ArrayList<Card> _alALLCards, int _bet, int _win, boolean _lastPlayer,
                                Suit _trumpSuit, ArrayList<Card> _alTableCards){
        bet = _bet;
        win = _win;
        trumpSuit = _trumpSuit;
        lastPlayer = _lastPlayer;


        //Alamcena la posición y dicha carta de las cartas sin seleccionar
        alIndex = new ArrayList<>(); //Posición de la carta (sin seleccionar) en el array de cartas del jugador
        alALLCards = _alALLCards;
        for(int i=0; i<alALLCards.size(); i++){
            if(!alALLCards.get(i).isSelected()){
                alIndex.add(i);
            }
        }

        alTableCards = _alTableCards;
        maxCardFirst = getMaxCardFirst(alTableCards);
        maxTrumpCard = getMaxCardTrump(trumpSuit, alTableCards);
    }

    public static ButtonEvent cardAnswer(GameLogic gameLogic, int currentPlayer) {
        PochaPlayer _player = gameLogic.getPlayers()[currentPlayer];
        ArrayList<Card> _alCards = _player.getAlCards();
        int _bet = _player.getBet();
        int _win = _player.getWin();
        boolean _lastPlayer = gameLogic.getGameData().getPreviousPlayer(gameLogic.getGameData().getHandPlayer())==currentPlayer;
        Suit _trumpSuit = gameLogic.getShufflerCards().getTrump().getSuit();
        ArrayList<Card> _alTableCards = gameLogic.getGameData().getAlCardsTable();
        initData(_alCards, _bet, _win, _lastPlayer, _trumpSuit, _alTableCards);

        showLogs("********** INICIO decisión IA: "+currentPlayer+" **********");

        int selected = getCardSelected();
        showLogs( "HE SELECCIONADO LA CARTA: "+selected);
        showLogs( "********** FIN decisión IA: "+currentPlayer+" **********");

        return new ButtonEvent(DecisionB.CARD, currentPlayer, selected);
    }

    private static int getCardSelected() {
        showPlayerCards(alALLCards);
        showTableCards(alTableCards);

        if (alIndex.size()>1) {
            int remainingBets = bet-win; //PUEDE SER NEGATIVO

            ArrayList<Integer> alTrumpSuiteCards = new ArrayList<>(); //Posición de las cartas del palo la pinte
            ArrayList<Integer> alRemainingCards = new ArrayList<>(); //Posición de las cartas restantes
            //Gestión de las cartas en mano
            for(int i=0; i<alIndex.size(); i++){
                Card auxCard = alALLCards.get(alIndex.get(i));
                //Cartas correspondientes al palo de la pinte
                if(auxCard.getSuit()==trumpSuit) alTrumpSuiteCards.add(alIndex.get(i));
                //Cartas restantes
                if(auxCard.getSuit()!=trumpSuit) alRemainingCards.add(alIndex.get(i));
            }

            showLogs("Ganadas/Pedidas --> "+win+"/"+bet);
            showLogs("TrumpSuite --> "+trumpSuit);
            showLogs("TrumpCards --> "+alTrumpSuiteCards.toString());
            showLogs("RemainingCards --> "+alRemainingCards.toString());

            showLogs( "******* Analizando casos específicos... *******");
            if (alTableCards.size()==0 && alIndex.size()==2 && (remainingBets==1 || remainingBets==2)) {
                showLogs("*** Soy el primer, me quedan dos y tengo que ganar una o dos ***");
                if (alTrumpSuiteCards.size()>0 && remainingBets == 2) {
                    showLogs("*** Tengo al menos una pinte y necesio ganar dos de dos. Echo LA MEJOR para robar pintes ***");
                    Card auxCard = new Card(-1, CardType.DOS, trumpSuit);
                    int selected = getBestCard(alALLCards, auxCard, alTrumpSuiteCards);
                    showLogs( "******* Caso específico encontrado *******");
                    return selected;
                }

                if (alTrumpSuiteCards.size()==1 && remainingBets == 1) {
                    showLogs("*** Tengo una pinte y necesio ganar una de dos. Echo la que no es pinte ***");
                    int selected = alRemainingCards.get(0);
                    showLogs( "******* Caso específico encontrado *******");
                    return selected;
                }
            }
            showLogs( "******* Casos específicos no encontrados *******");

            //Existen cartas en la mesa (No es el primero en hablar)
            if(alTableCards.size()>0){
                showLogs("*** YA existen cartas en la mesa, calculando... ***");
                Card firstCard = alTableCards.get(0);

                ArrayList<Integer> alSuitCFirst = new ArrayList<>(); //Posición de las cartas del palo de salida
                alRemainingCards.clear();
                //Gestión de las cartas en mano
                for(int i=0; i<alIndex.size(); i++){
                    Card auxCard = alALLCards.get(alIndex.get(i));
                    //Cartas correspondientes al palo de salida
                    if(auxCard.getSuit()==firstCard.getSuit()) alSuitCFirst.add(alIndex.get(i));
                    //Cartas restantes
                    if(auxCard.getSuit()!=firstCard.getSuit() && auxCard.getSuit()!=trumpSuit) alRemainingCards.add(alIndex.get(i));
                }

                //Tengo cartas del palo de salida
                if(alSuitCFirst.size()!=0){
                    showLogs("*** Tengo cartas del palo de salida ***");

                    if(alSuitCFirst.size()>=2) { //Tengo cartas del palo de salida >=2
                        if(remainingBets<=0) { //No necesito ganar más bazas (echo la peor carta)
                            showLogs("*** No necesito ganar más bazas ***");
                            if(maxCardFirst.getSuit()==trumpSuit){
                                showLogs("** La carta de inicio es del palo de la pinte.");

                                if(haveBetterCards(alALLCards, maxCardFirst)){
                                    showLogs("* Tengo cartas para superar la mejor en mesa");
                                    if(lastPlayer){
                                        showLogs("** Soy el último jugador. Echo la más alta de pinte para quitármela cuanto antes (me la voy a llevar igual)");
                                        return getBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                    } else {
                                        showLogs("** NO soy el último jugador. Echo la más baja que supere para intentar no llevármela");
                                        return getWorstCard(alALLCards, maxCardFirst, alSuitCFirst);
                                    }
                                } else {
                                    showLogs("* NO tengo cartas para superar la mejor en mesa. Echo la más alta");
                                    return getWorstCard(alALLCards, maxCardFirst, alSuitCFirst);
                                }
                            } else {
                                showLogs("** La carta de inicio NO es del palo de la pinte.");
                                if (existTrumpInTableDifferentOfInitial(alTableCards, trumpSuit)) {
                                    showLogs("** Existe un triunfo en mesa, echo la carta más alta del palo de salida.");
                                    return getBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                } else {
                                    showLogs("** No existe un triunfo en mesa, echo la carta más baja que supere el palo de salida.");
                                    return getWorstCard(alALLCards, maxCardFirst, alSuitCFirst);
                                }
                            }
                        } else { //Tengo del palo de salida. Necesito ganar >=1
                            showLogs("*** Necesito ganar "+remainingBets+" baza/s más. ***");
                            if(maxCardFirst.getSuit()==trumpSuit){
                                showLogs("** La carta de inicio es un triunfo");
                                if(haveBetterCards(alALLCards, maxCardFirst)){
                                    if(remainingBets==1){
                                        showLogs("** Tengo que ganar 1, con cartas para superar.");
                                        if(alTrumpSuiteCards.size()>(remainingBets+1)){
                                            showLogs("** Tengo demasiados triunfos para las que tengo que ganar ("+alTrumpSuiteCards.size()+"/"+remainingBets+")" +
                                                    "Echo la más alta, tengo que quitarme de encima");
                                            return getBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        } else {
                                            showLogs("** NO tengo demasiados triunfos. Echo la más baja para tener otra oportunidad si me lo ganan");
                                            return getWorstCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        }
                                    } else if(remainingBets>=2){
                                        showLogs("** Tengo que ganar >=2, con cartas para superar. Diferencia: "+alTrumpSuiteCards.size()+"/"+remainingBets+")");
                                        if(alTrumpSuiteCards.size()>(remainingBets+1)){
                                            showLogs("** Tengo demasiados triunfos. Echo la más baja.");
                                            return getWorstCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        } else {
                                            showLogs("** NO tengo demasiados triunfos. Echo la más alta");
                                            return getBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        }
                                    }
                                } else {
                                    if(remainingBets==1){
                                        showLogs("** Tengo que ganar 1, sin cartas para superar. Diferencia: "+alTrumpSuiteCards.size()+"/"+remainingBets);
                                        if(alTrumpSuiteCards.size()>(remainingBets+1)){
                                            showLogs("** Echo la más alta, tengo que quitarme de encima");
                                            return getSecondBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        } else if(alTrumpSuiteCards.size()>0){
                                            showLogs("** El palo de salida es un triunfo, y tengo triunfos. Echo la más baja");
                                            return getBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        } else {
                                            showLogs("** El palo de salida es un triunfo, pero no tengo triunfos - NO DEBERÍA LLEGAR");
                                            return -1;
                                        }
                                    } else if(remainingBets>=2){
                                        showLogs("** Tengo que ganar >=2, sin cartas para superar. Diferencia: "+alTrumpSuiteCards.size()+"/"+remainingBets);
                                        showLogs("** El palo de salida es un triunfo. Echo el más bajo.");
                                        return getBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                    }
                                }
                            } else {
                                showLogs("** La carta de inicio NO es un triunfo");
                                if(maxTrumpCard!=null){
                                    showLogs("** Existen triunfos en mesa. Ya no puedo ganar esta baza. ");
                                    if(alTrumpSuiteCards.size()>=remainingBets){
                                        showLogs("** Tengo los mismos triunfos o más que pedidas");
                                        if(alTrumpSuiteCards.size()==remainingBets){
                                            showLogs("* Tengo igual de triunfos que pedidas. Echo la carta más alta para quitarmela.");
                                            showLogs("\n* TODO - Gestionar si puedo ganar con esos triunfos. Si no, echar intermedia");
                                            //selected = getWorstCard(alALLCards, maxCardFirst, alSuitCFirst);
                                            Card auxCard = new Card(-1, CardType.AS, maxCardFirst.getSuit());
                                            return getWorstCard(alALLCards, auxCard, alSuitCFirst);
                                        } else if(alTrumpSuiteCards.size()>remainingBets){
                                            showLogs("* Tengo más triunfos que pedidas. Echo la carta más alta para quitarmela.");
                                            //selected = getWorstCard(alALLCards, maxCardFirst, alSuitCFirst);
                                            Card auxCard = new Card(-1, CardType.AS, maxCardFirst.getSuit());
                                            return getWorstCard(alALLCards, auxCard, alSuitCFirst);
                                        }
                                    } else {
                                        if(alTrumpSuiteCards.size()==0){
                                            showLogs("* NO tengo triunfos y tengo pedidas. Echo la más baja.");
                                            Card auxCard = new Card(-1, CardType.AS, maxCardFirst.getSuit());
                                            return getBestCard(alALLCards, auxCard, alSuitCFirst);
                                            //selected = getBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        } else {
                                            showLogs("* Tengo menos triunfos que pedidas. Echo la segunda más alta para intentar ganarla después.");
                                            //selected = getSecondBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                            Card auxCard = new Card(-1, CardType.AS, maxCardFirst.getSuit());
                                            return getSecondBestCard(alALLCards, auxCard, alSuitCFirst);
                                        }
                                    }
                                } else {
                                    showLogs("** NO existen triunfos en mesa");
                                    if(alTrumpSuiteCards.size()>0){
                                        showLogs("** No se empieza por triunfos, pero tengo en mano.");

                                        if(alTrumpSuiteCards.size()>remainingBets && remainingBets>=2){
                                            showLogs("* Tengo que ganar >=2 y tengo menos triunfos que pedidas. Intengo ganarla.");
                                            return getBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        } else if(remainingBets==1){
                                            showLogs("* Tengo que ganar 1, echo la más baja para ganar con triunfos.");
                                            return getWorstCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        } else if(remainingBets>=2) { //Necesito ganar más de una (echo algo intermedio)
                                            showLogs("* Tengo que ganar >=2, echo la más baja para ganar con triunfos.");
                                            return getWorstCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        }
                                    } else {
                                        showLogs("** No se empieza por triunfos, ni tengo en mano.");
                                        if (remainingBets == 1) { //Necesito ganar una (echo la más alta)
                                            showLogs("** Como NO existe triunfo en mesa, echo la más alta para ganar ***");
                                            return getBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        } else if (remainingBets >= 2) { //Necesito ganar más de una (echo algo intermedio)
                                            showLogs("** Como NO existe triunfo en mesa y no tengo triunfos, intento ganarla **");
                                            return getBestCard(alALLCards, maxCardFirst, alSuitCFirst);
                                        }
                                    }
                                }
                            }
                        }
                    } else { //Tengo cartas del palo de salida <2
                        showLogs("*** Selecciono la única que queda ***");
                        return alSuitCFirst.get(0);
                    }
                }
                //No tengo cartas del palo de salida, pero tengo triunfos
                else if(alTrumpSuiteCards.size()!=0){
                    Card auxMaxTrumpCard = (maxTrumpCard==null ? new Card(-1, CardType.DOS, trumpSuit) : maxTrumpCard);
                    showLogs( "*** Tengo triunfos, no cartas del palo de salida ***");
                    if (maxTrumpCard != null) {
                        showLogs("*** Existen triunfos en mesa...");
                        if(haveBetterCards(alALLCards, maxTrumpCard)){
                            showLogs("** Tengo triunfos para superar la más alta. Debo echarlo");
                            if (remainingBets <= 0) {
                                showLogs( "** NO tengo que ganar más. Echo la más baja posible");
                                return getWorstCard(alALLCards, maxTrumpCard, alTrumpSuiteCards);
                            } else if(remainingBets==1) {
                                showLogs( "** Tengo que ganar 1 y tengo para superar el triunfo en mesa. Echo la más alta");
                                return getBestCard(alALLCards, maxTrumpCard, alTrumpSuiteCards);
                            } else if(remainingBets>=2) {
                                showLogs( "** Tengo que ganar >=2 y tengo para superar el triunfo en mesa. Echo la más baja");
                                return getWorstCard(alALLCards, maxTrumpCard, alTrumpSuiteCards);
                            }
                        } else {
                            showLogs("** Tengo triunfos pero NO para superar. ¿Echo triunfo u otro palo?");
                            if(remainingBets<=0) {
                                showLogs("* NO puedo ganar más bazas. Echo el triunfo más alto.");
                                return getWorstCard(alALLCards, auxMaxTrumpCard, alTrumpSuiteCards);
                            } else {
                                showLogs("** Tengo que ganar >0 pero no puedo superar el triunfo. No tengo del palo de inicio.");
                                if (remainingBets == 1) {
                                    if (alTrumpSuiteCards.size() == 1) {
                                        showLogs("* Tengo que ganar 1 y tengo 1 triunfo. Echo ¿la más alta? de las restantes. DEBERÍA TENER AL MENOS UNA");
                                        return getRemainingCard(alALLCards, alRemainingCards, false);
                                    } else if (alTrumpSuiteCards.size()>1) {
                                        showLogs("* Tengo que ganar 1 y tengo más de 1 triunfo. Echo el segundo más alto");
                                        return getSecondBestCard(alALLCards, maxTrumpCard, alTrumpSuiteCards);
                                    } else {
                                        showLogs("** Debería tener al menos un triunfo - NO DEBERÍA LLEGAR");
                                        return -1;
                                    }
                                } else if (remainingBets == 2) {
                                    if (alTrumpSuiteCards.size() == 1) {
                                        showLogs("* Tengo que ganar 2 y tengo 1 triunfo. Echo una restante baja");
                                        return getRemainingCard(alALLCards, alRemainingCards, true);
                                    } else if (alTrumpSuiteCards.size() == 2) {
                                        if (alRemainingCards.size() >0 ) {
                                            showLogs("* Tengo que ganar 2 y tengo 2 triunfos. Echo una restante baja");
                                            return getRemainingCard(alALLCards, alRemainingCards, true);
                                        } else {
                                            showLogs("* Tengo que ganar 2 y tengo 2 triunfos. Echo la más baja");
                                            return getBestCard(alALLCards, maxTrumpCard, alTrumpSuiteCards);
                                        }
                                    } else if (alTrumpSuiteCards.size()>2 ){
                                        showLogs("* Tengo que ganar 2 y tengo >2 triunfos. Echo triunfo bajo. ");
                                        return getBestCard(alALLCards, maxTrumpCard, alTrumpSuiteCards);
                                    }
                                } else if(remainingBets > 2) {
                                    if (alTrumpSuiteCards.size() > remainingBets) {
                                        showLogs("* Tengo más triunfos que por ganar. Echo el triunfo más bajo. ");
                                        return getBestCard(alALLCards, maxTrumpCard, alTrumpSuiteCards);
                                    } else if (alTrumpSuiteCards.size() == remainingBets ){
                                        if (alRemainingCards.size() > 0) {
                                            showLogs("* Tengo los mismos triunfos que por ganar. Echo una restante alta.");
                                            return getRemainingCard(alALLCards, alRemainingCards, false);
                                        } else {
                                            showLogs("* Tengo los mismos triunfos que por ganar. Echo el más bajo. ");
                                            return getBestCard(alALLCards, maxTrumpCard, alTrumpSuiteCards);
                                        }
                                    } else if(alTrumpSuiteCards.size() < remainingBets) {
                                        if (alRemainingCards.size() > 0) {
                                            showLogs("* Tengo menos triunfos que por ganar. Echo una restante baja");
                                            return getRemainingCard(alALLCards, alRemainingCards, true);
                                        } else {
                                            showLogs("* Tengo menos triunfos que por ganar. Echo la más baja");
                                            return getBestCard(alALLCards, maxTrumpCard, alTrumpSuiteCards);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        showLogs("*** NO existen triunfos en mesa. Tengo que echar triunfo SÍ o SÍ");
                        if (alTrumpSuiteCards.size()>=2) {
                            if (remainingBets<=0) {
                                showLogs( "*** No necesito ganar más bazas (echo la peor carta) ***");
                                return getWorstCard(alALLCards, auxMaxTrumpCard, alTrumpSuiteCards);
                            } else {
                                showLogs( "*** Necesito ganar "+remainingBets+" bazas. NO existen triunfos en mesa. ");
                                if(remainingBets==1) {
                                    showLogs( "*** Necesito ganar una baza. Echo la más alta ***");
                                    return getBestCard(alALLCards, auxMaxTrumpCard, alTrumpSuiteCards);
                                } else if(remainingBets>=2) {
                                    showLogs( "*** Necesito ganar más de una baza. Echo la más baja ***");
                                    return getWorstCard(alALLCards, auxMaxTrumpCard, alTrumpSuiteCards);
                                }
                            }
                        } else {
                            showLogs("* Selecciono el único triunfo que tengo ***");
                            return alTrumpSuiteCards.get(0);
                        }
                    }
                }
                //No tengo ni del palo de salida ni triunfos
                //else if(alTrumpSuiteCards.size()==0 && alSuitCFirst.size()==0 && alSuitCFirst.size()==0) {
                else if(alRemainingCards.size()!=0){
                    showLogs( "*** SOLO tengo cartas restantes ***");
                    if(alRemainingCards.size()>1) {
                        if(remainingBets<=0){ //No necesito ganar más bazas (echo la carta más alta que tenga)
                            showLogs("*** No necesito ganar más bazas (echo la carta más alta que tenga) ***");
                            return getRemainingCard(alALLCards, alRemainingCards, false);
                        } else { //Necesito ganar más bazas (echo la carta más baja)
                            showLogs( "*** Necesito ganar más bazas (echo la carta más baja) ***");
                            return getRemainingCard(alALLCards, alRemainingCards, true);
                        }
                    } else {
                        showLogs( "*** Selecciono la única que queda ***");
                        return alRemainingCards.get(0);
                    }
                }
            }
            //El jugador es el primero en hablar
            else {
                showLogs( "*** NO existen cartas en la mesa, calculando... ***");
                if(remainingBets<=0){
                    showLogs("*** No necesito ganar más bazas  (echo la peor carta) ***");
                    if(alTrumpSuiteCards.size()!=0){
                        showLogs("***** Tengo triunfos, echo la más alta ***");
                        Card maxCardTrump = new Card(-1, CardType.DOS, trumpSuit);
                        return getBestCard(alALLCards, maxCardTrump, alTrumpSuiteCards);
                    } else {
                        showLogs("***** NO tengo triunfos, echo la más baja ***");
                        return getRemainingCard(alALLCards, alRemainingCards, true);
                    }
                } else if(remainingBets==1){
                    showLogs( "*** Necesito ganar una baza ***");
                    if(!alTrumpSuiteCards.isEmpty()){
                        int numOfTrump = alTrumpSuiteCards.size();
                        if(numOfTrump==alIndex.size()){
                            if(numOfTrump==2){
                                showLogs("** Tengo 2/2 triunfos. Echo el más bajo.");
                                Card auxCard = new Card(-1, CardType.DOS, trumpSuit);
                                return getWorstCard(alALLCards, auxCard, alTrumpSuiteCards);
                            } else if(numOfTrump>=3){
                                showLogs("** Tengo X/X triunfos, con X>=3. Echo el más alto.");
                                Card auxCard = new Card(-1, CardType.DOS, trumpSuit);
                                return getBestCard(alALLCards, auxCard, alTrumpSuiteCards);
                            }
                        } else {
                            showLogs("** Tengo triunfos, pero no son todas las cartas.");
                            if(numOfTrump==1){
                                showLogs("**Tengo un triunfo. Me lo guardo para luego.");
                                return getRemainingCard(alALLCards, alRemainingCards, true);
                            } else if(numOfTrump==2){
                                showLogs("**Tengo dos triunfos. Tiro el más pequeña.");
                                Card auxCard = new Card(-1, CardType.DOS, trumpSuit);
                                return getWorstCard(alALLCards, auxCard, alTrumpSuiteCards);
                            } else {
                                showLogs("** Tengo más de dos triunfos. Tengo más triunfos que pedidas. Echo la pinte más baja");
                                Card auxCard = new Card(-1, CardType.DOS, trumpSuit);
                                return getWorstCard(alALLCards, auxCard, alTrumpSuiteCards);
                            }
                        }
                    } else {
                        showLogs("** NO tengo triunfos. Echo la más baja para quedarme con las cartas altas al final");
                        return getRemainingCard(alALLCards, alRemainingCards, true);
                    }
                } else { //Necesito ganar más de una (echo algo intermedio)
                    showLogs( "*** Necesito ganar más de una baza ***");
                    if(!alTrumpSuiteCards.isEmpty()){
                        if(alTrumpSuiteCards.size()==alIndex.size()){
                            if(remainingBets==alTrumpSuiteCards.size()){
                                showLogs("** Tengo X/X triunfos, con X>=1. Tengo los mismos triunfos que pedidas. Echo el más ¿bajo?.");
                                Card auxCard = new Card(-1, CardType.DOS, trumpSuit);
                                return getBestCard(alALLCards, auxCard, alTrumpSuiteCards);
                            } else {
                                showLogs("** Tengo X/X triunfos, con X>=1. NO tengo los mismos triunfos que pedidas. Echo el más bajo.");
                                Card auxCard = new Card(-1, CardType.DOS, trumpSuit);
                                return getWorstCard(alALLCards, auxCard, alTrumpSuiteCards);
                            }
                        } else {
                            if(alTrumpSuiteCards.size()>remainingBets){
                                showLogs("** Tengo triunfos, pero no son todos. Tengo más triunfos que pedidas. Echo la pinte más baja");
                                Card auxCard = new Card(-1, CardType.DOS, trumpSuit);
                                return getWorstCard(alALLCards, auxCard, alTrumpSuiteCards);
                            } else if(alTrumpSuiteCards.size()==remainingBets){
                                showLogs("** Tengo triunfos, pero no son todos. Me guardo los triunfos");
                                return getRemainingCard(alALLCards, alRemainingCards, true);
                            } else {
                                if(alIndex.size()>remainingBets){
                                    showLogs("** Tengo más cartas que rondas. Me guardo los triunfos. ");
                                    return getRemainingCard(alALLCards, alRemainingCards, true);
                                } else if(alALLCards.size()==remainingBets){
                                    showLogs("** Tengo que ganar todas las rondas. Echo triunfo.");
                                    Card auxCard = new Card(-1, CardType.DOS, trumpSuit);
                                    return getBestCard(alALLCards, auxCard, alTrumpSuiteCards);
                                } else {
                                    showLogs("** Estoy jodido. Tiro lo que sea. ");
                                    return getRemainingCard(alALLCards, alRemainingCards, true);
                                }
                            }
                        }
                    } else {
                        showLogs("** NO tengo pintes. Echo la más baja para quedarme con las cartas altas al final");
                        return getRemainingCard(alALLCards, alRemainingCards, true);
                    }
                }
            }
        } else if (alIndex.size() == 1) return alIndex.get(0);

        if (alIndex.isEmpty()) {
            Gdx.app.error(TAG, "No existen cartas para seleccionar...");
        }

        Gdx.app.error(TAG, "Algo salío mal... Return -1");
        return -1;
    }

    /**
     * Devuelve la carta que mejor me venga para NO ganar la baza
     *      -Si tengo cartas para superar la mejor en mesa, echo la más baja que supere (intento no ganar la baza)
     *      -Si NO tengo cartas para superar, echo la más alta (me la quito)
     *
     *   (Comprobaciones con >= y <= para incluir el valor de la carta falsa anterior) ¿?
     * @param alCards Cartas en mano
     * @param tableCard Carta alta en mesa
     * @param alSuitCard
     * @return
     */
    private static int getWorstCard(ArrayList<Card> alCards, Card tableCard, ArrayList<Integer> alSuitCard){
        ArrayList<Integer>[] alAux = getLowerUpperCards(alCards,tableCard,alSuitCard);
        ArrayList<Integer> alLower = alAux[0];
        ArrayList<Integer> alUpper = alAux[1];

        int iCardSelected = -1;
        //Si tengo para superar, echo la más baja
        if(!alUpper.isEmpty()){
            for(int i=0; i<alUpper.size(); i++){
                if(iCardSelected!=-1){
                    int valueCardArray = alCards.get(alUpper.get(i)).getCardType().ordinal();
                    int valueCardSelected = alCards.get(iCardSelected).getCardType().ordinal();
                    if(valueCardSelected>=valueCardArray){
                        iCardSelected = alUpper.get(i);
                    }
                } else {
                    iCardSelected = alUpper.get(i);
                }
            }
        } else { //Si no, echo la más alta
            for(int i=0; i<alLower.size(); i++){
                if(iCardSelected!=-1){
                    int valueCardArray = alCards.get(alLower.get(i)).getCardType().ordinal();
                    int valueCardSelected = alCards.get(iCardSelected).getCardType().ordinal();
                    if(valueCardSelected<=valueCardArray){
                        iCardSelected = alLower.get(i);
                    }
                } else {
                    iCardSelected = alLower.get(i);
                }
            }
        }

        showLogs("*getWorstCard()*");
        showLogs( "*** Cartas que superan: "+alUpper);
        showLogs( "*** Cartas que NO superan: "+alLower);
        showLogs("*** SELECCIONO la carta: "+iCardSelected);
        return iCardSelected;
    }

    /**
     * Devuelve la carta que mejor me venga para GANAR la baza
     *      -Si tengo cartas para superar la mejor en mesa, echo la más alta (intento ganarla sí o sí
     *      -Si NO tengo cartas para superar, echo la más baja (para poder ganar con el resto de cartas)
     *
     *   (Comprobaciones con >= y <= para incluir el valor de la carta falsa anterior) ¿?
     * @param alCards Cartas en mano
     * @param tableCard Carta alta en mesa
     * @param alSuitCard
     * @return
     */
    private static int getBestCard(ArrayList<Card> alCards, Card tableCard, ArrayList<Integer> alSuitCard){
        ArrayList<Integer>[] arrayLists = getLowerUpperCards(alCards,tableCard,alSuitCard);
        ArrayList<Integer> alLower = arrayLists[0];
        ArrayList<Integer> alUpper = arrayLists[1];

        int iCardSelected = -1;
        //Si tengo para superar, echo la más alta
        if(!alUpper.isEmpty()){
            for(int i=0; i<alUpper.size(); i++){
                if(iCardSelected!=-1){
                    int valueCardArray = alCards.get(alUpper.get(i)).getCardType().ordinal();
                    int valueCardSelected = alCards.get(iCardSelected).getCardType().ordinal();
                    if(valueCardSelected<=valueCardArray){
                        iCardSelected = alUpper.get(i);
                    }
                } else {
                    iCardSelected = alUpper.get(i);
                }
            }
        } else { //Si no, echo la más baja
            for(int i=0; i<alLower.size(); i++){
                if(iCardSelected!=-1){
                    int valueCardArray = alCards.get(alLower.get(i)).getCardType().ordinal();
                    int valueCardSelected = alCards.get(iCardSelected).getCardType().ordinal();
                    if(valueCardSelected>=valueCardArray){
                        iCardSelected = alLower.get(i);
                    }
                } else {
                    iCardSelected = alLower.get(i);
                }
            }
        }

        showLogs("*getBestCard()*");
        showLogs( "*** Cartas que superan: "+alUpper);
        showLogs( "*** Cartas que NO superan: "+alLower);
        showLogs( "*** SELECCIONO la carta: "+iCardSelected);
        return iCardSelected;
    }

    /**
     * Devuelve la segunda mejor carta.
     *    -Si tengo varias para superar, la segunda mejor.
     *    -Si no tengo para superar, también la segunda mejor
     *
     * @param alCards
     * @param tableCard
     * @param alSuitCard
     * @return
     */
    private static int getSecondBestCard(ArrayList<Card> alCards, Card tableCard, ArrayList<Integer> alSuitCard){
        ArrayList<Integer>[] arrayLists = getLowerUpperCards(alCards,tableCard,alSuitCard);
        ArrayList<Integer> alLower = arrayLists[0];
        ArrayList<Integer> alUpper = arrayLists[1];

        int iBestCardSelected = -1;
        int iSecondBestCardSelected = -1;
        int iCardSelected = -1;
        //Si tengo para superar, echo la segunda más alta
        if(!alUpper.isEmpty()){
            showLogs( "AL UPPER");
            for(int i=0; i<alUpper.size(); i++){
                showLogs( "CARTA ACTUAL: "+alCards.get(alUpper.get(i)).toString());
                if(iBestCardSelected==-1){
                    iBestCardSelected = alUpper.get(i);
                    showLogs( "Ahora la mejor es: "+alCards.get(iBestCardSelected).toString()+"("+iBestCardSelected+")");
                    continue;
                }

                int valueArray = alCards.get(alUpper.get(i)).getCardType().ordinal();
                int valueBestCard = alCards.get(iBestCardSelected).getCardType().ordinal();

                if(iSecondBestCardSelected==-1){
                    if(valueBestCard<=valueArray){
                        iSecondBestCardSelected = iBestCardSelected;
                        iBestCardSelected = alUpper.get(i);
                        showLogs(  "Ahora la mejor es: "+alCards.get(iBestCardSelected).toString()+"("+iBestCardSelected+")");
                        showLogs(  "Ahora la segunda mejor es: "+alCards.get(iSecondBestCardSelected).toString()+"("+iSecondBestCardSelected+")");
                    } else {
                        iSecondBestCardSelected = alUpper.get(i);
                        showLogs(  "Ahora la segunda mejor es: "+alCards.get(iSecondBestCardSelected).toString()+"("+iSecondBestCardSelected+")");
                    }

                    continue;
                }

                int valueSecondCard = alCards.get(iSecondBestCardSelected).getCardType().ordinal();

                if(valueBestCard<=valueArray){
                    iSecondBestCardSelected = iBestCardSelected;
                    iBestCardSelected = alUpper.get(i);
                    showLogs(  "Ahora la mejor es: "+alCards.get(iBestCardSelected).toString()+"("+iBestCardSelected+")");
                    showLogs(  "Ahora la segunda mejor es: "+alCards.get(iSecondBestCardSelected).toString()+"("+iSecondBestCardSelected+")");
                } else if(valueSecondCard<=valueArray){
                    iSecondBestCardSelected = alUpper.get(i);
                    showLogs(  "Ahora la segunda mejor es: "+alCards.get(iSecondBestCardSelected).toString()+"("+iSecondBestCardSelected+")");
                }
            }
            iCardSelected = (iSecondBestCardSelected!=-1 ? iSecondBestCardSelected : iBestCardSelected);
        } else { //Si no, también
            showLogs(  "AL LOWER");
            for(int i=0; i<alLower.size(); i++){
                showLogs(  "CARTA ACTUAL: "+alCards.get(alLower.get(i)).toString());
                if(iBestCardSelected==-1){
                    iBestCardSelected = alLower.get(i);
                    showLogs(  "Ahora la mejor es: "+alCards.get(iBestCardSelected).toString()+"("+iBestCardSelected+")");
                    continue;
                }

                int valueArray = alCards.get(alLower.get(i)).getCardType().ordinal();
                int valueBestCard = alCards.get(iBestCardSelected).getCardType().ordinal();

                if(iSecondBestCardSelected==-1){
                    if(valueBestCard<=valueArray){
                        iSecondBestCardSelected = iBestCardSelected;
                        iBestCardSelected = alLower.get(i);
                        showLogs(  "Ahora la mejor es: "+alCards.get(iBestCardSelected).toString()+"("+iBestCardSelected+")");
                        showLogs(  "Ahora la segunda mejor es: "+alCards.get(iSecondBestCardSelected).toString()+"("+iSecondBestCardSelected+")");
                    } else {
                        iSecondBestCardSelected = alLower.get(i);
                        showLogs( "Ahora la segunda mejor es: "+alCards.get(iSecondBestCardSelected).toString()+"("+iSecondBestCardSelected+")");
                    }

                    continue;
                }

                int valueSecondCard = alCards.get(iSecondBestCardSelected).getCardType().ordinal();

                if(valueBestCard<=valueArray){
                    iSecondBestCardSelected = iBestCardSelected;
                    iBestCardSelected = alLower.get(i);
                    showLogs(  "Ahora la mejor es: "+alCards.get(iBestCardSelected).toString()+"("+iBestCardSelected+")");
                    showLogs(  "Ahora la segunda mejor es: "+alCards.get(iSecondBestCardSelected).toString()+"("+iSecondBestCardSelected+")");
                } else if(valueSecondCard<=valueArray){
                    iSecondBestCardSelected = alLower.get(i);
                    showLogs(  "Ahora la segunda mejor es: "+alCards.get(iSecondBestCardSelected).toString()+"("+iSecondBestCardSelected+")");
                }
            }
            iCardSelected = (iSecondBestCardSelected!=-1 ? iSecondBestCardSelected : iBestCardSelected);
        }

        showLogs("*getSecondBestCard()*");
        showLogs( "*** Cartas que superan: "+alUpper);
        showLogs( "*** Cartas que NO superan: "+alLower);
        showLogs( "*** SELECCIONO la carta: "+iCardSelected);
        return iCardSelected;
    }

    //LAS COMPROBACIONES SE REALIZARÁN CON >= ó <= para incluir el valor de la carta falsa anterior
    /* Devuelve una carta intermedia (del palo tableCard) para ganar:
        -Si tengo para superar, echo la más pequeña
        -Si no, la más baja
     */
    private static int getIntermediateCard(ArrayList<Card> alCards, Card tableCard, ArrayList<Integer> alSuitCard){
        ArrayList<Integer>[] arrayLists = getLowerUpperCards(alCards,tableCard,alSuitCard);
        ArrayList<Integer> alLower = arrayLists[0];
        ArrayList<Integer> alUpper = arrayLists[1];

        int iCardSelected = -1;
        //Si tengo para superar, echo la más baja
        if(!alUpper.isEmpty()){
            for(int i=0; i<alUpper.size(); i++){
                if(iCardSelected!=-1){
                    int valueCardSelected = alCards.get(iCardSelected).getCardType().ordinal();
                    int valueCardArray = alCards.get(alUpper.get(i)).getCardType().ordinal();
                    if(valueCardSelected>=valueCardArray){
                        iCardSelected = alUpper.get(i);
                    }
                } else {
                    iCardSelected = alUpper.get(i);
                }
            }
        } else { //Si no, echo la más baja también
            for(int i=0; i<alLower.size(); i++){
                if(iCardSelected!=-1){
                    int valueCardArray = alCards.get(alLower.get(i)).getCardType().ordinal();
                    int valueCardSelected = alCards.get(iCardSelected).getCardType().ordinal();
                    if(valueCardSelected>=valueCardArray){
                        iCardSelected = alLower.get(i);
                    }
                } else {
                    iCardSelected = alLower.get(i);
                }
            }
        }

        showLogs("*getIntermediateCard()*");
        showLogs( "*** Cartas que superan: "+alUpper);
        showLogs( "*** Cartas que NO superan: "+alLower);
        showLogs( "*** SELECCIONO la carta: "+iCardSelected);
        return iCardSelected;
    }

    private static int getRemainingCard(ArrayList<Card> alCards, ArrayList<Integer> alRemaining, boolean lower){
        int iLower = -1;
        int iUpper = -1;

        for(int i=0; i<alRemaining.size(); i++){
            int valueCardArray = alCards.get(alRemaining.get(i)).getCardType().ordinal();
            if(iLower!=-1){
                int valueCardLower = alCards.get(iLower).getCardType().ordinal();
                if(valueCardLower>=valueCardArray) iLower = alRemaining.get(i);
            }
            if(iUpper!=-1){
                int valueCardUpper = alCards.get(iUpper).getCardType().ordinal();
                if(valueCardUpper<=valueCardArray) iUpper = alRemaining.get(i);
            }

            if(iLower==-1) iLower = alRemaining.get(i);
            if(iUpper==-1) iUpper = alRemaining.get(i);
        }

        int answer = -1;
        if(lower) answer = iLower;
        else answer = iUpper;

        showLogs("*getRemainingCard()*");
        showLogs( "*** Carta más baja (Índice en mano): "+iLower);
        showLogs( "*** Carta más alta (Índice en mano): "+iUpper);
        showLogs( "*** SELECCIONO la carta: "+answer);

        return answer;
    }

    //LAS COMPROBACIONES SE REALIZARÁN CON >= ó <= para incluir el valor de la carta falsa anterior
    //ArrayList<>[0] --> Array con las cartas más bajas a la pinte
    //ArrayList<>[1] --> Array con las cartas más altas a la pinte
    private static ArrayList<Integer>[] getLowerUpperCards(ArrayList<Card> alCards, Card tableCard, ArrayList<Integer> alSuitCard){
        ArrayList<Integer> alLowerCards = new ArrayList<>();
        ArrayList<Integer> alUpperCards = new ArrayList<>();
        for(int i=0; i<alSuitCard.size(); i++){
            int indexCard = alSuitCard.get(i);
            if(tableCard.getCardType().ordinal()>=alCards.get(indexCard).getCardType().ordinal()){
                alLowerCards.add(indexCard);
            }
            if(tableCard.getCardType().ordinal()<=alCards.get(indexCard).getCardType().ordinal()){
                alUpperCards.add(indexCard);
            }
        }

        ArrayList<Integer>[] arrays = new ArrayList[2];
        arrays[0] = alLowerCards;
        arrays[1] = alUpperCards;

        return arrays;
    }

    //Existe un triunfo en mesa que no sea del mismo palo que la carta inicial
    private static boolean existTrumpInTableDifferentOfInitial(ArrayList<Card> alTableCards, Suit trumpSuit){
        boolean exist = false;
        for(int i=0; i<alTableCards.size(); i++){
            if(i!=0){
                Card card = alTableCards.get(i);
                if(card.getSuit()==trumpSuit && card.getSuit()!=alTableCards.get(0).getSuit()){
                    exist = true;
                    break;
                }
            }
        }

        return exist;
    }

    /**
     * Indica si tengo cartas en mano para superar la mejor de la mesa.
     *
     * @return true: Tengo para superar
     */
    private static boolean haveBetterCards(ArrayList<Card> alPlayerCards, Card maxCard){
        int value = maxCard.getCardType().ordinal();
        for (Card card : alPlayerCards){
            if (!card.isSelected()) {
                if(card.getSuit()==maxCard.getSuit() && card.getCardType().ordinal()>=value)
                    return true;
            }
        }
        return false;
    }

   private static int getNumOfSuitCards(ArrayList<Card> alALLCards, Suit suit){
        int number = 0;
        for(Card card : alALLCards){
            if(card.getSuit()==suit) number+=1;
        }
        return number;
   }

    private static void showLogs(String text){
        if(showLogs)
            Gdx.app.log(TAG, text);
    }

    private static void showPlayerCards(ArrayList<Card> alCards){
        showLogs("Jugador: "+alCards.toString());
    }

    private static void showTableCards(ArrayList<Card> alCards){
        showLogs("Mesa: "+alCards.toString());
    }

    private static Card getMaxCardFirst(ArrayList<Card> _alTableCards){
        Card card = null;
        if(_alTableCards.size()>0) {
            Suit firstSuit = _alTableCards.get(0).getSuit();
            for (int i = 0; i < _alTableCards.size(); i++) {
                if (_alTableCards.get(i).getSuit() == firstSuit) {
                    if (card != null) {
                        if (_alTableCards.get(i).getCardType().ordinal() > card.getCardType().ordinal()) {
                            card = _alTableCards.get(i);
                        }
                    } else card = _alTableCards.get(i);
                }
            }
        }

        //Si devuelve null, no existe carta en mesa
        return card;
    }
    private static Card getMaxCardTrump(Suit _trumpSuite, ArrayList<Card> _alTableCards){
        Card card = null;
        for(int i=0; i<_alTableCards.size(); i++){
            if(_alTableCards.get(i).getSuit()==_trumpSuite){
                if(card!=null){
                    if(_alTableCards.get(i).getCardType().ordinal()>card.getCardType().ordinal()) {
                        card = _alTableCards.get(i);
                    }
                } else card = _alTableCards.get(i);
            }
        }

        //Si devuelve null, no existe carta en mesa
        return card;
    }

    public static int doTEST(){
        return getCardSelected();
    }
}

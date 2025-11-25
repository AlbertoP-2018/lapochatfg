package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.player.PochaPlayer;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.layout.CardButton;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.PhasePlayer;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.DecisionB;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Suit;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Phase;

import java.util.ArrayList;

public class GUIBuilder_Cards {
    private static final String TAG = GUIBuilder_Cards.class.getName();
    private static final float W = Constants.VIEWPORT_WIDTH;
    private static final float H = Constants.VIEWPORT_HEIGHT;
    public static final float SIZE_CARDS[] = {W*0.28f, H*0.25f};

    private GUIBuilder guiBuilder;
    private Stack stack; //Pila con todas las tablas
    private ArrayList<CardButton> alCards;
    private ArrayList<Boolean> alVisibleCards;

    private boolean flagUpdateTableCards;

    // Variables to update available cards
    private int lastChance; // Last update chance
    private boolean turnOnAvailableCards, turnOffAvailableCards; // When to modify the availabilty

    public GUIBuilder_Cards(GUIBuilder guiBuilder){
        this.guiBuilder = guiBuilder;
        this.flagUpdateTableCards = false;

        this.lastChance = -1;
        this.turnOnAvailableCards = true;
        this.turnOffAvailableCards = true;

        //Crea todos los botones de cartas posibles
        alCards = new ArrayList<>();
        alVisibleCards = new ArrayList<>();
        for(int i = 0; i<guiBuilder.getGameScreen().getMAX_CHANCES(); i++){
            // Button bCard = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getSmallBlackSquare()));
            CardButton cardButton = new CardButton(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getSmallBlackSquare()), true);
            alCards.add(cardButton);
            alVisibleCards.add(false);

            final int finalI = i;
            cardButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    listenerCard((Button)actor, finalI);
                }
            });
        }
    }

    public void render(){
        int iP = guiBuilder.getGameScreen().getIndexPlayer();
        int cP = guiBuilder.getGameLogic().getGameData().getCurrentPlayer();

        boolean me = iP == cP;
        boolean phaseCard = guiBuilder.getGameLogic().getGameData().getPhase() == PochaEnum.Phase.CARD;
        boolean layerPhaseCard = guiBuilder.getGameLogic().getPlayers()[iP].getPhasePlayer() == PhasePlayer.CARD;
        boolean myTurn = me && phaseCard && layerPhaseCard;
        int currentChance = guiBuilder.getGameLogic().getGameScore().getNumChance();

        for (int i=0; i<alCards.size(); i++) {
            alCards.get(i).setVisible(alVisibleCards.get(i));
        }

        if (flagUpdateTableCards) {
            flagUpdateTableCards = false;
            updateTableCards();
        }

        // Turn on available cards
        if (turnOnAvailableCards && myTurn) {
            turnOnAvailableCards = false;
            turnOffAvailableCards = true;
            updateAvailableCard(true);
        }

        // Turn off available cards
        if (turnOffAvailableCards && (!me || (lastChance != currentChance))) {
            lastChance = currentChance;
            turnOnAvailableCards = true;
            turnOffAvailableCards = false;
            updateAvailableCard(false);
        }

        /* if(iP==cP){
            if(guiBuilder.getGameLogic().getGameData().getPhase()==Constants.Phase.CARD){
                if(guiBuilder.getGameLogic().getPlayers()[iP].getPhasePlayer()==PhasePlayer.CARD){
                    updateAvailableCard(true);
                } else updateAvailableCard(false);
            } else updateAvailableCard(false);
        } else updateAvailableCard(false); */
    }

    public void initHand(){
        Gdx.app.log(TAG, "*Init HAND*");

        int iP = guiBuilder.getGameLogic().getGameScreen().getIndexPlayer();
        PochaPlayer player = guiBuilder.getGameLogic().getPlayers()[iP];

        //Establece la imagen a cada carta del jugador
        for(int i=0; i<player.getAlCards().size(); i++){
            alCards.get(i).getStyle().up = new TextureRegionDrawable(player.getAlCards().get(i).getTrCard());
            alVisibleCards.set(i, true);
            alCards.get(i).setVisible(alVisibleCards.get(i));
        }
    }

    public void initChane(){
        Gdx.app.log(TAG, "*Init CHANCE*");
    }

    public void updateCardsFromReload() {
        Phase phase = guiBuilder.getGameLogic().getGameData().getPhase();

        if (phase == Phase.BET) {
            int iP = guiBuilder.getGameLogic().getGameScreen().getIndexPlayer();
            PochaPlayer player = guiBuilder.getGameLogic().getPlayers()[iP];

            // Establece la imagen a cada carta del jugador
            for(int i=0; i<player.getAlCards().size(); i++){
                Card card = player.getAlCards().get(i);
                alCards.get(i).getStyle().up = new TextureRegionDrawable(card.getTrCard());
                alVisibleCards.set(i, true);
                alCards.get(i).setVisible(alVisibleCards.get(i));
            }
        }

        if (phase == Phase.CARD) {
            int iP = guiBuilder.getGameLogic().getGameScreen().getIndexPlayer();
            PochaPlayer player = guiBuilder.getGameLogic().getPlayers()[iP];

            // Establece la imagen a cada carta del jugador
            for(int i=0; i<player.getAlCards().size(); i++){
                Card card = player.getAlCards().get(i);
                alCards.get(i).getStyle().up = new TextureRegionDrawable(card.getTrCard());
                alVisibleCards.set(i, !card.isSelected());
                alCards.get(i).setVisible(alVisibleCards.get(i));
            }
        }

        if (phase == Phase.CONTINUE) {
            int iP = guiBuilder.getGameLogic().getGameScreen().getIndexPlayer();
            PochaPlayer player = guiBuilder.getGameLogic().getPlayers()[iP];

            // Establece la imagen a cada carta del jugador
            for(int i=0; i<player.getAlCards().size(); i++){
                Card card = player.getAlCards().get(i);
                alCards.get(i).getStyle().up = new TextureRegionDrawable(card.getTrCard());
                alVisibleCards.set(i, false);
                alCards.get(i).setVisible(alVisibleCards.get(i));
            }
        }

        updateTableCards();
    }

    private void updateAvailableCard(boolean turnOn){
        for(int i=0; i<guiBuilder.getGameLogic().getGameScore().getNumChancesHand(); i++){
            String text = "Este texto no debería aparecer aquí...";
            boolean available = true;

            if (turnOn) {
                Object[] availableObject = isAvailableCard(i);
                available = (boolean) availableObject[0];
                text = (String) availableObject[1];
                if (available) alCards.get(i).setColor(Color.WHITE);
                else alCards.get(i).setColor(Color.DARK_GRAY);
            } else alCards.get(i).setColor(Color.WHITE);

            alCards.get(i).setAvailable(available);
            alCards.get(i).setUnavailableText(text);
        }
    }

    private void listenerCard(Button button, int indexCard){
        GameLogic gameLogic = guiBuilder.getGameLogic();
        int iP = gameLogic.getGameScreen().getIndexPlayer();
        PochaPlayer player = gameLogic.getPlayers()[iP];
        int cP = gameLogic.getGameData().getCurrentPlayer();

        if(iP!=cP || player.getPhasePlayer()!=PhasePlayer.CARD) return;

        // Object[] aux = isAvailableCard(indexCard);
        boolean available = alCards.get(indexCard).isAvailable();
        String unavailableText = alCards.get(indexCard).getUnavailableText();

        if(available && gameLogic.getPlayers()[iP].getPhasePlayer()==PhasePlayer.CARD && gameLogic.getPlayers()[iP].getSelectedCard()==-1){
            ButtonEvent buttonEvent = new ButtonEvent(DecisionB.CARD, cP, indexCard);
            guiBuilder.getGameScreen().controlButtonEvent(buttonEvent, guiBuilder.getGameScreen().getGameMode()==PochaEnum.GameMode.OFFLINE);
        } else {
            guiBuilder.getGuiBuilderGameInformation().showInformationWindow(unavailableText, false);
            Gdx.app.log(TAG,"La carta no se puede seleccionar.");
        }
    }

    // From render
    private Object[] isAvailableCard(int indexCard){
        GameLogic gameLogic = guiBuilder.getGameLogic();

        int iP = gameLogic.getGameScreen().getIndexPlayer();
        PochaPlayer player = gameLogic.getPlayers()[iP];

        int fP = gameLogic.getGameData().getFirstPlayer();
        int cP = gameLogic.getGameData().getCurrentPlayer();
        int numRemainingChances = gameLogic.getGameScore().getNumChancesHand()-gameLogic.getGameScore().getNumChance();

        String text = "Algo salió mal...";
        boolean available = false;
        if(numRemainingChances!=0){ //Si tengo más de una carta en la mano
            if(iP!=fP){ //Existe alguna carta en mesa
                Suit firstSuit = gameLogic.getGameData().getAlCardsTable().get(0).getSuit();
                Suit trumpSuit = gameLogic.getShufflerCards().getTrump().getSuit();
                ArrayList<Integer> alFirstSuit = player.getFirstSuit(firstSuit);
                ArrayList<Integer> alTrumpSuit = player.getTrumpSuit(trumpSuit);

                Card cardSelected = player.getCard(indexCard);

                //Si tengo del palo de salida
                if(!alFirstSuit.isEmpty()){
                    //Si la carta seleccionada no es del palo de salida
                    if(cardSelected.getSuit()!=firstSuit){
                        text = "Debes echar una carta del mismo palo";
                        available = false;
                    } else {
                        Card maxCardFirst = gameLogic.getGameData().getMaxCardFirst();
                        boolean haveBetterFirst = doHaveBetter(player, maxCardFirst);
                        Card maxTrumpCard = gameLogic.getGameData().getMaxCardTrump();

                        //Si existe una carta de la pinte
                        if(maxTrumpCard!=null){
                            //Si la carta de salida es la misma que la pinte, estoy obligado a superar
                            //if(maxTrumpCard.getSuit()==trumpSuit){
                            if(maxTrumpCard.getSuit()==firstSuit){
                                if(haveBetterFirst){
                                    if(cardSelected.getCardType().ordinal()<maxCardFirst.getCardType().ordinal()){
                                        text = "Debes superar la carta más alta";
                                        available = false;
                                    } else available = true;
                                } else available = true; //Si la carta seleccionada es superior
                            //Si la carta de salida NO es la misma que la pinte, no hace falta superar
                            } else available = true;
                        } else {
                            //Si tengo una carta para superar
                            if(haveBetterFirst){
                                //Si la carta seleccionada no es superior
                                if(cardSelected.getCardType().ordinal()<maxCardFirst.getCardType().ordinal()){
                                    text = "Debes superar la carta más alta";
                                    available = false;
                                } else available = true; //Si la carta seleccionada es superior
                            } else available = true; //Si no tengo para superar
                        }
                    }
                //Si no, si tengo del palo de la pinte
                } else if(!alTrumpSuit.isEmpty()){
                    Card maxTrumpCard = gameLogic.getGameData().getMaxCardTrump();
                    //Si existe una carta de la pinte en la mesa
                    if(maxTrumpCard!=null){
                        boolean haveBetterTrump = doHaveBetter(player, maxTrumpCard);
                        //Si tengo para superar
                        if(haveBetterTrump){
                            if(cardSelected.getSuit()==maxTrumpCard.getSuit()){
                                if(cardSelected.getCardType().ordinal()<maxTrumpCard.getCardType().ordinal()){
                                    text = "Debes superar la carta más alta";
                                    available = false;
                                } else available=true;
                            } else {
                                text = "Debes echar una carta de la pinte";
                                available = false;
                            }
                        } else available = true; //Si no, puedo echar cualquier otra carta
                    //Si no existe otra carta de la pinte en la mesa
                    } else {
                        //Si la carta seleccionada no es de la pinte
                        if(cardSelected.getSuit()!=trumpSuit){
                            text = "Debe echar una carta de la pinte";
                            available = false;
                        } else available = true;
                    } //FIN de else
                } else available = true; //No tengo cartas ni del palo de salida ni de la pinte
            } else available = true; //No existe ninguna carta en mesa (puede empezar por la que sea)
        } else available = true; //numRemainingChances==0, solo hay una carta

        if (available) text = "";

        return new Object[]{available,text};
    }

    //Permite mostrar el número de cartas correspondientes según la baza
    private void updateTableCards(){ //Run in main thread !!
        stack.clear();
        stack.add(createIndexTableLayers());
    }

    private boolean doHaveBetter(PochaPlayer player, Card maxCardFirst){
        boolean haveBetter = false;
        for(Card card : player.getUnselectedCards()){
            if(card.getSuit()==maxCardFirst.getSuit()){
                if(card.getCardType().ordinal()>maxCardFirst.getCardType().ordinal()){
                    haveBetter = true;
                    break;
                }
            }
        }
        return haveBetter;
    }

    public Stack createCardsLayer(){
        //Crea la pila y la correspondiente tabla de cartas
        stack = new Stack();
        stack.setDebug(false);
        stack.addActor(createIndexTableLayers());
        return stack;
    }

    private Table createIndexTableLayers(){
        int iP = guiBuilder.getGameScreen().getIndexPlayer();

        int numCards = guiBuilder.getGameLogic().getGameScore().getNumChancesHand();
        for(int i=0; i<numCards; i++){
            alCards.get(i).getStyle().up = new TextureRegionDrawable(getTRCard(iP,i));
        }

        Table table = new Table();
        table.setDebug(false);
        table.setFillParent(false);
        table.setVisible(true);
        table.bottom();

        Stack stack = null;

        float firstPad, basePad;
        int switchValue = numCards-1;
        switch(switchValue){
            case 0: //Cartas en mano: 1
                stack = createCardsStack(0, 0, 1);
                break;
            case 1: //Cartas en mano: 2
                table.right();
                firstPad = W*0.9f;
                basePad = SIZE_CARDS[0]*1.5f;
                stack = createCardsStack(firstPad, basePad, 2);
                break;
            case 2: //Cartas en mano: 3
                table.right();
                firstPad = W+SIZE_CARDS[0]*0.4f;
                basePad = SIZE_CARDS[0]*1.5f;
                stack = createCardsStack(firstPad, basePad, 3);
                break;
            case 3: //Cartas en mano: 4
                table.right();
                firstPad = W+SIZE_CARDS[0]*0.95f;
                basePad = SIZE_CARDS[0]*1.3f;
                stack = createCardsStack(firstPad, basePad, 4);
                break;
            case 4: //Cartas en mano: 5
                table.right();
                firstPad = W+SIZE_CARDS[0]*1.6f;
                basePad = SIZE_CARDS[0]*1.4f;
                stack = createCardsStack(firstPad, basePad, 5);
                break;
            case 5: //Cartas en mano: 6
                table.right();
                firstPad = W+SIZE_CARDS[0]*1.6f;
                basePad = SIZE_CARDS[0]*1.18f;
                stack = createCardsStack(firstPad, basePad, 6);
                break;
            case 6: //Cartas en mano: 7
                table.right();
                firstPad = W+SIZE_CARDS[0]*1.6f;
                basePad = SIZE_CARDS[0]*1.02f;
                stack = createCardsStack(firstPad, basePad, 7);
                break;
            case 7: //Cartas en mano: 8
                table.right();
                firstPad = W+SIZE_CARDS[0]*1.6f;
                basePad = SIZE_CARDS[0]*0.9f;
                stack = createCardsStack(firstPad, basePad, 8);
                break;
            case 8: //Cartas en mano: 9
                table.right();
                firstPad = W+SIZE_CARDS[0]*1.8f;
                basePad = SIZE_CARDS[0]*0.82f;
                stack = createCardsStack(firstPad, basePad, 9);
                break;
            case 9: //Cartas en mano: 10
                table.right();
                firstPad = W+SIZE_CARDS[0]*1.8f;
                basePad = SIZE_CARDS[0]*0.735f;
                stack = createCardsStack(firstPad, basePad, 10);
                break;
            case 10: //Cartas en mano: 11
                table.right();
                firstPad = W+SIZE_CARDS[0]*1.8f;
                basePad = SIZE_CARDS[0]*0.68f;
                stack = createCardsStack(firstPad, basePad, 11);
                break;
            default:
                Gdx.app.error(TAG, "NO EXISTE UNA TABLA PARA MÁS DE 10 CARTAS EN MANO");
                break;
        }

        table.add(stack).size(SIZE_CARDS[0],SIZE_CARDS[1]);

        return table;
    }

    private Table getNewTable(int indexButton){
        Table table = new Table();
        table.add((alCards.get(indexButton))).size(SIZE_CARDS[0],SIZE_CARDS[1]);
        return table;
    }

    private TextureRegion getTRCard(int player, int indexCard){
        Card card = guiBuilder.getGameLogic().getPlayers()[player].getCard(indexCard);
        if(card!=null) return card.getTrCard();
        return AssetLoader.aLoader.aImage.getSmallBlackSquare();
    }

    private Stack createCardsStack(float firstPad, float basePad, int numCards){
        Stack stack = new Stack();
        for (int i=0; i<numCards; i++) {
            stack.add(getNewTable(i).padRight(firstPad-(basePad*i)));
        }

        return stack;
    }

    /** Thread Button Event AND Thread Reload Game **/
    public void setFlagUpdateTableCards(boolean flag){ this.flagUpdateTableCards = flag; }
    public void hideCard(int index) {
        alVisibleCards.set(index, false);
    }

    /*** Getters And Setters ***/
    public ArrayList<CardButton> getAlCards(){ return alCards; }
}

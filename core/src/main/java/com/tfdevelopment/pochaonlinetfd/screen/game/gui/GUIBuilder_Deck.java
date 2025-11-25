package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;

public class GUIBuilder_Deck {
    private static final String TAG = GUIBuilder_Deck.class.getName();
    private static final float W = Constants.VIEWPORT_WIDTH;
    private static final float H = Constants.VIEWPORT_HEIGHT;
    private static final float SIZE_DECK[] =       {W*0.2f,    H*0.16f};
    private static final float SIZE_TRUMP[] =       {W*0.185f,    H*0.15f};
    private static int TOTAL_PLAYERS;

    private GUIBuilder guiBuilder;
    private Button bTrump;
    private Table tDecks, tCardBack;
    private Stack stackDecks;
    private Stack[] aDecks;
    private Stack sDeck;

    public GUIBuilder_Deck(GUIBuilder guiBuilder){
        this.guiBuilder = guiBuilder;
        this.TOTAL_PLAYERS = guiBuilder.getGameScreen().getTOTAL_PLAYERS();
        this.aDecks = new Stack[TOTAL_PLAYERS];
        for(int i = 0; i<TOTAL_PLAYERS; i++){
            aDecks[i] = new Stack();
        }
    }

    public void render() {
        if (guiBuilder.getGameLogic().getGameScore().isAllCardsDealt()) {
            bTrump.setColor(Color.DARK_GRAY);
            bTrump.getColor().a = 1f;
        }
    }

    public void initHand(){
        Card card = guiBuilder.getGameLogic().getShufflerCards().getTrump();
        if(card!=null && bTrump!=null) bTrump.getStyle().up = new TextureRegionDrawable(guiBuilder.getGameLogic().getShufflerCards().getTrump().getTrCard());
    }

    public Table createDeckLayer() {
        // Modificar escalado del tamaño del mazo (NO TOCAR EL RESTO)
        float scaleDeck = TOTAL_PLAYERS == 8 ? 1.9f : 1.2f;
        float widthBack = (W*0.2f)/scaleDeck;
        float heightBack = (H*0.17f)/scaleDeck;//(H*0.16f)/scaleDeck;

        TextureRegionDrawable trd = guiBuilder.getGameLogic().getShufflerCards().getTrump()==null
                ? new TextureRegionDrawable(AssetLoader.aLoader.aImage.getSmallBlackSquare())
                : new TextureRegionDrawable(guiBuilder.getGameLogic().getShufflerCards().getTrump().getTrCard());

        bTrump = new Button(trd);
        bTrump.setTransform(false);
        bTrump.setRotation(-15f);
        bTrump.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Config.TEST) Config.TIME_IA = Config.TIME_IA == 0 ? 1000 : 0;
            }
        });
        Table tTrump = new Table();
        tTrump.setDebug(false);
        tTrump.add(bTrump).size(widthBack,heightBack);

        Table table = new Table();
        table.setDebug(false);
        table.add(tTrump).expandX().expandY().top().left().padTop(GUIBuilder_Score.SIZE_SCORE[1]*1.05f).padLeft(W*0.01f);
        return table;
    }

//    public void render(){
//        int hP = guiBuilder.getGameLogic().getGameData().getHandPlayer();
//        if (guiBuilder.getGameScreen().getGameMode() == Constants.GameMode.ONLINE) {
//            hP = guiBuilder.getGameLogic().getGameData().getPlayerPositionAboutGUI(hP);
//        }
//
//        Cell cell = tDecks.getCell(aDecks[hP]);
//        if(cell!=null){
//            aDecks[hP] = createDeckCard();
//            cell.setActor(aDecks[hP]);
//        }
//
//        for(int i = 0; i<guiBuilder.getGameScreen().getTOTAL_PLAYERS(); i++){
//            if (DEBUG_SHOW) aDecks[i].setVisible(true);
//            else aDecks[i].setVisible(hP==i);
//
//            if (guiBuilder.getGameLogic().getGameScore().isAllCardsDealt()) {
//                tCardBack.setVisible(false);
//                aDecks[i].setColor(Color.DARK_GRAY);
//                aDecks[i].getColor().a = 0.5f;
//            }
//        }
//    }
//
//    public void initHand(){
//        Card card = guiBuilder.getGameLogic().getShufflerCards().getTrump();
//        if(card!=null && bTrump!=null) bTrump.getStyle().up = new TextureRegionDrawable(guiBuilder.getGameLogic().getShufflerCards().getTrump().getTrCard());
    }

//    public Table createDeckLayer(){
//        for (int i=0; i<TOTAL_PLAYERS; i++)
//            aDecks[i] = createDeckCard();
//
//        if (TOTAL_PLAYERS == 3) return getDeck3Players();
//        else if (TOTAL_PLAYERS == 4) return getDeck4Players();
////        else if (TOTAL_PLAYERS == 5) return getDeck5Players();
////        else if (numPlayers == 6) return getDeck6Players();
////        else if (numPlayers == 7) return getDeck7Players();
////        else if (numPlayers == 8) return getDeck8Players();
////        else if (numPlayers == 9) return getDeck9Players();
////        else if (numPlayers == 10) return getDeck10Players();
//        else {
//            Gdx.app.error(TAG, "---> TODO: Make deckPlayer for "+TOTAL_PLAYERS);
//            return getDeck4Players();
//        }
//    }
//
//    private Table getDeck3Players() {
//        float width = W*0.05f;
//        float height = H*0.04f;
//        tDecks = new Table();
//        tDecks.setDebug(true);
//        tDecks.top();
//        tDecks.add(new Actor()).expandX().padTop(H*0.26f).colspan(3); //Cell 0
//        tDecks.row();
//        tDecks.add(aDecks[2]).size(width,height);                   //Cell 1
//        tDecks.add(new Actor()).size((width)*2,height);        //Cell 2
//        tDecks.add(new Actor()).size(width,height);                    //Cell 3
//        tDecks.row();
//        tDecks.add(new Actor()).expandX().height(H*0.37f).colspan(3); //Cell 4
//        tDecks.row();
//        tDecks.add(aDecks[0]).size(width,height);                    //Cell 5
//        tDecks.add(new Actor()).size((width)*2,height);        //Cell 6
//        tDecks.add(aDecks[1]).size(width,height);                    //Cell 7
//
//        return tDecks;
//    }
//
//    private Table getDeck4Players() {
//        float width = W*0.05f;
//        float height = H*0.04f;
//        tDecks = new Table();
//        tDecks.setDebug(false);
//        tDecks.top();
//        tDecks.add(new Actor()).expandX().padTop(H*0.26f).colspan(3); //Cell 0
//        tDecks.row();
//        tDecks.add(aDecks[3]).size(width,height);                    //Cell 1
//        tDecks.add(new Actor()).size((width)*2,height);        //Cell 2
//        tDecks.add(aDecks[2]).size(width,height);                    //Cell 3
//        tDecks.row();
//        tDecks.add(new Actor()).expandX().height(H*0.37f).colspan(3); //Cell 4
//        tDecks.row();
//        tDecks.add(aDecks[0]).size(width,height);                    //Cell 5
//        tDecks.add(new Actor()).size((width)*2,height);        //Cell 6
//        tDecks.add(aDecks[1]).size(width,height);                    //Cell 7
//        return tDecks;
//    }

//    public Table createDeckLayerAUX() {
//        Stack stack = new Stack();
//        stack.add(table);
//        return stack;
//
//        for (int i=0; i<TOTAL_PLAYERS; i++)
//            aDecks[i] = createDeckAUX(i);
//
//        stackDecks = new Stack();
//        stackDecks.setDebug(false);
//        for (int i=0; i<TOTAL_PLAYERS; i++) {
//            stackDecks.add(addDeck(i));
//        }
//
//        return stackDecks;
//    }

//    private Table addDeck(int index) {
//        float padBottom, padRight, padTop, padLeft;
//        float paddingBottom_Me = GUIBuilder_Cards.SIZE_CARDS[1];
//        float paddingTop_Tops = GUIBuilder_Score.SIZE_SCORE[1]*1.60f;
//
//        PlayerAlignment alignment = guiBuilder.getGuiBuilderPlayers().getPlayerIcon(index).getAlignment();
//        float[] padding = guiBuilder.getGuiBuilderPlayers().getPlayerPadding(index);
//
//        Table tAux = new Table();
//        tAux.setDebug(true);
//        switch (alignment) {
//            case Bottom:
//                padRight = W*0.6f;
//                tAux.add(aDecks[index]).expandX().expandY().bottom().padBottom(paddingBottom_Me).padRight(padRight);
//                break;
//            case Right:
//                padBottom = padding[0]-H*0.3f;
//                padRight = padding[1]+W*0.03f;
//                tAux.add(aDecks[index]).expandX().expandY().right().padBottom(padBottom).padRight(padRight);
//                break;
//            case TopRight:
//                padRight = padding[1];
//                padTop = padding[2]+H*0.06f;
//                padLeft = padding[3]+W*0.45f;
//                tAux.add(aDecks[index]).expandX().expandY().top().padRight(padRight).padTop(padTop).padLeft(padLeft);
//                break;
//            case TopCenter:
//                padTop = padding[2]+H*0.06f;
//                padLeft = padding[3]+W*0.55f;
//                tAux.add(aDecks[index]).expandX().expandY().top().padTop(padTop).padLeft(padLeft);
//                break;
//            case TopLeft:
//                padTop = padding[2]+H*0.06f;
//                padLeft = -padding[1]+W*0.45f; // Pad Right
//                tAux.add(aDecks[index]).expandX().expandY().top().padTop(padTop).padLeft(padLeft);
//                break;
//            case Left:
//                padBottom = padding[0]+H*0.3f;
//                padLeft = padding[3]+W*0.03f;
//                tAux.add(aDecks[index]).expandX().expandY().left().padBottom(padBottom).padLeft(padLeft);
//                break;
//        }
//
//        return tAux;
//    }
//
//    private Stack createDeckAUX(int index) {
//        float scaleDeck = 1.2f; // <- Modificar escalado del tamaño del mazo (NO TOCAR EL RESTO)
//        float widthBack = (W*0.2f)/scaleDeck;
//        float heightBack = (H*0.16f)/scaleDeck;
//        float scaleTrump = 1.1f;
//        float widthTrumpCard = widthBack/scaleTrump;
//        float heightTrumpCard = heightBack/scaleTrump;
//        float padBottom = (H*0.008f)/scaleDeck;
//        float padRight = (W*0.02f)/scaleDeck;
//
//        Button bDeck = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getDeck()));
//        rotateButton(bDeck);
//        tCardBack = new Table();
//        tCardBack.setDebug(false);
//        tCardBack.add(bDeck).size(widthBack, heightBack);
//
//        TextureRegionDrawable trd;
//        if(guiBuilder.getGameLogic().getShufflerCards().getTrump()==null) trd = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getArAux());
//        else trd = new TextureRegionDrawable(guiBuilder.getGameLogic().getShufflerCards().getTrump().getTrCard());
//        Button bTrump = new Button(trd);
//        rotateButton(bTrump);
//        Table tTrump = new Table();
//        tTrump.setDebug(false);
//        tTrump.add(bTrump).size(widthTrumpCard,heightTrumpCard).padBottom(padBottom).padRight(padRight);
//
//        sDeck = new Stack();
//        sDeck.setDebug(false);
//        sDeck.add(tCardBack);
//        sDeck.add(tTrump);
//        return sDeck;
//    }
//
//    private Stack createDeckCard(){ //Junta la imagen del montón con la imagen de la pinte
//        Button bDeck = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getDeck()));
//        rotateButton(bDeck);
//        tCardBack = new Table();
//        tCardBack.setDebug(false);
//        tCardBack.add(bDeck).size(SIZE_DECK[0],SIZE_DECK[1])
//                .padBottom(H*0.05f).padLeft(W*0.05f);
//
//        TextureRegionDrawable trd;
//        if(guiBuilder.getGameLogic().getShufflerCards().getTrump()==null) trd = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getArAux());
//        else trd = new TextureRegionDrawable(guiBuilder.getGameLogic().getShufflerCards().getTrump().getTrCard());
//        bTrump = new Button(trd);
//        rotateButton(bTrump);
//        Table tTrump = new Table();
//        tTrump.setDebug(false);
//        tTrump.add(bTrump).size(SIZE_TRUMP[0],SIZE_TRUMP[1])
//                .padBottom(H*0.06f).padLeft(W*0.04f);
//
//        sDeck = new Stack();
//        sDeck.setDebug(false);
//        sDeck.add(tCardBack);
//        sDeck.add(tTrump);
//        return sDeck;
//    }
//
//    private void rotateButton(Button button){
//        button.setTransform(false);
//        button.setRotation(-15f);
//    }

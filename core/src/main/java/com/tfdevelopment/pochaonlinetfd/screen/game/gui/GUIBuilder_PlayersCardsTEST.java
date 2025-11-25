package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.player.PochaPlayer;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Phase;

import java.util.ArrayList;

public class GUIBuilder_PlayersCardsTEST {
    private static final String TAG = GUIBuilder_PlayersCardsTEST.class.getName();
    private static final float W = Constants.VIEWPORT_WIDTH;
    private static final float H = Constants.VIEWPORT_HEIGHT;
    public static final float SIZE_CARDS[] =       {W*0.14f,    H*0.12f};

    private GUIBuilder guiBuilder;
    private Stack stack; //Pila con todas las tablas
    private ArrayList<Button>[] alCards;

    private boolean flagUpdateTableCards;

    public GUIBuilder_PlayersCardsTEST(GUIBuilder guiBuilder){
        this.guiBuilder = guiBuilder;
        this.flagUpdateTableCards = false;

        alCards = new ArrayList[guiBuilder.getGameScreen().getTOTAL_PLAYERS()];
        //Crea todos los botones de cartas posibles
        for(int j = 0; j<guiBuilder.getGameScreen().getTOTAL_PLAYERS(); j++){
            alCards[j] = new ArrayList<>();
            for(int i = 0; i<guiBuilder.getGameScreen().getMAX_CHANCES(); i++){
                Button bCard = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getSmallBlackSquare()));
                alCards[j].add(bCard);
            }
        }
    }

    public void render(float delta) {
        if (flagUpdateTableCards) {
            flagUpdateTableCards = false;
            updateTableCards();
        }
    }

    public void initHand(){
        Gdx.app.log(TAG, "*Init HAND*");

        //Establece la imagen a cada carta del jugador
        for(int j = 0; j<guiBuilder.getGameScreen().getTOTAL_PLAYERS(); j++) {
            PochaPlayer player = guiBuilder.getGameLogic().getPlayers()[j];

            for (int i = 0; i < player.getAlCards().size(); i++) {
                alCards[j].get(i).getStyle().up = new TextureRegionDrawable(player.getAlCards().get(i).getTrCard());
                alCards[j].get(i).setVisible(true);
                alCards[j].get(i).setColor(Color.WHITE);
                alCards[j].get(i).getColor().a = 1f;
            }
        }
    }

    //Permite mostrar el número de cartas correspondientes según la baza
    private void updateTableCards(){ //Run in main thread !!
        stack.clear();
        Table tPlayers = new Table();
        tPlayers.setDebug(true);
        tPlayers.top().left();
        for(int i = 0; i<guiBuilder.getGameScreen().getTOTAL_PLAYERS(); i++){
            tPlayers.add(createIndexTableLayers(i));
            tPlayers.row();
        }
        stack.add(tPlayers);
    }

    public void updateCardsFromReload() {
        Phase phase = guiBuilder.getGameLogic().getGameData().getPhase();

        if (phase == Phase.BET) {
            //Establece la imagen a cada carta del jugador
            for(int j = 0; j<guiBuilder.getGameScreen().getTOTAL_PLAYERS(); j++) {
                PochaPlayer player = guiBuilder.getGameLogic().getPlayers()[j];

                for (int i = 0; i < player.getAlCards().size(); i++) {
                    alCards[j].get(i).getStyle().up = new TextureRegionDrawable(player.getAlCards().get(i).getTrCard());
                    alCards[j].get(i).setVisible(true);
                }
            }
        }

        if (phase == Phase.CARD) {
            //Establece la imagen a cada carta del jugador
            for(int j = 0; j<guiBuilder.getGameScreen().getTOTAL_PLAYERS(); j++) {
                PochaPlayer player = guiBuilder.getGameLogic().getPlayers()[j];

                for (int i = 0; i < player.getAlCards().size(); i++) {
                    alCards[j].get(i).getStyle().up = new TextureRegionDrawable(player.getAlCards().get(i).getTrCard());
                    alCards[j].get(i).setVisible(true);
                }
            }
        }

        if (phase == Phase.CONTINUE) {
            //Establece la imagen a cada carta del jugador
            for(int j = 0; j<guiBuilder.getGameScreen().getTOTAL_PLAYERS(); j++) {
                PochaPlayer player = guiBuilder.getGameLogic().getPlayers()[j];

                for (int i = 0; i < player.getAlCards().size(); i++) {
                    alCards[j].get(i).getStyle().up = new TextureRegionDrawable(player.getAlCards().get(i).getTrCard());
                    alCards[j].get(i).setVisible(true);
                    alCards[j].get(i).setColor(Color.DARK_GRAY);
                }
            }
        }

        updateTableCards();
    }

    public Stack createCardsLayer(){
        Table tPlayers = new Table();
        tPlayers.setDebug(true);
        tPlayers.top().left();
        for(int i = 0; i<guiBuilder.getGameScreen().getTOTAL_PLAYERS(); i++){
            tPlayers.add(createIndexTableLayers(i));
            tPlayers.row();
        }

        //Crea la pila y la correspondiente tabla de cartas
        stack = new Stack();
        stack.setDebug(false);
        stack.setVisible(false);
        stack.addActor(tPlayers);
        return stack;
    }

    private Table createIndexTableLayers(int index){
        float PAD_LEFT = W*0.15f;
        int iP = index;

        int numCards = guiBuilder.getGameLogic().getGameScore().getNumChancesHand();
        for (int i = 0; i < numCards; i++) {
            alCards[index].get(i).getStyle().up = new TextureRegionDrawable(getTRCard(iP, i));
        }

        Table table = new Table();
        table.setDebug(false);
        table.setFillParent(false);
        table.setVisible(true);

        Stack stack = new Stack();
        for(int i=0; i<=(numCards-1); i++){
            stack.add(getNewTable(index, i).padLeft(PAD_LEFT*i));
        }
        table.top().left();
        table.add(stack).size(SIZE_CARDS[0],SIZE_CARDS[1]);

        return table;
    }

    private Table getNewTable(int indexPlayer, int indexButton){
        Table table = new Table();
        table.add((alCards[indexPlayer].get(indexButton))).size(SIZE_CARDS[0],SIZE_CARDS[1]);
        return table;
    }

    private TextureRegion getTRCard(int player, int indexCard){
        Card card = guiBuilder.getGameLogic().getPlayers()[player].getCard(indexCard);
        if(card!=null) return card.getTrCard();
        return AssetLoader.aLoader.aImage.getSmallBlackSquare();
    }

    public void setVisible(){ this.stack.setVisible(!stack.isVisible()); }

    /***** Thread Button Event *****/
    public void setFlagUpdateTableCards(boolean flag){ this.flagUpdateTableCards = flag; }
    public void selectCard(final int player, final int card, final boolean selected) {
        //Modify GUI from the main thread
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Color color = selected ? Color.DARK_GRAY : Color.WHITE;
                alCards[player].get(card).setColor(color);
            }
        });
    }
}

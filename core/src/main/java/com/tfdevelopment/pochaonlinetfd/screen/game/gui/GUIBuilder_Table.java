package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.card.ShufflerCards;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.player.PochaPlayer;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.layout.PlayerIcon;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.layout.PlayerIcon.PlayerAlignment;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.GameMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class GUIBuilder_Table {
    private static final String TAG = GUIBuilder_Table.class.getName();
    private static final boolean DEBUG_SHOW = false;
    private static final float W = Constants.VIEWPORT_WIDTH;
    private static final float H = Constants.VIEWPORT_HEIGHT;
    private static final float ANIMATION_DURATION = 0.2f;
    private static final float SIZE_CARDS[] = {W*0.14f, H*0.12f};
    private static final float[] RANDOM_DEGREES = new float[]{-9,-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,9};
    private int TOTAL_PLAYERS;

    private GUIBuilder guiBuilder;
    private Button[] bCards;
    //Evita que se vea la carta en la mesa antes de empezar la animación
    private boolean[] bActions;
    //Permite girar ligeramente la carta al echarla a la mesa
    private float[] fDegrees;
    private boolean[] showCardsInReloading;

//    private int animationPlayer = -1;
    private ArrayList<Integer> animationPlayers;

    public GUIBuilder_Table(GUIBuilder guiBuilder) {
        this.guiBuilder = guiBuilder;
        this.TOTAL_PLAYERS = guiBuilder.getGameScreen().getTOTAL_PLAYERS();
        this.bCards = new Button[TOTAL_PLAYERS];
        this.bActions = new boolean[TOTAL_PLAYERS];
        this.fDegrees = new float[TOTAL_PLAYERS];
        this.showCardsInReloading = new boolean[TOTAL_PLAYERS];
        this.animationPlayers = new ArrayList<>();
        Arrays.fill(bActions, false);
        Arrays.fill(fDegrees, 0f);
    }

    public void render(float delta){
        setTextureRegionToCards();

        //Oculta la carta en la mesa hasta "empezar" animación
        for(int i = 0; i<TOTAL_PLAYERS; i++){
            if(bActions[i]){
                bActions[i] = false;
                bCards[i].setVisible(true);
            }
        }

        Iterator<Integer> iterator = animationPlayers.iterator();
        while (iterator.hasNext()) {
            makeAnimationCard(iterator.next());
            iterator.remove();
        }

//        if (animationPlayer != -1) {
//            makeAnimationCard();
//            animationPlayer = -1;
//        }
    }

    /***** Thread Button Event AND Main Thread*****/
    public void initChance(){
        Gdx.app.log(TAG, "*Init CHANCE*");
        for(int i = 0; i<TOTAL_PLAYERS; i++){
            if(bCards[i]!=null){
                if (!DEBUG_SHOW) bCards[i].setVisible(false);
                bCards[i].getStyle().up = getNullCard();
                bCards[i].setRotation(-fDegrees[i]);
                fDegrees[i] = 0f;
            }
        }
    }


    public void makeAnimationCard(int animationPlayer){
        int iPlayer = animationPlayer;
//        int iPlayer = this.animationPlayer;

        if (guiBuilder.getGameScreen().getGameMode() == GameMode.ONLINE) {
            iPlayer = guiBuilder.getGameLogic().getGameData().getPlayerPositionAboutGUI(iPlayer);
        }

        bCards[iPlayer].setOrigin(bCards[iPlayer].getWidth()/2,bCards[iPlayer].getHeight()/2);
        bCards[iPlayer].addAction(Actions.rotateBy(180f));
        bCards[iPlayer].addAction(Actions.rotateBy(-180f, ANIMATION_DURATION));

        PlayerAlignment alignment = guiBuilder.getGuiBuilderPlayers().getPlayerIcon(iPlayer).getAlignment();
        switch (alignment) {
            case Bottom:
                bCards[iPlayer].addAction(Actions.moveBy(0,-150));
                bCards[iPlayer].addAction(Actions.moveBy(0,150, ANIMATION_DURATION));
                break;
            case Right:
                bCards[iPlayer].addAction(Actions.moveBy(80,0));
                bCards[iPlayer].addAction(Actions.moveBy(-80,0, ANIMATION_DURATION));
                break;
            case TopLeft: case TopCenter: case TopRight:
                bCards[iPlayer].addAction(Actions.moveBy(0, 100));
                bCards[iPlayer].addAction(Actions.moveBy(0, -100, ANIMATION_DURATION));
                break;
            case Left:
                bCards[iPlayer].addAction(Actions.moveBy(-80,0));
                bCards[iPlayer].addAction(Actions.moveBy(80,0, ANIMATION_DURATION));
                break;
            default:
                Gdx.app.error(TAG, "---> TODO: Make animation of "+iPlayer);
                bCards[iPlayer].addAction(Actions.moveBy(0,-150));
                bCards[iPlayer].addAction(Actions.moveBy(0,150, ANIMATION_DURATION*3));
                break;
        }

        int indexDegree = new Random().nextInt(RANDOM_DEGREES.length);
        bCards[iPlayer].setRotation(RANDOM_DEGREES[indexDegree]);
        fDegrees[iPlayer] = RANDOM_DEGREES[indexDegree];
        bActions[iPlayer] = true;
    }

    public void updateCardsFromReload() {
        for (int i=0; i<TOTAL_PLAYERS; i++) {
            GameLogic gameLogic = guiBuilder.getGameLogic();
            PochaPlayer pochaPlayer = gameLogic.getPlayers()[i];
            ShufflerCards shufflerCards = gameLogic.getShufflerCards();
            int selected = pochaPlayer.getSelectedCard();

            int indexPlayer = i;
            if (guiBuilder.getGameScreen().getGameMode() == GameMode.ONLINE) {
                indexPlayer = gameLogic.getGameData().getPlayerPositionAboutGUI(indexPlayer);
            }

            if (selected != -1) {
                Card card = shufflerCards.getAlAllCards().get(selected);
                TextureRegionDrawable trd = new TextureRegionDrawable(card.getTrCard());
                bCards[indexPlayer].getStyle().up = new TextureRegionDrawable(trd);
                bCards[indexPlayer].setVisible(true);
            } else bCards[indexPlayer].setVisible(false);
        }
    }

    public Table createTableLayer() {
        final float FIX_PADDING_BOTTOM_SIDES = H*0.1f;
        Stack stack = new Stack();

        PlayerIcon[] playerIcons = guiBuilder.getGuiBuilderPlayers().getPlayerIcons();

        for (int index=0; index<TOTAL_PLAYERS; index++) {
            PlayerAlignment alignment = playerIcons[index].getAlignment();
            float[] padding = guiBuilder.getGuiBuilderPlayers().getPlayerPadding(index);
            float paddingBottomSides = padding[0] - FIX_PADDING_BOTTOM_SIDES;
            float paddingSidesTop = padding[1] != 0f ? padding[1] : padding[3];
            paddingSidesTop -= W*0.1f;

            bCards[index] = new Button(getNullCard());
            bCards[index].setVisible(DEBUG_SHOW);
            bCards[index].setTransform(true);

            Table tCard = new Table();
            tCard.setDebug(false);
            Cell cell = tCard.add(bCards[index]).size(SIZE_CARDS[0],SIZE_CARDS[1]);
            if (alignment == PlayerAlignment.Bottom) {
                tCard.bottom();
                if (TOTAL_PLAYERS == 6) cell.padBottom(-H*0.02f);
                if (TOTAL_PLAYERS == 7) cell.padBottom(-H*0.02f);
                if (TOTAL_PLAYERS == 8) cell.padBottom(-H*0.02f);
            }

            if (alignment == PlayerAlignment.Right) {
                tCard.right();
                cell.padBottom(paddingBottomSides);
                if (TOTAL_PLAYERS == 6 && index == 1) cell.padBottom(paddingBottomSides+H*0.11f);
                if (TOTAL_PLAYERS == 6 && index == 2) cell.padBottom(paddingBottomSides-H*0.09f);
                if (TOTAL_PLAYERS == 7 && index == 1) cell.padBottom(paddingBottomSides+H*0.09f);
                if (TOTAL_PLAYERS == 7 && index == 2) cell.padBottom(paddingBottomSides-H*0.09f);
                if (TOTAL_PLAYERS == 8 && index == 1) cell.padBottom(paddingBottomSides+H*0.03f);
                if (TOTAL_PLAYERS == 8 && index == 2) cell.padBottom(paddingBottomSides-H*0.14f).padRight(W*0.03f);
            }

            if (alignment == PlayerAlignment.TopRight) {
                tCard.top();
                cell.padLeft(paddingSidesTop);
                if (TOTAL_PLAYERS == 7) cell.padLeft(paddingSidesTop-W*0.05f);
                if (TOTAL_PLAYERS == 8) cell.padTop(cell.getPadTop()+H*0.02f).padLeft(paddingSidesTop-W*0.1f);
            }

            if (alignment == PlayerAlignment.TopCenter) {
                tCard.top();
            }

            if (alignment == PlayerAlignment.TopLeft) {
                tCard.top();
                cell.padRight(paddingSidesTop);
                if (TOTAL_PLAYERS == 7) cell.padRight(paddingSidesTop-W*0.05f);
                if (TOTAL_PLAYERS == 8) cell.padTop(cell.getPadTop()+H*0.02f).padRight(paddingSidesTop-W*0.1f);
            }

            if (alignment == PlayerAlignment.Left) {
                tCard.left();
                cell.padBottom(paddingBottomSides);
                if (TOTAL_PLAYERS == 6 && index == 4) cell.padBottom(paddingBottomSides-H*0.09f);
                if (TOTAL_PLAYERS == 6 && index == 5) cell.padBottom(paddingBottomSides+H*0.11f);
                if (TOTAL_PLAYERS == 7 && index == 5) cell.padBottom(paddingBottomSides-H*0.09f);
                if (TOTAL_PLAYERS == 7 && index == 6) cell.padBottom(paddingBottomSides+H*0.09f);
                if (TOTAL_PLAYERS == 8 && index == 6) cell.padBottom(paddingBottomSides-H*0.14f).padLeft(W*0.03f);
                if (TOTAL_PLAYERS == 8 && index == 7) cell.padBottom(paddingBottomSides+H*0.03f);
            }
            stack.add(tCard);
        }

        Table tTable = new Table();
        tTable.setDebug(false);
        tTable.top().padTop(H*0.26f);
        tTable.add(stack).size(W*0.45f, H*0.39f);

        return tTable;
    }

    private void setTextureRegionToCards() {
        GameLogic gameLogic = guiBuilder.getGameLogic();
        ArrayList<Card> alTableCard = gameLogic.getGameData().getAlCardsTable();

        //Establece a cada carta de la tabla su imagen correspondiente
        int iPlayerDeck = gameLogic.getGameData().getFirstPlayer();
        if (guiBuilder.getGameScreen().getGameMode() == GameMode.ONLINE) {
            iPlayerDeck = gameLogic.getGameData().getPlayerPositionAboutGUI(iPlayerDeck);
        }
        for(int i=0;i<alTableCard.size(); i++){
            TextureRegionDrawable trd = new TextureRegionDrawable(alTableCard.get(i).getTrCard());
            bCards[iPlayerDeck].getStyle().up = new TextureRegionDrawable(trd);
            iPlayerDeck = gameLogic.getGameData().getNextPlayer(iPlayerDeck);
        }
    }

    private TextureRegionDrawable getNullCard(){
        return new TextureRegionDrawable(AssetLoader.aLoader.aImage.getSmallBlackSquare());
    }

    /*** Getters And Setters ***/
    public Button[] getbCards(){ return bCards; }
    public void addAnimationPlayer(int indexPlayer) { this.animationPlayers.add(indexPlayer); }
//    public void setAnimationPlayer(int indexPlayer){ this.animationPlayer = indexPlayer; } //ThreadBE
}

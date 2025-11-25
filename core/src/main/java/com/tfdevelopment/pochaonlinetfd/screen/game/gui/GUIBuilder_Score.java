package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.model.game.GameData;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.game.GameScore;
import com.tfdevelopment.pochaonlinetfd.model.player.PochaPlayer;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Phase;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

import java.util.ArrayList;

public class GUIBuilder_Score {
    private static final String TAG = GUIBuilder_Score.class.getName();
    private static final float W = Constants.VIEWPORT_WIDTH;
    private static final float H = Constants.VIEWPORT_HEIGHT;
    public static final float SIZE_SCORE[] = {W, H*0.1f};
    private static final int NUM_ROUNDS_SHOW = 2;
    private final int TOTAL_PLAYERS, TOTAL_HANDS;

    private GUIBuilder guiBuilder;

    private Label lNamePlayers[];
    private Label lPointsPlayers[][];
    private Label lNRound[];
    private ScrollPane spPoints;

    private int coloredPlayer;

    public GUIBuilder_Score(GUIBuilder guiBuilder) {
        this.guiBuilder = guiBuilder;
        this.TOTAL_PLAYERS = guiBuilder.getGameScreen().getTOTAL_PLAYERS();
        this.TOTAL_HANDS = guiBuilder.getGameScreen().getTOTAL_HANDS();

        this.lNamePlayers = new Label[TOTAL_PLAYERS];
        this.lPointsPlayers = new Label[NUM_ROUNDS_SHOW][TOTAL_PLAYERS];
        this.lNRound = new Label[NUM_ROUNDS_SHOW];
    }

    public void render(float delta) {
        GameLogic gameLogic = guiBuilder.getGameLogic();
        GameData gameData = gameLogic.getGameData();
        GameScore gameScore = gameLogic.getGameScore();
        ArrayList<Integer> winnerPlayers = gameScore.getWinnerPlayerIndex();
        PochaPlayer[] pochaPlayers = gameLogic.getPlayers();
        int currentPlayer = gameData.getCurrentPlayer();
        String winnerColor = "[#D4DB00]";

        // Color current player
        if (gameLogic.getGameData().getPhase() != Phase.EXIT && coloredPlayer != currentPlayer) {
            coloredPlayer = currentPlayer;
            for (int i = 0; i < TOTAL_PLAYERS; i++) {
                if (i == currentPlayer) lNamePlayers[i].setText("[#2799FF]" + pochaPlayers[i].getName() + "[#2799FF]");
                else lNamePlayers[i].setText("[WHITE]" + pochaPlayers[i].getName() + "[WHITE]");
            }
        }

        // Winner player
        if(gameLogic.getGameData().getPhase() == Phase.EXIT) {
            for (int i = 0; i < TOTAL_PLAYERS; i++) {
                if (winnerPlayers.contains(i)) lNamePlayers[i].setText(winnerColor + pochaPlayers[i].getName() + winnerColor);
                else lNamePlayers[i].setText("[WHITE]" + pochaPlayers[i].getName() + "[WHITE]");
            }
        }

        // Automatic movement of scrollpane
        if (!spPoints.isTouchFocusListener() && currentPlayer != -1) {
            int index = currentPlayer <= 2 ? 0 : currentPlayer;
            float percentX = currentPlayer == (TOTAL_PLAYERS - 1) ? 1 : (1 / (float) TOTAL_PLAYERS) * index;
            spPoints.setScrollPercentX(percentX);
        }

        //Actualiza el marcador
        int[][][] score = gameScore.getScoreboard();
        int numHands = gameScore.getNumHands();
        if (numHands == 1) {
            lNRound[0].setText(numHands);
            lNRound[1].setText(numHands+1);
            for (int i = 0; i < TOTAL_PLAYERS; i++) {
                lPointsPlayers[0][i].setText(score[numHands - 1][i][1]);
                lPointsPlayers[1][i].setText("-");
            }
        } else if (numHands == 2) {
            lNRound[0].setText(numHands - 1);
            lNRound[1].setText(numHands);
            for (int i = 0; i < TOTAL_PLAYERS; i++) {
                if (gameLogic.getGameData().getPhase() == Phase.CONTINUE) {
                    lPointsPlayers[0][i].setText(score[numHands - 2][i][1]);
                    lPointsPlayers[1][i].setText(score[numHands - 2][i][1] + "   " + score[numHands - 1][i][1]);
                } else {
                    lPointsPlayers[0][i].setText(score[numHands - 2][i][1]);
                    lPointsPlayers[1][i].setText(score[numHands - 2][i][1] + "   " + "-");
                }
            }
        } else if(gameLogic.getGameData().getPhase()==Phase.EXIT){
            lNRound[0].setText(numHands);
            lNRound[1].setText(" FINAL ");
            for(int i=0; i<TOTAL_PLAYERS;i++){
                lPointsPlayers[0][i].setText(score[numHands-2][i][0]+"   "+score[numHands-1][i][1]);
                if(winnerPlayers.contains(i)) lPointsPlayers[1][i].setText(winnerColor+score[numHands-1][i][0]+"[WHITE]");
                else lPointsPlayers[1][i].setText("[WHITE]"+score[numHands-1][i][0]+"[WHITE]");
            }
        } else {
            for(int i=0; i<TOTAL_PLAYERS;i++){
                if(gameLogic.getGameData().getPhase()==Phase.CONTINUE) {
                    lNRound[0].setText(numHands);
                    lNRound[1].setText(numHands+1);
                    lPointsPlayers[0][i].setText(score[numHands-2][i][0]+"   "+score[numHands-1][i][1]);
                    lPointsPlayers[1][i].setText(score[numHands-1][i][0]+"   -");
                } else {
                    lNRound[0].setText(numHands-1);
                    lNRound[1].setText(numHands);
                    lPointsPlayers[0][i].setText(score[numHands-3][i][0]+"   "+score[numHands-2][i][1]);
                    lPointsPlayers[1][i].setText(score[numHands-2][i][0]+"   -");
                }
            }
        }
    }

    public Stack createScoreLayer() {
        Stack stack = new Stack();
        stack.add(getBackgroundTable());
        stack.add(getScoreTable());

        return stack;
    }

    private Table getBackgroundTable() {
        Button bScore = new Button(StaticsMethods.getNinePatchDrawable(AssetLoader.aLoader.aImage.getBlackboard(), 10));
        Table tBackground = new Table();
        tBackground.setDebug(false);
        tBackground.top();
        tBackground.add(bScore).size(SIZE_SCORE[0], SIZE_SCORE[1]);
        return tBackground;
    }

    private Table getScoreTable() {
        float ROUND_WIDTH = W*0.14f; //W*0.12f;
        float POINTS_WIDTH = W*0.87f;

        Table tScore = new Table();
        tScore.setDebug(false);
        tScore.add(getRoundTable(ROUND_WIDTH));
        tScore.add(getPointsTable(POINTS_WIDTH)).expandX();

        Table tScoreSize = new Table(); //Permite que el marcador no ocupe to-do el ancho de la pantalla
        tScoreSize.top().padTop(H*0.005f);
        tScoreSize.add(tScore).width(W*0.98f);
        return tScoreSize;
    }

    private Table getRoundTable(float tableWidth) {
        Label lRound = createLabelTitle("Nº ("+TOTAL_HANDS+")");
        for(int i=0; i<NUM_ROUNDS_SHOW; i++){
            lNRound[i] = createLabelData("-");
        }

        Table tRound = new Table();
        tRound.setDebug(false);
        tRound.top().left();
        tRound.add(lRound).width(tableWidth);
        tRound.row();
        for(int i=0; i<NUM_ROUNDS_SHOW; i++){
            tRound.add(lNRound[i]).width(tableWidth);
            tRound.row();
        }
        return tRound;
    }

    private Table getPointsTable(float tableWidth) {
        float columndWidth = tableWidth / 4.2f;

        for(int i=0; i<TOTAL_PLAYERS; i++){
            lNamePlayers[i] = createLabelTitle("");
            for(int j=0; j<NUM_ROUNDS_SHOW; j++){
                lPointsPlayers[j][i] = createLabelData("-");
            }
        }

        Table tPoints = new Table();
        tPoints.setDebug(false);
        tPoints.top().left();
        for(int i=0; i<TOTAL_PLAYERS; i++){
            if (TOTAL_PLAYERS <= 4) tPoints.add(lNamePlayers[i]).expandX();
            else tPoints.add(lNamePlayers[i]).width(columndWidth);
        }
        tPoints.row();
        for(int i=0; i<NUM_ROUNDS_SHOW; i++){
            for(int j=0; j<TOTAL_PLAYERS; j++){
                if (TOTAL_PLAYERS <= 4) tPoints.add(lPointsPlayers[i][j]).expandX();
                else tPoints.add(lPointsPlayers[i][j]).width(columndWidth);
            }
            tPoints.row();
        }

        spPoints = new ScrollPane(tPoints, StyleConfigurator.getSPS_WithoutBackground());
        spPoints.setVisible(true);
        spPoints.setScrollingDisabled(false, true);
        spPoints.setFadeScrollBars(true);
        spPoints.setOverscroll(false, false);
        spPoints.layout();

        Table tScrollPane = new Table();
        tScrollPane.setDebug(false);
        tScrollPane.add(spPoints).width(tableWidth);

        return tScrollPane;
    }

    private Label createLabelTitle(String text){
        Label label = new Label(text, StyleConfigurator.getLS_Maiandra50());
        label.setAlignment(Align.center);
        label.setColor(Color.WHITE);

        return label;
    }

    private Label createLabelData(String text){
        Label label = new Label(text, StyleConfigurator.getLS_Maiandra50());
        label.setAlignment(Align.center);
        label.setColor(Color.WHITE);

        return label;
    }
}

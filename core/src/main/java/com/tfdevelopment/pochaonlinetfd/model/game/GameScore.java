package com.tfdevelopment.pochaonlinetfd.model.game;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class GameScore {
    private static final String TAG = GameScore.class.getName();
    private final int TOTAL_PLAYERS, TOTAL_HANDS, MAX_CHANCES;

    private GameLogic gameLogic;
    private int numHands; //Número actual de la mano
    private int numChance; //Número actual de la baza
    private int numChancesHand; //Número de bazas en dicha mano

    private int[][][] scoreboard;

    public GameScore(GameLogic gameLogic){
        this.gameLogic = gameLogic;
        this.TOTAL_PLAYERS = gameLogic.getGameScreen().getTOTAL_PLAYERS();
        this.TOTAL_HANDS = gameLogic.getGameScreen().getTOTAL_HANDS();
        this.MAX_CHANCES = gameLogic.getGameScreen().getMAX_CHANCES();
        init();
    }

    private void init(){
        this.numHands = 0;
        this.numChance = 0;
        this.numChancesHand = 0;

        scoreboard = new int[TOTAL_HANDS][TOTAL_PLAYERS][2];
        for (int i=0; i<TOTAL_HANDS; i++){
            for (int j=0; j<TOTAL_PLAYERS; j++){
                for (int k=0; k<2; k++){
                    scoreboard[i][j][k] = 0;
                }
            }
        }
    }

    public void initHand(){
        Gdx.app.log(TAG, "*Init HAND*");
        numHands+=1;
        numChance=1;

        if(numHands<=TOTAL_PLAYERS) numChancesHand = 1;
        else if(numHands<=(TOTAL_HANDS-TOTAL_PLAYERS)) numChancesHand = (numHands-TOTAL_PLAYERS+1);
        else numChancesHand = MAX_CHANCES;

        Gdx.app.log(TAG, "***NumHands: "+numHands);
        Gdx.app.log(TAG, "***NumChance: "+numChance);
        Gdx.app.log(TAG, "***NumChancesHand: "+numChancesHand);
    }

    public void initChance(){
        Gdx.app.log(TAG, "*Init CHANCE*");
        numChance+=1;
    }

    public void reloadGame(int numHands, int numChance, int numChancesHand) {
        this.numHands = numHands;
        this.numChance = numChance;
        this.numChancesHand = numChancesHand;
    }

    /***** Thread Button Event *****/
    public void updateScoreboard(int numRound, int points[]){
        numRound-=1;
        for(int i = 0; i<gameLogic.getGameScreen().getTOTAL_PLAYERS(); i++){
            if(numRound==0){
                scoreboard[numRound][i][0] = points[i];
                scoreboard[numRound][i][1] = points[i];
            } else {
                scoreboard[numRound][i][0] = scoreboard[numRound-1][i][0]+points[i];
                scoreboard[numRound][i][1] = points[i];
            }
        }
    }

    public ArrayList<Integer> getWinnerPlayerIndex(){
        ArrayList<Integer> alIndex = new ArrayList<>();
        int points = -99999;
        int lastRound = TOTAL_HANDS;
        for(int i = 0; i<gameLogic.getGameScreen().getTOTAL_PLAYERS(); i++){
            if(scoreboard[lastRound-1][i][0]>points){
                points = scoreboard[lastRound-1][i][0];
                alIndex.clear();
                alIndex.add(i);
            } else if(scoreboard[lastRound-1][i][0]==points){
                alIndex.add(i);
            }
        }

        return alIndex;
    }

    public boolean isAllCardsDealt() {
        return this.numHands>(TOTAL_HANDS-TOTAL_PLAYERS);
    }

    /*** Getters And Setters ***/
    public int getNumChance(){ return numChance; }
    public int getNumHands(){ return numHands; }
    public int getNumChancesHand(){ return numChancesHand; }
    public int[][][] getScoreboard(){ return scoreboard; }
    public void setScoreboard(int[][][] scoreboard){ this.scoreboard = scoreboard; }
}

package com.tfdevelopment.pochaonlinetfd.server.object.game;

import com.tfdevelopment.pochaonlinetfd.server.object.buttonevent.ButtonEventServer;

import java.util.ArrayList;

public class ServerGameLogin {
    private int numHand, numChance, numChancesHand, handPlayer;
    private ArrayList<ButtonEventServer> alButtonEventServer;
    private int[][][] scoreboard;

    public ServerGameLogin(int numHand, int numChance, int numChancesHand, int handPlayer,
                           ArrayList<ButtonEventServer> alButtonEventServer, int[][][] scoreboard){
        this.numHand = numHand;
        this.numChance = numChance;
        this.numChancesHand = numChancesHand;
        this.handPlayer = handPlayer;
        this.alButtonEventServer = alButtonEventServer;
        this.scoreboard = scoreboard;
    }

    public int getNumHand() { return numHand; }
    public void setNumHand(int numHand) { this.numHand = numHand; }
    public int getNumChance() { return numChance; }
    public void setNumChance(int numChance) { this.numChance = numChance; }
    public int getNumChancesHand() { return numChancesHand; }
    public void setNumChancesHand(int numChancesHand) { this.numChancesHand = numChancesHand; }
    public int getHandPlayer() { return handPlayer; }
    public void setHandPlayer(int handPlayer) { this.handPlayer = handPlayer; }
    public ArrayList<ButtonEventServer> getAlButtonEventServer() { return alButtonEventServer; }
    public void setAlButtonEventServer(ArrayList<ButtonEventServer> alButtonEventServer) { this.alButtonEventServer = alButtonEventServer; }
    public int[][][] getScoreboard() { return scoreboard; }
    public void setScoreboard(int[][][] scoreboard) { this.scoreboard = scoreboard; }
}

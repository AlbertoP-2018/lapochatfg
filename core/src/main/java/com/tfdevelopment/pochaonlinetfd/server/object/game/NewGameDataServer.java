package com.tfdevelopment.pochaonlinetfd.server.object.game;

import java.util.ArrayList;

public class NewGameDataServer {
    private String id, hostName;
    private int totalPlayers, numBots;
    private String mode;
    private ArrayList<String> alPlayerNames;

    public NewGameDataServer(String id, String hostName, int totalPlayers, int numBots, String mode, ArrayList<String> alPlayerNames){
        this.id = id;
        this.hostName = hostName;
        this.totalPlayers = totalPlayers;
        this.numBots = numBots;
        this.mode = mode;
        this.alPlayerNames = alPlayerNames;
    }

    public boolean isPlayerInGame(String playerName) {
        for (String name : alPlayerNames) {
            if (name.equals(playerName)) return true;
        }
        return false;
    }

    public boolean isCompleted() { return this.totalPlayers == alPlayerNames.size(); }

    public String getId(){ return this.id; }
    public String getHostName(){ return this.hostName; }
    public int getTotalPlayers(){ return this.totalPlayers; }
    public int getNumPlayers(){ return this.alPlayerNames.size(); }
    public String getMode(){ return this.mode; }
    public ArrayList<String> getAlPlayerNames(){ return this.alPlayerNames; }
}

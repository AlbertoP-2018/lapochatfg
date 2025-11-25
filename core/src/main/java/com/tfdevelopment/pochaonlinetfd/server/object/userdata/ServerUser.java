package com.tfdevelopment.pochaonlinetfd.server.object.userdata;

import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;

public class ServerUser {
    private String serverId;
    private String name;
    private String gameId;
    private int playerIndex;
    private int userIcon;
    private boolean online;
    private boolean ia;

    /* Datos de partidas:
         position_XX - points_XX - ratio_XX - nWon_XX - nLost_XX
         0. Públicas - 1.TX - ... - N-1.T1 - N.T0
     */
    public static final int NUM_COLUMNS = 5;
    private float[][] gameData;

    //InitPlayer --> Solo ID
    public ServerUser(String serverId){
        this.serverId = serverId;
        this.name = "";
        this.gameId = "";
        this.playerIndex = -1;
        this.online = true;
        this.userIcon = 0;
        this.ia = false;

        initGameData();
    }

    //Reconexión a partida
    //getServerPlayer
    public void updateDataUser(String serverId, String name, String gameId, int playerIndex, boolean online,
                               int userIcon, boolean ia, float[] dataTT){
        this.serverId = serverId;
        this.name = name;
        this.gameId = gameId;
        this.playerIndex = playerIndex;
        this.online = online;
        this.userIcon = userIcon;
        this.ia = ia;
        this.gameData[0] = dataTT;
    }
    public void updateDataSeason(float[][] dataTX){
        for(int i = 1; i< Config.NUM_SEASONS; i++){
            for(int j=0; j<NUM_COLUMNS; j++){
                this.gameData[i][j] = dataTX[i][j];
            }
        }
    }

    public void updateDataFromLoginToGame(String gameId, int playerIndex) {
        this.gameId = gameId;
        this.playerIndex = playerIndex;
        this.online = true;
    }

    /*************************************************/
    public void initGameData(){
        this.gameData = new float[Config.NUM_SEASONS][NUM_COLUMNS];
        for(int i = 0; i< Config.NUM_SEASONS; i++){
            for(int j=0; j<NUM_COLUMNS; j++){
                this.gameData[i][j] = 0;
            }
        }
    }

    public String getGameId() { return gameId != null ? gameId : ""; }
    public boolean isInGame(){ return this.gameId != null && !this.gameId.isEmpty(); }

    /************ Getters And Setters ****************/
    public float getGameData(int row, int columns){ return gameData[row][columns]; }
    public void setName(String name) { this.name = name; }
    public String getServerId() { return serverId; }
    public String getName() { return name; }
    public boolean isIa() { return ia; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public int getPlayerIndex() { return playerIndex; }
    public boolean isOnline() {
        return online;
    }
    public int getUserIcon() {
        return userIcon;
    }
    public void setUserIcon(int userIcon){ this.userIcon = userIcon; }
}

package com.tfdevelopment.pochaonlinetfd.server.object.userdata;

public class UserDataServer {
    private String serverId;
    private String name;
    private String gameId;
    private int playerIndex;
    private int userIcon;
    private boolean online;

    public UserDataServer(String serverId, String name, String gameId, int playerIndex, int userIcon,
                          boolean online){

        this.serverId = serverId;
        this.name = name;
        this.gameId = gameId;
        this.playerIndex = playerIndex;
        this.online = online;
        this.userIcon = userIcon;
    }

    public void updateObject(UserDataServer sdu){
        this.serverId = sdu.serverId;
        this.name = sdu.name;
        this.gameId = sdu.gameId;
        this.playerIndex = sdu.playerIndex;
        this.online = sdu.online;
        this.userIcon = sdu.userIcon;
    }

    public String getServerId() { return serverId; }
    public String getName() { return name; }
    public String getGameId() { return gameId; }
    public void setGameId(String gameId){ this.gameId = gameId; }
    public int getPlayerIndex() { return playerIndex; }
    public int getUserIcon() { return userIcon; }
    public void setUserIcon(int userIcon) { this.userIcon = userIcon; }
    public boolean isOnline() { return online; }
}

package com.tfdevelopment.pochaonlinetfd.server.object.playerdata;

public class ServerPlayer {
    private static final String TAG = ServerPlayer.class.getName();

    private String id, name, gameId;
    private int playerIndex, userIcon;
    private boolean ai;
    private int[] cards;
    private boolean[] selected;
    private boolean disconnected;

    public ServerPlayer(String id, String name, String gameId, int playerIndex, int userIcon, boolean ia, int[] cards, boolean[] selected){
        this.id = id;
        this.name = name;
        this.gameId = gameId;
        this.playerIndex = playerIndex;
        this.userIcon = userIcon;
        this.ai = ia;
        this.cards = cards;
        this.selected = selected;
        this.disconnected = false;
    }

    public void newDealCards(int[] cards){
        for(int i=0; i<cards.length; i++){
            this.cards[i] = cards[i];
            this.selected[i] = false;
        }
    }

    public String getName(){ return name; }
    public String getGameId() { return gameId; }
    public int getPlayerIndex() { return playerIndex; }
    public boolean isAi(){ return ai; }
    public int[] getCards(){ return cards; }
    public boolean[] getSelected(){ return selected; }
    public boolean isDisconnected() { return disconnected; }
    public void setDisconnected(boolean disconnected){ this.disconnected = disconnected; }
}

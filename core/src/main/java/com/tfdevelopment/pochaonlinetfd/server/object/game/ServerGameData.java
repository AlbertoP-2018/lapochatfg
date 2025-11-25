package com.tfdevelopment.pochaonlinetfd.server.object.game;

import com.tfdevelopment.pochaonlinetfd.server.object.playerdata.ServerPlayer;
import com.tfdevelopment.pochaonlinetfd.server.object.buttonevent.ButtonEventServer;

import java.util.ArrayList;

public class ServerGameData {
    private static final String TAG = ServerGameData.class.getName();

    private String gameId;
    private ArrayList<ServerPlayer> gamePlayers;
    private int trump;
    private int firstPlayer;

    private int idLastEvent;
    private ArrayList<ButtonEventServer> alButtonEvent;
    private int indexPlayer;

    private ServerGameLogin serverGameLogin;

    public ServerGameData(String gameId, ArrayList<ServerPlayer> gamePlayers, int trump, int firstPlayer, int indexPlayer, ServerGameLogin serverGameLogin){
        this.gameId = gameId;
        this.gamePlayers = gamePlayers;
        this.trump = trump;
        this.firstPlayer = firstPlayer;

        // Define la posición del jugador principal, que será siempre el mismo valor
        this.indexPlayer = indexPlayer;

        this.idLastEvent = 0;
        this.alButtonEvent = new ArrayList<>();

        this.serverGameLogin = serverGameLogin;
    }

    public void addButtonEvent(int idLastEvent, ButtonEventServer buttonEventServer){
        this.idLastEvent = idLastEvent;
        this.alButtonEvent.add(buttonEventServer);
    }

    public void setAllCards(int trump, int[][] allCards){
        this.trump = trump;
        for(int i=0; i<gamePlayers.size(); i++){
            gamePlayers.get(i).newDealCards(allCards[i]);
        }
    }

    public ArrayList<ServerPlayer> getGamePlayers(){ return gamePlayers; }
    public int getIdLastEvent(){ return this.idLastEvent; }
    public void setIdLastEvent(int idLastEvent){ this.idLastEvent = idLastEvent; }
    public int getTrump(){ return this.trump; }
    public int getFirstPlayer(){ return this.firstPlayer; }
    public int getIndexPlayer(){ return this.indexPlayer; }
    public void setDataSGLogin(ServerGameLogin serverGameLogin){ this.serverGameLogin = serverGameLogin; }
    public ServerGameLogin getDataSGLogin(){ return serverGameLogin; }
}

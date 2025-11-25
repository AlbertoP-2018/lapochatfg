package com.tfdevelopment.pochaonlinetfd.server.object.ranking;

public class RankingUserDataServer {
    private static final String TAG = RankingUserDataServer.class.getName();

    private boolean season;
    private String name;
    private int points, won, lost, played;
    private float ratio;

    //Constructor para Clasificación
    public RankingUserDataServer(String name, int points, float ratio, int won, int lost){
        this.season = true;
        this.name = name;
        this.points = points;
        this.ratio = ratio;
        this.won = won;
        this.lost = lost;
    }

    //Constructor para Torneos
    public RankingUserDataServer(String name, int won, int played, float ratio){
        this.season = false;
        this.name = name;
        this.won = won;
        this.played = played;
        this.ratio = ratio;
    }

    public boolean isSeason(){ return this.season; }
    public String getName(){ return this.name; }
    public int getPoints(){ return this.points; }
    public int getWon(){ return this.won; }
    public int getLost(){ return this.lost; }
    public int getPlayed(){ return this.played; }
    public float getRatio(){ return this.ratio; }
}

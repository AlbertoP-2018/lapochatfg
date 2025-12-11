package com.tfdevelopment.pochaonlinetfd.utils.conf;

public class Config {
    public static final String VERSION = "v2.0.0";

    // public static final String URL = "http://192.168.31.155:8080";
    // public static final String URL = "http://192.168.1.106:8080";
    public static final String URL = "wss://pochaonlinetfd.eu-4.evennode.com";

    public static final boolean ADS_ON = true;
    public static final boolean AD_TEST = false;
    public static final boolean ADMIN_ON = false;
    public static final boolean CONFIG_SERVER = ADMIN_ON && false;
    public static final boolean TEST = CONFIG_SERVER && false; // Show the other player's card
    public static boolean TESTING = false;

    public static final int NUM_SEASONS = 1; //(TemporadasX+Temporada0)
    public static final int NUM_SEASONS_SHOW = 1; //No se tiene en cuenta las públicas
    public static final int POINTS_FOR_WINNING = 5;
    public static final int POINTS_FOR_LOST = 1;

    public static final boolean RUN_GAME = false; // Run the game without AI timers
    public static final boolean AUTOMATIC_RESPONSE = false;
    public static long TIME_IA = RUN_GAME ? 0 : 1000;
}

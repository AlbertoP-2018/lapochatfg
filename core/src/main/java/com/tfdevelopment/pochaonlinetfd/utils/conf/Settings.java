package com.tfdevelopment.pochaonlinetfd.utils.conf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;

public class Settings {
    private static final String SETTINGS = "pochaonline.set";
    private static final String SIZE_GUI_NORMAL = "sizeGUINormal";
    private static final String PLAYER_NAME = "playerName";
    private static final String PLAYER_PASSWORD = "playerPassword";
    private static final String SHOW_WELCOME = "showWelcome";
    private static final String SOUND_BUTTON_ON = "soundButtonOn";
    private static final String MUSIC_GAME_ON = "musicGameOn";
    private static final String SOUND_TURN_ON = "soundTurnOn";
    private static final String VIBRATION_TURN_ON = "vibrationTurnOn";
    private static final String INVITATION_GAME_ON = "invitationGameOn";
    private static final String NEWS_VERSION = "newsVersion";
    private static final String SHOW_CLASSIFICATION_TABLE_WINDOW = "showClassificationTable";
    private static final String SHOW_REVIEW = "showReview"; //Indica si ya se ha mostrado la ventana de valoración
    private static final String SHOW_REVIEW_NUM_SET = "showReviewSET";  //Indica el número de partidas jugadas para mostrar la valoración
    private static final String TYPE_CARDS = "typeCards"; //Clásicas o Linux

    public static final int NUM_GAMES_PLAYED_SHOW_REVIEW = 3;
    public static final String CLASSIC_TC = "classic";
    public static final String LINUX_TC = "linux";

    public static final Settings settings = new Settings();
    private static final Preferences preferences = Gdx.app.getPreferences(SETTINGS);

    private static String playerName, savePassword, newsVersion, typeCards;
    private static boolean showReview, muteObserverOn, sizeGUINormal,
            showWelcome, musicGameOn, soundButtonOn, soundTurnOn, vibrationTurnOn, invitationGameOn,
            showClassificationTableWindow;
    private static int showReviewSET;

    public static void load(){
        sizeGUINormal = preferences.getBoolean(SIZE_GUI_NORMAL, true);
        playerName = preferences.getString(PLAYER_NAME, "");
        savePassword = preferences.getString(PLAYER_PASSWORD, "");
        showWelcome = preferences.getBoolean(SHOW_WELCOME, false);
        musicGameOn = preferences.getBoolean(MUSIC_GAME_ON, true);
        soundButtonOn = preferences.getBoolean(SOUND_BUTTON_ON, true);
        soundTurnOn = preferences.getBoolean(SOUND_TURN_ON, true);
        vibrationTurnOn = preferences.getBoolean(VIBRATION_TURN_ON, true);
        invitationGameOn = preferences.getBoolean(INVITATION_GAME_ON, true);
        showClassificationTableWindow = preferences.getBoolean(SHOW_CLASSIFICATION_TABLE_WINDOW, true);
        newsVersion = preferences.getString(NEWS_VERSION, "");
        showReview = preferences.getBoolean(SHOW_REVIEW, false);
        showReviewSET = preferences.getInteger(SHOW_REVIEW_NUM_SET, 0);
        typeCards = preferences.getString(TYPE_CARDS, CLASSIC_TC);
    }

    public static void save(){
        preferences.putBoolean(SIZE_GUI_NORMAL, sizeGUINormal);
        preferences.putString(PLAYER_NAME, playerName);
        preferences.putString(PLAYER_PASSWORD, savePassword);
        preferences.putBoolean(SHOW_WELCOME, showWelcome);
        preferences.putBoolean(MUSIC_GAME_ON, musicGameOn);
        preferences.putBoolean(SOUND_BUTTON_ON, soundButtonOn);
        preferences.putBoolean(SOUND_TURN_ON, soundTurnOn);
        preferences.putBoolean(VIBRATION_TURN_ON, vibrationTurnOn);
        preferences.putBoolean(INVITATION_GAME_ON, invitationGameOn);
        preferences.putString(NEWS_VERSION, newsVersion);
        preferences.putBoolean(SHOW_CLASSIFICATION_TABLE_WINDOW, showClassificationTableWindow);
        preferences.putBoolean(SHOW_REVIEW, showReview);
        preferences.putInteger(SHOW_REVIEW_NUM_SET, showReviewSET);
        preferences.putString(TYPE_CARDS, typeCards);
        preferences.flush(); //Permite escribir los valores modificados en el archivo
    }

    /** Getters and Setters **/
    public static boolean isSizeGUINormal(){ return Settings.sizeGUINormal; }
    public static String getPlayerName() {
        return Settings.playerName;
    }
    public static String getPlayerPassword(){ return Settings.savePassword; }
    public static boolean isShowWelcome() {
        return Settings.showWelcome;
    }
    public static boolean isShowClassificationTableWindow() { return Settings.showClassificationTableWindow; }
    public static boolean isMusicGameOn(){
        return Settings.musicGameOn;
    }
    public static boolean isSoundClickButtonOn(){
        return false;
        //return Settings.soundButtonOn;
    }
    public static boolean isSoundTurnOn(){
        return Settings.soundTurnOn;
    }
    public static boolean isVibrationTurnOn(){
        return Settings.vibrationTurnOn;
    }
    public static boolean isMuteObserverOn(){ return Settings.muteObserverOn; }
    public static boolean isInvitationGameOn(){
        return Settings.invitationGameOn;
    }
    public static String getNewsVersion(){ return Settings.newsVersion; }
    public static boolean isShowReview(){ return Settings.showReview; }
    public static int getShowReviewSET(){ return Settings.showReviewSET; }
    public static String getTypeCards(){ return Settings.typeCards; }


    public static void setSizeGuiNormal(boolean sizeGUINormal){
        Settings.sizeGUINormal = sizeGUINormal;
        save();
    }
    public static void setPlayerName(String playerName) {
        Settings.playerName = playerName;
        save();
    }
    public static void setPlayerPassword(String playerPassword){
        Settings.savePassword = playerPassword;
        save();
    }
    public static void setShowWelcome(boolean showWelcome) {
        Settings.showWelcome = showWelcome;
        save();
    }

    public static void setMusicGameOn(boolean musicGameOn){
        Settings.musicGameOn = musicGameOn;
        save();
    }
    public static void setSoundButtonOn(boolean soundButtonOn){
        Settings.soundButtonOn = soundButtonOn;
        save();
    }
    public static void setSoundTurnOn(boolean soundTurnOn){
        Settings.soundTurnOn = soundTurnOn;
        save();
    }
    public static void setVibrationTurnOn(boolean vibrationTurnOn){
        Settings.vibrationTurnOn = vibrationTurnOn;
        save();
    }
    public static void setInvitationGameOn(boolean invitationGameOn){
        Settings.invitationGameOn = invitationGameOn;
        save();
    }

    public static void setMuteObserverOn(boolean muteObserverOn){
        Settings.muteObserverOn = muteObserverOn;
        save();
    }

    public static void setNewsVersion(String newsVersion){
        Settings.newsVersion = newsVersion;
        save();
    }
    public static void setShowReview(boolean showReview){
        Settings.showReview = showReview;
        save();
    }
    public static void setShowReviewSET(){
        Settings.showReviewSET = Settings.showReviewSET+1;
        save();
    }
    public static void setTypeCards(String type){
        Settings.typeCards = type;
        save();
    }
    public static void setShowClassificationTableWindow(boolean showClassificationTableWindow) {
        Settings.showClassificationTableWindow = showClassificationTableWindow;
        save();
    }
    /***************************************/
    public static void playSoundClickButton(){
        if(isSoundClickButtonOn()){
            AssetLoader.aLoader.aSound.getClickButton().play(1f);
        }
    }

    public static void playSoundWinGame(){
        if(isSoundTurnOn()){
            AssetLoader.aLoader.aSound.getApplauseWinGame().play(1f);
        }
    }

    public static void playSoundWinChance(){
        if(isSoundTurnOn()){
            AssetLoader.aLoader.aSound.getApplauseWinChance().play(1f);
        }
    }

    public static void playSoundLoseGame(){
        if(isSoundTurnOn()){
            AssetLoader.aLoader.aSound.getBooLoseGame().play(1f);
        }
    }

    public static void playSoundLoseChance(){
        if(isSoundTurnOn()){
            AssetLoader.aLoader.aSound.getBooLoseChance().play(1f);
        }
    }

    public static void playSelectCard(){
        if(isSoundClickButtonOn()){
            AssetLoader.aLoader.aSound.getSelectCard().play(1f);
        }
    }
}

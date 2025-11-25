package com.tfdevelopment.pochaonlinetfd.server.object.game;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.ServerObject;
import com.tfdevelopment.pochaonlinetfd.server.object.game.ServerGameDataJSON.ORIGIN_REQUEST;
import com.tfdevelopment.pochaonlinetfd.server.object.news.NewsJS;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.ServerUser;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerGameJS {
    private static final String TAG = ServerGameJS.class.getName();

    public static final String
        EX_RECONNECT_TO_GAME = "reconnectToGame", EX_GAME_TO_HALL = "gameToHall",
        EX_FINISHED_GAME = "finishedGame", EX_PLAYER_GAME_RECONNECTED = "playerGameReconnected",
        EX_START_GAME = "startGame";

    private Server nodeJS;

    public ServerGameJS(Server nodeJS) {
        this.nodeJS = nodeJS;
    }

    public void eiStartGame(Object... args) {
        Gdx.app.log(TAG, "eiStartGame");
        try {
            JSONObject data = ((JSONObject)args[0]).getJSONObject("game");
            ServerGameData serverGameData = ServerGameDataJSON.getServerGame(data, nodeJS.getServerUser().getName(), ORIGIN_REQUEST.NORMAL);

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if(serverMainScreen!=null) {
                if (nodeJS.isPaused()) nodeJS.setPausedAtStarted(true);
                serverMainScreen.startGame(serverGameData);
                Gdx.app.log(TAG, "eiStartGame - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiStartGame (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiStartGame (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiStartGame (Exception): " + e.getMessage());
        }
    }

    public void eiPlayerGameReconnected(Object... args){
        Gdx.app.log(TAG, "eiPlayerGameReconnected");

        try {
            JSONObject data = (JSONObject)args[0];

            boolean playerOut = data.getBoolean("playerOut");
            int indexPlayer = data.getInt("chair");

            OnlineGS onlineGS = nodeJS.getOnlineGS();
            if(onlineGS!=null){
                onlineGS.playerReconnected(playerOut, indexPlayer);
                Gdx.app.log(TAG, "eiPlayerGameReconnected - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiPlayerGameReconnected (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiPlayerGameReconnected (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiPlayerGameReconnected (Exception): " + e.getMessage());
        }
    }

    public void eiReconnectToGame(Object... args) {
        Gdx.app.log(TAG, "eiReconnectToGame");
        try {
            JSONObject data = (JSONObject)args[0];

            ServerUser serverUser = nodeJS.getServerUser();
            ServerGameData serverGameData = ServerGameDataJSON.getServerGame(data, serverUser.getName(), ORIGIN_REQUEST.RECONNECT);

            OnlineGS onlineGS = nodeJS.getOnlineGS();
            if (onlineGS != null){
                onlineGS.reconnectGame(serverGameData);
                Gdx.app.log(TAG, "eiReconnectToGame - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiReconnectToGame (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiReconnectToGame (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiReconnectToGame (Exception): " + e.getMessage());
        }
    }

    public void eoReconnectToGame(boolean pausedAtStarted){
        Gdx.app.log(TAG, "eo_ReconnectToGame");
        nodeJS.getSocket().emit(EX_RECONNECT_TO_GAME, nodeJS.getServerUser().getName(), pausedAtStarted);
    }

    public void eoFinishedGame(int[][][] scoreboard) {
        Gdx.app.log(TAG, "eoFinishedGame");
        String gameId = nodeJS.getServerUser().getGameId();
        String stringScoreboard = StaticsMethods.getConvertedScore(scoreboard);

        nodeJS.getSocket().emit(EX_FINISHED_GAME, gameId, stringScoreboard);
    }

    public void eiGameToHall(Object... args){
        Gdx.app.log(TAG, "eiGameToHall");

        try {
            JSONObject data = (JSONObject)args[0];

            String newsVersion = data.getString(NewsJS.NEWS_VERSION);

            OnlineGS onlineGS = nodeJS.getOnlineGS();
            if (onlineGS != null) {
                ServerObject serverObject = new ServerObject(newsVersion);
                onlineGS.goToHall(serverObject);
                Gdx.app.log(TAG, "eiGameToHall - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiGameToHall (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiGameToHall (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiGameToHall (Exception): " + e.getMessage());
        }
    }

    public void eoGameToHall() {
        Gdx.app.log(TAG, "eoGameToHall");
        ServerUser serverUser = nodeJS.getServerUser();
        nodeJS.getSocket().emit(EX_GAME_TO_HALL, serverUser.getGameId(), serverUser.getPlayerIndex());
    }
}

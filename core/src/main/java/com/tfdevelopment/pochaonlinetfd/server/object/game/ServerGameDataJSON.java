package com.tfdevelopment.pochaonlinetfd.server.object.game;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.playerdata.ServerPlayerJSON;
import com.tfdevelopment.pochaonlinetfd.server.object.buttonevent.ButtonEventJSON;
import com.tfdevelopment.pochaonlinetfd.server.object.buttonevent.ButtonEventServer;
import com.tfdevelopment.pochaonlinetfd.server.object.playerdata.ServerPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServerGameDataJSON {
    private static final String TAG = ServerGameDataJSON.class.getName();

    private static final String GAME_ID = "id";
    private static final String PLAYERS = "players";
    private static final String TRUMP = "trump";
    private static final String FIRST_PLAYER = "firstPlayer";
    private static final String GAME_RECONNECT_DATA = "gameReconnectData";

    private static final String NUM_HAND = "numHand";
    private static final String NUM_CHANCE = "numChance";
    private static final String NUM_CHANCES_HAND = "numChancesHand";
    private static final String HAND_PLAYER = "handPlayer";
    private static final String ARRAY_EVENTS = "arrayEvents";
    private static final String SCOREBOARD = "scoreboard";

    public enum ORIGIN_REQUEST { NORMAL, RECONNECT }

    public static ServerGameData getServerGame(JSONObject data, String indexPlayerName, ORIGIN_REQUEST origin) throws Exception {
        try {
            String gameId = data.getString(GAME_ID);
            JSONArray players = data.getJSONArray(PLAYERS);
            ArrayList<ServerPlayer> alPlayers = ServerPlayerJSON.getALGamePlayer(players);
            int trump = data.getInt(TRUMP);
            int firstPlayer = data.getInt(FIRST_PLAYER);
            boolean login = !data.isNull(GAME_RECONNECT_DATA);

            int playerIndex = -1;
            for (int i=0; i<alPlayers.size(); i++) {
                if (alPlayers.get(i).getName().equals(indexPlayerName)) playerIndex = i;
            }

            if (playerIndex == -1) throw new Exception("No se detecto un índice para el jugador. ServerUser no actualizado.");

            ServerGameLogin serverGameLogin = null;
            if (login && origin == ORIGIN_REQUEST.RECONNECT) {
                serverGameLogin = getServerGameLogin(data.getJSONObject(GAME_RECONNECT_DATA));
            }

            ServerGameData dataServerGame = new ServerGameData(gameId, alPlayers, trump, firstPlayer, playerIndex, serverGameLogin);
            return dataServerGame;
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getServerGame (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getServerGame (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getServerGame (Exception): " + e.getMessage());
            Server nodeJS = Server.getServerNodeJS(null);
            nodeJS.disconnectServer(false);
            throw e;
        }
    }

    //DataServerGameLogin
    private static ServerGameLogin getServerGameLogin(JSONObject data) throws Exception {
        try {
            int numHand = data.getInt(NUM_HAND);
            int numChance = data.getInt(NUM_CHANCE);
            int numChancesHand = data.getInt(NUM_CHANCES_HAND);
            int handPlayer = data.getInt(HAND_PLAYER);
            JSONArray jsonArrayEvents = data.getJSONArray(ARRAY_EVENTS);
            JSONArray jsonScoreboard = data.getJSONArray(SCOREBOARD);

            ArrayList<ButtonEventServer> alButtonEventServer = getALButtonEventServer(jsonArrayEvents);
            int[][][] scoreboard = getScoreboard(jsonScoreboard);

            return new ServerGameLogin(numHand, numChance, numChancesHand, handPlayer, alButtonEventServer, scoreboard);
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getServerGameLogin (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getServerGameLogin (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getServerGameLogin (Exception): " + e.getMessage());
            throw e;
        }
    }

    private static ArrayList<ButtonEventServer> getALButtonEventServer(JSONArray jsonArrayEvents) throws Exception {
        ArrayList<ButtonEventServer> alButtonEventServer = new ArrayList<>();
        for (int i=0; i<jsonArrayEvents.length(); i++) {
            ButtonEventServer bev = ButtonEventJSON.getButtonEventServer(jsonArrayEvents.getJSONObject(i));
            alButtonEventServer.add(bev);
        }
        return alButtonEventServer;
    }

    private static int[][][] getScoreboard(JSONArray jsonScoreboard) throws JSONException {
        int numHand = jsonScoreboard.length();
        int[][][] scoreboard = new int[numHand][][];
        for (int hand=0; hand<numHand; hand++) {
            JSONArray jsonPlayer = jsonScoreboard.getJSONArray(hand);
            int numPlayer = jsonPlayer.length();
            scoreboard[hand] = new int[numPlayer][];

            for (int player=0; player<numPlayer; player++) {
                JSONArray jsonPoints = jsonPlayer.getJSONArray(player);
                int numPoints = jsonPoints.length();
                scoreboard[hand][player] = new int[numPoints];

                for (int point=0; point<numPoints; point++){
                    scoreboard[hand][player][point] = jsonPoints.getInt(point);
                }
            }
        }

        return scoreboard;
    }
}

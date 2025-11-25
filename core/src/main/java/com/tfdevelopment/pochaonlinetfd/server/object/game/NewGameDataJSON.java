package com.tfdevelopment.pochaonlinetfd.server.object.game;

import com.badlogic.gdx.Gdx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewGameDataJSON {
    private static final String TAG = NewGameDataJSON.class.getName();

    //NewGameData - Object
    private static final String ID = "id";
    private static final String HOST_NAME = "hostName";
    private static final String NUM_PLAYERS = "numPlayers";
    private static final String NUM_BOTS = "numBots";
    private static final String MODE = "mode";
    private static final String PLAYER_NAMES = "playerNames";

    public static ArrayList<NewGameDataServer> getALNewGameDataJSON(JSONArray data) throws Exception {
        try{
            ArrayList<NewGameDataServer> alNewGameData = new ArrayList<>();
            for(int i=0; i<data.length(); i++){
                NewGameDataServer newGameDataServer = getNewGameDataJSON(data.getJSONObject(i));
                alNewGameData.add(newGameDataServer);
            }
            return alNewGameData;
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getALNewGameDataJSON (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getALNewGameDataJSON (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getALNewGameDataJSON (Exception): " + e.getMessage());
            throw e;
        }
    }

    public static NewGameDataServer getNewGameDataJSON(JSONObject joNewGameData) throws Exception {
        try{
            String id = joNewGameData.getString(ID);
            String hostName = joNewGameData.getString(HOST_NAME);
            int totalPlayers = joNewGameData.getInt(NUM_PLAYERS);
            int numBots = joNewGameData.getInt(NUM_BOTS);
            String mode = joNewGameData.getString(MODE);
            ArrayList<String> alPlayerNames = new ArrayList<>();
            JSONArray jsonArray = joNewGameData.getJSONArray(PLAYER_NAMES);
            for (int i=0; i<jsonArray.length(); i++) {
                alPlayerNames.add(jsonArray.getString(i));
            }

            return new NewGameDataServer(id, hostName, totalPlayers, numBots, mode, alPlayerNames);
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getNewGameDataJSON (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getNewGameDataJSON (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getNewGameDataJSON (Exception): " + e.getMessage());
            throw e;
        }
    }
}

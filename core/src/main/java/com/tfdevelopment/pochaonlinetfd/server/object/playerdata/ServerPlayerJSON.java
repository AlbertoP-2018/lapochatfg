package com.tfdevelopment.pochaonlinetfd.server.object.playerdata;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServerPlayerJSON {
    private static final String TAG = ServerPlayerJSON.class.getName();

    /******************* GAME PLAYER ******************/
    public static ArrayList<ServerPlayer> getALGamePlayer(JSONArray data) throws Exception {
        try{
            ArrayList<ServerPlayer> alServerPlayer = new ArrayList<>();
            for(int i=0; i<data.length(); i++){
                ServerPlayer auxGamePlayer = getGamePlayer(data.getJSONObject(i));
                if(auxGamePlayer!=null){ //Si es null es porque el nombre es propio de una IA
                    alServerPlayer.add(auxGamePlayer);
                }
            }
            return alServerPlayer;
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getALGamePlayer (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getALGamePlayer (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getALGamePlayer (Exception): " + e.getMessage());
            throw e;
        }
    }

    private static ServerPlayer getGamePlayer(JSONObject jsoServerPlayer) throws Exception {
        try{
            //Excluye aquellos sockets sin nombre de usuario
            if(!jsoServerPlayer.getString("name").equals(Constants.NOT_NAME_PLAYER)) {
                String id = jsoServerPlayer.getString("id");
                String name = jsoServerPlayer.getString("name");
                String gameId = jsoServerPlayer.getString("gameId");
                int playerIndex = jsoServerPlayer.getInt("playerIndex");
                int userIcon = jsoServerPlayer.getInt("userIcon");
                boolean ia = jsoServerPlayer.getBoolean("ai");

                JSONArray jsonCardsData = jsoServerPlayer.getJSONArray("cardsData");
                int numMaxCards = jsonCardsData.getJSONArray(0).length();
                int cards[] = new int[numMaxCards];
                boolean selected[] = new boolean[numMaxCards];
                for(int i=0; i<numMaxCards; i++){
                    cards[i] = jsonCardsData.getJSONArray(0).getInt(i);
                    selected[i] = jsonCardsData.getJSONArray(1).getBoolean(i);
                }

                ServerPlayer gamePlayer = new ServerPlayer(id, name, gameId, playerIndex, userIcon, ia, cards, selected);
                return gamePlayer;
            }

            return null;
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getGamePlayer (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getGamePlayer (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getGamePlayer (Exception): " + e.getMessage());
            throw e;
        }
    }
}

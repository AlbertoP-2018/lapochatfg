package com.tfdevelopment.pochaonlinetfd.server.object.ranking;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RankingUserDataJS {
    private static final String TAG = RankingUserDataJS.class.getName();

    public static final String EX_PLAYER_RANKING = "playerRanking";

    private static final String LAST_UPDATE_DATE = "lastRankingUpdate";
    private static final String PLAYER_RANKING = "listPlayerRanking";

    private Server nodeJS;

    public RankingUserDataJS(Server nodeJS) {
        this.nodeJS = nodeJS;
    }

    public void eiRankingPlayers(Object... args){
        Gdx.app.log(TAG, "eiRankingPlayers");

        try {
            JSONObject data = (JSONObject)args[0];

            //100: Torneo -- 0: Inauguración -- 1: Temporada 1 -- 2: Temporada 2...
            int iRanking = data.getInt("iRanking");
            int iOrder = data.getInt("iOrder");
            String lastUpdateDate = data.getString(LAST_UPDATE_DATE);

            ArrayList<RankingUserDataServer> alDataRanking = RankingUserDataJSON.getALDataRanking(data.getJSONArray(PLAYER_RANKING));

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if(serverMainScreen!=null) {
                serverMainScreen.setAlRankingPlayer(iRanking, iOrder, lastUpdateDate, alDataRanking);
                Gdx.app.log(TAG, "eiRankingPlayers - OK");
            }

        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiRankingPlayers (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiRankingPlayers (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiRankingPlayers (Exception): " + e.getMessage());
        }
    }

    public void eoRankingPlayers(int iRanking, int iOrder){
        Gdx.app.log(TAG, "eoRankingPlayers");
        nodeJS.getServerMainScreen().getHallMenuButtons().getHallMenuRanking().openFlagAnimation();
        nodeJS.getSocket().emit(EX_PLAYER_RANKING, iRanking, iOrder);
    }
}

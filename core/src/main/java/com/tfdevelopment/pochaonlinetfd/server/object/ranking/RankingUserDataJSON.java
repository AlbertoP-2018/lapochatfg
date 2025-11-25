package com.tfdevelopment.pochaonlinetfd.server.object.ranking;

import com.badlogic.gdx.Gdx;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RankingUserDataJSON {
    private static final String TAG = RankingUserDataJSON.class.getName();
    private static final String NAME_USER = "name";

    static ArrayList<RankingUserDataServer> getALDataRanking(JSONArray data) throws Exception{
        try{
            ArrayList<RankingUserDataServer> alSDRanking = new ArrayList<>();
            for(int i=0; i<data.length(); i++){
                String name = data.getJSONObject(i).getString(NAME_USER);

                float ratio = (float)data.getJSONObject(i).getDouble("ratio");
                int points = data.getJSONObject(i).getInt("points");
                int nWon = data.getJSONObject(i).getInt("nWon");
                int nLost = data.getJSONObject(i).getInt("nLost");
                RankingUserDataServer auxSDRanking = new RankingUserDataServer(name, points, ratio, nWon, nLost);

                alSDRanking.add(auxSDRanking);
            }
            return alSDRanking;
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getALDataRanking (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getALDataRanking (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getALDataRanking (Exception): " + e.getMessage());
            throw e;
        }
    }
}

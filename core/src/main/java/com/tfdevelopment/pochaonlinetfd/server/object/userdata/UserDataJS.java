package com.tfdevelopment.pochaonlinetfd.server.object.userdata;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDataJS {
    private static final String TAG = UserDataJS.class.getName();

    public static final String EX_MY_USER = "myUser";

    private static final String MY_USER = "myUser";
    private static final String ID_USER = "id";
    private static final String NAME_USER = "name";
    private static final String GAME_ID = "gameId";
    private static final String PLAYER_INDEX = "playerIndex";
    private static final String ONLINE_USER = "online";
    private static final String USER_ICON = "userIcon";
    private static final String AI_USER = "ai";

    private static final String POSITION_TT_USER = "position_TT";
    private static final String POINTS_TT_USER = "points_TT";
    private static final String RATIO_TT_USER = "ratio_TT";
    private static final String NWON_TT_USER = "nWon_TT";
    private static final String NLOST_TT_USER = "nLost_TT";

    private Server nodeJS;

    public UserDataJS(Server nodeJS) {
        this.nodeJS = nodeJS;
    }

    public void eiMyUser(Object... args){
        Gdx.app.log(TAG, "eiMyUser");

        try {
            JSONObject data = (JSONObject)args[0];
            updateMyUser(data.getJSONObject(MY_USER));

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if (serverMainScreen != null) {
                serverMainScreen.updateMyUser();
                Gdx.app.log(TAG, "eiMyUser - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiMyUser (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiMyUser (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiMyUser (Exception): " + e.getMessage());
        }
    }

    private void updateMyUser(JSONObject jsoServerPlayer) throws Exception{
        try{
            //Excluye aquellos sockets sin nombre de usuario
            if(!jsoServerPlayer.getString(NAME_USER).equals(Constants.NOT_NAME_PLAYER)) {
                String id = jsoServerPlayer.getString(ID_USER);
                String name = jsoServerPlayer.getString(NAME_USER);
                String gameId = null;
                if (!jsoServerPlayer.isNull(GAME_ID)) gameId = jsoServerPlayer.getString(GAME_ID);
                int playerIndex = jsoServerPlayer.getInt(PLAYER_INDEX);
                boolean online = jsoServerPlayer.getBoolean(ONLINE_USER);
                int userIcon = jsoServerPlayer.getInt(USER_ICON);
                boolean ia = jsoServerPlayer.getBoolean(AI_USER);

                float gameData[][] = new float[Config.NUM_SEASONS][ServerUser.NUM_COLUMNS];
                for(int i=0; i<Config.NUM_SEASONS; i++){
                    for(int j = 0; j< ServerUser.NUM_COLUMNS; j++){
                        gameData[i][j] = 0;
                    }
                }

                //Temporada actual
                int position_TT = jsoServerPlayer.getInt(POSITION_TT_USER);
                int points_TT = jsoServerPlayer.getInt(POINTS_TT_USER);
                float ratio_TT = (float)jsoServerPlayer.getDouble(RATIO_TT_USER);
                int nWon_TT = jsoServerPlayer.getInt(NWON_TT_USER);
                int nLost_TT = jsoServerPlayer.getInt(NLOST_TT_USER);
                gameData[0] = new float[]{position_TT, points_TT, ratio_TT, nWon_TT, nLost_TT};

                ServerUser serverUser = nodeJS.getServerUser();
                serverUser.updateDataUser(id, name, gameId, playerIndex, online, userIcon, ia, gameData[0]);
                serverUser.updateDataSeason(gameData);
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to updateMyUser (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to updateMyUser (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to updateMyUser (Exception): " + e.getMessage());
            throw e;
        }
    }
}

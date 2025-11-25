package com.tfdevelopment.pochaonlinetfd.server.object.userdata;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDataJSON {
    private static final String TAG = UserDataJSON.class.getName();

    //Registros de USERS
    static final String ID_USER = "id";
    static final String NAME_USER = "name";
    static final String GAME_ID = "gameId";
    static final String PLAYER_INDEX = "playerIndex";
    static final String ONLINE_USER = "online";
    static final String USER_ICON = "userIcon";
    static final String IA_USER = "ia";

    //DataUser - Object
    static final String ID_DATAUSER = ID_USER;
    static final String NAME_DATAUSER = NAME_USER;
    static final String GAME_ID_DATAUSER = GAME_ID;
    static final String PLAYER_INDEX_DATAUSER = PLAYER_INDEX;
    static final String ICON_DATAUSER = USER_ICON;
    static final String ONLINE_DATAUSER = ONLINE_USER;

    public static ArrayList<UserDataServer> getALUserDataJSON(JSONArray data) throws Exception {
        try{
            ArrayList<UserDataServer> alUserDataServer = new ArrayList<>();
            for(int i=0; i<data.length(); i++){
                UserDataServer userDataServer = getUserDataJSON(data.getJSONObject(i));
                if(userDataServer !=null){ //Si es null es porque el nombre es propio de una IA
                    alUserDataServer.add(userDataServer);
                }
            }
            return alUserDataServer;
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getALServerDataUser (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getALServerDataUser (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getALServerDataUser (Exception): " + e.getMessage());
            throw e;
        }
    }

    public static UserDataServer getUserDataJSON(JSONObject jsoServerPlayer) throws Exception {
        try{
            UserDataServer userDataServer;

            //Excluye aquellos sockets sin nombre de usuario
            if(!jsoServerPlayer.getString(NAME_DATAUSER).equals(Constants.NOT_NAME_PLAYER)) {
                String id = jsoServerPlayer.getString(ID_DATAUSER);
                String name = jsoServerPlayer.getString(NAME_DATAUSER);
                String gameId = "";
                if (!jsoServerPlayer.isNull(GAME_ID_DATAUSER)) gameId = jsoServerPlayer.getString(GAME_ID_DATAUSER);
                int playerIndex = jsoServerPlayer.getInt(PLAYER_INDEX_DATAUSER);
                boolean online = jsoServerPlayer.getBoolean(ONLINE_DATAUSER);

                int userIcon = jsoServerPlayer.getInt(ICON_DATAUSER);

                userDataServer = new UserDataServer(id, name, gameId, playerIndex, userIcon, online);
                return userDataServer;
            }

            return null;
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getUserDataJSON (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getUserDataJSON (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getUserDataJSONº (Exception): " + e.getMessage());
            throw e;
        }
    }
}

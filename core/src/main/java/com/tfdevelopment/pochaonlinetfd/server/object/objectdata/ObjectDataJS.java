package com.tfdevelopment.pochaonlinetfd.server.object.objectdata;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.game.NewGameDataJSON;
import com.tfdevelopment.pochaonlinetfd.server.object.game.NewGameDataServer;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.UserDataJSON;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.UserDataServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ObjectDataJS {
    private static final String TAG = ObjectDataJS.class.getName();

    public static final String EX_USER_ICON = "updateUserIcon", EX_DATA_OBJECT = "dataObject";

    private static final String ARRAY_DATAUSER = "aDataUser";
    private static final String ARRAY_NEWGAMESDATA = "newGamesData";

    private Server nodeJS;

    public ObjectDataJS(Server nodeJS) {
        this.nodeJS = nodeJS;
    }

    public void eiDataObject(Object... args){
        Gdx.app.log(TAG, "eiDataObject");

        try {
            JSONObject data = (JSONObject)args[0];
            ArrayList<UserDataServer> alUserDataServer = UserDataJSON.getALUserDataJSON(data.getJSONArray(ARRAY_DATAUSER));
            ArrayList<NewGameDataServer> alNewGameDataServer = NewGameDataJSON.getALNewGameDataJSON(data.getJSONArray(ARRAY_NEWGAMESDATA));

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if(serverMainScreen != null) {
                serverMainScreen.setDataObject(alUserDataServer, alNewGameDataServer);
                Gdx.app.log(TAG, "eiDataObject - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiDataObject (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiDataObject (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiDataObject (Exception): " + e.getMessage());
        }
    }

    public void eoDataObject(){
        Gdx.app.log(TAG, "eoDataObject");
        nodeJS.getSocket().emit(EX_DATA_OBJECT);
    }

    public void eiUpdateUserIcon(Object... args) {
        Gdx.app.log(TAG, "eiUpdateUserIcon");

        try {
            JSONObject data = (JSONObject)args[0];
            String playerName = data.getString("playerName");
            int icon = data.getInt("icon");



            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if(serverMainScreen != null) {
                serverMainScreen.updateUserIcon(playerName, icon);
                Gdx.app.log(TAG, "eiUpdateUserIcon - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiUpdateUserIcon (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiUpdateUserIcon (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiUpdateUserIcon (Exception): " + e.getMessage());
        }
    }

    public void eoSelectUserIcon(int userIcon){
        Gdx.app.log(TAG, "eoSelectUserIcon");
        nodeJS.getSocket().emit(EX_USER_ICON, nodeJS.getServerUser().getName(), userIcon);
    }
}

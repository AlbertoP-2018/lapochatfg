package com.tfdevelopment.pochaonlinetfd.server.object.login;

import static com.tfdevelopment.pochaonlinetfd.server.object.news.NewsJS.NEWS_VERSION;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.screen.server.login.LoginScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.game.ServerGameDataJSON;
import com.tfdevelopment.pochaonlinetfd.server.object.game.ServerGameData;
import com.tfdevelopment.pochaonlinetfd.server.object.ServerObject;
import com.tfdevelopment.pochaonlinetfd.server.object.playerdata.ServerPlayer;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.ServerUser;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.UserDataJSON;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.UserDataServer;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginJS {
    private static final String TAG = LoginJS.class.getName();

    public static final String
        EX_SIGN_IN = "signIn", EX_SIGN_UP = "signUp", EX_ADD_USER = "addUser", EX_DELETE_USER = "deleteUser",
        EX_LOGIN_TO_HALL = "loginToHall", EX_LOGIN_TO_GAME = "loginToGame", EX_SERVER_NOT_AVAILABLE = "notAvailable",
        EX_INIT_PLAYER = "initPlayer";

    private Server nodeJS;

    public LoginJS(Server nodeJS) {
        this.nodeJS = nodeJS;
    }

    public void eiNotAvailable(Object... args){
        Gdx.app.log(TAG, "eiNotAvailable");

        boolean availableServer = true;
        String message = "El servidor se encuentra en mantenimiento.\nInténtelo más tarde.";

        try {
            JSONObject data = (JSONObject)args[0];

            availableServer = data.getBoolean("available");
            message = data.getString("message");

            Gdx.app.log(TAG, "ei_NotAvailable - OK");
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to ei_NotAvailable (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to ei_NotAvailable (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to ei_NotAvailable (Exception): " + e.getMessage());
        }

        nodeJS.getLoginScreen().availableServer_NJS(availableServer, message);
    }

    public void eiInitPlayer(Object... args){
        Gdx.app.log(TAG, "eiInitPlayer");
        try {
            JSONObject data = (JSONObject)args[0];

            String id = data.getString("id");
            String vAppNodeJS = data.getString("version");

            nodeJS.setServerUser(new ServerUser(id));
            nodeJS.setvAppNodeJS(vAppNodeJS);

            Gdx.app.log(TAG, "eiInitPlayer - OK\n Version: "+vAppNodeJS+" - ID Jugador: "+id);
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiInitPlayer (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiInitPlayer (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiInitPlayer (Exception): " + e.getMessage());
        }
    }

    public void eiSignIn(Object... args){
        Gdx.app.log(TAG, "eiSignIn");

        try {
            JSONObject data = (JSONObject)args[0];

            boolean correctUser = data.getBoolean("signIn");
            String userName = data.getString("userName");
            String info = data.getString("messageError");

            if(correctUser){
                nodeJS.getServerUser().setName(userName);
                nodeJS.setPaused(false);
                nodeJS.setPausedAtStarted(false);
            }
            nodeJS.getLoginScreen().signIn.signIn_NJS(correctUser, info);

            Gdx.app.log(TAG, "eiSignIn - OK");
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiSignIn (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiSignIn (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiSignIn (Exception): " + e.getMessage());
        }
    }

    public void eoSignIn(String playerName, String pass){
        Gdx.app.log(TAG, "eoSignIn");
        nodeJS.getServerUser().setName(playerName);
        nodeJS.getSocket().emit(EX_SIGN_IN, playerName, pass, Config.ADMIN_ON);
    }

    public void eiSignUp(Object... args){
        Gdx.app.log(TAG, "eiSignUp");
        boolean correctUser = true;
        String info = "";

        JSONObject data = (JSONObject)args[0];
        try {
            correctUser = data.getBoolean("signUp");
            info = data.getString("messageError");

            Gdx.app.log(TAG, "eiSignUp - OK");
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiSignUp: " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiSignUp (Exception): " + e.getMessage());
        }

        nodeJS.getLoginScreen().signUp.signUp_NJS(correctUser, info);
    }

    public void eoSignUp(String playerName, String pass){
        Gdx.app.log(TAG, "eoSignUp");
        nodeJS.getServerUser().setName(playerName);
        nodeJS.getSocket().emit(EX_SIGN_UP, playerName, pass);
    }

    public void eiAddUser(Object... args) {
        Gdx.app.log(TAG, "eiAddUser");

        try{
            JSONObject data = (JSONObject)args[0];

            UserDataServer userDataServer = UserDataJSON.getUserDataJSON(data.getJSONObject("newUser"));;
            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if (serverMainScreen != null ) {
                serverMainScreen.addUserDataServer(userDataServer);
                Gdx.app.log(TAG, "eiAddUser - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiAddUser (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiAddUser (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiAddUser (Exception): " + e.getMessage());
        }
    }

    public void eiDeleteUser(Object... args) {
        Gdx.app.log(TAG, "eiDeleteUser");

        try{
            JSONObject data = (JSONObject)args[0];
            String name = data.getString("userName");

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if (serverMainScreen != null ) {
                serverMainScreen.deleteUserDataServer(name);
                Gdx.app.log(TAG, "eiDeleteUser - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiDeleteUser (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiDeleteUser (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiDeleteUser (Exception): " + e.getMessage());
        }
    }

    public void eiLoginToHall(Object... args){
        Gdx.app.log(TAG, "eiLoginToHall");

        try{
            JSONObject data = (JSONObject)args[0];

            String newsVersion = data.getString(NEWS_VERSION);
            ServerObject serverObject = new ServerObject(newsVersion);

            nodeJS.getLoginScreen().loginToHall(serverObject);

            Gdx.app.log(TAG, "eiLoginToHall - OK");
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiLoginToHall (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiLoginToHall (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiLoginToHall (Exception): " + e.getMessage());
        }
    }

    public void eiLoginToGame(Object... args){
        Gdx.app.log(TAG, "eiLoginToGame");

        try{
            JSONObject data = (JSONObject)args[0];

            ServerUser serverUser = nodeJS.getServerUser();
            ServerGameData serverGameData = ServerGameDataJSON.getServerGame(data, serverUser.getName(), ServerGameDataJSON.ORIGIN_REQUEST.RECONNECT);

//            nodeJS.setPlayerNameSocket(serverUser.getName());
            String gameId = "";
            int playerIndex = -1;
            for (ServerPlayer serverPlayer : serverGameData.getGamePlayers()) {
                if (serverPlayer.getName().equals(serverUser.getName())) {
                    gameId = serverPlayer.getGameId();
                    playerIndex = serverPlayer.getPlayerIndex();
                }
            }
            serverUser.updateDataFromLoginToGame(gameId, playerIndex);

            LoginScreen loginScreen = nodeJS.getLoginScreen();
            if(loginScreen!=null){
                loginScreen.loginToGame(serverGameData);
                Gdx.app.log(TAG, "eiLoginToGame - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiLoginToGame (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiLoginToGame (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiLoginToGame (Exception): " + e.getMessage());
        }
    }
}

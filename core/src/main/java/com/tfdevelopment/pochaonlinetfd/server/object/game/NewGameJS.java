package com.tfdevelopment.pochaonlinetfd.server.object.game;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.ServerUser;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;

import org.json.JSONException;
import org.json.JSONObject;

public class NewGameJS {
    private static final String TAG = NewGameJS.class.getName();

    public static final String
        EX_NEW_GAME = "newGame", EX_DELETE_GAME = "deleteGame", EX_GAME_INVITATION = "gameInvitation",
        EX_ADD_PLAYER_GAME = "addPlayerGame", EX_REMOVE_PLAYER_GAME = "removePlayerGame",
        EX_HIDE_STARTED_GAME = "hideStartedGame";

    private Server nodeJS;

    public NewGameJS(Server nodeJS) {
        this.nodeJS = nodeJS;
    }

    public void eiNewGame(Object... args) {
        Gdx.app.log(TAG, "eiNewGame");
        try {
            JSONObject data = ((JSONObject)args[0]).getJSONObject("newGameData");
            NewGameDataServer newGameDataServer = NewGameDataJSON.getNewGameDataJSON(data);

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if(serverMainScreen!=null){
                serverMainScreen.addNewGame(newGameDataServer);
                Gdx.app.log(TAG, "eiNewGame - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiNewGame (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiNewGame (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiNewGame (Exception): " + e.getMessage());
        }
    }

    public void eoNewGame(int players, int bots, String mode) {
        Gdx.app.log(TAG, "eoNewGame");
        nodeJS.getSocket().emit(EX_NEW_GAME, players, bots, mode);
    }

    public void eiDeleteGame(Object... args) {
        Gdx.app.log(TAG, "eiDeleteGame");
        try {
            JSONObject data = (JSONObject)args[0];
            String id = data.getString("id");

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if (serverMainScreen != null) {
                serverMainScreen.deleteGame(id);
                Gdx.app.log(TAG, "eiDeleteGame - OK");
            }

        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiDeleteGame (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiDeleteGame (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiDeleteGame (Exception): " + e.getMessage());
        }
    }

    public void eiHideStartedGame(Object... args) {
        Gdx.app.log(TAG, "eiHideStartedGame");
        try {
            JSONObject data = (JSONObject)args[0];
            String id = data.getString("id");

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if (serverMainScreen!=null) {
                serverMainScreen.hideStartedGame(id);
                Gdx.app.log(TAG, "eiHideStartedGame - OK");
            }

        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiHideStartedGame (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiHideStartedGame (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiHideStartedGame (Exception): " + e.getMessage());
        }
    }

    public void eiAddPlayerGame(Object... args) {
        Gdx.app.log(TAG, "eiAddPlayerGame");
        try {
            JSONObject data = (JSONObject)args[0];
            String id = data.getString("id");
            String playerName = data.getString("playerName");

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if (serverMainScreen!=null) {
                serverMainScreen.addPlayerGame(id, playerName);
                Gdx.app.log(TAG, "eiAddPlayerGame - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiAddPlayerGame (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiAddPlayerGame (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiAddPlayerGame (Exception): " + e.getMessage());
        }
    }

    public void eoJoinGame(String gameId) {
        Gdx.app.log(TAG, "eoJoinGame");
        nodeJS.getSocket().emit(EX_ADD_PLAYER_GAME, gameId);
    }

    public void eiRemovePlayerGame(Object... args) {
        Gdx.app.log(TAG, "eiRemovePlayerGame");
        try {
            JSONObject data = (JSONObject)args[0];
            String id = data.getString("id");
            String playerName = data.getString("playerName");

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if (serverMainScreen!=null) {
                serverMainScreen.removePlayerGame(id, playerName);
                Gdx.app.log(TAG, "eiRemovePlayerGame - OK");
            }

        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiRemovePlayerGame (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiRemovePlayerGame (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiRemovePlayerGame (Exception): " + e.getMessage());
        }
    }

    public void eoLeaveGame() {
        Gdx.app.log(TAG, "eoLeaveGame");
        nodeJS.getSocket().emit(EX_REMOVE_PLAYER_GAME);
    }

    public void eiGameInvitation(Object... args) {
        Gdx.app.log(TAG, "eiGameInvitation");
        try {
            JSONObject data = (JSONObject)args[0];

            String playerName = data.getString("playerName");
            String gameId = data.getString("gameId");
            int remainingPlayers = data.getInt("remainingPlayers");

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if(PochaEnum.screenType == PochaEnum.ScreenType.ONLINE_GAME){
                Gdx.app.error(TAG, "TODO --> eiInvitation_Game()");
                OnlineGS onlineGS = nodeJS.getOnlineGS();
                onlineGS.setGameInvitation(playerName, gameId, remainingPlayers);

                //gsOnline.showInvitationGame(nameUser,table,remainingPlayers);
                //Gdx.app.log(TAG, "eiInvitation_Game - OK");
            } else if (PochaEnum.screenType == PochaEnum.ScreenType.SERVER_MAIN){
                serverMainScreen.showInvitationGame(playerName, gameId, remainingPlayers);
            }

            Gdx.app.log(TAG, "eiGameInvitation - OK");

        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiGameInvitation (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiGameInvitation (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiGameInvitation (Exception): " + e.getMessage());
        }
    }

    public void eoSendInvitation(String userName, int remainingPlayers) {
        Gdx.app.log(TAG, "eoSendInvitation");
        ServerUser serverUser = nodeJS.getServerUser();
        nodeJS.getSocket().emit(EX_GAME_INVITATION, serverUser.getName(), serverUser.getGameId(), userName, remainingPlayers);
    }
}

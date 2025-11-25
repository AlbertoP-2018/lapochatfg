package com.tfdevelopment.pochaonlinetfd.server.object.others;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;

import org.json.JSONException;
import org.json.JSONObject;

public class OthersJS {
    private static final String TAG = OthersJS.class.getName();

    public static final String
        EX_PULL_OUT_SERVER = "pullOutServer", EX_USER_PAUSE_GAME = "userPause",
        EX_USER_LEAVE = "userLeave", EX_SERVER_HANDLER = "serverHandler",
        EX_NOTICE_SERVER = "noticeServer";

    private Server nodeJS;

    public OthersJS(Server nodeJS) {
        this.nodeJS = nodeJS;
    }

    public void eiNoticeServer(Object... args){
        Gdx.app.log(TAG, "eiNoticeServer");
        try {
            JSONObject data = (JSONObject)args[0];

            String title = data.getString("title");
            title = " "+title+" ";
            String notice = (data.getString("notice").replace("\\n","AUX")).replace("AUX","\n");

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if(serverMainScreen!=null){
                serverMainScreen.setNoticeServer(title, notice);
                Gdx.app.log(TAG, "eiNoticeServer - OK");
            }

            OnlineGS onlineGS = nodeJS.getOnlineGS();
            if(onlineGS!=null){
                onlineGS.setNoticeServer(title, notice);
                Gdx.app.log(TAG, "eiNoticeServer - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiNoticeServer (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiNoticeServer (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiNoticeServer (Exception): " + e.getMessage());
        }
    }

    public void eoNoticeServer(String code, String title, String text){
        Gdx.app.log(TAG, "eoNoticeServer");
        nodeJS.getSocket().emit(EX_NOTICE_SERVER, code, title, text);
    }

    public void eoUserLeave(boolean fromMenu){
        Gdx.app.log(TAG, "eoUserLeave");
        nodeJS.getSocket().emit(EX_USER_LEAVE, fromMenu);
    }

    public void eoServerHandler(int logIndex){
        Gdx.app.log(TAG, "eoServerHandler");
        nodeJS.getSocket().emit(EX_SERVER_HANDLER, logIndex);
    }

    public void eoPullOutServer(String name){
        Gdx.app.log(TAG, "eo_pullOutServer");
        nodeJS.getSocket().emit(EX_PULL_OUT_SERVER, name);
    }

    public void eoUserPauseGame(boolean pause) {
        Gdx.app.log(TAG, "eoUserPauseGame");
        nodeJS.setPaused(pause);
        nodeJS.getSocket().emit(EX_USER_PAUSE_GAME, nodeJS.getServerUser().getName(), pause);

        if (!nodeJS.isPaused() && nodeJS.isPausedAtStarted()) {
            nodeJS.setPausedAtStarted(false);
            nodeJS.getServerGameJS().eoReconnectToGame(true);
        }
    }
}

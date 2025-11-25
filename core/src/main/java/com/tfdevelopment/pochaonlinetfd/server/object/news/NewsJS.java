package com.tfdevelopment.pochaonlinetfd.server.object.news;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsJS {
    private static final String TAG = NewsJS.class.getName();

    public static final String EX_NEWS = "news", NEWS_VERSION  = "newsVersion";

    private static final String ARRAY_NEWS  = "arrayNews";

    private Server nodeJS;

    public NewsJS(Server nodeJS) {
        this.nodeJS = nodeJS;
    }

    public void eiNews(Object... args){
        Gdx.app.log(TAG, "eiNews");
        try {
            JSONObject data = (JSONObject)args[0];
            String newsVersion = data.getString(NEWS_VERSION);
            ArrayList<NewsServer> alNews = NewsJSON.getALNews(data.getJSONArray(ARRAY_NEWS));

            ServerMainScreen serverMainScreen = nodeJS.getServerMainScreen();
            if(serverMainScreen!=null){
                serverMainScreen.setNews(newsVersion, alNews);
                Gdx.app.log(TAG, "eiNews - OK");
            }
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiNews (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiNews (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiNews (Exception): " + e.getMessage());
        }
    }

    public void eoGetNews(){
        Gdx.app.log(TAG, "eoGetNews");
        nodeJS.getSocket().emit(EX_NEWS);
    }
}

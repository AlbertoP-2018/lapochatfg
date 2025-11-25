package com.tfdevelopment.pochaonlinetfd.server.object.news;

import com.badlogic.gdx.Gdx;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class NewsJSON {
    private static final String TAG = NewsJSON.class.getName();

    public static ArrayList<NewsServer> getALNews(JSONArray dataNews) throws Exception {
        try {
            ArrayList<NewsServer> alNews = new ArrayList<>();
            String title, date, text;

            for(int i=0; i<dataNews.length(); i++){
                title = dataNews.getJSONObject(i).getString("title");
                date = dataNews.getJSONObject(i).getString("date");
                text = dataNews.getJSONObject(i).getString("text");

                alNews.add(new NewsServer(title, date, text));
            }

            return alNews;
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getALNews (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getALNews (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getALNews (Exception): " + e.getMessage());
            throw e;
        }
    }
}

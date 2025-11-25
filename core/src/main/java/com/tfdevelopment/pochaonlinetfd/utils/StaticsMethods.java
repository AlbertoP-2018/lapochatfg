package com.tfdevelopment.pochaonlinetfd.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;

import org.json.JSONArray;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class StaticsMethods {
    public static String getFormatRatio(float _ratio){
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", dfs);
        String sRatio = df.format(_ratio);
        switch (sRatio.length()){
            case 1:
                sRatio = sRatio.concat(".00");
                break;
            case 3:
                sRatio = sRatio.concat("0");
                break;
            case 4:
                sRatio = sRatio.concat("");
                break;
        }

        return sRatio;
    }

    public static NinePatchDrawable getNinePatchDrawable(TextureAtlas.AtlasRegion atlasRegion, int size){
        return new NinePatchDrawable(new NinePatch(atlasRegion, size, size, size, size));
    }

    public static String getConvertedScore(int[][][] scoreboard) {
        JSONArray jsonScoreboard = new JSONArray();
        for (int[][] score2 : scoreboard){
            JSONArray json2 = new JSONArray();
            for (int[] score1 : score2) {
                JSONArray json1 = new JSONArray();
                for (int point : score1) {
                    json1.put(point);
                }
                json2.put(json1);
            }
            jsonScoreboard.put(json2);
        }

        return jsonScoreboard.toString();
    }

    public static void showLogResize(String TAG) { Gdx.app.log(TAG, "Override: RESIZE ("+ PochaEnum.screenType+")"); }
    public static void showLogPause(String TAG) { Gdx.app.log(TAG, "Override: PAUSE ("+ PochaEnum.screenType+")"); }
    public static void showLogResume(String TAG) { Gdx.app.log(TAG, "Override: RESUME ("+PochaEnum.screenType+")"); }
    public static void showLogHide(String TAG) { Gdx.app.log(TAG, "Override: HIDE ("+PochaEnum.screenType+")"); }
    public static void showLogDispose(String TAG) { Gdx.app.log(TAG, "Override: DISPOSE ("+PochaEnum.screenType+")"); }
}

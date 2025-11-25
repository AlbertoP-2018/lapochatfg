package com.tfdevelopment.pochaonlinetfd.utils.conf;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Constants {
    public static final float VIEWPORT_WIDTH = 450f;
    public static final float VIEWPORT_HEIGHT = 800f;
    public static final float VIEWPORT_HEIGHT_GAME = getGameViewportHeight();

    public static final String URI_PLAYSTORE = "https://play.google.com/store/apps/details?id=com.tfdevelopment.pochaonlinetfd&gl=ES";
    public static final String URI_TWITTER = "https://twitter.com/TF_Development";
    public static final String NOT_NAME_PLAYER = "--null--";

    public static final String SKIN_UI = "skins/uiskin/uiskin.json";
    public static final String TA_UI = "skins/uiskin/uiskin.atlas";

    private static float getGameViewportHeight() {
        float heightBanner = 0f;
        if (Config.ADS_ON && Gdx.app.getType() == Application.ApplicationType.Android) {
            heightBanner = 95f / ((float)(Gdx.graphics.getHeight() / Gdx.app.getGraphics().getWidth()));
        }
        return VIEWPORT_HEIGHT + heightBanner;
    }
}

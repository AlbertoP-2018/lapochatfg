package com.tfdevelopment.pochaonlinetfd.utils.googleads;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class PochaGoogleAds {
    private static ActionResolver actionResolver;
    public static void setActionResolver(ActionResolver _actionResolver) { actionResolver = _actionResolver; }

    public static void showBannerAd(boolean show) { if (isEnable()) actionResolver.showBannerAd_Android(show); }
    public static void loadInterstitialAd() { if (isEnable()) actionResolver.loadInterstitialAd_Android(); }
    public static void showInterstitialAd() { if (isEnable()) actionResolver.showInterstitialAd_Android(); }

    private static boolean isEnable() { return actionResolver != null && Gdx.app.getType() == Application.ApplicationType.Android; }
}

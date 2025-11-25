package com.tfdevelopment.pochaonlinetfd.android;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.tfdevelopment.pochaonlinetfd.utils.googleads.ActionResolver;

public class AndroidActionResolver implements ActionResolver {
    private static final String TAG = ActionResolver.class.getName();

    private Activity activity;
    private AdView avBanner;
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;
    private RewardedInterstitialAd riaRewardedInterstitial;

    private Handler handler;

    public AndroidActionResolver(Activity activity, AdView avBanner){
        this.activity = activity;
        this.avBanner = avBanner;

        handler = new Handler();
    }

    @Override
    public void showBannerAd_Android(boolean show) {
        if(avBanner !=null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Gdx.app.log(TAG, "***Banner AD --> Show: "+show+"***");
                    if(show) avBanner.setVisibility(View.VISIBLE);
                    else avBanner.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void loadInterstitialAd_Android() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ((AndroidLauncher)activity).createInterstitialAd();
            }
        });
    }

    @Override
    public void showInterstitialAd_Android() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                interstitialAd = ((AndroidLauncher)activity).getIaInterstitial();

                if(interstitialAd!=null){
                    Gdx.app.error(TAG, "***InterstitialAD: SHOW***");
                    interstitialAd.show(activity);
                } else {
                    Gdx.app.error(TAG, "***InterstitialAD: The interstitial ad wasn't ready yet.");
                }
            }
        });
    }
}

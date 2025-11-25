package com.tfdevelopment.pochaonlinetfd.android;

import static com.tfdevelopment.pochaonlinetfd.utils.conf.Config.AD_TEST;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.tfdevelopment.pochaonlinetfd.PochaOnlineMain;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {

    private static final String TAG = AndroidLauncher.class.getName();

    private static final String ID_BANNER_TEST = "ca-app-pub-3940256099942544/6300978111";
    private static final String ID_INTERSTITIAL_TEST = "ca-app-pub-3940256099942544/1033173712";
    private static final String ID_INTERSTITIAL_VIDEO_TEST = "ca-app-pub-3940256099942544/8691691433";
    private static final String ID_REWARDED_TEST = "ca-app-pub-3940256099942544/5224354917";
    private static final String ID_REWARDED_INTERSTITIAL_TEST = "ca-app-pub-3940256099942544/5354046379";

    private static final String ID_GAME_BANNER = "ca-app-pub-6070995768137331/5303791047"; //ad1_GameBanner
    private static final String ID_GAME_FINISHED_INTERSTITIAL = "ca-app-pub-6070995768137331/3202702896"; //ad1_FinishedGame

    private static final String AD1_GAME_BANNER = (AD_TEST ? ID_BANNER_TEST : ID_GAME_BANNER); //ad2_GameBanner
    private static final String AD2_GAMEFINISHED_INTERSTITIAL = (AD_TEST ? ID_INTERSTITIAL_VIDEO_TEST : ID_GAME_FINISHED_INTERSTITIAL); //ad2_FinishedGame

    private AndroidActionResolver actionResolver;

    private View gameView; //Banner
    private AdView avBanner; //Banner

    private InterstitialAd iaInterstitial;	//Interstitial
    private RewardedAd raRewarded; //Rewarded
    private RewardedInterstitialAd riaRewardedInterstitial; //Rewarded Interstitial

    public InterstitialAd getIaInterstitial(){ return iaInterstitial; }
    public RewardedAd getRaRewarded(){ return raRewarded; }
    public RewardedInterstitialAd getRiaRewardedInterstitial(){ return riaRewardedInterstitial; }

    //RGPD
    private ConsentInformation consentInformation;
    private ConsentForm consentForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();

        createBannerAd();

        //Se lanza la aplicación - LIBGDX
        actionResolver = new AndroidActionResolver(this, avBanner);

        configuration.useImmersiveMode = true; // Recommended, but not required.
        gameView = initializeForView(new PochaOnlineMain(actionResolver, false), configuration); //initialize(new PochaOnlineMain(actionResolver), config);

        muteAds();
        configureBannerAd();

        //RGPD
        configureRGPD();
    }

    private void muteAds(){
        MobileAds.initialize(this.getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        MobileAds.setAppMuted(true);
    }

    /******* Banner - Ad *******/
    private void createBannerAd(){
        //Se crea el banner - AdMob
        avBanner = new AdView(this);
        avBanner.setVisibility(View.INVISIBLE);
        avBanner.setAdUnitId(AD1_GAME_BANNER);
        avBanner.setAdSize(getAdSize());
        //avBanner.setAdSize(AdSize.BANNER);
    }
    private void configureBannerAd(){
        RelativeLayout layout = new RelativeLayout(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        avBanner.loadAd(adRequest);
        layout.addView(gameView);
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
        avBanner.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        adParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layout.addView(avBanner, adParams);
        setContentView(layout);
    }
    //Permite establecer el tamaño del banner (Banner Adaptativo)
    private AdSize getAdSize(){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this,adWidth);
    }

    /******* Interstitial - Ad *******/
    public void createInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this.getContext(), AD2_GAMEFINISHED_INTERSTITIAL, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Gdx.app.error(TAG, "***InterstitialAD: Interstitial Failed*******");
                iaInterstitial = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                Gdx.app.log(TAG, "***InterstitialAD: Interstitial Loaded*******");
                iaInterstitial = interstitialAd;

                iaInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        Gdx.app.log(TAG, "***InterstitialAD: Interstitial Clicked*******");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        Gdx.app.error(TAG, "***InterstitialAD: Ad dismissed to show fullscreen content*******");
                        iaInterstitial = null;
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        Gdx.app.error(TAG, "***InterstitialAD: Ad failed to show fullscreen content*******");
                        iaInterstitial = null;
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        Gdx.app.log(TAG, "***InterstitialAD: Ad recorded an impression.*******");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        Gdx.app.log(TAG, "***InterstitialAD: Ad showed fullscreen content.********");
                    }
                });
            }
        });
    }

    /******* Rewarded - Ad *******/
    public void createRewardedAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this.getContext(), ID_REWARDED_TEST, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                raRewarded = null;
                Gdx.app.error(TAG, "***RewardedAD: Rewarded Failed*******");
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                Gdx.app.log(TAG, "***RewardedAD: Rewarded Loaded****");
                raRewarded = rewardedAd;

                //Permite controlar los eventos relacionados con la visualización
                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        Gdx.app.log(TAG, "***RewardedAD: Rewarded Clicked******");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        Gdx.app.error(TAG, "***RewardedAD: Ad dismissed to show fullscreen content*******");
                        raRewarded = null;
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        Gdx.app.error(TAG, "***RewardedAD: Ad failed to show fullscreen content.********");
                        raRewarded = null;
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        Gdx.app.log(TAG, "***RewardedAD: Ad recorded an impression.*******");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        Gdx.app.log(TAG, "***RewardedAD: Ad showed fullscreen content.********");
                    }
                });
            }
        });
    }

    /******* Rewarded Interstitial - Ad *******/
    public void createRewardedInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(this.getContext(), ID_REWARDED_INTERSTITIAL_TEST, adRequest, new RewardedInterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Gdx.app.error(TAG, "***RewardedInterstitialAD: Rewarded Failed*******");
                riaRewardedInterstitial = null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                super.onAdLoaded(rewardedInterstitialAd);
                Gdx.app.error(TAG, "***RewardedInterstitialAD: Rewarded Loaded*******");
                riaRewardedInterstitial = rewardedInterstitialAd;

                //Permite controlar los eventos relacionados con la visualización
                rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        Gdx.app.log(TAG, "***RewardedInterstitialAD: Rewarded Clicked******");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        Gdx.app.error(TAG, "***RewardedInterstitialAD: Ad dismissed to show fullscreen content.*******");
                        riaRewardedInterstitial = null;
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        Gdx.app.error(TAG, "***RewardedInterstitialAD: Ad failed to show fullscreen content.********");
                        riaRewardedInterstitial = null;
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        Gdx.app.log(TAG, "***RewardedInterstitialAD: Ad recorded an impression.*******");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        Gdx.app.log(TAG, "***RewardedInterstitialAD: Ad showed fullscreen content.********");
                    }
                });
            }
        });
    }

    /**** Mensaje conforme al RGPD () ****/
    private void configureRGPD(){
        // Set tag for under age of consent. false means users are not under age.
        ConsentRequestParameters params = new ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build();
        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(this, params,
            new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
                @Override
                public void onConsentInfoUpdateSuccess() {
                    Gdx.app.log(TAG, "***ConfigureRGPD: onConsentInfoUpdateSuccess.********");
                    // The consent information state was updated.
                    // You are now ready to check if a form is available.
                    if(consentInformation.isConsentFormAvailable()){
                        Gdx.app.log(TAG, "***ConfigureRGPD: onConsentInfoUpdateSuccess: Available ********");
                        loadForm();
                    }
                }
            },
            new ConsentInformation.OnConsentInfoUpdateFailureListener() {
                @Override
                public void onConsentInfoUpdateFailure(@NonNull FormError formError) {
                    // Handle the error.
                    Gdx.app.error(TAG, "***ConfigureRGPD: onConsentInfoUpdateFailure.********");
                }
            }
        );
    }
    private void loadForm(){
        // Loads a consent form. Must be called on the main thread.
        UserMessagingPlatform.loadConsentForm(this,
            new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
                @Override
                public void onConsentFormLoadSuccess(@NonNull ConsentForm consentForm) {
                    Gdx.app.log(TAG, "***ConfigureRGPD: onConsentFormLoadSuccess ********");
                    AndroidLauncher.this.consentForm = consentForm;
                    if(consentInformation.getConsentStatus()==ConsentInformation.ConsentStatus.REQUIRED){
                        Gdx.app.log(TAG, "***ConfigureRGPD: onConsentFormLoadSuccess: REQUIRED ********");
                        consentForm.show(
                            AndroidLauncher.this,
                            new ConsentForm.OnConsentFormDismissedListener() {
                                @Override
                                public void onConsentFormDismissed(@Nullable FormError formError) {
                                    Gdx.app.log(TAG, "***ConfigureRGPD: onConsentFormDismissed ********");
                                    if(consentInformation.getConsentStatus()==ConsentInformation.ConsentStatus.OBTAINED){
                                        // App can start requesting ads.
                                        Gdx.app.log(TAG, "***ConfigureRGPD: onConsentFormDismissed: OBTAINED ********");
                                    }

                                    // Handle dismissal by reloading form.
                                    loadForm();
                                }
                            }
                        );
                    }
                }
            },
            new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
                @Override
                public void onConsentFormLoadFailure(@NonNull FormError formError) {
                    // Handle the error.
                    Gdx.app.error(TAG, "***ConfigureRGPD: onConsentFormLoadFailure ********");
                }
            });
    }
}

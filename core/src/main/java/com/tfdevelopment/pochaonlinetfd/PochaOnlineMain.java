package com.tfdevelopment.pochaonlinetfd;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.tfdevelopment.pochaonlinetfd.screen.menu.MenuScreen;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.utils.googleads.ActionResolver;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.googleads.PochaGoogleAds;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;

public class PochaOnlineMain extends Game {
	private static final String TAG = PochaOnlineMain.class.getName();

	public PochaOnlineMain(ActionResolver ar, boolean testing) {
        PochaGoogleAds.setActionResolver(ar);
        Config.TESTING = testing;
    }

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.log(TAG,"*Pocha Online TFD* Running...");

		AssetLoader.aLoader.init(new AssetManager());
		Settings.load();

		Gdx.input.setCatchKey(Input.Keys.BACK,true);
		setScreen(new MenuScreen(this));
	}

    @Override
    public void dispose() {
        super.dispose();
        Gdx.app.log(TAG, "Override: DISPOSE");
        AssetLoader.aLoader.dispose();
    }
}

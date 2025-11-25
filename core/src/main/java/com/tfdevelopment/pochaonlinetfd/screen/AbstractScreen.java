package com.tfdevelopment.pochaonlinetfd.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.ScreenType;
import com.tfdevelopment.pochaonlinetfd.utils.googleads.PochaGoogleAds;

public abstract class AbstractScreen implements Screen {
    private static final String TAG = AbstractScreen.class.getName();

    private final Game game;
    private Camera camera;
    private Viewport viewport;
    private Stage stage;
    private final Skin skin;

    protected AbstractScreen(Game game){
        this.game = game;
        this.skin = new Skin(Gdx.files.internal(Constants.SKIN_UI), new TextureAtlas(Constants.TA_UI));

        PochaGoogleAds.showBannerAd(false);
    }

    @Override
    public void pause() { StaticsMethods.showLogPause(TAG); }

    @Override
    public void resume() { StaticsMethods.showLogResume(TAG); }

    @Override
    public void hide() { StaticsMethods.showLogHide(TAG); }

    @Override
    public void dispose() {
        StaticsMethods.showLogDispose(TAG);
        PochaGoogleAds.showBannerAd(false);
        this.skin.dispose();
        this.stage.dispose();
    }

    public void initScreen(ScreenType screenType, Camera camera, float viewportWidth, float viewportHeight) {
        PochaEnum.screenType = screenType;
        this.viewport = camera == null ? new FitViewport(viewportWidth, viewportHeight) : new FitViewport(viewportWidth, viewportHeight, camera);
        this.camera = this.viewport.getCamera();
        this.stage = new Stage(this.viewport);

        Gdx.input.setInputProcessor(this.stage);
    }

    public void changeScreen(Screen _screen, boolean transition) {
        Runnable action = () -> {
            this.game.setScreen(_screen);
            this.dispose();
        };

        if (transition) {
            this.stage.addAction(Actions.sequence(
                Actions.fadeOut(0.3f),
                Actions.alpha(0),
                Actions.run(action))
            );
        } else this.stage.addAction(Actions.run(action));
    }

    public Game getGame() { return this.game; }
    public Camera getCamera() { return this.camera; }
    public Viewport getViewport() { return this.viewport; }
    public Stage getStage() { return this.stage; }
    public Skin getSkin() { return this.skin; }
}

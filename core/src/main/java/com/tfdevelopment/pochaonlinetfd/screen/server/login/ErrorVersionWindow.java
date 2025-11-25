package com.tfdevelopment.pochaonlinetfd.screen.server.login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class ErrorVersionWindow {

    private LoginScreen loginScreen;
    Window wErrorVersion;

    public ErrorVersionWindow(LoginScreen loginScreen){
        this.loginScreen = loginScreen;
    }

    public void render(float delta){
        updateWindowVersion();
    }

    private void updateWindowVersion(){
        //Ventana de error de versión (Versión desactualizada)
        if(loginScreen.getNodeJS()!=null && loginScreen.getNodeJS().isSocketConnected() && !loginScreen.getNodeJS().getvAppNodeJS().equals(Config.VERSION))
            wErrorVersion.setVisible(true);
        else wErrorVersion.setVisible(false);
    }

    Table buildWindowErrorVersion(){
        wErrorVersion = new Window("Versión desactualizada", StyleConfigurator.getWS_Maiandra80(loginScreen.getSkin()));
        wErrorVersion.setVisible(false);
        wErrorVersion.setMovable(false);
        wErrorVersion.setResizable(false);
        wErrorVersion.getTitleLabel().setAlignment(Align.center);

        String message =
                "La versión actual no corresponde con la última\n"+
                        "versión disponible. Para actualizar la\n"+
                        "aplicación siga los siguientes pasos:";
        String android =
                "-App móvil (Android)\n"+
                        "   Para actualizar la aplicación acceda a la\n"+
                        "   PlayStore pulsando la siguiente imagen:";
        Label lMessage = new Label(message, StyleConfigurator.getLS_Maiandra65());
        Label lAndroid = new Label(android, StyleConfigurator.getLS_Maiandra65());
        Button bPlayStore = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getbPlayStore()));
        bPlayStore.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(Constants.URI_PLAYSTORE);
            }
        });
        TextButton tbOk = new TextButton("Volver al menú", StyleConfigurator.getTBS_RoundButton60(loginScreen.getSkin()));
        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                loginScreen.disconnectedServer(true);
            }
        });

        Table layerVersion = new Table();
        layerVersion.setFillParent(true);
        wErrorVersion.add(lMessage).center().space(10f);
        wErrorVersion.row();
        wErrorVersion.add(lAndroid).left().space(10f);
        wErrorVersion.row();
        wErrorVersion.add(bPlayStore).center().size(200,70).space(5f);
        wErrorVersion.row();
        wErrorVersion.add(tbOk).center().space(20f);
        layerVersion.add(wErrorVersion);

        return layerVersion;
    }
}

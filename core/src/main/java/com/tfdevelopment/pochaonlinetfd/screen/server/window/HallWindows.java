package com.tfdevelopment.pochaonlinetfd.screen.server.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class HallWindows {
    private static final String TAG = HallWindows.class.getName();

    private ServerMainScreen serverMainScreen;

    private HW_Notice noticeHW;
    private HW_Invitation invitationHW;

    private Window wServerDisconnected, wErrorVersion;

    public HallWindows(ServerMainScreen serverMainScreen){
        this.serverMainScreen = serverMainScreen;

        this.noticeHW = new HW_Notice(serverMainScreen, this);
        this.invitationHW = new HW_Invitation(serverMainScreen, this);
    }

    public void addWindows(Stack stack){
        stack.add(buildWindowErrorVersion());
        stack.add(invitationHW.buildOKInvitationGame());
        stack.add(invitationHW.buildSendInvitationGame());

        //Ventana de noticias
        stack.add(noticeHW.buildWindowNoticeServer());
        stack.add(noticeHW.buildWindowNoticeTournament());

        stack.add(buildWindowServerDisconnected());
    }

    private Table buildWindowErrorVersion(){
        wErrorVersion = new Window("Versión desactualizada", StyleConfigurator.getWS_Maiandra80(serverMainScreen.getSkin()));
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
        String desktop =
                "-App de escritorio (PC)\n"+
                        "   Para obtener la última versión de escritorio,\n"+
                        "   por favor, póngase en contacto con el\n"+
                        "   administrador a través del siguiente correo\n"+
                        "   electrónico:\n" +
                        "   TFDevelopment.Contact@gmail.com";
        Label lMessage = new Label(message, StyleConfigurator.getLS_Maiandra65());
        Label lAndroid = new Label(android, StyleConfigurator.getLS_Maiandra65());
        Button bPlayStore = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getbPlayStore()));
        bPlayStore.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(Constants.URI_PLAYSTORE);
            }
        });
        Label lDesktop = new Label(desktop, StyleConfigurator.getLS_Maiandra65());
        TextButton tbOk = new TextButton("Volver al menú", StyleConfigurator.getTBS_Maiandra65(serverMainScreen.getSkin()));
        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                serverMainScreen.disconnectedServer(true);
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
        wErrorVersion.add(lDesktop).left().space(10f);
        wErrorVersion.row();
        wErrorVersion.add(tbOk).center().space(20f);
        layerVersion.add(wErrorVersion);

        return layerVersion;
    }

    public Table buildWindowServerDisconnected(){
        wServerDisconnected = new Window("Error de conexión", StyleConfigurator.getWS_Maiandra80(serverMainScreen.getSkin()));
        wServerDisconnected.setVisible(false);
        wServerDisconnected.setMovable(false);
        wServerDisconnected.setResizable(false);
        wServerDisconnected.getTitleLabel().setAlignment(Align.center);

        Label lServerDisconnected = new Label("Se ha perdido la conexión con el servidor.", StyleConfigurator.getLS_Maiandra65());
        TextButton tbExit = new TextButton("Salir", StyleConfigurator.getTBS_Maiandra65(serverMainScreen.getSkin()));

        tbExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                serverMainScreen.disconnectedServer(true);
            }
        });

        Table layerReturnMenu = new Table();
        layerReturnMenu.setFillParent(true);
        wServerDisconnected.add(lServerDisconnected).space(10f);
        wServerDisconnected.row();
        wServerDisconnected.add(tbExit).space(10f);
        wServerDisconnected.row();
        layerReturnMenu.add(wServerDisconnected);

        return layerReturnMenu;
    }

    public void updateWindowVersion(){
        //Ventana de error de versión (Versión desactualizada)
        if(serverMainScreen.getNodeJS()!=null && serverMainScreen.getNodeJS().isSocketConnected() && !serverMainScreen.getNodeJS().getvAppNodeJS().equals(Config.VERSION))
            wErrorVersion.setVisible(true);
        else wErrorVersion.setVisible(false);
    }

    public void setStyleWindow(Window window){
        window.setColor(new Color(50f/255f,75f/255f,250f/255f,1f));
        window.getTitleLabel().setAlignment(Align.center);
    }

    public HW_Notice getNoticeHW(){ return noticeHW; }
    public HW_Invitation getInvitationHW(){ return invitationHW; }
    public Window getwServerDisconnected(){ return wServerDisconnected; }
}

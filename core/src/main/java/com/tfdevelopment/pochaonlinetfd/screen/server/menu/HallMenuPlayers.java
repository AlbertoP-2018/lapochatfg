package com.tfdevelopment.pochaonlinetfd.screen.server.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.game.NewGameDataServer;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.UserDataServer;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.ServerUser;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

import java.util.ArrayList;

public class HallMenuPlayers {
    private static final String TAG = HallMenuPlayers.class.getName();

    private static final float tbSendInvitation_Width = 0f;
    private static final float tbSendInvitation_Height = ServerMainScreen.content_Height /40f;

    private ServerMainScreen serverMainScreen;

    private List<String> lUsers;
    private List<String> lInGame;
    private VerticalGroup vgInvitations;

    private Table tPlayersContent;
    private ScrollPane spPlayers;
    private Table tLoading;
    private Image iLoad;

    public HallMenuPlayers(ServerMainScreen serverMainScreen){
        this.serverMainScreen = serverMainScreen;
    }

    Table buildMenuPlayers(){
        tPlayersContent = new Table();
        tPlayersContent.top();

        buildActors();
        buildChargeIcon();

        //Pila con Background, Contenido y Loading
        Stack sAllContent = new Stack();
        sAllContent.setVisible(true);
        sAllContent.add(buildBackground());
        sAllContent.add(spPlayers);
        sAllContent.add(tLoading);

        //Contiene la pila con Contenido y Loading
        Table tAllContent = new Table();
        tAllContent.setVisible(false);
        tAllContent.setDebug(false);
        tAllContent.top();
        tAllContent.padTop(ServerMainScreen.content_PadTop*0.8f);
        tAllContent.add(sAllContent).size(ServerMainScreen.content_Width, ServerMainScreen.content_Height);
        return tAllContent;
    }

    public void updatePlayers(){
        tLoading.setVisible(true);
        spPlayers.setVisible(false);

        if(lUsers!=null && serverMainScreen.getNodeJS()!=null && serverMainScreen.getNodeJS().isSocketConnected() &&
                serverMainScreen.getNodeJS().getServerUser()!=null && serverMainScreen.getAlUserDataServer()!=null
                && serverMainScreen.getAlUserDataServer().size()>0) {
            tLoading.setVisible(false);
            spPlayers.setVisible(true);

            final ArrayList<UserDataServer> _alUserDataServer = (ArrayList<UserDataServer>)serverMainScreen.getAlUserDataServer().clone();

            tPlayersContent.clear();
            tPlayersContent.padTop(ServerMainScreen.content_Height*0.01f);
            for (int i = 0; i < _alUserDataServer.size(); i++) {
                UserDataServer uds = _alUserDataServer.get(i);
                //Añade únicamente los jugadores, no las IAs (ni los usuarios que no están online
                if (!uds.getName().contains("CPU")) { //&& !uds.getName().contains("Bot")) {
                    tPlayersContent.add(getRowPlayer(uds)).padBottom(ServerMainScreen.content_Height*0.01f);
                    tPlayersContent.row();
                }
            }
        }
    }

    public void updateLoadingAnimation(){
        if(iLoad!=null){
            if(tLoading.isVisible()) {
                iLoad.setOrigin(iLoad.getWidth() / 2, iLoad.getHeight() / 2);
                iLoad.rotateBy(-5);
            }
        }
    }

    private void buildActors(){
        buildScrollPane();

        lUsers = new List<>(StyleConfigurator.getLS_HallPlayers(serverMainScreen.getSkin()));
        lInGame = new List<>(StyleConfigurator.getLS_HallPlayers(serverMainScreen.getSkin()));
        vgInvitations = new VerticalGroup();
    }

    private Table getRowPlayer(UserDataServer sdu){
        String info = getPlayerInfo(sdu);
        final String userName = sdu.getName();
        int iconIndex = sdu.getUserIcon();

        //Table
        Image image = new Image(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getUserIcon(iconIndex,0)));
        Label lName = new Label(userName, StyleConfigurator.getLS_Maiandra50());
        Label lInfo = new Label(info, StyleConfigurator.getLS_Maiandra50()); lInfo.setAlignment(Align.center);
        ImageButton iSend = new ImageButton(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiSendInvitation()));

        if(!serverMainScreen.getNodeJS().getServerUser().getName().equals(userName)){
            iSend.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Server nodeJS = serverMainScreen.getNodeJS();
                    if (nodeJS != null && nodeJS.isSocketConnected()) {
                        ServerUser serverUser = nodeJS.getServerUser();
                        boolean inGame = serverUser.isInGame();
                        if (inGame) {
                            int remainingPlayers = 0;
                            ArrayList<NewGameDataServer> alNGDS = serverMainScreen.getALNewGameDataServer();
                            for (NewGameDataServer ngds : alNGDS) {
                                if (ngds.getId() == serverUser.getGameId()) {
                                    remainingPlayers = ngds.getTotalPlayers()-ngds.getNumPlayers();
                                    break;
                                }
                            }
                            nodeJS.getNewGameJS().eoSendInvitation(userName, remainingPlayers);
                        }
                        serverMainScreen.getHallWindows().getInvitationHW().showOKInvitationGame(inGame);
                    }
                }
            });
        } else iSend.setVisible(false);

        Table tContentRow = new Table();
        tContentRow.setDebug(false);
        tContentRow.add(image).size(ServerMainScreen.content_Width*0.06f,ServerMainScreen.content_Height*0.038f).left();
        tContentRow.add(lName).width(ServerMainScreen.content_Width*0.48f).left().padLeft(ServerMainScreen.content_Width*0.02f).left();
        tContentRow.add(lInfo).width(ServerMainScreen.content_Width*0.3f).left().padRight(ServerMainScreen.content_Width*0.01f).center();
        tContentRow.add(iSend).size(ServerMainScreen.content_Width*0.06f,ServerMainScreen.content_Height*0.032f)
                .padLeft(ServerMainScreen.content_Width*0.02f).right();
        if (sdu.getName().contains("Bot")) iSend.setVisible(false);

        //Background
        NinePatchDrawable npd = StaticsMethods.getNinePatchDrawable(AssetLoader.aLoader.aImage.getBackgroundPlayer(),10);
        Table tBackground = new Table();
        tBackground.setDebug(false);
        tBackground.add(new Image(npd));

        //Stack Content Row
        Stack stackRow = new Stack();
        stackRow.add(tBackground);
        stackRow.add(tContentRow);

        //All content
        Table table = new Table();
        table.setDebug(false);
        table.add(stackRow).size(ServerMainScreen.content_Width*0.98f, ServerMainScreen.content_Height*0.042f);
        return table;
    }

    private void buildScrollPane(){
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle(null,null,null,null,null);
        spPlayers = new ScrollPane(tPlayersContent, scrollPaneStyle);
        spPlayers.setVisible(true);
        spPlayers.setScrollingDisabled(true, false);
        spPlayers.setFadeScrollBars(true);
        spPlayers.layout();
    }

    private void buildChargeIcon(){
        TextureRegion textureRegion = new TextureRegion(AssetLoader.aLoader.aImage.getiLoad());
        textureRegion.flip(true, false);
        iLoad = new Image(textureRegion);
        iLoad.setColor(Color.GREEN);
        tLoading = new Table();
        tLoading.add(iLoad).size(ServerMainScreen.content_Width*0.15f, ServerMainScreen.content_Height*0.09f);
    }

    private Table buildBackground(){
        Image iBackground = new Image(StaticsMethods.getNinePatchDrawable(AssetLoader.aLoader.aImage.getRoundLightGrey(),10));
        Table tBackground = new Table();
        tBackground.setDebug(false);
        tBackground.setVisible(true);
        tBackground.add(iBackground).size(ServerMainScreen.content_Width, ServerMainScreen.content_Height);
        return tBackground;
    }

    private String getPlayerInfo(UserDataServer serverPlayer){
        String gameId = serverPlayer.getGameId();
        if(!gameId.isEmpty()) {
            for (NewGameDataServer ngds : serverMainScreen.getALNewGameDataServer()) {
                if (ngds.getId().equals(gameId)) {
                    String players = ngds.getNumPlayers() + "/" + ngds.getTotalPlayers();

                    if (ngds.isCompleted()) return "[RED]" + serverPlayer.getGameId() + " - " + players + "[RED]";
                    else return "[GREEN]" + serverPlayer.getGameId() + " - " + players + "[GREEN]";
                }

            }
        }
        return "";
    }
}

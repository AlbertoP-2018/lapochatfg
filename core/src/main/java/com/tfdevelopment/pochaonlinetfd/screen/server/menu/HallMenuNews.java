package com.tfdevelopment.pochaonlinetfd.screen.server.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.news.NewsServer;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

import java.util.ArrayList;
import java.util.Collections;

public class HallMenuNews {
    private static final String TAG = HallMenuNews.class.getName();

    private ServerMainScreen serverMainScreen;

    private Table newsContent, tLoading;
    private ScrollPane spContent;
    private Image iLoad;

    public HallMenuNews(ServerMainScreen serverMainScreen){
        this.serverMainScreen = serverMainScreen;
    }

    Table buildMenuNews(){
        newsContent = new Table();
        newsContent.setDebug(false);
        newsContent.setVisible(true);

        buildScrollPane();
        buildChargeIcon();

        //Pila con Background, Contenido y Loading
        Stack sAllContent = new Stack();
        sAllContent.setVisible(true);
        sAllContent.add(buildBackground());
        sAllContent.add(spContent);
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

    private Table getLayerBackground(float height){
        Button bBackground = new Button(StaticsMethods.getNinePatchDrawable(AssetLoader.aLoader.aImage.getBlackboard(), 10));
        Table layerBackground = new Table();
        layerBackground.setDebug(false);
        layerBackground.setFillParent(true);
        layerBackground.add(bBackground).size(ServerMainScreen.content_Width, height);

        return layerBackground;
    }

    public void updateLoadingAnimation(){
        if(iLoad!=null){
            if(iLoad.isVisible()) {
                iLoad.setOrigin(iLoad.getWidth() / 2, iLoad.getHeight() / 2);
                iLoad.rotateBy(-5);
            }
        }
    }

    public void updateNews(){
        tLoading.setVisible(true);
        spContent.setVisible(false);

        Server snJS = serverMainScreen.getNodeJS();
        ArrayList<NewsServer> alNews = serverMainScreen.getAlNews();
        if(snJS!=null && snJS.isSocketConnected() && serverMainScreen.getAlNews().size()>0){
            tLoading.setVisible(false);
            spContent.setVisible(true);

            if(!Settings.getNewsVersion().equals(serverMainScreen.getNewsVersion())){
                Settings.setNewsVersion(serverMainScreen.getNewsVersion());
                serverMainScreen.getHallMenuButtons().getIMHActor(2).updateInfo(false,"");
                serverMainScreen.getHallMenuButtons().listenerNews(true);
            }

            newsContent.clearChildren();
            newsContent.clear();
            Collections.reverse(alNews);
            for(NewsServer newsServer : alNews){
                Label lTitle = new Label("[#4A93FF]"+ newsServer.getTitle(), StyleConfigurator.getLS_Maiandra70());
                Label lDate = new Label("[#4A93FF]"+ newsServer.getDate(), StyleConfigurator.getLS_Maiandra50());
                Label lText = new Label("[WHITE]"+ newsServer.getText(), StyleConfigurator.getLS_Maiandra65());
                Label lReview = new Label("¡Pulsa el icono para dejar una valoración!", StyleConfigurator.getLS_Maiandra65());
                Button bPlayStore = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getbPlayStore()));
                bPlayStore.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.net.openURI(Constants.URI_PLAYSTORE);
                    }
                });
                lTitle.setAlignment(Align.center);
                lDate.setAlignment(Align.center);
                lText.setAlignment(Align.center);
                lReview.setAlignment(Align.center);

                Table table = new Table();
                table.setDebug(false);
                if(newsServer.getTitle().equals("¡Mus Online!")) table.padTop(30f);
                else table.padTop(30f);
                table.add(lTitle).center();
                table.add(lDate).right().padRight(5f);
                table.row();
                table.add(lText).width(ServerMainScreen.content_Width *0.9f).center().colspan(2);
                if(newsServer.getTitle().equals("¡Mus Online!")){
                    table.row();
                    table.add(lReview).padTop(25f).colspan(2);
                    table.row();
                    table.add(bPlayStore).size(ServerMainScreen.hall_Width*0.5f,ServerMainScreen.hall_Height*0.12f).colspan(2);
                }
                table.padBottom(25f);

                Stack stack = new Stack();
                stack.add(getLayerBackground(table.getMinHeight()));
                stack.add(table);

                newsContent.add(stack).padBottom(15f);
                newsContent.row();
            }
            newsContent.add(new Actor()).expandY().colspan(2);

        }
    }

    private void buildScrollPane(){
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle(null,null,null,null,null);
        spContent = new ScrollPane(newsContent, scrollPaneStyle);
        spContent.setVisible(true);

        spContent.setScrollingDisabled(true, false);
        spContent.setFadeScrollBars(true);
        spContent.layout();
    }

    private Table buildBackground(){
        //Background
        Image iBackground = new Image(StaticsMethods.getNinePatchDrawable(AssetLoader.aLoader.aImage.getRoundLightGrey(),10));
        Table tBackground = new Table();
        tBackground.setDebug(false);
        tBackground.setVisible(true);
        tBackground.add(iBackground).size(ServerMainScreen.content_Width, ServerMainScreen.content_Height);
        return tBackground;
    }

    private void buildChargeIcon(){
        TextureRegion textureRegion = new TextureRegion(AssetLoader.aLoader.aImage.getiLoad());
        textureRegion.flip(true, false);
        iLoad = new Image(textureRegion);
        iLoad.setColor(Color.GREEN);
        tLoading = new Table();
        tLoading.add(iLoad).size(ServerMainScreen.content_Width*0.15f, ServerMainScreen.content_Height*0.09f);
    }
}

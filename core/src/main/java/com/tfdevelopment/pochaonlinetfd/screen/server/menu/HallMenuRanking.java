package com.tfdevelopment.pochaonlinetfd.screen.server.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.object.ranking.RankingUserDataServer;
import com.tfdevelopment.pochaonlinetfd.server.object.ServerObject;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

import java.util.ArrayList;

public class HallMenuRanking {
    private static final String TAG = HallMenuRanking.class.getName();

    private static final Color COLOR_SERVERMAINSCREEN = new Color(24/255f, 108/255f, 227/255f,1f);
    private static final String SEASONS = "CLASIFICACIÓN";

    private ServerMainScreen serverMainScreen;
    private HallMenuRanking_Season hmrSeason;

    private Table rankingContent,
            tSelectBoxes, //Tabla con todos los select-box
            tSeason, //Tabla
            tLoading;
    private Stack sRanking; //Clasificación y Torneos con ScrollPane
    private ScrollPane spContent; //Clasificación y Torneos con ScrollPane
    private SelectBox sbRankingType, sbSeason, sbOrderBy;
    private Label lLastRankingUpdate;

    private Image iLoad;
    private boolean flagAnimation;

    public HallMenuRanking(ServerMainScreen serverMainScreen){
        this.serverMainScreen = serverMainScreen;
        this.hmrSeason = new HallMenuRanking_Season(serverMainScreen,this);

        this.tSeason = hmrSeason.gettSeason();
        this.flagAnimation = false;
    }

    Table buildMenuRanking(){
        rankingContent = new Table();
        rankingContent.top();

        buildActors();
        buildChargeIcon();

        //Pila con Background, Contenido y Loading
        Stack sAllContent = new Stack();
        sAllContent.setVisible(true);
        sAllContent.add(buildBackground());
        sAllContent.add(rankingContent);
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

    public void updateSeasons(){
        if(serverMainScreen.getNodeJS()!=null && serverMainScreen.getNodeJS().isSocketConnected()){
            ServerObject so = serverMainScreen.getServerObject();
            ArrayList<RankingUserDataServer> alAux = so.getALRanking(so.getiRanking(), so.getiOrder());
            lLastRankingUpdate.setText(so.getLastUpdateDate());

            if(alAux!=null){
                tSeason.clear();
                hmrSeason.updateSeason(alAux);
            }
        }
    }

    public void openFlagAnimation(){
        Table tAux;
        tAux = tSeason;

        tAux.clear();
        tAux.top();
        tAux.add(new Actor()).height(ServerMainScreen.content_Height*0.3f);
        tAux.row();
        tAux.add(iLoad).size(ServerMainScreen.content_Width*0.15f, ServerMainScreen.content_Height*0.09f);

        this.flagAnimation = true;
    }

    public void updateLoadingAnimation(){
        if(flagAnimation){
            if(iLoad!=null){
                if(iLoad.isVisible()) {
                    iLoad.setOrigin(iLoad.getWidth() / 2, iLoad.getHeight() / 2);
                    iLoad.rotateBy(-5);
                }
            }
        }
    }

    private void listenerSeasons(){
        if(serverMainScreen.getNodeJS()!=null && serverMainScreen.getNodeJS().isSocketConnected()) {
            int numTemporada = getIndexSeason();
            int order = getIndexOrderBy();

            ServerObject so = serverMainScreen.getServerObject();
            if(so.getALRanking(numTemporada, order)!=null) {
                Gdx.app.log(TAG, "La temporada ya está almacenada. iRanking/iOrder --> "+numTemporada+"/"+order);
                serverMainScreen.openFlagRanking(numTemporada, order);
            } else serverMainScreen.getNodeJS().getRankingJS().eoRankingPlayers(numTemporada, order);
        }
    }

    private void listenerOrderBy(){
        if(serverMainScreen.getNodeJS()!=null && serverMainScreen.getNodeJS().isSocketConnected()){
            int iRanking = -1;
            int iOrder = getIndexOrderBy();

            if(sbRankingType.getItems().get(sbRankingType.getSelectedIndex()).equals(SEASONS))
                iRanking = getIndexSeason();
            else iRanking = getIndexTournament();

            ServerObject so = serverMainScreen.getServerObject();
            if(so.getALRanking(iRanking, iOrder)!=null){
                Gdx.app.log(TAG, "La temporada/torneo ya está almacenada. iRanking/iOrder --> "+iRanking+"/"+iOrder);
                serverMainScreen.openFlagRanking(iRanking, iOrder);
            } else serverMainScreen.getNodeJS().getRankingJS().eoRankingPlayers(iRanking, iOrder);
        }
    }

    private void buildActors(){
        buildScrollPane();
        buildSelectBoxes();
        buildStage();
    }

    private void buildStage(){
        rankingContent.setDebug(false);
        rankingContent.add(tSelectBoxes);
        rankingContent.row();
        rankingContent.add(spContent).size(ServerMainScreen.content_Width,ServerMainScreen.content_Height*0.78f);
    }

    private void buildSelectBoxes(){
        //Se crean los selectBox de Clasificación-Temporada
        sbRankingType = new SelectBox(StyleConfigurator.getSBS_RankingType(serverMainScreen.getSkin()));
        sbRankingType.setAlignment(Align.center);
        sbRankingType.getList().setAlignment(Align.center);
        sbRankingType.setItems(SEASONS);
        sbRankingType.getStyle().fontColor = Color.GOLD;
        sbRankingType.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(serverMainScreen.getNodeJS()!=null && serverMainScreen.getNodeJS().isSocketConnected()) {
                    if(sbRankingType.getSelectedIndex()==0){
                        listenerSeasons();
                        sbSeason.setVisible(true);
                        tSeason.setVisible(true);
                    }
                }
            }
        });

        sbSeason = new SelectBox(StyleConfigurator.getSBS_Ranking(serverMainScreen.getSkin()));
        sbSeason.setAlignment(Align.center);
        sbSeason.getList().setAlignment(Align.center);

        String[] iTemporada = new String[Config.NUM_SEASONS_SHOW];
        for (int i=0; i<Config.NUM_SEASONS_SHOW; i++){
            // int index = (NUM_SEASONS-2-i);
            int index = (Config.NUM_SEASONS-1-i);
            if(index == 0) iTemporada[i] = "Temporada de Inaguración";
            else iTemporada[i] = "Temporada "+index;
        }

        sbSeason.setItems(iTemporada);
        sbSeason.getStyle().fontColor = COLOR_SERVERMAINSCREEN;
        sbSeason.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listenerSeasons();
            }
        });

        Label lOrderBy = new Label("Ordenar por: ", StyleConfigurator.getLS_Verdana55());
        lOrderBy.setAlignment(Align.center);
        lOrderBy.setColor(Color.GOLD);
        sbOrderBy = new SelectBox(StyleConfigurator.getSBS_Ranking(serverMainScreen.getSkin()));
        sbOrderBy.setAlignment(Align.center);
        sbOrderBy.getList().setAlignment(Align.center);
        sbOrderBy.setItems(" Puntos ", " Ratio (>20 partidas) ");
        sbOrderBy.getStyle().fontColor = COLOR_SERVERMAINSCREEN;
        sbOrderBy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listenerOrderBy();
            }
        });

        sbSeason.setVisible(true);
        Stack sSelectBox = new Stack();
        sSelectBox.add(sbSeason);

        lLastRankingUpdate = new Label(serverMainScreen.getServerObject().getLastUpdateDate(),
                StyleConfigurator.getLS_Maiandra50());
        lLastRankingUpdate.setAlignment(Align.center);
        lLastRankingUpdate.setColor(Color.BLACK);

        tSelectBoxes = new Table();
        tSelectBoxes.setDebug(false);
        /*tSelectBoxes.add(sbRankingType).size(ServerMainScreen.content_Width*0.4f,ServerMainScreen.content_Height*0.05f).padTop(5f);
        tSelectBoxes.add(sSelectBox).size(ServerMainScreen.content_Width*0.56f,ServerMainScreen.content_Height*0.05f).padTop(5f);
        tSelectBoxes.row();
        tSelectBoxes.add(lOrderBy).size(ServerMainScreen.content_Width*0.4f,ServerMainScreen.content_Height*0.05f).padBottom(5f);
        tSelectBoxes.add(sbOrderBy).size(ServerMainScreen.content_Width*0.56f,ServerMainScreen.content_Height*0.05f).padBottom(5f);
        tSelectBoxes.row();
        tSelectBoxes.add(lLastRankingUpdate).expandX().colspan(2).padTop(0f).padBottom(10f);*/
        tSelectBoxes.add(sbRankingType).size(ServerMainScreen.content_Width,ServerMainScreen.content_Height*0.05f).padTop(5f);
        tSelectBoxes.row();
        tSelectBoxes.add(sSelectBox).size(ServerMainScreen.content_Width,ServerMainScreen.content_Height*0.05f);
        tSelectBoxes.row();
        tSelectBoxes.add(sbOrderBy).size(ServerMainScreen.content_Width,ServerMainScreen.content_Height*0.05f).padBottom(5f);
        tSelectBoxes.row();
        tSelectBoxes.add(lLastRankingUpdate).expandX().padTop(0f).padBottom(10f);
    }

    private void buildScrollPane(){
        sRanking = new Stack();
        sRanking.add(tSeason);

        //ScrollPane
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle(null,null,null,null,null);
        spContent = new ScrollPane(sRanking, scrollPaneStyle);
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

    private int getIndexSeason(){ return (Config.NUM_SEASONS-1)-sbSeason.getSelectedIndex(); }
    private int getIndexTournament(){ return 100; }
    private int getIndexOrderBy(){ return sbOrderBy.getSelectedIndex(); }
}

package com.tfdevelopment.pochaonlinetfd.screen.server.menu;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.screen.server.layout.UserEditIcon;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.ServerUser;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

import java.util.ArrayList;

public class HallMenuUser {
    private static final String TAG = HallMenuUser.class.getName();

    private ServerMainScreen serverMainScreen;

    private Table tUser, tLoading;
    private Window wSelectUserIcon, wReview;
    private Image iLoad;

    private Label lName, lInfo;
    private UserEditIcon userEditIcon;

    private Label lClasificacion;

    //Públicas y clasificación (Fijas)
    private Label lPosicion_Palmares, lPoints_Palmares, lRatio_Palmares, lPG_Palmares, lPP_Palmares;
    //Públicas y clasificación (Datos)
    private Label[][] alGameData;
    private Label[][] alName; //Nombre y estado

    //Leyenda
    private Label lPG, lPP;

    public HallMenuUser(ServerMainScreen serverMainScreen){
        this.serverMainScreen = serverMainScreen;

        this.alName = new Label[Config.NUM_SEASONS][2];
        this.alGameData = new Label[Config.NUM_SEASONS][ServerUser.NUM_COLUMNS];
        for(int i = 0; i< Config.NUM_SEASONS; i++){
            for(int j = 0; j< ServerUser.NUM_COLUMNS; j++){
                this.alGameData[i][j] = getLabelSmall_AlignCenter("-");
            }
        }
    }

    Table buildMenuUser(){
        tUser = new Table();
        tUser.setVisible(true);
        tUser.setDebug(false);

        buildActors();
        buildStage();
        buildChargeIcon();

        //Table con Contenido
        Table table = new Table();
        table.setVisible(true);
        table.setDebug(false);
        table.top();
        table.padTop(ServerMainScreen.content_PadTop*0.8f);
        table.add(tUser).size(ServerMainScreen.content_Width, ServerMainScreen.content_Height);

        //Pila con Contenido y Loading
        Stack sAllContent = new Stack();
        sAllContent.add(buildBackground());
        sAllContent.add(table);
        sAllContent.add(tLoading);

        //Contiene la pila con Background, Contenido y Loading
        Table tAllContent = new Table();
        tAllContent.setDebug(false);
        tAllContent.setVisible(false);
        tAllContent.top();
        tAllContent.add(sAllContent).size(ServerMainScreen.content_Width, ServerMainScreen.content_Height);
        return tAllContent;
    }

    public void updateUser(){
        tLoading.setVisible(true);
        tUser.setVisible(false);

        Server snJS = serverMainScreen.getNodeJS();
        if(snJS!=null && snJS.isSocketConnected() && serverMainScreen.getAlUserDataServer().size()>0){
            tLoading.setVisible(false);
            tUser.setVisible(true);

            ServerUser sp = serverMainScreen.getNodeJS().getServerUser();
            if(tUser!=null && sp!=null){

                lName.setText(sp.getName());
                userEditIcon.updateIcon(sp.getUserIcon());

                //Clasificación
                for(int i = 0; i< Config.NUM_SEASONS; i++){
                    int nPlayed_XX = (int)(sp.getGameData(i,3)+sp.getGameData(i,4));

                    for(int j = 0; j< ServerUser.NUM_COLUMNS; j++){
                        String data = "";
                        if(j==2) data = StaticsMethods.getFormatRatio((sp.getGameData(i,j)));
                        else data = Integer.toString((int)sp.getGameData(i,j));

                        alGameData[i][j].setText(data);

                        if(nPlayed_XX==0) alGameData[i][j].setText("-");
                    }

                    //Públicas y Temporada actual
                    if(i==0 || i==1) alGameData[i][0].setText("-");
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

    private void buildStage(){
        final int NUM_CELLS = 7;
        float cellHeight = 0.03f;
        float widthFirstCell = ServerMainScreen.content_Width *0.25f;

        tUser.clear();

        //Background Top
        Image iBackgroundTop = new Image(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getTopBorderUser()));
        Table tBackgroundTop = new Table();
        tBackgroundTop.top();
        tBackgroundTop.add(iBackgroundTop).size(ServerMainScreen.content_Width,ServerMainScreen.content_Height*0.18f);

        //Name & Icon
        Table tName = new Table();
        tName.top();
        tName.add(lName).padTop(ServerMainScreen.content_Height*0.03f);
        tName.row();
        tName.add(userEditIcon).padTop(ServerMainScreen.content_Height*0.02f);

        //Seasons
        Table tSeasons = new Table();
        tSeasons.setDebug(false);
        tSeasons.top().padTop(ServerMainScreen.content_Height*0.23f);
        tSeasons.add(lClasificacion).height(ServerMainScreen.content_Height *cellHeight).expandX().colspan(NUM_CELLS).center().padBottom(10f);
        tSeasons.row();
        tSeasons.add(new Actor()).height(ServerMainScreen.content_Height *cellHeight).width(widthFirstCell).center();
        tSeasons.add(lPosicion_Palmares).height(ServerMainScreen.content_Height *cellHeight).expandX().center();
        tSeasons.add(lPoints_Palmares).height(ServerMainScreen.content_Height *cellHeight).expandX();
        tSeasons.add(lRatio_Palmares).height(ServerMainScreen.content_Height *cellHeight).expandX();
        tSeasons.add(lPG_Palmares).height(ServerMainScreen.content_Height *cellHeight).expandX();
        tSeasons.add(lPP_Palmares).height(ServerMainScreen.content_Height *cellHeight).expandX();
        tSeasons.row();
        //Temporada X
        for(int i = 0; i< Config.NUM_SEASONS; i++){
            tSeasons.add(alName[i][0]).height(ServerMainScreen.content_Height *cellHeight).width(widthFirstCell).center();
            for(int j = 0; j< ServerUser.NUM_COLUMNS; j++){
                tSeasons.add(alGameData[i][j]).height(ServerMainScreen.content_Height *cellHeight).expandX().center();
            }
            tSeasons.row();
            tSeasons.add(alName[i][1]).height(ServerMainScreen.content_Height *cellHeight).width(widthFirstCell).center();
            tSeasons.add(new Actor()).height(ServerMainScreen.content_Height *cellHeight).expandX().center().colspan(NUM_CELLS-1);
            tSeasons.row();
        }
        //Legend
        Table tLegend = new Table();
        tLegend.setDebug(false);
        tLegend.top().padTop(ServerMainScreen.content_Height*0.92f);
        tLegend.add(lPG).height(ServerMainScreen.content_Height *0.025f).expandX().padBottom(5f).colspan(1);
        tLegend.add(lPP).height(ServerMainScreen.content_Height *0.025f).expandX().padBottom(5f).colspan(1);
        tLegend.row();

        //Stack final
        Stack stack = new Stack();
        //stack.add(tBackground);
        stack.add(tBackgroundTop);
        stack.add(tSeasons);
        //stack.add(tLegend);
        stack.add(tName);
        stack.add(createWindowSelectUserIcon());
        stack.add(buildWindowReview());

        tUser.add(stack).size(ServerMainScreen.content_Width, ServerMainScreen.content_Height);
    }

    private void buildActors(){
        final ServerUser sp = serverMainScreen.getNodeJS().getServerUser();

        userEditIcon = new UserEditIcon(sp.getUserIcon());
        userEditIcon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(sp.getUserIcon()!=0 || Gdx.app.getType()!= Application.ApplicationType.Android || Settings.isShowReview()) {
                    wSelectUserIcon.setVisible(!wSelectUserIcon.isVisible());
                } else {
                    wReview.setVisible(true);
                }
            }
        });

        lName = new Label(sp.getName(), StyleConfigurator.getLS_Maiandra65());
        if(sp.getName().length()==10) lName.setFontScale(0.65f);
        else if(sp.getName().length()>=8) lName.setFontScale(0.8f);
        else lName.setFontScale(0.9f);
        lName.setAlignment(Align.center);
        lName.setColor(new Color(24/255f,108/255f,227/255f,1f));

        /////////////////////////////////////////////////////////
        lClasificacion = getLabelSmall_AlignCenter("CLASIFICACIÓN");
        lClasificacion.setFontScale(0.65f);
        lClasificacion.setColor(new Color(126f/255f, 59f/255f, 192f/255f, 1f));
        lPosicion_Palmares = getLabelSmall_AlignCenter("Posición"); lPosicion_Palmares.setColor(Color.ORANGE);
        lPoints_Palmares = getLabelSmall_AlignCenter("Puntos"); lPoints_Palmares.setColor(Color.CYAN);
        lRatio_Palmares = getLabelSmall_AlignCenter("Ratio"); lRatio_Palmares.setColor(Color.CYAN);
        lPG_Palmares = getLabelSmall_AlignCenter("P.G"); lPG_Palmares.setColor(Color.MAROON);
        lPP_Palmares = getLabelSmall_AlignCenter("P.P"); lPP_Palmares.setColor(Color.MAROON);

        for(int i = 0; i< Config.NUM_SEASONS; i++){
            String title = "Temporada "+(Config.NUM_SEASONS-1-i);
            alName[i][0] = getLabelSmall_AlignCenter(title);

            alName[i][1] = getLabelSmall_AlignCenter(title);
            String state = ""; Color color;
            if(i==0){
                state = "Abierta";
                color = Color.GREEN;
            } else {
                state = "Cerrada";
                color = Color.RED;
            }
            alName[i][1] = getLabelSmall_AlignCenter(state);
            alName[i][1].setColor(color);

            for(int j = 0; j< ServerUser.NUM_COLUMNS; j++){
                alGameData[i][j] = getLabelSmall_AlignCenter("-");
            }
        }
        /////////////////////////////////////////////////////////
        lPG = new Label("P.G: Partidas Ganadas [GREEN](+"+ Config.POINTS_FOR_WINNING+")[WHITE]", StyleConfigurator.getLS_Maiandra50());
        lPG.setAlignment(Align.left);
        lPP = new Label("P.J: Partidas Jugadas [GREEN]("+ Config.POINTS_FOR_LOST+")[WHITE]", StyleConfigurator.getLS_Maiandra50());
        lPP.setAlignment(Align.left);
    }

    private Table buildBackground(){
        //Background
        Image iBackground = new Image(StaticsMethods.getNinePatchDrawable(AssetLoader.aLoader.aImage.getRoundLightGrey(),10));
        Table tBackground = new Table();
        tBackground.setDebug(false);
        tBackground.top().padTop(ServerMainScreen.content_PadTop*0.8f);
        tBackground.add(iBackground).size(ServerMainScreen.content_Width, ServerMainScreen.content_Height);
        return tBackground;
    }

    private Table createWindowSelectUserIcon(){
        //Crear ventana de selección de icono de usuario
        //WindowSelectUserIcon
        final int NUM_USER_ICONS = 6;
        ArrayList<Drawable> alDIcons = new ArrayList<>();
        ArrayList<ImageButton> alIBIcons = new ArrayList<>();

        for(int i=1; i<=NUM_USER_ICONS; i++){
            alDIcons.add(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getUserIcon(i, 0)));
        }

        for(int i=1; i<=NUM_USER_ICONS; i++){
            final int finalI = i;
            alIBIcons.add(new ImageButton(alDIcons.get((i-1))));
            alIBIcons.get((i-1)).addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(serverMainScreen.getNodeJS()!=null && serverMainScreen.getNodeJS().isSocketConnected()){
                        serverMainScreen.getNodeJS().getObjectDataJS().eoSelectUserIcon(finalI);
                    }
                    wSelectUserIcon.setVisible(false);
                }
            });
        }

        wSelectUserIcon = new Window("Selecciona un icono", StyleConfigurator.getWS_Default(serverMainScreen.getSkin()));
        wSelectUserIcon.setVisible(false);
        wSelectUserIcon.setDebug(false);
        wSelectUserIcon.setMovable(false);
        wSelectUserIcon.setResizable(false);
        //wSelectUserIcon.setColor(new Color(86f/255f,195f/255f,54f/255f,1f));
        wSelectUserIcon.setColor(new Color(112f/255f,213f/255f,82f/255f,1f));
        wSelectUserIcon.getTitleLabel().setAlignment(Align.center);

        Table layerSelectUserIcon = new Table();
        layerSelectUserIcon.setFillParent(true);
        wSelectUserIcon.add(alIBIcons.get(0)).size(ServerMainScreen.content_Width *0.22f, ServerMainScreen.content_Height *0.08f).padTop(10f);
        wSelectUserIcon.add(alIBIcons.get(1)).size(ServerMainScreen.content_Width *0.22f, ServerMainScreen.content_Height *0.08f).padTop(10f);
        wSelectUserIcon.add(alIBIcons.get(2)).size(ServerMainScreen.content_Width *0.22f, ServerMainScreen.content_Height *0.08f).padTop(10f);
        wSelectUserIcon.row();
        wSelectUserIcon.add(alIBIcons.get(3)).size(ServerMainScreen.content_Width *0.22f, ServerMainScreen.content_Height *0.08f).padTop(10f).padBottom(10f);
        wSelectUserIcon.add(alIBIcons.get(4)).size(ServerMainScreen.content_Width *0.22f, ServerMainScreen.content_Height *0.08f).padTop(10f).padBottom(10f);
        wSelectUserIcon.add(alIBIcons.get(5)).size(ServerMainScreen.content_Width *0.22f, ServerMainScreen.content_Height *0.08f).padTop(10f).padBottom(10f);
        layerSelectUserIcon.add(wSelectUserIcon);

        return layerSelectUserIcon;
    }

    private void buildChargeIcon(){
        TextureRegion textureRegion = new TextureRegion(AssetLoader.aLoader.aImage.getiLoad());
        textureRegion.flip(true, false);
        iLoad = new Image(textureRegion);
        iLoad.setColor(Color.GREEN);
        tLoading = new Table();
        tLoading.add(iLoad).size(ServerMainScreen.content_Width*0.15f, ServerMainScreen.content_Height*0.09f);
    }

    public Table buildWindowReview(){
        String text =
                "¡Déjanos una valoración y ayúdanos\n"+
                "a llegar a más gente!\n\n"+
                "Valóranos y cambia tu icono de juego";
        Label lReview = new Label(text, StyleConfigurator.getLS_Maiandra65());
        lReview.setAlignment(Align.center);

        TextButton tbAgree = new TextButton(" ¡Dejar valoración! ", StyleConfigurator.getTBS_RoundButton60(serverMainScreen.getSkin()));
        tbAgree.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                wReview.setVisible(false);
                //MusOnlineMain.showReviewInApp();
                Gdx.net.openURI(Constants.URI_PLAYSTORE);
                Settings.setShowReview(true);
                serverMainScreen.getNodeJS().getObjectDataJS().eoSelectUserIcon(2);
            }
        });

        TextButton tbCancel = new TextButton(" Quizás más tarde ", StyleConfigurator.getTBS_RoundButton60(serverMainScreen.getSkin()));
        tbCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                wReview.setVisible(false);
            }
        });

        wReview = new Window("  ¡Ayúdanos a llegar a más gente!  ", StyleConfigurator.getWS_Default(serverMainScreen.getSkin()));
        // wReview.setColor(new Color(112f/255f,213f/255f,82f/255f,1f));
        wReview.setColor(new Color(50f/255f,75f/255f,250f/255f,1f));
        wReview.setVisible(false);
        wReview.setMovable(false);
        wReview.setResizable(false);
        wReview.getTitleLabel().setAlignment(Align.center);
        wReview.add(lReview).padLeft(10f).padRight(10f).padTop(5f).padBottom(5f).colspan(2);
        wReview.row();
        wReview.add(tbAgree).padBottom(20f).padTop(20f);
        wReview.add(tbCancel).padBottom(10f).padLeft(10f).padRight(5f).padTop(20f);

        Table table = new Table();
        table.setFillParent(true);
        table.add(wReview);

        return table;
    }

    public void hideWindowSelectUserIcon(){
        wSelectUserIcon.setVisible(false);
    }

    private Label getLabelSmall_AlignCenter(String text){
        Label label = new Label(text, StyleConfigurator.getLS_Maiandra50());
        label.setAlignment(Align.center);
        label.setColor(Color.BLACK);
        return label;
    }
}

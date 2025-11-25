package com.tfdevelopment.pochaonlinetfd.screen.server.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.object.ranking.RankingUserDataServer;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

import java.util.ArrayList;

public class HallMenuRanking_Season {
    private ServerMainScreen serverMainScreen;
    private HallMenuRanking hallMenuRanking;

    private Table tSeason;
    private Label lName, lPuntos, lRatio, lGanadas, lPerdidas;
    private Label lPG, lPP; //Leyenda

    public HallMenuRanking_Season(ServerMainScreen serverMainScreen, HallMenuRanking hallMenuRanking){
        this.serverMainScreen = serverMainScreen;
        this.hallMenuRanking = hallMenuRanking;

        buildActors();
    }

    public void updateSeason(ArrayList<RankingUserDataServer> alRankingPlayer){
        int numberCell = 6;
        float padLeftName = 5f;
        float padBetweenUser = 8f;
        float cellHeight = 0.03f;
        float widthFirstCell = ServerMainScreen.content_Width *0.37f;

        Table tFooter = new Table();
        tFooter.add(lPG).height(ServerMainScreen.content_Height *0.025f).expandX().padBottom(5f).colspan(1);
        tFooter.add(lPP).height(ServerMainScreen.content_Height *0.025f).expandX().padBottom(5f).colspan(1);
        tFooter.row();

        Table tPlayers = new Table();
        tPlayers.setDebug(false);
        //tPlayers.add(lName).height(ServerMainScreen.halls_Height*cellHeight).expandX().center();
        tPlayers.add(lName).height(ServerMainScreen.content_Height *cellHeight).width(widthFirstCell).center();
        tPlayers.add(lPuntos).height(ServerMainScreen.content_Height *cellHeight).expandX();
        tPlayers.add(lRatio).height(ServerMainScreen.content_Height *cellHeight).expandX();
        tPlayers.add(lGanadas).height(ServerMainScreen.content_Height *cellHeight).expandX();
        tPlayers.add(lPerdidas).height(ServerMainScreen.content_Height *cellHeight).expandX();
        tPlayers.row();


        //Contenido de cada jugador
        Color colorPlayer;
        for(int i=0; i<alRankingPlayer.size(); i++){
            RankingUserDataServer auxSDRanking = alRankingPlayer.get(i);

            String namePlayer = auxSDRanking.getName();
            int points = auxSDRanking.getPoints();
            float ratio = auxSDRanking.getRatio();
            int nWon = auxSDRanking.getWon();
            int nLost = auxSDRanking.getLost();

            if(i%2==0) colorPlayer = Color.WHITE;
            else colorPlayer = Color.LIGHT_GRAY;

            Label lNameP = getLabelCustom(" "+(i+1)+". "+namePlayer, Align.left);
            lNameP.setColor(colorPlayer);
            Label lPointsP = getLabelCustom(Integer.toString(points), Align.center);
            lPointsP.setColor(colorPlayer);
            Label lRatio = getLabelCustom(StaticsMethods.getFormatRatio(ratio), Align.center);
            lRatio.setColor(colorPlayer);
            Label lGanadaP = getLabelCustom(Integer.toString(nWon), Align.center);
            lGanadaP.setColor(colorPlayer);
            Label lPerdidasP = getLabelCustom(Integer.toString(nLost), Align.center);
            lPerdidasP.setColor(colorPlayer);

            tPlayers.add(lNameP).height(ServerMainScreen.content_Height *cellHeight).width(widthFirstCell).left().padLeft(padLeftName);
            tPlayers.add(lPointsP).height(ServerMainScreen.content_Height *cellHeight).expandX().center();
            tPlayers.add(lRatio).height(ServerMainScreen.content_Height *cellHeight).expandX().center();
            tPlayers.add(lGanadaP).height(ServerMainScreen.content_Height *cellHeight).expandX().center();
            tPlayers.add(lPerdidasP).height(ServerMainScreen.content_Height *cellHeight).expandX().center();
            tPlayers.row();
            tPlayers.add(new Actor()).height(padBetweenUser).colspan(numberCell);
            tPlayers.row();
        }
        tPlayers.add(new Actor()).height(padBetweenUser*2).colspan(numberCell);

        tSeason.add(tPlayers).width(ServerMainScreen.content_Width *0.95f);
        tSeason.row();
        tSeason.add(tFooter).width(ServerMainScreen.content_Width *0.95f);
    }

    private void buildActors(){
        tSeason = new Table();

        lPG = new Label("P.G: Partidas Ganadas [GREEN](+"+ Config.POINTS_FOR_WINNING+")[WHITE]", StyleConfigurator.getLS_Maiandra50());
        lPG.setAlignment(Align.left);
        lPP = new Label("P.J: Partidas Jugadas [GREEN](+"+ Config.POINTS_FOR_LOST+")[WHITE]", StyleConfigurator.getLS_Maiandra50());
        lPP.setAlignment(Align.left);

        lName = new Label("Nombre", StyleConfigurator.getLS_Maiandra50());
        lName.setAlignment(Align.center);
        lName.setColor(Color.ORANGE);
        lPuntos = new Label("Puntos", StyleConfigurator.getLS_Maiandra50());
        lPuntos.setAlignment(Align.center);
        lPuntos.setColor(Color.CYAN);
        lRatio = new Label("Ratio", StyleConfigurator.getLS_Maiandra50());
        lRatio.setAlignment(Align.center);
        lRatio.setColor(Color.CYAN);
        lGanadas = new Label("P.G", StyleConfigurator.getLS_Maiandra50());
        lGanadas.setAlignment(Align.center);
        lGanadas.setColor(Color.MAROON);
        lPerdidas = new Label("P.P", StyleConfigurator.getLS_Maiandra50());
        lPerdidas.setAlignment(Align.center);
        lPerdidas.setColor(Color.MAROON);
    }

    private Label getLabelCustom(String text, int align){
        Label label = new Label(text, StyleConfigurator.getLS_Maiandra65());
        label.setAlignment(align);
        return label;
    }

    public Table gettSeason(){ return tSeason; }
}

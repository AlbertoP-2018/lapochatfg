package com.tfdevelopment.pochaonlinetfd.screen.server.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.object.game.NewGameDataServer;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

import java.util.ArrayList;

public class GameContainer {
    private static final float CONTAINER_HEIGHT = ServerMainScreen.content_Height*0.9f;
    private static final float NEW_HEIGHT = ServerMainScreen.content_Height*0.08f;

    private static final float BOX_WIDTH = ServerMainScreen.content_Width*0.99f;
    private static final float BOX_HEIGHT = ServerMainScreen.content_Height*0.17f;
    static final float BOX_WIDTH_CONTAINER = BOX_WIDTH*0.95f; //ServerMainScreen.content_Width*0.98f;
    static final float BOX_HEIGHT_CONTAINER = BOX_HEIGHT*0.93f; //ServerMainScreen.content_Height*0.15f;

    private ServerMainScreen serverMainScreen;
    private NewGameWindow newGameWindow;
    private CurrentGameWindow currentGameWindow;

    private Table tContainer, tEmpty;
    private TextButton tbNew;
//    private ArrayList<GameBox> alGameBox;

    public GameContainer(ServerMainScreen serverMainScreen){
        this.serverMainScreen = serverMainScreen;
        this.newGameWindow = new NewGameWindow(serverMainScreen, this);
        this.currentGameWindow = new CurrentGameWindow(serverMainScreen, this);
//        this.alGameBox = new ArrayList<>();
    }

    public void updateGameContainer() {
//        alGameBox.clear();
//        ArrayList<NewGameDataServer> alNewGameDataServer = serverMainScreen.getServerObject().getAlNewGameDataServer();
//        for (int i=0; i<alNewGameDataServer.size(); i++) {
//            NewGameDataServer _newGameDataServer = alNewGameDataServer.get(i);
//            GameBox _gameBox = new GameBox(serverMainScreen, _newGameDataServer, serverMainScreen.getSkin(), i);
//            alGameBox.add(_gameBox);
//        }

        boolean host = false;
        boolean inGame = false;
        boolean openGame = false;
        tContainer.clear();
//        for (GameBox gameBox : alGameBox) {
        ArrayList<NewGameDataServer> alNewGameDataServer = serverMainScreen.getServerObject().getAlNewGameDataServer();
        for (int i=0; i<alNewGameDataServer.size(); i++) {
            NewGameDataServer _newGameDataServer = alNewGameDataServer.get(i);
            GameBox gameBox = new GameBox(serverMainScreen, _newGameDataServer, serverMainScreen.getSkin());

            String serverPlayerName = serverMainScreen.getNodeJS().getServerUser().getName();
            NewGameDataServer newGameDataS = gameBox.getNewGameDataServer();

            if (newGameDataS.isPlayerInGame(serverPlayerName)) {
                inGame = true;
                currentGameWindow.updateCurrentGameWindow(newGameDataS, host);
            }

            if (newGameDataS.getHostName().equals(serverPlayerName)) host = true;

            if (!newGameDataS.isCompleted()) {
                NinePatchDrawable npd = StaticsMethods.getNinePatchDrawable(AssetLoader.aLoader.aImage.getBlackboard(), 10);
                Table tGameBox = new Table();
                tGameBox.setBackground(npd);
                tGameBox.add(gameBox).size(BOX_WIDTH_CONTAINER, BOX_HEIGHT_CONTAINER);
                tContainer.add(tGameBox).size(BOX_WIDTH, BOX_HEIGHT).padBottom(CONTAINER_HEIGHT*0.01f);
//                tContainer.add(gameBox).size(BOX_WIDTH, BOX_HEIGHT);
                openGame = true;
            }
            tContainer.row();
        }

        tEmpty.setVisible(!openGame);
        tContainer.setVisible(openGame && !inGame);
        tbNew.setVisible(!inGame);
        if (!inGame) newGameWindow.getwNewGame().setVisible(false);
        currentGameWindow.getwCurrentGameLayout().setVisible(inGame);
    }

    public Stack buildGameContainer(){
        Stack stack = new Stack();
        stack.add(buildGamesLayout());
        stack.add(newGameWindow.buildNewGameWindow());
        stack.add(currentGameWindow.builCurrentGameWindow());
        return stack;
    }

    private Table buildGamesLayout() {
        Label lNotFound = new Label("No existen partidas abiertas...", StyleConfigurator.getLS_Maiandra65());
        Label lNewGame = new Label("¡Crea una nueva!", StyleConfigurator.getLS_Maiandra65());
        tEmpty = new Table();
        tEmpty.setVisible(true);
        tEmpty.add(lNotFound);
        tEmpty.row();
        tEmpty.add(lNewGame);

        tContainer = new Table();
        tContainer.setVisible(false);
        tContainer.top();

        ScrollPane scrollPane = new ScrollPane(tContainer, StyleConfigurator.getSPS_WithoutBackground());
        scrollPane.setVisible(true);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(true);
        scrollPane.layout();

        Stack sContainer = new Stack();
        sContainer.setDebug(false);
        sContainer.add(tEmpty);
        sContainer.add(scrollPane);

        tbNew = new TextButton(" Crear partida ", StyleConfigurator.getTBS_RoundButton(serverMainScreen.getSkin()));
        tbNew.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newGameWindow.showNewGame();
            }
        });

        Table tFinal = new Table();
        tFinal.setDebug(false);
        tFinal.top().padTop(ServerMainScreen.content_PadTop);
        tFinal.add(sContainer).size(ServerMainScreen.content_Width, CONTAINER_HEIGHT).top();
        tFinal.row();
        tFinal.add(new Actor()).size(ServerMainScreen.content_Width,
                ServerMainScreen.content_Height-CONTAINER_HEIGHT-NEW_HEIGHT);
        tFinal.row();
        tFinal.add(tbNew).size(ServerMainScreen.content_Width, NEW_HEIGHT).bottom();

        return tFinal;
    }

    void setStyleWindow(Window window){
        //window.setColor(new Color(50f/255f,75f/255f,250f/255f,0.95f));
        window.setColor(new Color(50f/255f,75f/255f,250f/255f,1f));
        window.getTitleLabel().setAlignment(Align.center);
    }

    /*** Getters And Setters ***/
    public NewGameWindow getGameWindows(){ return this.newGameWindow; }
}

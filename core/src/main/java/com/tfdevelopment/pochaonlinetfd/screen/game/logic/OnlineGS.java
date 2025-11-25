package com.tfdevelopment.pochaonlinetfd.screen.game.logic;

import static com.tfdevelopment.pochaonlinetfd.utils.conf.Config.TEST;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.thread.ThreadReloadGame;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.screen.menu.MenuScreen;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.GUIBuilder;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.game.ServerGameData;
import com.tfdevelopment.pochaonlinetfd.server.object.ServerObject;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.GameMode;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.ScreenType;

import java.util.concurrent.Semaphore;

public class OnlineGS extends AbstractGameScreen {
    private static final String TAG = OnlineGS.class.getName();

    private GameLogic gameLogic;
    private ThreadReloadGame threadReloadGame;
    private Semaphore semaphoreReload;

    private boolean flagNotice, flagGameInvitation;
    private boolean flagReloadGameStarted, flagReconnectGame, flagPlayerReconnected, flagUpdateCardsFromReloading;
    private boolean reloadingGame;

    private ServerObject serverObject;
    private boolean flagGameToHall;

    private ServerGameData serverGameData, sgDataGameStarted; //Objeto para la reconexión a partida empezada
    private Server nodeJS;

    private boolean playerOut;
    private int indexPlayerDisconnected, indexPlayerReconnected;
    private boolean flagSendDataGameFinihsed;

    //Indica si se están procesando los eventos para evitar perder uno al recibirlo
    //private boolean processingDataGameStarted;
    /* Permite pulsar una única vez el botón de salir para volver al hall.
        Al pulsarlo dos veces seguidas, luego no REFLEJA los eventos que recibe al recibir dos
        veces el evento de volver al hall y sobreescribir los objetos correspondientes.*/
    private boolean buttonExitPressed;
    public boolean pauseApp;

    public OnlineGS(Game game, Server nodeJS, ServerGameData serverGameData, boolean previouslyStartedGame){
        super(game, GameMode.ONLINE, serverGameData.getGamePlayers().size(), previouslyStartedGame);

        setGameInit(false);
        this.pauseApp = false;
        this.buttonExitPressed = false;

        this.nodeJS = nodeJS;
        this.serverGameData = serverGameData;
        setConfig(serverGameData.getFirstPlayer(), serverGameData.getIndexPlayer());
        nodeJS.setOnlineScreen(this);

        this.gameLogic = new GameLogic(this);
        this.guiBuilder = new GUIBuilder(this, gameLogic);
        this.gameLogic.setBuilder(guiBuilder);

        this.semaphoreReload = new Semaphore(1);
        this.flagReloadGameStarted = previouslyStartedGame;
        this.reloadingGame = false;
        this.flagReconnectGame = false;

        this.flagNotice = false;
        this.flagGameToHall = false;
    }

    @Override
    public void show() {
        initScreen(ScreenType.ONLINE_GAME, null, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT_GAME);

        gameLogic.initHand();

        getStage().clear();
        getStage().addActor(guiBuilder.getMainStack());

        super.show();
        super.initTimer();
        setGameInit(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f); //Pinta la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Limpia la pantalla

        manageConnection();

        getStage().act(delta);
        getStage().draw();
        getStage().setDebugAll(false);

        if(isGameInit()){ //Los actores ya se han inicializado
            if (!reloadingGame) guiBuilder.render(delta);
            gameLogic.render(delta);
        }

        updateEventServer(delta); //Permite ejecutar los eventos recibidos por el servidor en el hilo principal

        //////////////////////////////////////////////////////////
        //ENTER o tocar la pantalla con dos dedos
        if(TEST) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                gameLogic.showTest();
                guiBuilder.getGuiBuilderPlayersCardsTEST().setVisible();
                aux();
            }
            if (Gdx.input.isTouched(1) && auxTouched <= 0f) {
                auxTouched = 1f;
                gameLogic.showTest();
                guiBuilder.getGuiBuilderPlayersCardsTEST().setVisible();
                aux();
            } else {
                auxTouched -= delta;
            }
        }
    }
    private float auxTouched = 0f;
    private void aux(){
        Gdx.app.log(TAG, "Player 0: "+gameLogic.getPlayers()[0].getName());
        Gdx.app.log(TAG, "Player 1: "+gameLogic.getPlayers()[1].getName());
        Gdx.app.log(TAG, "Player 2: "+gameLogic.getPlayers()[2].getName());
        Gdx.app.log(TAG, "Player 3: "+gameLogic.getPlayers()[3].getName());
        Gdx.app.log(TAG, "Player Hand: "+gameLogic.getPlayers()[gameLogic.getGameData().getHandPlayer()].getName());
        Gdx.app.log(TAG, "First Player: "+gameLogic.getPlayers()[gameLogic.getGameData().getFirstPlayer()].getName());
        Gdx.app.log(TAG, "Current Player: "+gameLogic.getPlayers()[gameLogic.getGameData().getCurrentPlayer()].getName());
    }

    @Override
    public void resize(int width, int height) {
        StaticsMethods.showLogResize(TAG);
        getViewport().update(width,height,true);
    }

    @Override
    public void pause() {
        StaticsMethods.showLogPause(TAG);
        nodeJS.getOthersJS().eoUserPauseGame(true);
    }

    @Override
    public void resume() {
        StaticsMethods.showLogResume(TAG);
        nodeJS.getOthersJS().eoUserPauseGame(false);
    }

    @Override
    public void hide() {
        super.hide();
        StaticsMethods.showLogHide(TAG);
    }

    @Override
    public void controlButtonEvent(ButtonEvent buttonEvent, boolean input) {
        if(input){
            gameLogic.controlButtonEvent(buttonEvent);
        } else {
            if(nodeJS!=null && nodeJS.getServerUser()!=null) {
                int idEvent = serverGameData.getIdLastEvent()+1;
                nodeJS.getButtonEventJS().eoButtonEvent(idEvent, buttonEvent, gameLogic);
            }
        }
    }

    @Override
    public void exitGame(boolean toMenu) {
        if(nodeJS==null || !nodeJS.isSocketConnected()) {
            changeScreen(new MenuScreen(getGame()), false);

        } else if(toMenu){
            nodeJS.disconnectServer(false);
            changeScreen(new MenuScreen(getGame()), false);
        } else {
            if(!buttonExitPressed) {
                buttonExitPressed = true;
                nodeJS.getServerGameJS().eoGameToHall();
            }
        }
    }

    /*** Métodos del Servidor ***/
    public void updateEventServer(float delta) {
        if(flagPlayerReconnected){
            flagPlayerReconnected = false;
            guiBuilder.getGuiBuilderWindows().getGuiW_Connection().playerReconnected(playerOut, indexPlayerReconnected);
        }

        if (flagReloadGameStarted && isGameInit()) {
            flagReloadGameStarted = false;
            //Se indica que se están procesando los eventos para evitar recibir otro nuevo y no procesarlo.
            //De esta manera, el nuevo evento de botón quedará a la espera hasta que termine de procesarse el array
            reloadingGame = true;
            guiBuilder.getGuiBuilderWindows().getGuiW_Reload().getwLogin().setVisible(true);

            createThreadReload();

            //Se indica que se están procesando los eventos para evitar recibir otro nuevo y no procesarlo.
            //De esta manera, el nuevo evento de botón quedará a la espera hasta que termine de procesarse el array
            reloadingGame = true;

            threadReloadGame.start();
        }

//        if (flagUpdateCardsFromReloading /*&& !reloadingGame*/) {
//            flagUpdateCardsFromReloading = false;
//            guiBuilder.getGuiBuilderCards().updateCardsFromReload();
//            guiBuilder.getGuiBuilderPlayersCardsTEST().updateCardsFromReload();
//            guiBuilder.getGuiBuilderTable().updateCardsFromReload();
//        }

        if (flagUpdateCardsFromReloading) {
            flagUpdateCardsFromReloading = false;
            guiBuilder.getGuiBuilderCards().updateCardsFromReload();
            guiBuilder.getGuiBuilderPlayersCardsTEST().updateCardsFromReload();
            guiBuilder.getGuiBuilderTable().updateCardsFromReload();
            guiBuilder.getGuiBuilderTimer().resetTimer();
            setStartGame(true);
            guiBuilder.getGuiBuilderWindows().getGuiW_Reload().getwLogin().setVisible(false);
            reloadingGame = false;
        }

        if (flagReconnectGame) {
            flagReconnectGame = false;
            guiBuilder.getGuiBuilderWindows().getGuiW_Reload().getwLogin().setVisible(true);
            changeScreen(new OnlineGS(getGame(), nodeJS, serverGameData, true), true);
        }

        if (flagNotice) {
            flagNotice = false;
            guiBuilder.getGuiBuilderWindows().getGuiW_Notice().showNotice();
        }

        if (flagGameInvitation) {
            flagGameInvitation = false;
            guiBuilder.getGuiBuilderWindows().getGuiW_Invitation().showNotice();
        }

        if (flagGameToHall) {
            flagGameToHall = false;
            changeScreen(new ServerMainScreen(getGame(), nodeJS, serverObject), true);
        }

        if (isFlagErrorWindow()) {
            setFlagErrorWindow(false);
            guiBuilder.getGuiBuilderWindows().getGuiW_Error().showErrorWindow();
        }
    }

    public void setNoticeServer(String title, String noticeServer){
        if (guiBuilder != null) {
            guiBuilder.getGuiBuilderWindows().getGuiW_Notice().setNotice(title, noticeServer);
            flagNotice = true;
        }
    }

    public void setGameInvitation(String playerName, String gameId, int remainingPlayers) {
        if (guiBuilder != null) {
            guiBuilder.getGuiBuilderWindows().getGuiW_Invitation().setInvitation(playerName, gameId, remainingPlayers);
            flagGameInvitation = true;
        }
    }

    public void playerReconnected(boolean playerOut, int indexPlayer){
        this.playerOut = playerOut;
        this.indexPlayerReconnected = indexPlayer;
        this.flagPlayerReconnected = true;
    }

    private void manageConnection(){
        Window window = guiBuilder.getGuiBuilderWindows().getGuiW_Connection().getwServerDisconnected();
        window.setVisible(nodeJS==null || !nodeJS.isSocketConnected());
    }

    public void setFlagUpdateCardsFromReloading() {
        this.flagUpdateCardsFromReloading = true;
    }

    public void reconnectGame(ServerGameData serverGameData) {
        this.flagReconnectGame = true;
        //Se indica que se están procesando los eventos para evitar recibir otro nuevo y no procesarlo.
        //De esta manera, el nuevo evento de botón quedará a la espera hasta que termine de procesarse el array
        this.reloadingGame = true;
        this.serverGameData = serverGameData;
    }

    public void createThreadReload() {
        threadReloadGame = new ThreadReloadGame(semaphoreReload, this, gameLogic, serverGameData);
    }

    public boolean isReloading() {
        return flagReloadGameStarted || reloadingGame;
    }

    public void goToHall(ServerObject _serverObject) {
        this.serverObject = _serverObject;
        flagGameToHall = true;
    }

    /*** Getters And Setters ***/
    public Server getServerNodeJS(){ return nodeJS; }
    public ServerGameData getServerGame() { return serverGameData; }
    public GameLogic getGameLogic(){ return gameLogic; }
    public boolean isFlagReloadGameStarted() { return this.flagReloadGameStarted; }
}

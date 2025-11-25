package com.tfdevelopment.pochaonlinetfd.screen.server;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.tfdevelopment.pochaonlinetfd.screen.AbstractScreen;
import com.tfdevelopment.pochaonlinetfd.screen.menu.MenuScreen;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.screen.server.games.GameContainer;
import com.tfdevelopment.pochaonlinetfd.screen.server.menu.HallMenuButtons;
import com.tfdevelopment.pochaonlinetfd.screen.server.window.HallWindows;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.server.object.game.NewGameDataServer;
import com.tfdevelopment.pochaonlinetfd.server.object.news.NewsServer;
import com.tfdevelopment.pochaonlinetfd.server.object.ranking.RankingUserDataServer;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.ServerUser;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.UserDataServer;
import com.tfdevelopment.pochaonlinetfd.server.object.game.ServerGameData;
import com.tfdevelopment.pochaonlinetfd.server.object.ServerObject;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.ScreenType;

import java.util.ArrayList;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class ServerMainScreen extends AbstractScreen {
    private static final String TAG = ServerMainScreen.class.getName();
    private static final boolean STAGE_DEBUG_FLAG = false;
    public static boolean ENABLE_LIGHTS = true;

    //Contenido del panel principal (excluye el menú)
    public static float content_Width = Constants.VIEWPORT_WIDTH *0.95f;
    public static float content_Height = Constants.VIEWPORT_HEIGHT *0.88f;
    public static float content_PadTop = Constants.VIEWPORT_HEIGHT *0.01f;

    //Pestañas - Tipo Partida (Clasificación y CPU)
    public static float tabsHeightTG = content_Height*0.04f;

    //Contenido de los tipos de partida
    public static float typeGame_Width = content_Width;
    public static float typeGame_Height = content_Height- tabsHeightTG;

    //Contenido de los salones de cada tipo de partida
    public static float hall_Width = typeGame_Width*0.95f;
    public static float hall_Height = typeGame_Height*0.93f;

    private GameContainer gameContainer;

    private HallWindows hallWindows;
    private HallMenuButtons hallMenuButtons;

    private ServerGameData serverGameData;

    private Skin skin;

    private Server nodeJS;
    private ServerObject serverObject;

    Stack layerHall;

    private boolean flagStartGame, flagInvitationGame, flagNews,
            flagRanking, flagNoticeServer, flagUpdateGamesDataServer, flagUpdateUserDataServer;
    private boolean flagMyUser;

    private World world;
    private RayHandler rayHandler;
    private ConeLight[] coneLight;

    private String titleNoticeServer, textNoticeServer;

    public ServerMainScreen(Game game, Server nodeJS, ServerObject serverObject){
        super(game);
        this.nodeJS = nodeJS;
        this.serverObject = serverObject;

        skin = new Skin(Gdx.files.internal(Constants.SKIN_UI), new TextureAtlas(Constants.TA_UI));
        gameContainer = new GameContainer(this);
        hallWindows = new HallWindows(this);
        hallMenuButtons = new HallMenuButtons(this);

        this.flagStartGame = false;
        this.flagNews = false;
        this.flagRanking = false;

        nodeJS.setServerMainScreen(this);
        nodeJS.getObjectDataJS().eoDataObject();
        flagMyUser = true;

        checkNews();
    }

    @Override
    public void show() {
        initScreen(ScreenType.SERVER_MAIN, new OrthographicCamera(), Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        buildStage(); //Construye la escena
        if(ENABLE_LIGHTS) initLights();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,1.0f); //Pinta la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Limpia la pantalla

        manageConnection();

        getStage().act(delta); //Permite actuar a los actores y activar los eventos de entrada y salida
        getStage().draw(); //Dibuja el escenario
        if(ENABLE_LIGHTS && isOnlyShowingLightingHall()) updateLights();
        getStage().setDebugAll(STAGE_DEBUG_FLAG); //Activa las líneas para debug

        updateData(delta); //Actualiza cualquier objeto de la pantalla que pueda cambiar
        updateEventServer(delta); //Permite ejecutar los eventos recibidos por el servidor en el hilo principal
    }

    @Override
    public void resize(int width, int height) {
        StaticsMethods.showLogResize(TAG);
        getStage().getViewport().update(width,height,true);
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
        StaticsMethods.showLogHide(TAG);
    }

    @Override
    public void dispose() {
        StaticsMethods.showLogDispose(TAG);
        if (ENABLE_LIGHTS) world.dispose();
        if (ENABLE_LIGHTS) rayHandler.dispose();
    }

    /*** Métodos propios ***/

    /**
     * Permite desconectarse del servidor
     *      backMenu: true --> Vuelve al menú principal
     *      backMenu: false --> No vuelve al menú principal (En caso de error de conexión con servidor
     *          se mantiene en la misma pantalla para volver a intentarlo)
     */
    public void disconnectedServer(boolean backMenu) {
        if (nodeJS != null) {
            if (nodeJS.isSocketConnected()) {
                nodeJS.disconnectServer(false);
                Gdx.app.log(TAG, "*DESCONEXION CON EL SERVIDOR*");
            } else Gdx.app.log(TAG, "*No existe conexion con el servidor*");
        }
        if (backMenu) changeScreen(new MenuScreen(getGame()), false);
    }

    private void updateData(float delta){
        hallWindows.updateWindowVersion();
        hallMenuButtons.doAnimation(delta);
    }

    /* INI - Métodos InterfaceServerScreen */
    public void updateEventServer(float delta){
        if(nodeJS!=null && nodeJS.isSocketConnected()){
            if(flagNews){
                flagNews = false;
                hallMenuButtons.getHallMenuNews().updateNews();
            }

            if(flagRanking){
                flagRanking = false;
                hallMenuButtons.getHallMenuRanking().updateSeasons();
            }

            if(flagMyUser){
                flagMyUser = false;
                hallMenuButtons.getHallMenuUser().updateUser();
            }

            if (flagUpdateUserDataServer) {
                flagUpdateUserDataServer = false;
                updateUserDataServer();
            }

            if(flagInvitationGame){
                flagInvitationGame = false;
                hallWindows.getInvitationHW().showSendInvitationGame();
            }

            if(flagNoticeServer){
                flagNoticeServer = false;
                hallWindows.getNoticeHW().updateNoticeServer(titleNoticeServer, textNoticeServer);
            }

            if(flagUpdateGamesDataServer) {
                flagUpdateGamesDataServer = false;
                gameContainer.updateGameContainer();
            }

            if (flagStartGame) {
                flagStartGame = false;
                /* Se deshabilitan las luces para evitar que el viewport de GSOnline sea "Stretch" dado que no se llama al método "resize"
                   hasta que no se minimiza la aplicación, provocando que el diseño del juego no sea el correcto.
                Esto pasa debido a algún factor implicado por la cámara que se crea para las luces. Al estar activas algún método de
                "updateLights" en ServerMainScreen es lo que provoca la imagen deforme.*/
                ENABLE_LIGHTS = false;
                changeScreen(new OnlineGS(getGame(), nodeJS, serverGameData, false), true);
            }
        }
    }
    /* FIN - Métodos InterfaceServerScreen */

    /** Eventos del Servidor **/
    public void setDataObject(ArrayList<UserDataServer> alUserDataServer, ArrayList<NewGameDataServer> alNewGameDataServer){
        serverObject.setAlServerDataUser(alUserDataServer);
        for (NewGameDataServer newGameDataServer : alNewGameDataServer) {
            //This allows updating the user list information
            serverObject.addNewGameDataServer(newGameDataServer);
        }
        this.flagUpdateUserDataServer = true;
        this.flagUpdateGamesDataServer = true;
    }

    public void addUserDataServer(UserDataServer userDataServer) {
        serverObject.addUserDataServer(userDataServer);
        this.flagUpdateUserDataServer = true;
    }

    public void deleteUserDataServer(String userName) {
        serverObject.deleteUserDataServer(userName);
        this.flagUpdateUserDataServer = true;
    }

    public void addNewGame(NewGameDataServer newGameDataServer){
        serverObject.addNewGameDataServer(newGameDataServer);
        updateMyGameId(newGameDataServer.getHostName(), newGameDataServer.getId());
        this.flagUpdateGamesDataServer = true;
        this.flagUpdateUserDataServer = true;
    }

    public void deleteGame(String id) {
        serverObject.deleteNewGameDataServer(id);
        deleteMyGameId(id);
        this.flagUpdateGamesDataServer = true;
        this.flagUpdateUserDataServer = true;
    }

    public void hideStartedGame(String id) {
//        serverObject.hideStartedGame(id);
        this.flagUpdateGamesDataServer = true;
        this.flagUpdateUserDataServer = true;
    }

    public void addPlayerGame(String id, String playerName) {
        serverObject.addPlayerGame(id, playerName);
        updateMyGameId(playerName, id);
        this.flagUpdateGamesDataServer = true;
        this.flagUpdateUserDataServer = true;
    }

    public void removePlayerGame(String id, String playerName) {
        serverObject.removePlayerGame(id, playerName);
        updateMyGameId(playerName,"");
        this.flagUpdateGamesDataServer = true;
        this.flagUpdateUserDataServer = true;
    }

    public void startGame(ServerGameData serverGameData) {
        this.serverGameData = serverGameData;
        this.flagStartGame = true;
    }

    public void showInvitationGame(String playerName, String gameId, int remainingPlayers){
        //Primero se establecen los valores a cambiar
        hallWindows.getInvitationHW().setInvitationGame(playerName, gameId, remainingPlayers);
        //Después abrimos el flag para mostrarla
        this.flagInvitationGame = true;
    }

    public void updateMyUser(){
        this.flagMyUser = true;
    }

    public void setNewSignIn(UserDataServer userDataServer){
        boolean exist = false;
        for(UserDataServer sdu : getAlUserDataServer()){
            if(sdu.getName().equals(userDataServer.getName())){
                sdu.updateObject(userDataServer);
                exist = true;
                break;
            }
        }
        if(!exist) getAlUserDataServer().add(userDataServer);

        this.flagUpdateUserDataServer = true;
        this.flagUpdateGamesDataServer = true;
    }

    public void setAlRankingPlayer(int iRanking, int iOrder, String lastRankingUpdate, ArrayList<RankingUserDataServer> alSDRanking){
        serverObject.setAlSPRanking(iRanking, iOrder, lastRankingUpdate, alSDRanking);
        this.flagRanking = true;
    }

    public void setNoticeServer(String title, String noticeServer){
        this.titleNoticeServer = title;
        this.textNoticeServer = noticeServer;
        this.flagNoticeServer = true;
    }

    public void updateUserIcon(String userName, int icon) {
        serverObject.updateUserIcon(userName, icon);
        updateMyUserIcon(userName, icon);
        this.flagMyUser = true;
        this.flagUpdateUserDataServer = true;
    }

    public void checkNews(){
        if(!serverObject.getNewsVersion().equals(Settings.getNewsVersion())){
            nodeJS.getNewsJS().eoGetNews();
        }
    }

    public void setNews(String newsVersion, ArrayList<NewsServer> alNews){
        serverObject.setNewsVersion(newsVersion);
        serverObject.setAlNews(alNews);
        this.flagNews = true;
    }

    private void updateMyGameId(String name, String gameId) {
        ServerUser serverUser = nodeJS.getServerUser();
        if (name.equals(serverUser.getName()))
            serverUser.setGameId(gameId);
    }

    private void deleteMyGameId(String gameId) {
        if (nodeJS != null) {
            ServerUser serverUser = nodeJS.getServerUser();
            if (serverUser.getGameId().equals(gameId))
                serverUser.setGameId("");
        }
    }

    private void updateMyUserIcon(String name, int icon) {
        if (nodeJS != null) {
            ServerUser serverUser = nodeJS.getServerUser();
            if (name.equals(serverUser.getName()))
                serverUser.setUserIcon(icon);
        }
    }

    /** Métodos GUI **/
    private void buildStage(){
        layerHall = builHallLayer();

        getStage().clear();
        Stack stack = new Stack();
        getStage().addActor(stack);
        stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);


        stack.add(layerHall);
        hallWindows.addWindows(stack);
    }

    private Stack builHallLayer(){
        Stack stackContentMenu = hallMenuButtons.buildMenuLayer();

        Stack mainStack = new Stack();
        mainStack.setFillParent(false);
        mainStack.setVisible(true);
        mainStack.setDebug(false);
        mainStack.add(gameContainer.buildGameContainer());
        mainStack.add(stackContentMenu);

        return mainStack;
    }

    public void updateLights(){
        rayHandler.setCombinedMatrix((OrthographicCamera) getCamera());
        rayHandler.updateAndRender();
    }

    private boolean isOnlyShowingLightingHall(){
        boolean gameExists = false;
        for (NewGameDataServer ngds: serverObject.getAlNewGameDataServer()) {
            if (!ngds.isCompleted()) {
                gameExists = true;
                break;
            }
        }

        return layerHall.isVisible() && !hallMenuButtons.isMenuContentVisible() && !gameExists;
    }

    private void initLights(){
        world = new World(new Vector2(0, -9.8f), true);
        RayHandler.setGammaCorrection(true);
        rayHandler = new RayHandler(world);
        rayHandler.useCustomViewport(getViewport().getScreenX(),getViewport().getScreenY(),getViewport().getScreenWidth(),getViewport().getScreenHeight());
        rayHandler.setShadows(false);
        rayHandler.setAmbientLight(0,0,0,0.2f);
        rayHandler.setBlurNum(3);

        coneLight = new ConeLight[6];
        coneLight[0] = new ConeLight(rayHandler, 10, null, 1200, Constants.VIEWPORT_WIDTH /1.05f,
                Constants.VIEWPORT_HEIGHT /1.09f, -130,32);
    }

    public void openFlagRanking(int iRanking, int iOrder){
        serverObject.setiRanking(iRanking);
        serverObject.setiOrder(iOrder);
        this.flagRanking=true;
    }

    private void updateUserDataServer() {
        hallMenuButtons.getHallMenuPlayers().updatePlayers();
        hallMenuButtons.updateTextButtonPlayers();
    }

    private void manageConnection(){
        if (nodeJS == null || !nodeJS.isSocketConnected())
            hallWindows.getwServerDisconnected().setVisible(true);
    }

    /** Getters and Setters */
    // public Stage getStage(){ return stage; }
    public Skin getSkin(){ return skin; }
    public Server getNodeJS(){ return nodeJS; }
    public ArrayList<NewsServer> getAlNews(){ return serverObject.getAlNews(); }
    public String getNewsVersion(){ return serverObject.getNewsVersion(); }
    public HallWindows getHallWindows(){ return hallWindows; }
    public HallMenuButtons getHallMenuButtons(){ return hallMenuButtons; }
    public ServerObject getServerObject(){ return serverObject; }
    public ArrayList<UserDataServer> getAlUserDataServer(){ return serverObject.getAlServerDataUser(); }
    public ArrayList<NewGameDataServer> getALNewGameDataServer(){ return serverObject.getAlNewGameDataServer(); }
}

package com.tfdevelopment.pochaonlinetfd.server;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.screen.server.login.LoginScreen;
import com.tfdevelopment.pochaonlinetfd.server.object.buttonevent.ButtonEventJS;
import com.tfdevelopment.pochaonlinetfd.server.object.game.ServerGameJS;
import com.tfdevelopment.pochaonlinetfd.server.object.login.LoginJS;
import com.tfdevelopment.pochaonlinetfd.server.object.game.NewGameJS;
import com.tfdevelopment.pochaonlinetfd.server.object.news.NewsJS;
import com.tfdevelopment.pochaonlinetfd.server.object.objectdata.ObjectDataJS;
import com.tfdevelopment.pochaonlinetfd.server.object.others.OthersJS;
import com.tfdevelopment.pochaonlinetfd.server.object.ranking.RankingUserDataJS;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.ServerUser;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.UserDataJS;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Server {
    private static final String TAG = Server.class.getName();

    private static Server server;
    private String vAppNodeJS = Config.VERSION;
    private Socket socket;

    private ServerUser serverUser;
    private String idSocket; //, playerNameSocket;

    private LoginJS loginJS;
    private ObjectDataJS objectDataJS;
    private UserDataJS userDataJS;
    private NewsJS newsJS;
    private ServerGameJS serverGameJS;
    private ButtonEventJS buttonEventJS;
    private RankingUserDataJS rankingJS;
    private NewGameJS newGameJS;
    private OthersJS othersJS;

    LoginScreen loginScreen;
    private ServerMainScreen serverMainScreen;
    private OnlineGS onlineGS;

    //Indica si la partida ha empezado con la aplicación en pausa (Evita perder eventos de decisión)
    private boolean paused;
    private boolean pausedAtStarted;

    // private ServerNodeJS(LoginScreen loginScreen) throws URISyntaxException {
    private Server(LoginScreen loginScreen) throws Exception {
        super();
        this.loginScreen = loginScreen;

        this.loginJS = new LoginJS(this);
        this.objectDataJS = new ObjectDataJS(this);
        this.userDataJS = new UserDataJS(this);
        this.newsJS = new NewsJS(this);
        this.rankingJS = new RankingUserDataJS(this);
        this.newGameJS = new NewGameJS(this);
        this.serverGameJS = new ServerGameJS(this);
        this.buttonEventJS = new ButtonEventJS(this);
        this.othersJS = new OthersJS(this);

        this.idSocket = "";
        this.paused = false;
        this.pausedAtStarted = false;

        initSocket();
        initSocketEvents();
    }

    public static Server getServerNodeJS(LoginScreen _loginScreen) throws Exception {
        if(server !=null){
            //Se debe guardar la nueva pantalla que se crea al salir y volver a HallScreen para
            //no perder la referencia a los objetos en pantalla
            server.setLoginScreen(_loginScreen);
        } else server = new Server(_loginScreen);

        return server;
    }

    public void connectServer(){
        if (socket == null) {
            Gdx.app.error(TAG, "connectServer() - Socket is null");
            return;
        }

        if (socket.connected()) {
            Gdx.app.log(TAG, "connectServer() - The socket is already connected.");
            return;
        }

        if (!socket.connected()) {
            socket.connect();
            Gdx.app.log(TAG, "connectServer() - Establishing connection to the server...");
        }
    }

    //fromMenu --> Indica si se ha solicitado desde el menu de login
    public void disconnectServer(boolean fromMenu){
        if (socket == null) {
            Gdx.app.log(TAG, "disconnectServer() - Socket null. ¡DISCONNECTED!");
            return;
        }

        if (socket.connected()) {
            othersJS.eoUserLeave(fromMenu);
            Gdx.app.log(TAG, "disconnectServer() - Disconnecting from the server...");
            socket.disconnect();
        }

        if (!socket.connected()) Gdx.app.log(TAG, "disconnectServer() - ¡DISCONNECTED!");
    }

    /**** Sockets ****/
    private void initSocket() throws Exception {
        Gdx.app.log(TAG, "* Init Socket *");
        socket = IO.socket(Config.URL);
        socket.connect();
    }

    private void initSocketEvents(){
        socket.on(Socket.EVENT_CONNECT, args -> eiConnect())
        .on(Socket.EVENT_DISCONNECT, args -> eiDisconnect())
        .on("forcedDisconnection", args -> eiDisconnect())
        .on(LoginJS.EX_INIT_PLAYER, args -> loginJS.eiInitPlayer(args))
        .on(LoginJS.EX_SERVER_NOT_AVAILABLE, args -> loginJS.eiNotAvailable(args))

        .on(LoginJS.EX_SIGN_IN, args -> loginJS.eiSignIn(args))
        .on(LoginJS.EX_SIGN_UP, args -> loginJS.eiSignUp(args))
        .on(LoginJS.EX_LOGIN_TO_HALL, args -> loginJS.eiLoginToHall(args))
        .on(LoginJS.EX_LOGIN_TO_GAME, args -> loginJS.eiLoginToGame(args))

        .on(UserDataJS.EX_MY_USER, args -> userDataJS.eiMyUser(args))
        .on(ObjectDataJS.EX_DATA_OBJECT, args -> objectDataJS.eiDataObject(args))
        .on(LoginJS.EX_ADD_USER, args -> loginJS.eiAddUser(args))
        .on(LoginJS.EX_DELETE_USER, args -> loginJS.eiDeleteUser(args))

        .on(NewsJS.EX_NEWS, args -> newsJS.eiNews(args))
        .on(RankingUserDataJS.EX_PLAYER_RANKING, args -> rankingJS.eiRankingPlayers(args))
        .on(ObjectDataJS.EX_USER_ICON, args -> objectDataJS.eiUpdateUserIcon(args))
        .on(OthersJS.EX_NOTICE_SERVER, args -> othersJS.eiNoticeServer(args))

        .on(NewGameJS.EX_NEW_GAME, args -> newGameJS.eiNewGame(args))
        .on(NewGameJS.EX_DELETE_GAME, args -> newGameJS.eiDeleteGame(args))
        .on(NewGameJS.EX_ADD_PLAYER_GAME, args -> newGameJS.eiAddPlayerGame(args))
        .on(NewGameJS.EX_REMOVE_PLAYER_GAME, args -> newGameJS.eiRemovePlayerGame(args))
        .on(NewGameJS.EX_GAME_INVITATION, args -> newGameJS.eiGameInvitation(args))
        .on(NewGameJS.EX_HIDE_STARTED_GAME, args -> newGameJS.eiHideStartedGame(args))

        .on(ServerGameJS.EX_START_GAME, args -> serverGameJS.eiStartGame(args))
        .on(ButtonEventJS.EX_BUTTON_EVENT, args -> buttonEventJS.eiButtonEvent(args))
        .on(ServerGameJS.EX_PLAYER_GAME_RECONNECTED, args -> serverGameJS.eiPlayerGameReconnected(args))
        .on(ServerGameJS.EX_RECONNECT_TO_GAME, args -> serverGameJS.eiReconnectToGame(args))
        .on(ServerGameJS.EX_GAME_TO_HALL, args -> serverGameJS.eiGameToHall(args));
    }

    /*************** EVENTOS *************/

    /** Eventos de entrada **/
    private void eiConnect(){
        Gdx.app.log(TAG,"eiConnect. Se ha conectado al Servidor. ID: "+ server.socket.id());

        if (!server.idSocket.isEmpty()) {
            if(PochaEnum.screenType == PochaEnum.ScreenType.ONLINE_GAME) {
                Gdx.app.log(TAG, "eiConnect. Reconectando a ID anterior: " + server.idSocket);
                serverGameJS.eoReconnectToGame(false);
            }
        } else server.idSocket = server.socket.id();
    }

    private void eiDisconnect(){
        Gdx.app.log(TAG,"eiDisconnect.");
        server.disconnectServer(false);
        Gdx.app.log(TAG,"eiDisconnect. OK. Se ha desconectado del Servidor.");
    }

    /*** Getters And Setters ***/
    public Socket getSocket(){ return socket; }
    public boolean isSocketConnected(){ return socket != null && socket.connected(); }
    public String getvAppNodeJS(){ return vAppNodeJS; }
    public void setvAppNodeJS(String vAppNodeJS){ this.vAppNodeJS = vAppNodeJS; }
    public ServerUser getServerUser(){ return serverUser; }
    public void setServerUser(ServerUser serverUser) { this.serverUser = serverUser; }

    public LoginJS getLoginJS() { return loginJS; }
    public ObjectDataJS getObjectDataJS() { return objectDataJS; }
    public UserDataJS getUserDataJS() { return userDataJS; }
    public NewsJS getNewsJS() { return newsJS; }
    public ServerGameJS getServerGameJS() { return serverGameJS; }
    public ButtonEventJS getButtonEventJS() { return buttonEventJS; }
    public RankingUserDataJS getRankingJS() { return rankingJS; }
    public NewGameJS getNewGameJS() { return newGameJS; }
    public OthersJS getOthersJS(){ return othersJS; }

    public LoginScreen getLoginScreen(){ return this.loginScreen; }
    public void setLoginScreen(LoginScreen loginScreen){ this.loginScreen = loginScreen; }
    public ServerMainScreen getServerMainScreen(){ return serverMainScreen; }
    public void setServerMainScreen(ServerMainScreen serverMainScreen){ this.serverMainScreen = serverMainScreen; }
    public OnlineGS getOnlineGS(){ return this.onlineGS; }
    public void setOnlineScreen(OnlineGS onlineGS){ this.onlineGS = onlineGS; }

    public void setPaused(boolean pause){ this.paused = pause; }
    public void setPausedAtStarted(boolean pausedAtStarted){ this.pausedAtStarted = pausedAtStarted; }

    public boolean isPaused(){ return this.paused; }
    public boolean isPausedAtStarted(){ return this.pausedAtStarted; }
}

package com.tfdevelopment.pochaonlinetfd.screen.server.login;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.server.object.ServerObject;
import com.tfdevelopment.pochaonlinetfd.server.object.game.ServerGameData;
import com.tfdevelopment.pochaonlinetfd.screen.AbstractScreen;
import com.tfdevelopment.pochaonlinetfd.screen.menu.MenuScreen;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.ScreenType;

import java.security.MessageDigest;

public class LoginScreen extends AbstractScreen {
    private static final String TAG = LoginScreen.class.getName();
    static final int MIN_CHARACTER_USERNAME = 3;
    static final int MAX_CHARACTER_USERNAME = 10;

    private static final boolean STAGE_DEBUG_FLAG = false;

    private Server nodeJS;

    public SignIn signIn;
    public SignUp signUp;
    private ErrorVersionWindow errorVersionWindow;
    private Table tSignIn, tSignUp, tErrorVersion;

    private boolean flagGoToHall, flagToGameStarted;
    private ServerObject serverObject;
    private ServerGameData serverGameData;

    private boolean flagReloadGame;

    public LoginScreen(Game game, boolean flagReloadGame) {
        super(game);

        signIn = new SignIn(this);
        signUp = new SignUp(this);
        errorVersionWindow = new ErrorVersionWindow(this);

        try {
            nodeJS = Server.getServerNodeJS(this);
            joinServer();
        } catch (Exception e) {
            Gdx.app.error(TAG,"***EXCEPCTION EN EL CONSTRUCTOR SERVER NODE JS***");
            e.printStackTrace();
        }

        this.flagReloadGame = flagReloadGame;
    }


    @Override
    public void show() {
        initScreen(ScreenType.LOGIN, new OrthographicCamera(), Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        buildStage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,1.0f); //Pinta la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Limpia la pantalla

        getStage().act(delta); //Permite actuar a los actores y activar los eventos de entrada y salida
        getStage().draw(); //Dibuja el escenario
        getStage().setDebugAll(STAGE_DEBUG_FLAG); //Activa las líneas para debug

        updateData(delta); //Actualiza cualquier objeto de la pantalla que pueda cambiar
        updateEventServer(delta); //Permite ejecutar los eventos recibidos por el servidor en el hilo principal
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width,height,true);
    }

    @Override
    public void hide() {
        Gdx.app.log(TAG, "Override: HIDE");
    }

    /****** Métodos propios *********/
    private void updateData(float delta){
        errorVersionWindow.render(delta);
        signIn.render(delta);
        signUp.render(delta);
    }

    private void updateEventServer(float delta){
        if(nodeJS!=null && nodeJS.isSocketConnected()){
            if(flagGoToHall){
                flagGoToHall = false;
                changeScreen(new ServerMainScreen(getGame(), nodeJS, serverObject), true);
            }

            if(flagToGameStarted){
                flagToGameStarted = false;
                changeScreen(new OnlineGS(getGame(), nodeJS, serverGameData, true), true);
            }
        }
    }

    void changeWindow(){
        if(tSignIn.isVisible()){
            tSignIn.setVisible(false);
            tSignUp.setVisible(true);
        } else {
            tSignUp.setVisible(false);
            tSignIn.setVisible(true);
        }
    }

    public void loginToHall(ServerObject _serverObject){
        serverObject = _serverObject;
        flagGoToHall = true;
    }

    public void loginToGame(ServerGameData dataServerGame){
        this.serverGameData = dataServerGame;
        flagToGameStarted = true;
    }

    void signUpSuccessfull(String name, String pass){
        signIn.lError.setColor(Color.GREEN);
        signIn.lError.setText("¡Registrado! Inicie sesión");
        signIn.tfUsuario.setText(name);
        signIn.tfPass.setText(pass);
        tSignUp.setVisible(false);
        tSignIn.setVisible(true);
    }

    private void buildStage(){
        tSignIn = signIn.buildSignIn();
        tSignUp = signUp.buildSignUp();
        tErrorVersion = errorVersionWindow.buildWindowErrorVersion();

        //Monta el escenario con las distintas capas de la pantalla Menú
        getStage().clear();
        Stack stack = new Stack();
        getStage().addActor(stack); //Añade la pila al escenario
        stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        //Añade las distintas capas a la pila
        stack.add(buildBackground());
        stack.add(tSignIn);
        stack.add(tSignUp);
        stack.add(tErrorVersion);

        //Oculta la interfaz cuando se vaya a recargar la partida
        if(this.flagReloadGame){
            signIn.tbCreateUser.setVisible(false);
            signIn.tbUnirse.setVisible(false);
            signIn.tbMenu.setVisible(false);
        }
    }

    public void joinServer()  {
        if(nodeJS!=null) {
            if (!nodeJS.isSocketConnected()) {
                nodeJS.connectServer();
                if(nodeJS.isSocketConnected()) Gdx.app.log(TAG, "*CONEXIÓN CON EL SERVIDOR*");
            } else Gdx.app.log(TAG, "*Ya existe una conexión con el servidor*");
        }
    }

    /**
     * Permite desconectarse del servidor
     *      backMenu: true --> Vuelve al menú principal
     *      backMenu: false --> No vuelve al menú principal (En caso de error de conexión con servidor
     *          se mantiene en la misma pantalla para volver a intentarlo)
     */
    public void disconnectedServer(boolean backMenu) {
        if (nodeJS != null) {
            if (nodeJS.isSocketConnected()) {
                nodeJS.disconnectServer(true);
                Gdx.app.log(TAG, "*DESCONEXION CON EL SERVIDOR*");
            } else Gdx.app.log(TAG, "*No existe conexion con el servidor*");
        }
        if (backMenu) changeScreen(new MenuScreen(getGame()), false);
    }

    /** INI - Eventos del Servidor **/
    public void availableServer_NJS(boolean available, String message){
        Label lAux;
        if(tSignIn.isVisible()) { lAux = signIn.lError; signIn.waitingResponse = false; }
        else if(tSignUp.isVisible()) { lAux = signUp.lError; signUp.waitingResponse = false; }
        else lAux = signIn.lError;

        lAux.setColor(Color.RED);
        if(!available) lAux.setText(message);
        else lAux.setText("");
    }
    /** FIN - Eventos del Servidor **/

    String getEncryptString(String text){
        if(!text.isEmpty()){
            String encrypt = "";

            try{
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(text.getBytes());
                StringBuffer sb = new StringBuffer();
                for(byte b:hash){
                    sb.append(String.format("%02x",b));
                }
                text = sb.toString();

            } catch (Exception exception){
                text = "";
            }
        } else text = "";

        return text;
    }

    private Table buildBackground(){
        Image image = new Image(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getBackgroundMenu()));

        Table layer = new Table();
        layer.setFillParent(true);
        layer.left().top();
        layer.add(image).size(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);

        return layer;
    }

    /****** Getters and Setters *********/
    public Server getNodeJS(){ return nodeJS; }
}

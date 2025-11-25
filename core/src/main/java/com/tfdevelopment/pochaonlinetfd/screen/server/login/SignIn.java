package com.tfdevelopment.pochaonlinetfd.screen.server.login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class SignIn {
    private static final String TAG = SignIn.class.getName();

    private static final int LOGIN_WIDTH = 160;
    private static final int LOGIN_HEIGHT = 30;
    private static final int TIME_SERVER_CONNECTION = 1200;

    private LoginScreen loginScreen;

    private Table layerUser;
    TextButton tbUnirse, tbCreateUser, tbMenu;
    TextField tfUsuario, tfPass;
    Label lError;
    private Label lStatusServer;

    private boolean signInPressed;
    boolean waitingResponse;
    private float secondsWaitingResponse;

    private boolean flagSignIn;
    private String infoSignIn;
    private Color colorSignIn;

    public SignIn(LoginScreen loginScreen){
        this.loginScreen = loginScreen;

        signInPressed = false;
        waitingResponse = false;
        secondsWaitingResponse = 0f;
    }

    public void render(float delta){
        updateData(delta);

        if(flagSignIn){
            flagSignIn = false;
            lError.setColor(colorSignIn);
            lError.setText(infoSignIn);
        }
    }

    private void updateData(float delta){
        if (loginScreen.getNodeJS() != null){
            if (loginScreen.getNodeJS().isSocketConnected()) {
                lStatusServer.setColor(Color.GREEN);
                lStatusServer.setText("¡CONECTADO!");

                if(waitingResponse){
                    secondsWaitingResponse+=delta;
                    if(secondsWaitingResponse>=2.4f) lError.setText("Esperando respuesta...");
                    else if(secondsWaitingResponse>=1.8f) lError.setText("Esperando respuesta..");
                    else if(secondsWaitingResponse>=1.2f) lError.setText("Esperando respuesta.");
                    else if(secondsWaitingResponse>=0.6f) lError.setText("Esperando respuesta");

                    if(secondsWaitingResponse>=2.4f) secondsWaitingResponse = 0f;
                }
            } else {
                lStatusServer.setColor(Color.RED);
                lStatusServer.setText("Pulsa \"ENTRAR\" para conectarse al servidor");
            }
        } else {
            lStatusServer.setColor(Color.RED);
            lStatusServer.setText("ERROR al conectarse con el servidor");
        }
    }

    /** INI - Eventos del Servidor **/
    public void signIn_NJS(boolean login, String info){
        if(login){
            colorSignIn = Color.GREEN;
            infoSignIn = info;
            Gdx.input.setOnscreenKeyboardVisible(false);
        } else {
            waitingResponse = false; //Se establece a false solo en el error para no enviar dos eventos de inicio de sesión
            colorSignIn = Color.RED;
            infoSignIn = info;
        }
        flagSignIn = true;
    }
    /** FIN - Eventos del Servidor **/

    Table buildSignIn(){
        Window wLogin = new Window("¡Únete al salón!", StyleConfigurator.getWS_Maiandra80(loginScreen.getSkin()));
        wLogin.setFillParent(false);
        wLogin.setDebug(false);
        wLogin.setMovable(false);
        wLogin.setResizable(false);
        wLogin.getTitleLabel().setAlignment(Align.center);

        Label lUserName = new Label("Nombre de usuario  ", StyleConfigurator.getLS_Maiandra60());
        lUserName.setAlignment(Align.center);

        Label lPass = new Label("Contraseña  ", StyleConfigurator.getLS_Maiandra60());
        lPass.setAlignment(Align.center);

        tbUnirse = new TextButton("¡ENTRAR!", StyleConfigurator.getTBS_RoundButton60(loginScreen.getSkin()));
        tbCreateUser = new TextButton("Crear usuario", StyleConfigurator.getTBS_RoundButton60(loginScreen.getSkin()));
        tbMenu = new TextButton("Volver al menú", StyleConfigurator.getTBS_RoundButton60(loginScreen.getSkin()));

        tfUsuario = new TextField("", StyleConfigurator.getTFS_Login(loginScreen.getSkin()));
        tfUsuario.setAlignment(Align.center);
        tfUsuario.setText(Settings.getPlayerName());

        tfPass = new TextField("", StyleConfigurator.getTFS_Login(loginScreen.getSkin()));
        tfPass.setAlignment(Align.center);
        tfPass.setPasswordCharacter(("*").charAt(0));
        tfPass.setPasswordMode(true);
        tfPass.setText("");

        final CheckBox cbPass = new CheckBox("   Recordar contraseña", StyleConfigurator.getCBS_ShowWindowWelcome(loginScreen.getSkin()));

        if(Settings.getPlayerPassword().isEmpty()){
            tfPass.setText("");
            cbPass.setChecked(false);
        } else {
            tfPass.setText(Settings.getPlayerPassword());
            cbPass.setChecked(true);
        }

        lError = new Label("", StyleConfigurator.getLS_Maiandra50());
        lError.setColor(Color.RED);
        lError.setAlignment(Align.center);
        lStatusServer = new Label("", StyleConfigurator.getLS_Maiandra50());
        lStatusServer.setColor(Color.RED);
        lStatusServer.setAlignment(Align.center);

        layerUser = new Table();
        layerUser.setFillParent(false);
        layerUser.setVisible(true);
        layerUser.setDebug(false);
        wLogin.add(lUserName).size(LOGIN_WIDTH,LOGIN_HEIGHT);
        wLogin.add(tfUsuario).size(LOGIN_WIDTH,LOGIN_HEIGHT).space(10f).padTop(Constants.VIEWPORT_HEIGHT*0.01f);
        wLogin.row();
        wLogin.add(lPass).size(LOGIN_WIDTH,LOGIN_HEIGHT);
        wLogin.add(tfPass).size(LOGIN_WIDTH,LOGIN_HEIGHT).space(10f);
        wLogin.row();
        wLogin.add(cbPass).size(LOGIN_WIDTH,LOGIN_HEIGHT).colspan(2).padTop(10f);
        wLogin.row();
        wLogin.add(tbMenu).size(LOGIN_WIDTH,LOGIN_HEIGHT*1.3f).padTop(20f);
        wLogin.add(tbCreateUser).size(LOGIN_WIDTH,LOGIN_HEIGHT*1.3f).padTop(20f);
        wLogin.row();
        wLogin.add(tbUnirse).size(LOGIN_WIDTH*2+6f,LOGIN_HEIGHT*1.3f).padTop(10f).colspan(2).left();
        wLogin.row();
        wLogin.add(lError).size(LOGIN_WIDTH,LOGIN_HEIGHT).colspan(2).padTop(10f).padBottom(10f);
        wLogin.row();
        wLogin.add(lStatusServer).size(LOGIN_WIDTH,LOGIN_HEIGHT).colspan(2);

        layerUser.add(wLogin).size(Constants.VIEWPORT_WIDTH *0.9f,Constants.VIEWPORT_HEIGHT *0.45f);

        tbUnirse.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (loginScreen.getNodeJS()!=null && loginScreen.getNodeJS().isSocketConnected() && loginScreen.getNodeJS().getvAppNodeJS().equals(Config.VERSION)) {
                    if(!signInPressed && !waitingResponse) {
                        signInPressed = true;
                        Settings.playSoundClickButton();
                        if (isCorrectCredentials()) {
                            try {
                                Settings.setPlayerName(tfUsuario.getText());
                                if (cbPass.isChecked())
                                    Settings.setPlayerPassword(tfPass.getText());
                                else Settings.setPlayerPassword("");

                                loginScreen.joinServer(); //Se vuelve a intentar conectar por si hubiese habido algún error

                                //Segundos de espera para la conexión con el servidor
                                Thread.sleep(TIME_SERVER_CONNECTION);

                                //Si se ha conectado, intenta entrar con dicho nombre de usuario
                                if (loginScreen.getNodeJS().isSocketConnected()) {
                                    String passEncrypt = loginScreen.getEncryptString(tfPass.getText());
                                    loginScreen.getNodeJS().getLoginJS().eoSignIn(tfUsuario.getText(), passEncrypt);
                                    waitingResponse = true;
                                } else { //Si no ha llegado a conectarse, mensaje de error
                                    lStatusServer.setText("Error de conexión. Inténtelo otra vez.");
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } /*catch (Exception e) {
                                Gdx.app.error(TAG, "ERROR. Conexión servidor: " + e.getMessage());
                                lStatusServer.setText("Ocurrió un problema con el servidor.");
                            }*/
                        }
                        signInPressed = false;
                    }
                }
            }
        });

        tbCreateUser.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loginScreen.changeWindow();
            }
        });

        tbMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                loginScreen.disconnectedServer(true);
            }
        });

        return layerUser;
    }

    private boolean isCorrectCredentials(){
        if(tfUsuario.getText().isEmpty()){
            lError.setText("Introduzca un nombre");
            return false;
        }

        if(tfUsuario.getText().length() < LoginScreen.MIN_CHARACTER_USERNAME){
            lError.setText("El nombre debe tener al menos " + LoginScreen.MIN_CHARACTER_USERNAME + " caracteres");
            return false;
        }

        if(tfUsuario.getText().length() > LoginScreen.MAX_CHARACTER_USERNAME){
            lError.setText("El nombre no puede superar los " + LoginScreen.MAX_CHARACTER_USERNAME + " caracteres");
            return false;
        }

        if(tfUsuario.getText().contains(" ")){
            lError.setText("No se permiten espacios en el nombre de usuario");
            return false;
        }

        if(tfPass.getText().isEmpty()){
            lError.setText("Introduzca una contraseña");
            return false;
        }

        if(tfPass.getText().length()<=3){
            lError.setText("La contraseña debe tener al menos 4 caracteres");
            return false;
        }

        return true;
    }
}

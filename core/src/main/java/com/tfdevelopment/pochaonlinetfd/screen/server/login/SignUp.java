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

import java.util.regex.Pattern;

public class SignUp {
    private static final String TAG = SignUp.class.getName();

    private static final int LOGIN_WIDTH = 160;
    private static final int LOGIN_HEIGHT = 30;
    private static final int TIME_SERVER_CONNECTION = 1200;

    private LoginScreen loginScreen;

    private Table layerUser;
    private TextField tfUsuario, tfPassOne, tfPassTwo;
    private Label lStatusServer;
    Label lError;

    private boolean signUpPressed;
    boolean waitingResponse;
    private float secondsWaitingResponse;

    public SignUp(LoginScreen loginScreen){
        this.loginScreen = loginScreen;

        signUpPressed = false;
        waitingResponse = false;
        secondsWaitingResponse = 0f;
    }

    public void render(float delta){
        updateData(delta);
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
    public void signUp_NJS(boolean login, String info){
        waitingResponse = false;
        if(!login) {
            lError.setColor(Color.RED);
            lError.setText(info);
        } else {
            lError.setColor(Color.GREEN);
            lError.setText("¡Registrado! Inicie sesión en la pantalla anterior");
            loginScreen.signUpSuccessfull(tfUsuario.getText(), tfPassOne.getText());
            Gdx.input.setOnscreenKeyboardVisible(false);
        }
    }
    /** FIN - Eventos del Servidor **/

    Table buildSignUp(){
        Window wLogin = new Window("¡Regístrate!", StyleConfigurator.getWS_Maiandra80(loginScreen.getSkin()));
        wLogin.setFillParent(false);
        wLogin.setMovable(false);
        wLogin.setResizable(false);
        wLogin.getTitleLabel().setAlignment(Align.center);

        Label lUserName = new Label("Nombre de usuario  ", StyleConfigurator.getLS_Maiandra60());
        lUserName.setAlignment(Align.center);

        Label lPassOne = new Label("Contraseña  ", StyleConfigurator.getLS_Maiandra60());
        lPassOne.setAlignment(Align.center);

        Label lPassTwo = new Label("Repetir contraseña  ", StyleConfigurator.getLS_Maiandra60());
        lPassTwo.setAlignment(Align.center);

        TextButton tbUnirse = new TextButton("¡REGISTRARSE!", StyleConfigurator.getTBS_RoundButton60(loginScreen.getSkin()));
        TextButton tbHaveUser = new TextButton("Ya tengo usuario", StyleConfigurator.getTBS_RoundButton60(loginScreen.getSkin()));

        tfUsuario = new TextField("", StyleConfigurator.getTFS_Login(loginScreen.getSkin()));
        tfUsuario.setAlignment(Align.center);

        tfPassOne = new TextField("", StyleConfigurator.getTFS_Login(loginScreen.getSkin()));
        tfPassOne.setAlignment(Align.center);
        tfPassOne.setText("");

        tfPassTwo = new TextField("", StyleConfigurator.getTFS_Login(loginScreen.getSkin()));
        tfPassTwo.setAlignment(Align.center);
        tfPassTwo.setText("");

        final CheckBox cbPass = new CheckBox("   Recordar contraseña", StyleConfigurator.getCBS_ShowWindowWelcome(loginScreen.getSkin()));
        cbPass.setChecked(false);

        lError = new Label("", StyleConfigurator.getLS_Maiandra50());
        lError.setColor(Color.RED);
        lError.setAlignment(Align.center);
        lStatusServer = new Label("", StyleConfigurator.getLS_Maiandra50());
        lStatusServer.setColor(Color.RED);
        lStatusServer.setAlignment(Align.center);

        layerUser = new Table();
        layerUser.setFillParent(false);
        layerUser.setVisible(false);
        layerUser.setDebug(false);
        wLogin.add(lUserName).size(LOGIN_WIDTH,LOGIN_HEIGHT);
        wLogin.add(tfUsuario).size(LOGIN_WIDTH,LOGIN_HEIGHT).space(10f);
        wLogin.row();
        wLogin.add(lPassOne).size(LOGIN_WIDTH,LOGIN_HEIGHT);
        wLogin.add(tfPassOne).size(LOGIN_WIDTH,LOGIN_HEIGHT).space(10f);
        wLogin.row();
        wLogin.add(lPassTwo).size(LOGIN_WIDTH,LOGIN_HEIGHT);
        wLogin.add(tfPassTwo).size(LOGIN_WIDTH,LOGIN_HEIGHT).space(10f);
        wLogin.row();
        wLogin.add(cbPass).size(LOGIN_WIDTH,LOGIN_HEIGHT).colspan(2).padTop(10f);
        wLogin.row();
        wLogin.add(tbHaveUser).size(LOGIN_WIDTH*2+6f,LOGIN_HEIGHT*1.3f).padTop(20f).colspan(2);
        wLogin.row();
        wLogin.add(tbUnirse).size(LOGIN_WIDTH*2+6f,LOGIN_HEIGHT*1.3f).padTop(10f).colspan(2);
        wLogin.row();
        wLogin.add(lError).size(LOGIN_WIDTH,LOGIN_HEIGHT).colspan(2).padTop(10f).padBottom(10f);
        wLogin.row();
        wLogin.add(lStatusServer).size(LOGIN_WIDTH,LOGIN_HEIGHT).colspan(2);

        layerUser.add(wLogin).size(Constants.VIEWPORT_WIDTH *0.9f,Constants.VIEWPORT_HEIGHT *0.50f);

        tbUnirse.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (loginScreen.getNodeJS()!=null && loginScreen.getNodeJS().isSocketConnected() && loginScreen.getNodeJS().getvAppNodeJS().equals(Config.VERSION)) {
                    if(!Config.CONFIG_SERVER) {
                        if (!signUpPressed && !waitingResponse) {
                            signUpPressed = true;
                            Settings.playSoundClickButton();
                            if (isCorrectCredentials()) {
                                try {
                                    Settings.setPlayerName(tfUsuario.getText());
                                    if (cbPass.isChecked())
                                        Settings.setPlayerPassword(tfPassOne.getText());
                                    else Settings.setPlayerPassword("");

                                    loginScreen.joinServer(); //Se vuelve a intentar conectar por si hubiese habido algún error

                                    //Segundos de espera para la conexión con el servidor
                                    Thread.sleep(TIME_SERVER_CONNECTION);

                                    //Si se ha conectado, intenta entrar con dicho nombre de usuario
                                    if (loginScreen.getNodeJS().isSocketConnected()) {
                                        String passEncrypt = loginScreen.getEncryptString(tfPassOne.getText());
                                        loginScreen.getNodeJS().getLoginJS().eoSignUp(tfUsuario.getText(), passEncrypt);
                                        waitingResponse = true;
                                    } else { //Si no ha llegado a conectarse, mensaje de error
                                        lStatusServer.setText("Error de conexión. Inténtelo otra vez.");
                                    }
                                } catch (InterruptedException e) {
                                    Gdx.app.error(TAG, "ERROR. Conexión servidor: " + e.getMessage());
                                    lStatusServer.setText("Ocurrió un problema con el servidor");
                                }
                            }
                            signUpPressed = false;
                        }
                    } else if(Config.ADMIN_ON && Config.CONFIG_SERVER) configServer();
                }
            }
        });

        tbHaveUser.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loginScreen.changeWindow();
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

        String userName = tfUsuario.getText().toLowerCase();
        String reservedName[] = new String[]{"cpu","null","undefined", "root", "admin",
                "jimmy", "jinny", "jinmy", "jimny", "jimmmy", "jimy", "jiny", "gimmy", "jimmi",
                "gilipollas", "hijoputa", "hijodeputa", "subnormal", "tonto", "idiota", "imbecil", "puta",
                "bot", "bbot", "boot", "bott"};
        if(!Config.ADMIN_ON) {
            for (int i = 0; i < reservedName.length; i++) {
                if (userName.contains(reservedName[i])) {
                    lError.setText("Nombre de usuario reservado");
                    return false;
                }
            }
        }

        if(userName.contains(" ") || tfPassOne.getText().contains(" ")){
            lError.setText("No se permiten espacios.");
            return false;
        }

        String regularExrepssion = "^[a-zA-Z0-9]*$";
        if(!Pattern.matches(regularExrepssion,userName) || !Pattern.matches(regularExrepssion,tfPassOne.getText())){
            lError.setText("Solo se permiten letras y números");
            return false;
        }

        if(tfPassOne.getText().isEmpty() || tfPassTwo.getText().isEmpty()){
            lError.setText("Introduzca una contraseña");
            return false;
        }

        if(tfPassOne.getText().length()<=3){
            lError.setText("La contraseña debe tener al menos 4 caracteres");
            return false;
        }

        if(!tfPassOne.getText().equals(tfPassTwo.getText())){
            lError.setText("Las contraseñas deben coincidir");
            return false;
        }

        return true;
    }

    private void configServer(){
        try {
            if(tfUsuario.getText().isEmpty() && tfPassOne.getText().isEmpty())
                loginScreen.getNodeJS().getOthersJS().eoServerHandler(Integer.parseInt(tfPassTwo.getText()));
            else if(tfUsuario.getText().isEmpty() && tfPassTwo.getText().isEmpty())
                loginScreen.getNodeJS().getOthersJS().eoPullOutServer(tfPassOne.getText());
            else loginScreen.getNodeJS().getOthersJS().eoNoticeServer(tfUsuario.getText(), tfPassOne.getText(), tfPassTwo.getText());
        } catch (Exception ex){
            ex.getMessage();
        }
    }
}

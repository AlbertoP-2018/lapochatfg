package com.tfdevelopment.pochaonlinetfd.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tfdevelopment.pochaonlinetfd.screen.AbstractScreen;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OfflineGS;
import com.tfdevelopment.pochaonlinetfd.screen.server.login.LoginScreen;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.ScreenType;

public class MenuScreen extends AbstractScreen {
    private static final String TAG = MenuScreen.class.getName();

    private static final boolean STAGE_DEBUG_FLAG = false;

    private Window wWelcome, wInfo, wReview, wNewGame;
    private CheckBox cbDontShowWelcome;

    public MenuScreen(Game game){
        super(game);
    }

    /*** Métodos implementados de Screen ***/
    @Override
    public void show() {
        initScreen(ScreenType.MENU, null, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        buildStage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,1.0f); //Pinta la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Limpia la pantalla

        getStage().act(delta); //Permite actuar a los actores y activar los eventos de entrada y salida
        getStage().draw(); //Dibuja el escenario
        getStage().setDebugAll(STAGE_DEBUG_FLAG); //Activa las líneas para debug
    }

    @Override
    public void resize(int width, int height) {
        StaticsMethods.showLogResize(TAG);
        getStage().getViewport().update(width,height,true);
    }

    @Override
    public void pause() {
        StaticsMethods.showLogPause(TAG);
    }

    @Override
    public void resume() { StaticsMethods.showLogResume(TAG); }

    @Override
    public void hide() {
        super.hide();
        StaticsMethods.showLogHide(TAG);
    }

    /*** Métodos propios ***/
    private void buildStage(){
        getStage().clear();
        Stack stack = new Stack();
        getStage().addActor(stack);
        stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        stack.add(buildBackgroundLayer());
        stack.add(buildButtonLayer());
        stack.add(buildButtonsConfLayer());
        stack.add(buildWindowInfoLayer());
        stack.add(buildWelcomLayer());
        stack.add(buildWindowReview());
        stack.add(buildSettingsGame());
    }

    private Table buildBackgroundLayer(){
        Image image = new Image(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getBackgroundMenu()));

        Table layer = new Table();
        layer.setFillParent(true);
        layer.left().top();
        layer.add(image).size(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);

        return layer;
    }

    private Table buildButtonLayer(){
        float widthOptionButton = 2.6f;
        float heightOptionButton = 11.5f;
        float spaceOptionButton = 40f;
        float widthExitButton = 3f;
        float heightExitButton = 13.4f;
        float spaceExitButton = 160f;

        TextButton tbOnline = new TextButton("Partida Online", StyleConfigurator.getTBS_Grey());
        tbOnline.getLabel().setColor(Color.BLACK);
        tbOnline.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                changeScreen(new LoginScreen(getGame(), false), false);
            }
        });

        final TextButton tbIA = new TextButton("Un jugador (IA)", StyleConfigurator.getTBS_Grey());
        tbIA.getLabel().setColor(Color.BLACK);
        tbIA.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wNewGame.setVisible(true);
            }
        });

        TextButton tbSalir = new TextButton("Salir", StyleConfigurator.getTBS_Grey());
        tbSalir.getLabel().setColor(Color.RED);
        tbSalir.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        Table layer = new Table();
        layer.center();
        layer.add(tbOnline).size(Constants.VIEWPORT_WIDTH /widthOptionButton, Constants.VIEWPORT_HEIGHT /heightOptionButton).space(spaceOptionButton);
        layer.row();
        layer.add(tbIA).size(Constants.VIEWPORT_WIDTH /widthOptionButton, Constants.VIEWPORT_HEIGHT /heightOptionButton).space(spaceOptionButton);
        layer.row();
        layer.add(tbSalir).size(Constants.VIEWPORT_WIDTH /widthExitButton, Constants.VIEWPORT_HEIGHT /heightExitButton).space(spaceExitButton);

        return layer;
    }

    private Table buildButtonsConfLayer(){
        Button bInfo = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getbInfo()));
        bInfo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                if(wWelcome.isVisible() && !wInfo.isVisible()) {
                    wWelcome.setVisible(false);
                    wInfo.setVisible(true);
                } else if(wWelcome.isVisible()) wWelcome.setVisible(false);
                else if(wInfo.isVisible()) wInfo.setVisible(false);
                else wInfo.setVisible(true);
            }
        });

        Table layer = new Table();
        layer.add(new Actor()).expand();
        layer.row().right();
        layer.add(bInfo).size(Constants.VIEWPORT_WIDTH /9,Constants.VIEWPORT_HEIGHT /16);

        return layer;
    }

    private Table buildWindowInfoLayer(){
        Button bTwitter = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getbTwitter()));
        bTwitter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI(Constants.URI_TWITTER);
            }
        });
        Button bGmail = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getbGmail()));

        Label lTwitter = new Label("TF_Development", StyleConfigurator.getLS_Maiandra50());
        lTwitter.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(Constants.URI_TWITTER);
            }
        });
        Label lGmail = new Label("TFDevelopment.Contact@gmail.com", StyleConfigurator.getLS_Maiandra50());
        Label lVersion = new Label(Config.VERSION, StyleConfigurator.getLS_Maiandra50());
        lVersion.setColor(Color.RED);

        Button bPlayStore = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getbPlayStore()));
        bPlayStore.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(Constants.URI_PLAYSTORE);
            }
        });

        TextButton tbWelcome = new TextButton("¿Cómo jugar?", StyleConfigurator.getTBS_RoundButton(getSkin()));
        tbWelcome.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                if(Settings.isShowWelcome()) cbDontShowWelcome.setChecked(false);
                else cbDontShowWelcome.setChecked(true);
                wWelcome.setVisible(true);
            }
        });
        TextButton tbOk = new TextButton("Aceptar", StyleConfigurator.getTBS_RoundButton(getSkin()));
        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wInfo.setVisible(false);
            }
        });

        wInfo = new Window("TFDevelopment ©",StyleConfigurator.getWS_Maiandra80(getSkin()));
        setStyleWindow(wInfo);
        wInfo.setVisible(false);
        wInfo.setMovable(false);
        wInfo.setResizable(false);

        Table layer = new Table();
        layer.setFillParent(true);
        layer.setVisible(true);
        wInfo.add(bTwitter).size(85,55).padTop(10f);
        wInfo.add(lTwitter).left().space(10f);
        wInfo.row();
        wInfo.add(bGmail).size(85,55).padTop(5f);
        wInfo.add(lGmail).space(10f).padRight(10f);
        wInfo.row();
        wInfo.add(bPlayStore).size(220,90).colspan(2).space(10f);
        wInfo.row();
        wInfo.add(tbWelcome).colspan(2).space(20f);
        wInfo.row();
        wInfo.add(tbOk).colspan(2);
        wInfo.row();
        wInfo.add(lVersion).colspan(2).right();
        layer.add(wInfo);

        return layer;
    }

    private Table buildWelcomLayer(){
        String text =
                "                          Partida Online\n"+
                        " ¡Únete al salón y juega contra otros jugadores!\n"+
                        " Juega partidas públicas o demuestra tu nivel en\n"+
                        " en el modo clasificación.\n\n"+
                        "                          Un jugador (IA)\n"+
                        " Si no tienes internet o prefieres jugar solo \n"+
                        " prueba tu capacidad jugando contra la máquina. \n\n"+
                        " Contacta con nosotros a través de las redes\n"+
                        " sociales en el botón de información.";

        wWelcome = new Window("¡Bienvenido a La Pocha Online!", StyleConfigurator.getWS_Maiandra80(getSkin()));
        setStyleWindow(wWelcome);
        if(Settings.isShowWelcome())
            wWelcome.setVisible(true);
        else  wWelcome.setVisible(false);
        wWelcome.setMovable(false);
        wWelcome.setResizable(false);

        //Label lGameMode = new Label(text, StyleConfigurator.getLS_Default_Custom());
        Label lGameMode = new Label(text, StyleConfigurator.getLS_Maiandra60());
        //lGameMode.getStyle().font.getData().setScale(0.65f);
        cbDontShowWelcome = new CheckBox(" No volver a mostrar", StyleConfigurator.getCBS_Default(getSkin()));
        cbDontShowWelcome.setChecked(false);
        TextButton tbOk = new TextButton("Continuar", StyleConfigurator.getTBS_RoundButton(getSkin()));
        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                if(cbDontShowWelcome.isChecked()) Settings.setShowWelcome(false);
                else Settings.setShowWelcome(true);

                Settings.save();
                wWelcome.setVisible(false);
            }
        });

        Table layer = new Table();
        layer.setFillParent(true);
        wWelcome.add(lGameMode).space(10f);
        wWelcome.row();
        wWelcome.add(cbDontShowWelcome).space(10f);
        wWelcome.row();
        wWelcome.add(tbOk).space(10f);
        wWelcome.row();
        layer.add(wWelcome);

        return layer;
    }

    public Table buildWindowReview(){
        String text =   "Déjanos una valoración en la\n"+
                "Play Store y ayúdanos a llegar a\nmás personas.\n\n"+
                "¡Valóranos y juega sin límites!";
        Label lReview = new Label(text, StyleConfigurator.getLS_Maiandra65());
        lReview.setAlignment(Align.center);

        TextButton tbAgree = new TextButton(" ¡Dejar valoración! ", StyleConfigurator.getTBS_RoundButton50(getSkin()));
        tbAgree.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                wReview.setVisible(false);
                //MusOnlineMain.showReviewInApp();
                Gdx.net.openURI(Constants.URI_PLAYSTORE);
                Settings.setShowReview(true);
            }
        });

        TextButton tbCancel = new TextButton(" Quizás más tarde ", StyleConfigurator.getTBS_RoundButton50(getSkin()));
        tbCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                wReview.setVisible(false);
            }
        });

        wReview = new Window("  ¡Ayúdanos a llegar a más gente!  ", StyleConfigurator.getWS_Maiandra80(getSkin()));
        wReview.setColor(new Color(112f/255f,213f/255f,82f/255f,1f));
        wReview.setVisible(false);
        wReview.setMovable(false);
        wReview.setResizable(false);
        wReview.getTitleLabel().setAlignment(Align.center);
        wReview.add(lReview).padLeft(10f).padRight(10f).padTop(5f).padBottom(5f).colspan(2);
        wReview.row();
        wReview.add(tbAgree).padBottom(20f).padTop(20f);
        wReview.add(tbCancel).padBottom(20f).padLeft(20f).padRight(5f).padTop(20f);

        Table table = new Table();
        table.setFillParent(true);
        table.add(wReview);

        return table;
    }

    private Table buildSettingsGame() {
        wNewGame = new Window("¡Nueva partida!", StyleConfigurator.getWS_Maiandra80(getSkin()));
        wNewGame.setVisible(false);
        wNewGame.setMovable(false);
        wNewGame.setResizable(false);
        wNewGame.setColor(new Color(50f/255f,75f/255f,250f/255f,1f));
        wNewGame.getTitleLabel().setAlignment(Align.center);

        Label lNumPlayers = new Label("Jugadores ", StyleConfigurator.getLS_Maiandra54());
        final SelectBox sbPlayers = new SelectBox(StyleConfigurator.getSBS_LevelIA(getSkin()));
        Array<Integer> numPlayers = new Array<>();
        numPlayers.add(4); numPlayers.add(5);
        numPlayers.add(6); numPlayers.add(7); numPlayers.add(8);
        sbPlayers.setItems(numPlayers);
        sbPlayers.getList().setAlignment(Align.center);
        sbPlayers.setSelected(5);

        Label lAILevel = new Label("Dificultad ", StyleConfigurator.getLS_Maiandra54());
        final SelectBox sbAILevel = new SelectBox(StyleConfigurator.getSBS_LevelIA(getSkin()));
        sbAILevel.setItems(new String[]{" Fácil "});
        sbAILevel.getList().setAlignment(Align.center);

        Label lTypeGame = new Label("Modo de juego ", StyleConfigurator.getLS_Maiandra54());
        final SelectBox slMode = new SelectBox(StyleConfigurator.getSBS_LevelIA(getSkin()));
        slMode.setItems(new String[]{" Subiendo "});
        slMode.getList().setAlignment(Align.center);

        TextButton tbNewGame = new TextButton(" Empezar partida ", StyleConfigurator.getTBS_RoundButton60(getSkin()));
        tbNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int numPlayers = (int) sbPlayers.getSelected();
                Settings.playSoundClickButton();
                changeScreen(new OfflineGS(getGame(), numPlayers), true);
                wNewGame.setVisible(false);
            }
        });

        TextButton tbCancel = new TextButton(" Cancelar ", StyleConfigurator.getTBS_RoundButton60(getSkin()));
        tbCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wNewGame.setVisible(false);
            }
        });

        Table tNewGame = new Table();
        tNewGame.setFillParent(true);
        wNewGame.add(lNumPlayers);
        wNewGame.add(sbPlayers).padBottom(Constants.VIEWPORT_HEIGHT*0.005f);
        wNewGame.row();
        wNewGame.add(lAILevel);
        wNewGame.add(sbAILevel).padBottom(Constants.VIEWPORT_HEIGHT*0.005f);
        wNewGame.row();
        wNewGame.add(lTypeGame);
        wNewGame.add(slMode).padBottom(Constants.VIEWPORT_HEIGHT*0.008f);
        wNewGame.row();
        wNewGame.add(tbCancel).space(10f);
        wNewGame.add(tbNewGame).space(10f);
        wNewGame.row();
        tNewGame.add(wNewGame).size(Constants.VIEWPORT_WIDTH*0.82f, Constants.VIEWPORT_HEIGHT*0.3f);

        return tNewGame;
    }

    private void setStyleWindow(Window window){
        //window.setColor(new Color(50f/255f,75f/255f,250f/255f,0.95f));
        window.setColor(new Color(50f/255f,75f/255f,250f/255f,1f));
        window.getTitleLabel().setAlignment(Align.center);
    }
}

package com.tfdevelopment.pochaonlinetfd.screen.game.logic;

import static com.tfdevelopment.pochaonlinetfd.utils.conf.Config.TEST;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.screen.menu.MenuScreen;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.GUIBuilder;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.ScreenType;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.GameMode;

import java.util.Random;

public class OfflineGS extends AbstractGameScreen {
    private static final String TAG = OfflineGS.class.getName();

    private GameLogic gameLogic;
    private GUIBuilder guiBuilder;

    public OfflineGS(Game game, int numPlayers){
        super(game, GameMode.OFFLINE, numPlayers, false);

        setConfig(new Random().nextInt(this.getTOTAL_PLAYERS()), 0); // setConfig(2, 0);

        this.gameLogic = new GameLogic(this);
        this.guiBuilder = new GUIBuilder(this, gameLogic);
        this.gameLogic.setBuilder(guiBuilder);
    }

    @Override
    public void show() {
        initScreen(ScreenType.OFFLINE_GAME, null, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT_GAME);
        gameLogic.initHand();

        getStage().clear();
        getStage().addActor(guiBuilder.getMainStack());

        super.show();
        setGameInit(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f); //Pinta la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Limpia la pantalla

        getStage().act(delta);
        getStage().draw();
        getStage().setDebugAll(false);

        if(isGameInit()){ //Los actores ya se han inicializado
            guiBuilder.render(delta);
            gameLogic.render(delta);

            if (isFlagErrorWindow()) {
                setFlagErrorWindow(false);
                guiBuilder.getGuiBuilderWindows().getGuiW_Error().showErrorWindow();
            }
        }

        //////////////////////////////////////////////////////////
            //ENTER o tocar la pantalla con dos dedos
        if(TEST) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                gameLogic.showTest();
                //gameBuilder.getGuiBuilder().getGuiBuilderCards().testingCards();
                guiBuilder.getGuiBuilderPlayersCardsTEST().setVisible();
            }
            if (Gdx.input.isTouched(1) && auxTouched <= 0f) {
                auxTouched = 1f;
                gameLogic.showTest();
                //gameBuilder.getGuiBuilder().getGuiBuilderCards().testingCards();
                guiBuilder.getGuiBuilderPlayersCardsTEST().setVisible();
            } else {
                auxTouched -= delta;
            }
        }
    }
    private float auxTouched = 0f;

    @Override
    public void resize(int width, int height) {
        StaticsMethods.showLogResize(TAG);
        getViewport().update(width,height,true);
    }

    @Override
    public void pause() {
        StaticsMethods.showLogPause(TAG);
    }

    @Override
    public void resume() {
        StaticsMethods.showLogResume(TAG);
    }

    @Override
    public void hide() {
        super.hide();
        StaticsMethods.showLogHide(TAG);
    }

    @Override
    public void controlButtonEvent(ButtonEvent buttonEvent, boolean input) {
        gameLogic.controlButtonEvent(buttonEvent);
    }

    @Override
    public void exitGame(boolean toMenu) {
        changeScreen(new MenuScreen(getGame()), false);
    }
}

package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.model.game.GameData;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.model.player.PochaPlayer;
import com.tfdevelopment.pochaonlinetfd.model.player.ai.AI_Main;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.AbstractGameScreen;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.GameMode;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.PhasePlayer;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class GUIBuilder_Timer {
    private static final String TAG = GUIBuilder_Timer.class.getName();
    public static final float TIMER_TIME = 12f;

    private static final boolean TIMER_ON = true;
    private static final float TIMER_TIME_VISIBLE = TIMER_TIME - 2f;
    private static final float TIMER_TIME_MARGIN = -0.5f;

    private GUIBuilder guiBuilder;

    private TextButton tbTimer;
    private Label lTimer;

    private float timeTimer; //Temporizador de turno
    private int lastPlayerTimer; //Indica cuál ha ido el último jugador para reiniciar el temporizador
    private boolean answerInTimer; //Indica si se está gestionando una respuesta automática al finalizar el tiempo
    private boolean countDownTimer; //Indica si el temporizador está en marcha

    public GUIBuilder_Timer(GUIBuilder guiBuilder){
        this.guiBuilder = guiBuilder;
    }

    //Debido a la gestión de la respuesta automática, únicamente debe ser usado para el usuario, no para la IA
    public void render(float delta){
        AbstractGameScreen gameScreen = guiBuilder.getGameScreen();
        GameLogic gameLogic = guiBuilder.getGameLogic();
        GameData gameData = gameLogic.getGameData();
        int indexPlayer = gameScreen.getIndexPlayer();
        int auxPlayer = gameData.getCurrentPlayer();

        if (!gameScreen.isStartGame() || gameLogic.getPlayers()[auxPlayer].isAIPlayer()) {
            tbTimer.setVisible(false);
            return;
        }

        if(TIMER_ON && guiBuilder.getGameScreen().getGameMode() == GameMode.ONLINE && !gameLogic.isFinishedGame()) {
            //Se gestiona si se ha cambiado de jugador desde la última vez
            if (auxPlayer != lastPlayerTimer) {
                countDownTimer = false;
                resetTime();
                answerInTimer = false;
                lastPlayerTimer = auxPlayer;
            } else {
                countDownTimer = true;
                timeTimer -= delta;
                if(timeTimer<=0f) lTimer.setText("0");
                else lTimer.setText(Math.round(timeTimer));
            }

            tbTimer.setVisible(timeTimer < TIMER_TIME_VISIBLE);

            if (!answerInTimer && timeTimer <= TIMER_TIME_MARGIN) {
                answerInTimer = !sendAutomaticResponse();
            }
        } else tbTimer.setVisible(false);
    }

    //Permite reiniciar el temporizador (todos los valores correctos) al responder, ser mano y volver a hablar
    public void resetTimer(){
        lastPlayerTimer = -1;
    }

    //Permite reiniciar el temporizador (únicamente el tiempo)
    public void resetTime(){
        timeTimer = TIMER_TIME;
    }

    public void setTimeTimer(float _time){
        if(countDownTimer) timeTimer = _time;
    }

    private boolean sendAutomaticResponse(){
        GameLogic gameLogic = guiBuilder.getGameLogic();
        AbstractGameScreen gameScreen = guiBuilder.getGameScreen();
        PochaPlayer player = gameLogic.getPlayers()[gameLogic.getGameData().getCurrentPlayer()];

        if(!gameLogic.isExistAI() && gameLogic.getSemaphoreIA().availablePermits() == 1 && player.getPhasePlayer() != PhasePlayer.WAITING) {
            gameLogic.setExistAI(true);
            ButtonEvent buttonEvent = AI_Main.getAnswer(gameLogic);
            if (buttonEvent!=null) {
                gameScreen.controlButtonEvent(buttonEvent, false);
                return true;
            } else gameLogic.setExistAI(false);
        }
        return false;
    }

    public Table buildTimerLayer(){
        float padTop = 3.5f;
        float padRight = 80f;
        float widthTimer = 8f;
        float heightTimer = 14.5f;

        Drawable dTimer = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getTimer());

        lTimer = new Label("-", StyleConfigurator.getLS_Maiandra50());
        lTimer.setColor(Color.BLACK);
        tbTimer = new TextButton("", StyleConfigurator.getTBS_Maiandra50(dTimer));
        tbTimer.setColor(tbTimer.getColor().r, tbTimer.getColor().g, tbTimer.getColor().a, 0.8f);
        tbTimer.setLabel(lTimer);
        tbTimer.getLabel().setAlignment(Align.center);
        tbTimer.setDisabled(true);

        tbTimer.setVisible(guiBuilder.getGameScreen().getGameMode()==GameMode.ONLINE);

        Table table = new Table();
        table.setDebug(false);
        table.setFillParent(true);
        table.right().top();
        table.padTop(Constants.VIEWPORT_HEIGHT /padTop).padRight(Constants.VIEWPORT_WIDTH /padRight);
        table.add(tbTimer).size(Constants.VIEWPORT_WIDTH /widthTimer, Constants.VIEWPORT_HEIGHT /heightTimer);
        return table;
    }

    public float getTimeTimer(){ return this.timeTimer; }
}

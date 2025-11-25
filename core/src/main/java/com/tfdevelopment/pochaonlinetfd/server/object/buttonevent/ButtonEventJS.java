package com.tfdevelopment.pochaonlinetfd.server.object.buttonevent;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.server.Server;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.Ack;

public class ButtonEventJS {
    private static final String TAG = ButtonEventJS.class.getName();
    private static final String ACK = "ack";

    public static final String EX_BUTTON_EVENT = "buttonEvent";

    private Server nodeJS;

    public ButtonEventJS(Server nodeJS) {
        this.nodeJS = nodeJS;
    }

    public void eiButtonEvent(Object... args){
        Gdx.app.log(TAG, "eiButtonEvent");

        try {
            JSONObject data = (JSONObject)args[0];

            OnlineGS onlineGS = nodeJS.getOnlineGS();
            ButtonEventServer buttonEventServer = ButtonEventJSON.getButtonEventServer(data);

            if(onlineGS!=null){
                while (!onlineGS.isGameInit()) {
                    Gdx.app.log(TAG, "** Initializing game. Waiting... ");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                while (onlineGS.isReloading()) {
                    Gdx.app.log(TAG, "** Reloading game. Waiting...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                while (nodeJS.isPaused()) {
                    Gdx.app.log(TAG, "** Game in paused. Waiting... ");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (onlineGS.getServerGame().getIdLastEvent() != (buttonEventServer.getIdEvent()-1)) {
                    Gdx.app.error(TAG, "** Incorrect received button index.");
                    nodeJS.getServerGameJS().eoReconnectToGame(nodeJS.isPausedAtStarted());
                    return;
                }

                ButtonEventServer.runButtonEventServer(onlineGS, buttonEventServer);
                Gdx.app.log(TAG, "eiButtonEvent - OK");

            } else Gdx.app.error(TAG, "onlineGS is null");
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to eiButtonEvent (JSONException): " + e.getMessage());
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to eiButtonEvent (ClassCastException): " + e.getMessage());
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to eiButtonEvent (Exception): " + e.getMessage());
        }
    }

    public void eoButtonEvent(final int idEvent, ButtonEvent buttonEvent, final GameLogic gameLogic){
        Gdx.app.log(TAG, "eoButtonEvent");

        OnlineGS onlineGS = nodeJS.getOnlineGS();
        if (onlineGS.isFlagReloadGameStarted() || onlineGS.isReloading()) {
            Gdx.app.log(TAG, "eoButtonEvent - FAIL. Reloading.");
            return;
        }

        final String gameId = nodeJS.getServerUser().getGameId();
        final int decisionB = buttonEvent.getDecisionB().ordinal();
        final int player = buttonEvent.getPlayer();
        final int answer = buttonEvent.getAnswer();
        final int numHand = gameLogic.getGameScore().getNumHands();
        final int numChance = gameLogic.getGameScore().getNumChance();
        final int numChancesHand = gameLogic.getGameScore().getNumChancesHand();
        final int firstPlayer = gameLogic.getGameData().getFirstPlayer();
        final int handPlayer = gameLogic.getGameData().getHandPlayer();
        String stringScoreboard = StaticsMethods.getConvertedScore(gameLogic.getGameScore().getScoreboard());

        sendButtonEvent(1, gameId, idEvent, decisionB, player, answer,
                numHand, numChance, numChancesHand, handPlayer,
                firstPlayer, stringScoreboard);

        Gdx.app.log(TAG, "eoButtonEvent - OK");
    }

    private void sendButtonEvent(final int attempt, final String gameId, final int idEvent, final int decisionB, final int playerIndex, final int answer,
                                 final int numHand, final int numChance, final int numChancesHand, final int handPlayer,
                                 final int firstPlayer, final String scoreboard){

        Gdx.app.log(TAG, "sendButtonEvent - Attempt: "+attempt);

        if (attempt > 2) {
            Gdx.app.error(TAG, "Failed to deliver event after " + 2 + " attempts");
            return;
        }

        final Timer timerButtonEvent = new Timer();
        timerButtonEvent.schedule(new TimerTask() {
            @Override
            public void run() {
                Gdx.app.error(TAG,"Timeout reached. No response from server.");
                Gdx.app.error(TAG,"IDEvent: "+idEvent+" :: Table: "+gameId+" :: DecisionB: "+decisionB+ " :: Player: "+playerIndex+" :: Answer: "+answer);
                timerButtonEvent.cancel();
                timerButtonEvent.purge();
                sendButtonEvent(attempt+1, gameId, idEvent, decisionB, playerIndex, answer,
                        numHand, numChance, numChancesHand, handPlayer,
                        firstPlayer, scoreboard);
            }
        }, (long) 7000f);

        nodeJS.getSocket().emit(EX_BUTTON_EVENT, gameId, idEvent, decisionB, playerIndex, answer,
                numHand, numChance, numChancesHand, handPlayer,
                firstPlayer, scoreboard, new Ack() {
                    @Override
                    public void call(Object... args) {
                        if (args.length > 0 && args[0] instanceof String) {
                            String response = (String) args[0];

                            timerButtonEvent.cancel();
                            timerButtonEvent.purge();

                            if (ACK.equals(response)) {
                                Gdx.app.log(TAG,"eoButtonEvent: Event was successfully delivered and acknowledged by the server.");
                            } else {
                                Gdx.app.error(TAG,"eoButtonEvent - KO: Server did not acknowledge the event.");
                            }
                        } else {
                            Gdx.app.error(TAG,"eoButtonEvent - No response from server or invalid response type...");
                        }
                    }
                });
    }
}

package com.tfdevelopment.pochaonlinetfd.server.object.buttonevent;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.player.ButtonEvent;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ButtonEventJSON {
    private static final String TAG = ButtonEventJSON.class.getName();

    private static final String ID_BUTTONEVENT = "idButtonEvent";
    private static final String DECISIONB_BUTTONEVENT = "decisionB";
    private static final String PLAYER_BUTTONEVENT = "player";
    private static final String ANSWER_BUTTONEVENT = "answer";
    private static final String TRUMP = "trump";
    private static final String CARDS = "cards";

    public static ButtonEventServer getButtonEventServer(JSONObject jsoButtonEventServer)  throws Exception {
        try {
            int idEvent = jsoButtonEventServer.getInt(ID_BUTTONEVENT);
            int intDecisionB = jsoButtonEventServer.getInt(DECISIONB_BUTTONEVENT);
            int player = jsoButtonEventServer.getInt(PLAYER_BUTTONEVENT);
            int answer = jsoButtonEventServer.getInt(ANSWER_BUTTONEVENT);

            int trump = -1;
            if (!jsoButtonEventServer.isNull(TRUMP)) {
                trump = jsoButtonEventServer.getInt(TRUMP);
            }

            int[][] allCards = null;
            if (!jsoButtonEventServer.isNull(CARDS)) {
                JSONArray jsonCards = jsoButtonEventServer.getJSONArray(CARDS);
                if(jsonCards.length()>0) {
                    allCards = new int[jsonCards.length()][];

                    for (int j = 0; j < jsonCards.length(); j++) {
                        int numMaxCards = jsonCards.getJSONArray(j).length();
                        allCards[j] = new int[numMaxCards];
                        for (int i = 0; i < numMaxCards; i++) {
                            allCards[j][i] = jsonCards.getJSONArray(j).getInt(i);
                        }
                    }
                    Gdx.app.log(TAG, "Cards: "+allCards.toString());
                }
            }

            PochaEnum.DecisionB decisionB = PochaEnum.DecisionB.values()[intDecisionB];

            ButtonEvent buttonEvent = new ButtonEvent(decisionB, player, answer);
            ButtonEventServer buttonEventServer = new ButtonEventServer(idEvent, buttonEvent, trump, allCards);

            return buttonEventServer;
        } catch (JSONException e) {
            Gdx.app.error(TAG, "Exception to getButtonEventServer (JSONException): " + e.getMessage());
            throw e;
        } catch (ClassCastException e){
            Gdx.app.error(TAG, "Exception to getButtonEventServer (ClassCastException): " + e.getMessage());
            throw e;
        } catch (Exception e){
            Gdx.app.error(TAG, "Exception to getButtonEventServer (Exception): " + e.getMessage());
            throw e;
        }
    }
}

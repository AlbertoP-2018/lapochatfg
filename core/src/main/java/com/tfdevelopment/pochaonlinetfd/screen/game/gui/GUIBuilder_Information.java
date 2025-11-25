package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;

public class GUIBuilder_Information {
    private static final boolean DEBUG_SHOW = false;
    private static final float TIME_NOTIFICATION = 4f;
    private static final float W = Constants.VIEWPORT_WIDTH;
    private static final float H = Constants.VIEWPORT_HEIGHT;
    private static final float SIZE_CLOTH[] = {W*0.85f, H*0.75f};
    private int TOTAL_PLAYERS;

    private GUIBuilder guiBuilder;

    private Table tInformation;
    private Label lInformation;
    private float timeInformation;

    public GUIBuilder_Information(GUIBuilder guiBuilder) {
        this.guiBuilder = guiBuilder;
        this.TOTAL_PLAYERS = guiBuilder.getGameScreen().getTOTAL_PLAYERS();
    }

    public void render(float delta){
        if(timeInformation >0f){
            timeInformation -=delta;
            if (!DEBUG_SHOW) if(timeInformation <=0f) tInformation.setVisible(false);
        }

        if(guiBuilder.getGameLogic().getGameData().getPhase() == PochaEnum.Phase.EXIT){
            setText(" *FIN DE LA PARTIDA *");
            tInformation.setVisible(true);
        }
    }

    /***** Thread Button Event AND Main Thread *****/
    public void showInformationWindow(final String text, final boolean chance){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if(chance){
                    lInformation.setColor(Color.GOLD);
                    setText(text + " ha ganado la baza");
                } else {
                    lInformation.setColor(Color.RED);
                    setText(text);
                }
                timeInformation = TIME_NOTIFICATION;
                tInformation.setVisible(true);
            }
        });
    }

    public Table createInformation() {
        lInformation = new Label("", StyleConfigurator.getLS_Information());
        setText("????");

        lInformation.setAlignment(Align.center);
        lInformation.setColor(Color.GOLD);

        tInformation = new Table();
        tInformation.setDebug(DEBUG_SHOW);
        tInformation.setVisible(DEBUG_SHOW);
        if (TOTAL_PLAYERS == 8) {
            tInformation.bottom();
            tInformation.add(lInformation).padBottom(GUIBuilder_Cards.SIZE_CARDS[1]/1.13f);
        } else {
            tInformation.top();
            tInformation.add(lInformation).padTop(H*0.12f);
        }
        return tInformation;
    }

    private void setText(String text) {
        lInformation.setText("  " + text + "  ");
    }
}

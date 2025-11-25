package com.tfdevelopment.pochaonlinetfd.screen.server.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.server.object.game.NewGameDataServer;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class GameBox extends Table {

    private ServerMainScreen serverMainScreen;
    private NewGameDataServer newGameDataServer;
    private Skin skin;

    public GameBox(ServerMainScreen serverMainScreen, NewGameDataServer newGameDataServer, Skin skin) {
        super();
        this.serverMainScreen = serverMainScreen;
        this.newGameDataServer = newGameDataServer;
        this.skin = skin;

        this.setDebug(false);
        this.add(getHost()).width(GameContainer.BOX_WIDTH_CONTAINER /2f).colspan(1);
        this.add(getGameId()).width(GameContainer.BOX_WIDTH_CONTAINER /2f).colspan(1);
        this.row();
        this.add(getTypeGame()).width(GameContainer.BOX_WIDTH_CONTAINER /2f);
        this.add(getNumPlayers()).width(GameContainer.BOX_WIDTH_CONTAINER /2f);
        this.row();
        this.add(getJoin()).width(GameContainer.BOX_WIDTH_CONTAINER /2f).padTop(GameContainer.BOX_HEIGHT_CONTAINER *0.1f).colspan(2);
    }

    private Label getHost() {
        Label label = new Label(newGameDataServer.getHostName(), StyleConfigurator.getLS_Maiandra70());
        label.setAlignment(Align.center);
        label.setColor(Color.GREEN);
        return label;
    }

    private Label getGameId() {
        Label label = new Label(newGameDataServer.getId(), StyleConfigurator.getLS_Maiandra60());
        label.setAlignment(Align.center);
        label.setColor(Color.BLUE);
        return label;
    }

    private Label getTypeGame() {
        String text = "Modo de juego: ¡Subiendo!";
        Label label = new Label(text, StyleConfigurator.getLS_Maiandra54());
        label.setAlignment(Align.left);
        return label;
    }

    private Label getNumPlayers() {
        String text = "Jugadores: "+ newGameDataServer.getNumPlayers()+"/"+ newGameDataServer.getTotalPlayers();
        Label label = new Label(text, StyleConfigurator.getLS_Maiandra54());
        label.setAlignment(Align.right);
        return label;
    }

    private TextButton getJoin() {
        TextButton textButton = new TextButton(" UNIRSE ",StyleConfigurator.getTBS_RoundButton60(skin));
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                serverMainScreen.getNodeJS().getNewGameJS().eoJoinGame(newGameDataServer.getId());
            }
        });
        return textButton;
    }

    public NewGameDataServer getNewGameDataServer(){ return this.newGameDataServer; }
}

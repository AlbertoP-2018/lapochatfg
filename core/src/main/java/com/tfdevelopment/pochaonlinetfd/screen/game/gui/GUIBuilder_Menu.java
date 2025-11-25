package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.GameMode;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

public class GUIBuilder_Menu {
    private static final String TAG = GUIBuilder_Menu.class.getName();

    private GUIBuilder guiBuilder;

    private Stack stack;
    private Button bConfiguration;
    private Window wConfiguration;


    public GUIBuilder_Menu(GUIBuilder guiBuilder) {
        this.guiBuilder = guiBuilder;
    }

    public Stack createMenuLayer(){
        stack = new Stack();

        createWindowsConfig();
        createButtonConfig();

        return stack;
    }

    private void updateIconMenuConfig(){
        if(wConfiguration.isVisible()) bConfiguration.getStyle().up = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getbConfDown());
        if(!wConfiguration.isVisible()) bConfiguration.getStyle().up = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getbConfUp());
    }

    private void createWindowsConfig(){
        float padLine = Constants.VIEWPORT_HEIGHT*0.015f;

        String howToPlay_Title = "¿CÓMO JUGAR?";
        String howToPlay_Text =
                "Jugar una carta: Toca la carta que quieres echar.\n\n"+
                "Triunfo: Podrá verse en la esquina superior\n" +
                "izquierda. Si dicha carta aparece difuminada,\nserá propiedad del jugador que reparte.\n\n"+
                "Jugador mano: Dicho jugador tendrá el nombre\nsubrayado en la placa.\n\n" +
                "Jugador actual: Aparecerá coloreado en el\nmarcador.";

        String rules_Title = "REGLAS";
        String rules_Text =
                "Si desconoces las reglas, puedes aprender\na jugar en el siguiente enlace: [BLACK]Pulse aquí.";

        Label lHowToPlay_Title = new Label(howToPlay_Title, StyleConfigurator.getLS_Maiandra60());
        Label lHowToPlay_Text = new Label(howToPlay_Text, StyleConfigurator.getLS_Maiandra50());
        Label lRules_Title = new Label(rules_Title, StyleConfigurator.getLS_Maiandra60());
        Label lRules_Text = new Label(rules_Text, StyleConfigurator.getLS_Maiandra50());
        lRules_Text.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://www.ludoteka.com/clasika/pocha.html");
            }
        });

        TextButton tbExit = new TextButton(" Abandonar partida ", StyleConfigurator.getTBS_RoundButton60(guiBuilder.getSkin()));
        tbExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean toMenu = true;
                if (guiBuilder.getGameLogic().getGameScreen().getGameMode() == GameMode.ONLINE) {
                    toMenu = !guiBuilder.getGameLogic().isFinishedGame();
                }

                guiBuilder.getGameScreen().exitGame(toMenu);
            }
        });
        tbExit.setColor(Color.RED);
        TextButton tbOk = new TextButton(" Continuar ", StyleConfigurator.getTBS_RoundButton60(guiBuilder.getSkin()));
        tbOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                wConfiguration.setVisible(false);
                updateIconMenuConfig();
            }
        });

        wConfiguration = new Window("", StyleConfigurator.getWS_WSettings());
        wConfiguration.getTitleLabel().setVisible(false);
        wConfiguration.setFillParent(false);
        wConfiguration.setVisible(false);
        wConfiguration.setMovable(false);
        wConfiguration.setResizable(false);
        wConfiguration.add(lHowToPlay_Title).center().padBottom(Constants.VIEWPORT_HEIGHT*0.015f); //.padBottom(10f);
        wConfiguration.row();
        wConfiguration.add(lHowToPlay_Text).left().padLeft(Constants.VIEWPORT_WIDTH*0.03f); //.padLeft(10f);
        wConfiguration.row();
        wConfiguration.add(new Label("------------------------------------------------------------", guiBuilder.getSkin())).
                height(Constants.VIEWPORT_HEIGHT*0.01f).padTop(padLine).padBottom(padLine);
        wConfiguration.row();
        wConfiguration.add(lRules_Title).center().padBottom(Constants.VIEWPORT_HEIGHT*0.015f); //.padBottom(10f);
        wConfiguration.row();
        wConfiguration.add(lRules_Text).left().padLeft(Constants.VIEWPORT_WIDTH*0.03f); //.padLeft(10f);
        wConfiguration.row();
        wConfiguration.add(new Label("------------------------------------------------------------", guiBuilder.getSkin())).
                height(Constants.VIEWPORT_HEIGHT*0.01f).padTop(padLine).padBottom(padLine);
        wConfiguration.row();
        wConfiguration.add(tbExit);
        wConfiguration.row();
        wConfiguration.add(tbOk).padTop(Constants.VIEWPORT_HEIGHT*0.05f); //.padTop(35f);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(wConfiguration).size(Constants.VIEWPORT_WIDTH*0.85f, Constants.VIEWPORT_HEIGHT*0.71f);

        stack.add(table);
    }

    private void createButtonConfig(){
        float buttonWidth = Constants.VIEWPORT_WIDTH /10f;
        float buttonHeight = Constants.VIEWPORT_HEIGHT /20f;

        bConfiguration = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getbConfUp()));
        bConfiguration.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.playSoundClickButton();
                wConfiguration.setVisible(!wConfiguration.isVisible());
                updateIconMenuConfig();
            }
        });

        Table table = new Table();
        table.setDebug(false);
        table.top().right();
        table.padTop(Constants.VIEWPORT_HEIGHT*0.115f);
        table.row();
        table.add(bConfiguration).size(buttonWidth,buttonHeight).center().padRight(Constants.VIEWPORT_WIDTH*0.02f);

        stack.add(table);
    }
}

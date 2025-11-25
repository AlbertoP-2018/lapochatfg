package com.tfdevelopment.pochaonlinetfd.screen.game.gui.layout;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Phase;
import com.tfdevelopment.pochaonlinetfd.utils.StaticsMethods;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

/**
 * Representa el icono de jugador junto a su nombre (Para TableBuilder y HallScreen)
 */
public class PlayerIcon {
    private static final String TAG = PlayerIcon.class.getName();

    private GameLogic gameLogic;
    private String nameUser;

    private Label lName, lBet;
    private Image underLine;
    private Cell cellUnderLine;

    private Stack sIcon, sPlaque;
    private Table tPlayerGame;
    private PlayerAlignment alignment;

    public enum PlayerAlignment { // Modify GUIBuilder_Table if any more are added
        Bottom,
        Right,
        TopRight,
        TopCenter,
        TopLeft,
        Left
    }

    public PlayerIcon(GameLogic gameLogic, String nameUser, Skin skin, PlayerAlignment playerAlignment){
        super();
        this.gameLogic = gameLogic;
        this.nameUser = nameUser;
        this.alignment = playerAlignment;

        sIcon = new Stack();
        sPlaque = new Stack();

        createStackIcon();
        createStackPlaque();
    }

    public void updatePlaque(int win, int bet, boolean disconnected, int indexPlayer){
        Phase phase = gameLogic.getGameData().getPhase();
        String red = "[#AD1212]";
        String green = "[#125A12]";
        String color = "";

        if (phase != Phase.BET) {
            if (win > bet) color=red;
            if (win == bet) color=green;
            if (win < bet) color=red;
        } else color = "[BLACK]";
        if (win == -1 || bet == -1) color = "[BLACK]";

        String sWin = win == -1 ? "-" : "" + win;
        String sBet = bet == -1 ? "-" : "" + bet;

        lName.setText(nameUser);
        if (disconnected) lName.setColor(Color.RED);
        else lName.setColor(Color.BLACK);

        lBet.setText(color + sWin + "[BLACK] / " + sBet + "[BLACK]");
        if (cellUnderLine != null) {
            cellUnderLine.width(lName.getPrefWidth());
            // underLine.setVisible(gameLogic.getGameData().getFirstPlayer() == indexPlayer);
            underLine.setVisible(gameLogic.getGameData().getHandPlayer() == indexPlayer);
        }
    }

    private void createStackIcon(){
        TextureRegionDrawable trd = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getPlayer());
        Image image = new Image(trd);

        Table tIcon = new Table();
        tIcon.setDebug(false);
        tIcon.setFillParent(false);
        tIcon.top();
        tIcon.add(image).size(Constants.VIEWPORT_WIDTH*0.13f,Constants.VIEWPORT_HEIGHT*0.1f);

        sIcon.add(tIcon);
    }

    private void createStackPlaque(){
        lName = new Label(nameUser, StyleConfigurator.getLS_Maiandra50());
        lName.setColor(Color.BLACK);
        lName.setAlignment(Align.center);
        lBet = new Label("- / -", StyleConfigurator.getLS_Maiandra50());
        lBet.setAlignment(Align.center);

        NinePatchDrawable npd = StaticsMethods.getNinePatchDrawable(AssetLoader.aLoader.aImage.getSmallBlackSquare(),1);
        underLine = new Image(npd);

        tPlayerGame = new Table();
        tPlayerGame.setDebug(false);
        tPlayerGame.setFillParent(true);
        tPlayerGame.top();

        Drawable drawable = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getNameplate());
        Button ibBackground = new Button(drawable);
        Table tableBackground = new Table();
        tableBackground.setDebug(false);
        tableBackground.add(ibBackground).size(Constants.VIEWPORT_WIDTH /4,Constants.VIEWPORT_HEIGHT /15f);

        Table tableContent = new Table();
        tableContent.setDebug(false);
        tableContent.add(lName).width(Constants.VIEWPORT_WIDTH /4);
        tableContent.row();
        cellUnderLine = tableContent.add(underLine).height(Constants.VIEWPORT_HEIGHT*0.0005f).top();
        tableContent.row();
        tableContent.add(lBet).width(Constants.VIEWPORT_WIDTH /4);

        Stack stackPlate = new Stack();
        stackPlate.add(tableBackground);
        stackPlate.add(tableContent);
        tPlayerGame.add(stackPlate);

        sPlaque.add(tPlayerGame);
    }

    public Stack getsIcon(){ return sIcon; }
    public Stack getsPlaque(){ return sPlaque; }
    public PlayerAlignment getAlignment(){ return this.alignment; }
}

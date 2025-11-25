package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tfdevelopment.pochaonlinetfd.model.player.PochaPlayer;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.layout.PlayerIcon;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.layout.PlayerIcon.PlayerAlignment;
import com.tfdevelopment.pochaonlinetfd.server.object.playerdata.ServerPlayer;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.GameMode;

import java.util.Arrays;

public class GUIBuilder_Players {
    private static final String TAG = GUIBuilder_Table.class.getName();
    private final int TOTAL_PLAYERS;
    private static final float W = Constants.VIEWPORT_WIDTH;
    private static final float H = Constants.VIEWPORT_HEIGHT;
    private static final float PADDING_SIDES = W*0.01f;
    private static final float PADDING_TOP_TOP = GUIBuilder_Score.SIZE_SCORE[1]*1.02f;

    private final GUIBuilder guiBuilder;
    private final PlayerIcon[] playerIcons;
    private final Table[] playerTables;
    private final float[][] padding; //Bottom, Right, Top, Left

    protected GUIBuilder_Players(GUIBuilder guiBuilder){
        this.guiBuilder = guiBuilder;
        this.TOTAL_PLAYERS = guiBuilder.getGameScreen().getTOTAL_PLAYERS();
        this.playerIcons = new PlayerIcon[TOTAL_PLAYERS];
        this.playerTables = new Table[TOTAL_PLAYERS];
        this.padding = new float[TOTAL_PLAYERS][4];
        for (int i=0; i<TOTAL_PLAYERS; i++)
            Arrays.fill(padding[i], 0f);
    }

    public void render(float delta){
        if (guiBuilder.getGameScreen().getGameMode()==GameMode.OFFLINE) {
            PochaPlayer[] pochaPlayers = guiBuilder.getGameLogic().getPlayers();
            for(int indexPlayer = 0; indexPlayer<TOTAL_PLAYERS; indexPlayer++){
                int win = pochaPlayers[indexPlayer].getWin();
                int bet = pochaPlayers[indexPlayer].getBet();

                playerIcons[indexPlayer].updatePlaque(win, bet, false, indexPlayer);
            }
        }

        if (guiBuilder.getGameScreen().getGameMode()==GameMode.ONLINE) {
            PochaPlayer[] pochaPlayers = guiBuilder.getGameLogic().getPlayers();
            for (int indexPlayer = 0; indexPlayer<TOTAL_PLAYERS; indexPlayer++) {
                int win = pochaPlayers[indexPlayer].getWin();
                int bet = pochaPlayers[indexPlayer].getBet();
                boolean disconnected = ((OnlineGS)guiBuilder.getGameScreen()).getServerGame().getGamePlayers().get(indexPlayer).isDisconnected();

                int index = guiBuilder.getGameLogic().getGameData().getPlayerPositionAboutGUI(indexPlayer);
                playerIcons[index].updatePlaque(win, bet, disconnected, indexPlayer);
            }
        }
    }

    protected Stack createPlayersLayer(){
        Stack stack = new Stack();
        stack.add(createStackPlayer());
        return stack;
    }

    private Stack createStackPlayer() {
        PochaPlayer[] players = guiBuilder.getGameLogic().getPlayers();
        String[] names = new String[TOTAL_PLAYERS];
        Arrays.fill(names, "?");

        if (guiBuilder.getGameScreen().getGameMode() == GameMode.OFFLINE) {
            for (int i = 0; i < players.length; i++) {
                names[i] = players[i].getName();
            }
        }

        //Empieza colocando el jugador 'index' en la posición de abajo
        if (guiBuilder.getGameScreen().getGameMode() == GameMode.ONLINE) {
            int index = guiBuilder.getGameScreen().getIndexPlayer();
            for (int i = 0; i < players.length; i++) {
                ServerPlayer gamePlayer = ((OnlineGS) guiBuilder.getGameScreen()).getServerGame().getGamePlayers().get(index);
                names[i] = gamePlayer.getName();

                index += 1;
                if (index == players.length) index = 0;
            }
        }

        if (TOTAL_PLAYERS == 3) getStack3Players(names);
        else if (TOTAL_PLAYERS == 4) getStack4Players(names);
        else if (TOTAL_PLAYERS == 5) getStack5Players(names);
        else if (TOTAL_PLAYERS == 6) getStack6Players(names);
        else if (TOTAL_PLAYERS == 7) getStack7Players(names);
        else if (TOTAL_PLAYERS == 8) getStack8Players(names);
        else Gdx.app.error(TAG, "---> TODO: Make stackPlayer for "+TOTAL_PLAYERS);

        Stack stack = new Stack();
        for (int i=0; i<TOTAL_PLAYERS; i++)
            stack.add(playerTables[i]);
        return stack;
    }

    private void getStack3Players(String[] names) {
        float PADDING_BOTTOM_SIDES = H*0.15f;//H*0.25f;
        int index = -1;

        index = 0;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Bottom);
        playerTables[index] = new Table();
        playerTables[index].bottom();
        playerTables[index].add(getStackPlayer(null));

        index = 1;
        padding[index][0] = PADDING_BOTTOM_SIDES;
        padding[index][1] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Right);
        playerTables[index] = new Table();
        playerTables[index].right().padBottom(padding[index][0]).padRight(padding[index][1]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 2;
        padding[index][0] = PADDING_BOTTOM_SIDES;
        padding[index][3] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Left);
        playerTables[index] = new Table();
        playerTables[index].left().padBottom(padding[index][0]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));
    }

    private void getStack4Players(String[] names) {
        float PADDING_BOTTOM_SIDES = H*0.1f;
        int index = -1;

        index = 0;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Bottom);
        playerTables[index] = new Table();
        playerTables[index].bottom();
        playerTables[index].add(getStackPlayer(null));

        index = 1;
        padding[index][0] = PADDING_BOTTOM_SIDES;
        padding[index][1] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Right);
        playerTables[index] = new Table();
        playerTables[index].right().padBottom(padding[index][0]).padRight(padding[index][1]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 2;
        padding[index][2] = PADDING_TOP_TOP;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.TopCenter);
        playerTables[index] = new Table();
        playerTables[index].top().padTop(padding[index][2]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 3;
        padding[index][0] = PADDING_BOTTOM_SIDES;
        padding[index][3] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Left);
        playerTables[index] = new Table();
        playerTables[index].left().padBottom(padding[index][0]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));
    }

    private void getStack5Players(String[] names) {
        float PADDING_BOTTOM_SIDES = H*0.1f;
        float PADDING_SIDES_TOP = W*0.35f;
        int index = -1;

        index = 0;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Bottom);
        playerTables[index] = new Table();
        playerTables[index].bottom();
        playerTables[index].add(getStackPlayer(null));

        index = 1;
        padding[index][0] = PADDING_BOTTOM_SIDES;
        padding[index][1] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Right);
        playerTables[index] = new Table();
        playerTables[index].right().padBottom(padding[index][0]).padRight(padding[index][1]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 2;
        padding[index][2] = PADDING_TOP_TOP;
        padding[index][3] = PADDING_SIDES_TOP;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.TopRight);
        playerTables[index] = new Table();
        playerTables[index].top().padRight(padding[index][1]).padTop(padding[index][2]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 3;
        padding[index][1] = PADDING_SIDES_TOP;
        padding[index][2] = PADDING_TOP_TOP;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.TopLeft);
        playerTables[index] = new Table();
        playerTables[index].top().padRight(padding[index][1]).padTop(padding[index][2]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 4;
        padding[index][0] = PADDING_BOTTOM_SIDES;
        padding[index][3] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Left);
        playerTables[index] = new Table();
        playerTables[index].left().padBottom(padding[index][0]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));
    }

    private void getStack6Players(String[] names) {
        float PADDING_BOTTOM_SIDES_DOWN = -H*0.15f;
        float PADDING_BOTTOM_SIDES_UP = H*0.3f;
        int index = -1;

        index = 0;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Bottom);
        playerTables[index] = new Table();
        playerTables[index].bottom();
        playerTables[index].add(getStackPlayer(null));

        index = 1;
        padding[index][0] = PADDING_BOTTOM_SIDES_DOWN;
        padding[index][1] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Right);
        playerTables[index] = new Table();
        playerTables[index].right().padBottom(padding[index][0]).padRight(padding[index][1]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 2;
        padding[index][0] = PADDING_BOTTOM_SIDES_UP;
        padding[index][1] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Right);
        playerTables[index] = new Table();
        playerTables[index].right().padBottom(padding[index][0]).padRight(padding[index][1]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 3;
        padding[index][2] = PADDING_TOP_TOP;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.TopCenter);
        playerTables[index] = new Table();
        playerTables[index].top().padTop(padding[index][2]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 4;
        padding[index][0] = PADDING_BOTTOM_SIDES_UP;
        padding[index][3] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Left);
        playerTables[index] = new Table();
        playerTables[index].left().padBottom(padding[index][0]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 5;
        padding[index][0] = PADDING_BOTTOM_SIDES_DOWN;
        padding[index][3] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Left);
        playerTables[index] = new Table();
        playerTables[index].left().padBottom(padding[index][0]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));
    }

    private void getStack7Players(String[] names) {
//        float PADDING_BOTTOM_SIDES_DOWN = -H*0.15f;
//        float PADDING_BOTTOM_SIDES_UP = H*0.3f;
        float PADDING_BOTTOM_SIDES_DOWN = -H*0.22f;
        float PADDING_BOTTOM_SIDES_UP = H*0.21f;
        float PADDING_SIDES_TOP = W*0.35f;
        int index = -1;

        index = 0;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Bottom);
        playerTables[index] = new Table();
        playerTables[index].bottom();
        playerTables[index].add(getStackPlayer(null));

        index = 1;
        padding[index][0] = PADDING_BOTTOM_SIDES_DOWN;
        padding[index][1] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Right);
        playerTables[index] = new Table();
        playerTables[index].right().padBottom(padding[index][0]).padRight(padding[index][1]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 2;
        padding[index][0] = PADDING_BOTTOM_SIDES_UP;
        padding[index][1] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Right);
        playerTables[index] = new Table();
        playerTables[index].right().padBottom(padding[index][0]).padRight(padding[index][1]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 3;
        padding[index][2] = PADDING_TOP_TOP;
        padding[index][3] = PADDING_SIDES_TOP;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.TopRight);
        playerTables[index] = new Table();
        playerTables[index].top().padRight(padding[index][1]).padTop(padding[index][2]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 4;
        padding[index][1] = PADDING_SIDES_TOP;
        padding[index][2] = PADDING_TOP_TOP;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.TopLeft);
        playerTables[index] = new Table();
        playerTables[index].top().padRight(padding[index][1]).padTop(padding[index][2]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 5;
        padding[index][0] = PADDING_BOTTOM_SIDES_UP;
        padding[index][3] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Left);
        playerTables[index] = new Table();
        playerTables[index].left().padBottom(padding[index][0]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 6;
        padding[index][0] = PADDING_BOTTOM_SIDES_DOWN;
        padding[index][3] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Left);
        playerTables[index] = new Table();
        playerTables[index].left().padBottom(padding[index][0]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));
    }

    private void getStack8Players(String[] names) {
        float PADDING_BOTTOM_SIDES_DOWN = -H*0.22f;
        float PADDING_BOTTOM_SIDES_UP = H*0.21f;
        float PADDING_SIDES_TOP = W*0.55f;
        int index = -1;

        index = 0;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Bottom);
        playerTables[index] = new Table();
        playerTables[index].bottom();
        playerTables[index].add(getStackPlayer(null));

        index = 1;
        padding[index][0] = PADDING_BOTTOM_SIDES_DOWN;
        padding[index][1] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Right);
        playerTables[index] = new Table();
        playerTables[index].right().padBottom(padding[index][0]).padRight(padding[index][1]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 2;
        padding[index][0] = PADDING_BOTTOM_SIDES_UP;
        padding[index][1] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Right);
        playerTables[index] = new Table();
        playerTables[index].right().padBottom(padding[index][0]).padRight(padding[index][1]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 3;
        padding[index][2] = PADDING_TOP_TOP;
        padding[index][3] = PADDING_SIDES_TOP;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.TopRight);
        playerTables[index] = new Table();
        playerTables[index].top().padRight(padding[index][1]).padTop(padding[index][2]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 4;
        padding[index][2] = PADDING_TOP_TOP;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.TopCenter);
        playerTables[index] = new Table();
        playerTables[index].top().padTop(padding[index][2]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 5;
        padding[index][1] = PADDING_SIDES_TOP;
        padding[index][2] = PADDING_TOP_TOP;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.TopLeft);
        playerTables[index] = new Table();
        playerTables[index].top().padRight(padding[index][1]).padTop(padding[index][2]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 6;
        padding[index][0] = PADDING_BOTTOM_SIDES_UP;
        padding[index][3] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Left);
        playerTables[index] = new Table();
        playerTables[index].left().padBottom(padding[index][0]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));

        index = 7;
        padding[index][0] = PADDING_BOTTOM_SIDES_DOWN;
        padding[index][3] = PADDING_SIDES;
        playerIcons[index] = new PlayerIcon(guiBuilder.getGameLogic(), names[index], guiBuilder.getSkin(), PlayerAlignment.Left);
        playerTables[index] = new Table();
        playerTables[index].left().padBottom(padding[index][0]).padLeft(padding[index][3]);
        playerTables[index].add(getStackPlayer(playerIcons[index]));
    }

    private Stack getStackPlayer(PlayerIcon pi){
        Table tPlaque = new Table();
        Stack stackPlayer = new Stack();

        if (pi != null) {
            Table tIcon = new Table();
            tIcon.top();
            tIcon.add(pi.getsIcon());

            tPlaque.top();
            tPlaque.add(pi.getsPlaque()).padTop(tIcon.getMinHeight()*0.85f);

            stackPlayer.add(tIcon);
        } else { // Only for bottom player (Me)
            tPlaque.bottom();
            tPlaque.add(playerIcons[0].getsPlaque()).padBottom(GUIBuilder_Cards.SIZE_CARDS[1]*1.018f);
        }

        stackPlayer.add(tPlaque);
        return stackPlayer;

//        Table tIcon = new Table();
//        tIcon.top();
//        tIcon.add(pi.getsIcon());
//
//        Table tPlaque = new Table();
//        tPlaque.top();
//        tPlaque.add(pi.getsPlaque()).padTop(tIcon.getMinHeight()*0.85f);
//
//        Stack stackPlayer = new Stack();
//        stackPlayer.add(tIcon);
//        stackPlayer.add(tPlaque);
//        return stackPlayer;
    }

    /*** Getters And Setters ***/
    public PlayerIcon[] getPlayerIcons() { return this.playerIcons; }
    public PlayerIcon getPlayerIcon(int index) { return this.playerIcons[index]; }
    public float[][] getPlayerPadding() { return this.padding; }
    public float[] getPlayerPadding(int index) { return this.padding[index]; }
}

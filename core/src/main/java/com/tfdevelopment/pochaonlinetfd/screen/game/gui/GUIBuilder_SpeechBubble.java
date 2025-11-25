package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.layout.PlayerIcon.PlayerAlignment;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.GameMode;
import com.tfdevelopment.pochaonlinetfd.screen.StyleConfigurator;

import java.util.Arrays;

public class GUIBuilder_SpeechBubble {
    private static final String TAG = GUIBuilder_SpeechBubble.class.getName();
    private static final float SPEECH_BUBBLE_TIME = 1.35f;
    private static final boolean DEBUG_SHOW = false;
    private static final float W = Constants.VIEWPORT_WIDTH;
    private static final float H = Constants.VIEWPORT_HEIGHT;
    private static final float SIZE_SB[] = {W*0.24f, H*0.06f};
    private static final float PADDING_BOTTOM_ME = H*0.25f;
    private static final float PADDING_LEFT_ME = H*0.28f;
    private static final float PADDING_SIDES = W*0.07f; //Pad lateral de los dialogos laterales
    private static final float FIX_PADDING_SIDES = H*0.22f;
    private static final float FIX_PADDING_TOP = H*0.14f;
    private static final float FIX_PADDING_LEFT_TOPCENTER = W*0.25f;
    private static final float FIX_PADDING_TOP_SIDES = W*0.08f;
    private int TOTAL_PLAYERS;

    private GUIBuilder guiBuilder;
    //private GUIBuilder_Players guiPlayers;

    private Table[] tButtons;
    private Table[] tableSpeechBubbles;
    private Label[] labelSpeechBubbles;
    private float[] timeSpeechBubbles;

    //Variables for show speech bubble from main thread
    private int iPlayer;
    private String text;

    public GUIBuilder_SpeechBubble(GUIBuilder guiBuilder) {
        this.guiBuilder = guiBuilder;
        //this.guiPlayers = guiBuilder.getGuiBuilderPlayers();

        this.iPlayer = -1;
        this.text = "?";

        TOTAL_PLAYERS = guiBuilder.getGameScreen().getTOTAL_PLAYERS();
        this.tButtons = new Table[TOTAL_PLAYERS];
        this.tableSpeechBubbles = new Table[TOTAL_PLAYERS];
        this.labelSpeechBubbles = new Label[TOTAL_PLAYERS];
        this.timeSpeechBubbles = new float[TOTAL_PLAYERS];

        Arrays.fill(timeSpeechBubbles, 0);
    }

    public void render(float delta){
        if (iPlayer != -1) {
            showSpeechBubble();
            iPlayer = -1;
            text = "?";
        }

        for (int i=0; i<TOTAL_PLAYERS; i++) {
            if (timeSpeechBubbles[i] > 0f) {
                timeSpeechBubbles[i] -= delta;
                //if(timeSpeechBubbles[i] <= 0f) tableSpeechBubbles[i].setVisible(false);
                if(timeSpeechBubbles[i] <= 0f) tableSpeechBubbles[i].setVisible(DEBUG_SHOW);
            }
        }
    }

    /***** Thread Button Event *****/
    public void setShowSpeechBubble(int iPlayer, String text) {
        this.text = text;
        this.iPlayer = iPlayer;
    }

    public void showSpeechBubble(){
        if (guiBuilder.getGameScreen().getGameMode() == GameMode.ONLINE) {
            iPlayer = guiBuilder.getGameLogic().getGameData().getPlayerPositionAboutGUI(iPlayer);
        }

        boolean[] config = getIsLeftAndRotation();
        addResizeButton(tButtons[iPlayer], config[0], config[1]);
        labelSpeechBubbles[iPlayer].setText(text);
        tableSpeechBubbles[iPlayer].setVisible(true);
        timeSpeechBubbles[iPlayer] = SPEECH_BUBBLE_TIME;
    }

    public Stack createSpeechBubbleLayer() {
        if (TOTAL_PLAYERS == 3) return getSpeechBubble3Players();
        if (TOTAL_PLAYERS == 4) return getSpeechBubble4Players();
        if (TOTAL_PLAYERS == 5) return getSpeechBubble5Players();
        if (TOTAL_PLAYERS == 6) return getSpeechBubble6Players();
        if (TOTAL_PLAYERS == 7) return getSpeechBubble7Players();
        if (TOTAL_PLAYERS == 8) return getSpeechBubble8Players();
        Gdx.app.error(TAG, "---> TODO: Make stackPlayer for "+TOTAL_PLAYERS);
        return null;
    }

    private Stack getSpeechBubble3Players() {
        float[][] padding = guiBuilder.getGuiBuilderPlayers().getPlayerPadding();

        Stack stack = new Stack();
        stack.add(createSpeechBubble(0, 0, 0,0, PADDING_BOTTOM_ME, 0f, 0f, PADDING_LEFT_ME));
        stack.add(createSpeechBubble(1, 1, -1,1, padding[1][0]-FIX_PADDING_SIDES, PADDING_SIDES, 0f,0f));
        stack.add(createSpeechBubble(2, 3, -1, 3, padding[2][0]-FIX_PADDING_SIDES, 0f, 0f, PADDING_SIDES));
        return stack;
    }

    private Stack getSpeechBubble4Players() {
        float[][] padding = guiBuilder.getGuiBuilderPlayers().getPlayerPadding();

        Stack stack = new Stack();
        stack.add(createSpeechBubble(0, 0, 0,0, PADDING_BOTTOM_ME, 0f, 0f,PADDING_LEFT_ME));
        stack.add(createSpeechBubble(1, 1, -1,1, padding[1][0]-FIX_PADDING_SIDES, PADDING_SIDES, 0f,0f));
        stack.add(createSpeechBubble(2, -1, -1, 2, 0f, 0f,
                padding[2][2]+FIX_PADDING_TOP, padding[2][3]+ FIX_PADDING_LEFT_TOPCENTER));
        stack.add(createSpeechBubble(3, 3, -1, 3, padding[3][0]-FIX_PADDING_SIDES, 0f, 0f, PADDING_SIDES));
        return stack;
    }

    private Stack getSpeechBubble5Players() {
        float[][] padding = guiBuilder.getGuiBuilderPlayers().getPlayerPadding();

        Stack stack = new Stack();
        stack.add(createSpeechBubble(0, 0, 0,0, PADDING_BOTTOM_ME, 0f, 0f,PADDING_LEFT_ME));
        stack.add(createSpeechBubble(1, 1, -1,1, padding[1][0]-FIX_PADDING_SIDES, PADDING_SIDES, 0f,0f));
        stack.add(createSpeechBubble(2, -1, -1, 2, 0f, 0f,
                padding[2][2]+FIX_PADDING_TOP, padding[2][3]+FIX_PADDING_TOP_SIDES));
        stack.add(createSpeechBubble(3, -1, -1, 2, 0f, padding[3][1]+FIX_PADDING_TOP_SIDES,
                padding[3][2]+FIX_PADDING_TOP, 0f));
        stack.add(createSpeechBubble(4, 3, -1, 3, padding[4][0]-FIX_PADDING_SIDES, 0f, 0f, PADDING_SIDES));
        return stack;
    }

    private Stack getSpeechBubble6Players() {
        float[][] padding = guiBuilder.getGuiBuilderPlayers().getPlayerPadding();

        Stack stack = new Stack();
        stack.add(createSpeechBubble(0, 0, 0,0, PADDING_BOTTOM_ME, 0f, 0f,PADDING_LEFT_ME));
        stack.add(createSpeechBubble(1, 1, -1,1, padding[1][0]-FIX_PADDING_SIDES, PADDING_SIDES, 0f,0f));
        stack.add(createSpeechBubble(2, 1, -1,1, padding[2][0]-FIX_PADDING_SIDES, PADDING_SIDES, 0f,0f));
        stack.add(createSpeechBubble(3, -1, -1, 2, 0f, 0f,
                padding[3][2]+FIX_PADDING_TOP, padding[3][3]+ FIX_PADDING_LEFT_TOPCENTER));
        stack.add(createSpeechBubble(4, 3, -1, 3, padding[4][0]-FIX_PADDING_SIDES, 0f, 0f, PADDING_SIDES));
        stack.add(createSpeechBubble(5, 3, -1, 3, padding[5][0]-FIX_PADDING_SIDES, 0f, 0f, PADDING_SIDES));
        return stack;
    }

    private Stack getSpeechBubble7Players() {
        float[][] padding = guiBuilder.getGuiBuilderPlayers().getPlayerPadding();

        Stack stack = new Stack();
        stack.add(createSpeechBubble(0, 0, 0,0, PADDING_BOTTOM_ME, 0f, 0f,PADDING_LEFT_ME));
        stack.add(createSpeechBubble(1, 1, -1,1, padding[1][0]-FIX_PADDING_SIDES, PADDING_SIDES, 0f,0f));
        stack.add(createSpeechBubble(2, 1, -1,1, padding[2][0]-FIX_PADDING_SIDES, PADDING_SIDES, 0f,0f));
        stack.add(createSpeechBubble(3, -1, -1, 2, 0f, 0f,
                padding[3][2]+FIX_PADDING_TOP, padding[3][3]+FIX_PADDING_TOP_SIDES));
        stack.add(createSpeechBubble(4, -1, -1, 2, 0f, padding[4][1]+FIX_PADDING_TOP_SIDES,
                padding[4][2]+FIX_PADDING_TOP, 0f));
        stack.add(createSpeechBubble(5, 3, -1, 3, padding[5][0]-FIX_PADDING_SIDES, 0f, 0f, PADDING_SIDES));
        stack.add(createSpeechBubble(6, 3, -1, 3, padding[6][0]-FIX_PADDING_SIDES, 0f, 0f, PADDING_SIDES));
        return stack;
    }

    private Stack getSpeechBubble8Players() {
        float[][] padding = guiBuilder.getGuiBuilderPlayers().getPlayerPadding();
        float paddingBottomMe = PADDING_BOTTOM_ME + H*0.08f;
        float paddingLeftMe = W*0.2f;
        float paddingLeftTopRight = padding[3][3]+FIX_PADDING_TOP_SIDES+W*0.05f;
        float paddingLeftTopCenter = padding[4][3]+ FIX_PADDING_LEFT_TOPCENTER-W*0.15f;

        Stack stack = new Stack();
        stack.add(createSpeechBubble(0, 0, 0,0, paddingBottomMe, 0f, 0f, paddingLeftMe));
        stack.add(createSpeechBubble(1, 1, -1,1, padding[1][0]-FIX_PADDING_SIDES, PADDING_SIDES, 0f,0f));
        stack.add(createSpeechBubble(2, 1, -1,1, padding[2][0]-FIX_PADDING_SIDES, PADDING_SIDES, 0f,0f));
        stack.add(createSpeechBubble(3, -1, -1, 2, 0f, 0f,
                padding[3][2]+FIX_PADDING_TOP, paddingLeftTopRight));
        stack.add(createSpeechBubble(4, -1, -1, 2, 0f, 0f,
                padding[4][2]+FIX_PADDING_TOP, paddingLeftTopCenter));
        stack.add(createSpeechBubble(5, -1, -1, 2, 0f, padding[5][1]+FIX_PADDING_TOP_SIDES,
                padding[5][2]+FIX_PADDING_TOP, 0f));
        stack.add(createSpeechBubble(6, 3, -1, 3, padding[6][0]-FIX_PADDING_SIDES, 0f, 0f, PADDING_SIDES));
        stack.add(createSpeechBubble(7, 3, -1, 3, padding[7][0]-FIX_PADDING_SIDES, 0f, 0f, PADDING_SIDES));
        return stack;
    }

    private Table createSpeechBubble(int index, int tableAlignment, int labelAlignment, int sbAlignment,
                         float padBottom, float padRight, float padTop, float padLeft) {
        boolean[] config = getIsLeftAndRotation();

        tButtons[index] = new Table();
        tButtons[index].setFillParent(true);
        tButtons[index].setDebug(false);
        if (tableAlignment == 0) tButtons[index].bottom();
        if (tableAlignment == 1) tButtons[index].right();
        if (tableAlignment == 2) tButtons[index].top();
        if (tableAlignment == 3) tButtons[index].left();
        addResizeButton(tButtons[index], config[0], config[1]);

        labelSpeechBubbles[index] = createLabel();
        Table tLabel = new Table();
        tLabel.setFillParent(true);
        if (labelAlignment == 0) tLabel.bottom();
        if (labelAlignment == 1) tLabel.right();
        if (labelAlignment == 2) tLabel.top();
        if (labelAlignment == 3) tLabel.left();
        tLabel.add(labelSpeechBubbles[index]).size(SIZE_SB[0], SIZE_SB[1]);

        Stack sBottom = new Stack();
        sBottom.add(tButtons[index]);
        sBottom.add(tLabel);

        tableSpeechBubbles[index] = new Table();
        tableSpeechBubbles[index].setVisible(DEBUG_SHOW);
        if (sbAlignment == 0) tableSpeechBubbles[index].bottom();
        if (sbAlignment == 1) tableSpeechBubbles[index].right();
        if (sbAlignment == 2) tableSpeechBubbles[index].top();
        if (sbAlignment == 3) tableSpeechBubbles[index].left();
        tableSpeechBubbles[index].add(sBottom)
                .padBottom(padBottom)
                .padRight(padRight)
                .padTop(padTop)
                .padLeft(padLeft);

        return tableSpeechBubbles[index];
    }

    private Label createLabel(){
        Label label = new Label("?", StyleConfigurator.getLS_Maiandra60());
        label.setAlignment(Align.center);
        label.setColor(Color.BLACK);
        return label;
    }

    private void addResizeButton(Table table, boolean left, boolean rotate) {
        TextureRegionDrawable trdLeft = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getSpeechBubbleLeft());
        TextureRegionDrawable trdRight = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getSpeechBubbleRight());
        TextureRegionDrawable trd = left ? trdLeft : trdRight;

        float padTop = rotate ? H*0.042f : -H*0.055f; //Todos menos abajo
        float padBottom = !rotate ? -H*0.012f : 0f; //Solo abajo
        float padRight = !left && rotate ? W*0.022f : 0f; //Solo arriba

        table.clear();
        table.add(createButton(trd, rotate)).size(SIZE_SB[0], SIZE_SB[1]).padTop(padTop).padBottom(padBottom).padRight(padRight);
    }

    private Button createButton(Drawable drawable, boolean rotate){
        Button button = new Button(drawable);
        button.setDisabled(true);

        if (rotate) {
            button.setTransform(true);
            button.setOrigin(button.getWidth()*0.5f,button.getHeight()*0.5f);
            button.setRotation(180);
        }

        return button;
    }

    /**
     *
     * @return
     *      Index 0 - Left:  The corner is at left of speech bubble
     *      Index 1 - Rotate: The corner is at up of speech bubble
     */
    private boolean[] getIsLeftAndRotation() {
        if (iPlayer != -1) {
            PlayerAlignment alignment = guiBuilder.getGuiBuilderPlayers().getPlayerIcon(iPlayer).getAlignment();

            if (alignment == PlayerAlignment.Bottom) return new boolean[]{true, false};
            if (alignment == PlayerAlignment.Right) return new boolean[]{true, true};
//                if (TOTAL_PLAYERS == 8 && iPlayer == 1) return new boolean[]{ false, true };
//                return new boolean[]{true, true};
//            }
            if (alignment == PlayerAlignment.TopRight) return new boolean[]{false, true};
            if (alignment == PlayerAlignment.TopCenter) return new boolean[]{false, true};
            if (alignment == PlayerAlignment.TopLeft) return new boolean[]{true, true};
            if (alignment == PlayerAlignment.Left) return new boolean[]{false, true};
        }

        return new boolean[]{false, false};
//
//        if (iPlayer == 0) return new boolean[]{true, false};
//        if (iPlayer == 1) return new boolean[]{true, true};
//
//        if (TOTAL_PLAYERS == 3 && iPlayer == 2) return new boolean[]{false, true};
//
//        if (TOTAL_PLAYERS == 4 && iPlayer == 2) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 4 && iPlayer == 3) return new boolean[]{false, true};
//
//        if (TOTAL_PLAYERS == 5 && iPlayer == 2) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 5 && iPlayer == 3) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 5 && iPlayer == 4) return new boolean[]{false, true};
//
//        if (TOTAL_PLAYERS == 6 && iPlayer == 2) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 6 && iPlayer == 3) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 6 && iPlayer == 4) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 6 && iPlayer == 5) return new boolean[]{false, true};
//
//        if (TOTAL_PLAYERS == 7 && iPlayer == 2) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 7 && iPlayer == 3) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 7 && iPlayer == 4) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 7 && iPlayer == 5) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 7 && iPlayer == 6) return new boolean[]{false, true};
//
//        if (TOTAL_PLAYERS == 8 && iPlayer == 2) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 8 && iPlayer == 3) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 8 && iPlayer == 4) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 8 && iPlayer == 5) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 8 && iPlayer == 6) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 8 && iPlayer == 7) return new boolean[]{false, true};
//
//        if (TOTAL_PLAYERS == 9 && iPlayer == 2) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 9 && iPlayer == 3) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 9 && iPlayer == 4) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 9 && iPlayer == 5) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 9 && iPlayer == 6) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 9 && iPlayer == 7) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 9 && iPlayer == 8) return new boolean[]{false, true};
//
//        if (TOTAL_PLAYERS == 10 && iPlayer == 2) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 10 && iPlayer == 3) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 10 && iPlayer == 4) return new boolean[]{true, true};
//        if (TOTAL_PLAYERS == 10 && iPlayer == 5) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 10 && iPlayer == 6) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 10 && iPlayer == 7) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 10 && iPlayer == 8) return new boolean[]{false, true};
//        if (TOTAL_PLAYERS == 10 && iPlayer == 9) return new boolean[]{false, true};
//
//        return new boolean[]{true, false};
    }

    /*** Getters And Setters ***/
}

package com.tfdevelopment.pochaonlinetfd.screen.game.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Constants;

public class GUIBuilder_Cloth {
    private static final String TAG = GUIBuilder_Cloth.class.getName();
    private static final float W = Constants.VIEWPORT_WIDTH;
    private static final float H = Constants.VIEWPORT_HEIGHT;
    private static final float SIZE_CLOTH[] = {W*0.85f, H*0.75f};

    private GUIBuilder guiBuilder;

    public GUIBuilder_Cloth(GUIBuilder guiBuilder) {
        this.guiBuilder = guiBuilder;
    }

    public Stack createClothLayer() {
        Stack stack = new Stack();

        Button bBackground = new Button(new TextureRegionDrawable(AssetLoader.aLoader.aImage.getTablecloth()));
        Table tTable = new Table();
        tTable.setDebug(false);
        tTable.add(bBackground).size(SIZE_CLOTH[0], SIZE_CLOTH[1]);
        stack.add(tTable);

        return stack;
    }
}

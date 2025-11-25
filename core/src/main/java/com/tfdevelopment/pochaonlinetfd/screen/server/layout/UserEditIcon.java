package com.tfdevelopment.pochaonlinetfd.screen.server.layout;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.screen.server.ServerMainScreen;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;

public class UserEditIcon extends Stack {
    private static final String TAG = UserEditIcon.class.getName();

    private Table tUser;
    private ImageButton ibUserIcon;
    private int indexUserIcon;

    public UserEditIcon(int indexUserIcon){
        super();
        this.indexUserIcon = indexUserIcon;

        createIcon();
    }

    private void createIcon(){
        Drawable dUserIcon = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getUserIcon(indexUserIcon,0));
        ibUserIcon = new ImageButton(dUserIcon);

        Drawable dEditIcon = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiEdit());
        ImageButton ibEditIcon = new ImageButton(dEditIcon);
        //ibEditIcon.setSize(ServerMainScreen.halls_Width*0.007f, ServerMainScreen.halls_Height*0.002f);

        tUser = new Table();
        tUser.setFillParent(true);
        updateTable();

        Table tEdit = new Table();
        tEdit.setFillParent(true);
        tEdit.add(ibEditIcon).size(ServerMainScreen.content_Width *0.12f, ServerMainScreen.content_Height *0.03f).padLeft(40f).padBottom(40f);
        tEdit.row();
        tEdit.add(new Actor());

        this.add(tUser);
        //this.add(tEdit);
    }

    public void updateIcon(int index){
        this.indexUserIcon = index;
        ibUserIcon.getStyle().imageUp = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getUserIcon(indexUserIcon, 0));

        updateTable();
    }

    private void updateTable(){
        tUser.clear();
        if(indexUserIcon==0) {
            tUser.add(ibUserIcon).size(ServerMainScreen.content_Width * 0.35f, ServerMainScreen.content_Height * 0.1f);
        } else {
            tUser.add(ibUserIcon).size(ServerMainScreen.content_Width * 0.35f, ServerMainScreen.content_Height * 0.1f);
        }
    }
}

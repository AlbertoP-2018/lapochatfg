package com.tfdevelopment.pochaonlinetfd.screen.server.layout.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;

public class PlayerIconGames extends Stack {
    private static final String TAG = PlayerIconGames.class.getName();

    private ActorChairGames actorChairGames;

    private String nameUser;
    private int userIcon;

    private Skin skin;
    private Drawable dPlayer, dName;
    private ImageButton ibPlayer;
    private TextButton tbName;
    private TextButton.TextButtonStyle tbsName;

    private Table tIconHall;

    //Constructor para HallScreen
    public PlayerIconGames(ActorChairGames actorChairGames, String nameUser, Skin skin){
        super();
        this.actorChairGames = actorChairGames;
        this.nameUser = nameUser;
        this.userIcon = 0;
        this.skin = skin;

        createToHallScreen();
    }

    private void createToHallScreen(){
        dPlayer = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getArPlayerHall());

        dName = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getBackgroundPlayerName());
        tbsName = new TextButton.TextButtonStyle(dName, dName, dName, AssetLoader.aLoader.aFont.getBfMaiandra50());
        tbsName.fontColor = Color.BLACK;

        ibPlayer = new ImageButton(dPlayer);
        ibPlayer.setSize(10,200);
        tbName = new TextButton(nameUser, skin);
        tbName.setStyle(tbsName);

        tIconHall = new Table();
        tIconHall.setFillParent(true);
        tIconHall.add(ibPlayer).size(66f, 88f).padBottom(15f);

        Table tName = new Table();
        tName.setFillParent(true);
        tName.add(new Actor()).size(0, 30).expand();
        tName.row();
        tName.add(tbName).size(85, 15);

        this.add(tIconHall);
        this.add(tName);
    }

    public void setName(String nameUser) {
        this.nameUser = nameUser;
        tbName.setText(this.nameUser);
    }

    public void setUserIcon(int userIcon){
        this.userIcon = userIcon;

        tIconHall.clear();
        if(userIcon==0){ //Icono por defecto
            tIconHall.add(ibPlayer).size(55, 75).padBottom(8f);
        } else {
            tIconHall.add(ibPlayer).size(66f, 88f).padBottom(15f);
        }
        ibPlayer.getStyle().imageUp = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getUserIcon(userIcon, 0));
    }

    public void setColorNameCPU(){ tbsName.fontColor = Color.RED; }

    public String getId(){ return actorChairGames.getId(); }
    public String getNameUser(){ return nameUser; }
}

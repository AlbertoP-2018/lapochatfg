package com.tfdevelopment.pochaonlinetfd.screen.server.layout.games;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ActorChairGames extends Button {
    private static final String TAG = ActorChairGames.class.getName();
    private String id; //ID del botón (nMesa-nSilla)

    private ButtonChairGames buttonChairGames;
    private PlayerIconGames playerIconGames;

    public ActorChairGames(TextureRegionDrawable trdChair, Skin skin, String id, String nameUser){
        super(skin);
        this.id = id;
        //this.nameUser = nameUser;

        buttonChairGames = new ButtonChairGames(this, trdChair);
        playerIconGames = new PlayerIconGames(this, nameUser, skin);
    }

    public String getId(){ return this.id; }
    public String getNameUser(){ return playerIconGames.getNameUser(); }

    public Button getButtonChair(){ return this.buttonChairGames; }
    public PlayerIconGames getPlayerIconGames(){ return this.playerIconGames; }

    public void standUp(Table table, ActorChairGames actorChairGames){
        playerIconGames.setName("");
        Cell cell = table.getCell(actorChairGames.getPlayerIconGames());
        if(cell!=null) cell.setActor(actorChairGames.getButtonChair());
    }

    public void sitDown(Table table, ActorChairGames actorChairGames, String nameUser, int userIcon){
        playerIconGames.setName(nameUser);
        playerIconGames.setUserIcon(userIcon);

        Cell cell = table.getCell(actorChairGames.getButtonChair());
        if(cell!=null) {
            if(nameUser.contains("CPU"))
                actorChairGames.getPlayerIconGames().setColorNameCPU();
            cell.setActor(actorChairGames.getPlayerIconGames());
        }
        //table.getCell(actorChair.getButtonChair()).setActor(actorChair.getPlayerIcon());
    }
}

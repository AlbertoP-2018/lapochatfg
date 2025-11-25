package com.tfdevelopment.pochaonlinetfd.screen.server.layout.games;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonChairGames extends Button {
    private ActorChairGames actorChairGames;

    public ButtonChairGames(ActorChairGames actorChairGames, TextureRegionDrawable trdChair){
        super(trdChair);
        this.actorChairGames = actorChairGames;
    }

    public String getId(){ return actorChairGames.getId(); }
    public String getNameUser(){ return actorChairGames.getNameUser(); }
}

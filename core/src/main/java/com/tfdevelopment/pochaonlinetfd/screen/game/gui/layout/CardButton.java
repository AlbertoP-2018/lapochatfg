package com.tfdevelopment.pochaonlinetfd.screen.game.gui.layout;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CardButton extends Button{
    private boolean available;
    private String unavailableText;

    public CardButton(TextureRegionDrawable trd, boolean available) {
        super(trd);
        this.available = available;
    }

    public boolean isAvailable(){ return this.available; }
    public void setAvailable(boolean available){ this.available = available; }
    public String getUnavailableText(){ return this.unavailableText; }
    public void setUnavailableText(String unavailableText){ this.unavailableText = unavailableText; }
}

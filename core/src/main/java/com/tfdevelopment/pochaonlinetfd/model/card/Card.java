package com.tfdevelopment.pochaonlinetfd.model.card;

import static com.tfdevelopment.pochaonlinetfd.utils.conf.Config.TESTING;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.CardType;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Suit;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Settings;

public class Card {
    private static final String TAG = Card.class.getName();

    private TextureRegion trCard;
    private Suit suit;
    private CardType cardType;
    private boolean selected; //Indica si ya se ha seleccionado (por ronda)

    //IA
    private boolean winning;
    private boolean halfWinning;

    public Card(int index, CardType cardType, Suit suit) {
        if (!TESTING){
            if (Settings.getTypeCards().equals(Settings.CLASSIC_TC)) this.trCard = AssetLoader.aLoader.aCClassic.getAtlasRegionCard(index);
            if (Settings.getTypeCards().equals(Settings.LINUX_TC)) this.trCard = AssetLoader.aLoader.aCLinux.getAtlasRegionCard(index);
        }
        this.cardType = cardType;
        this.suit = suit;
        this.selected = false;
        this.winning = false;
        this.halfWinning = false;
    }

    public void resetCard(){
        this.selected = false;
        this.winning = false;
        this.halfWinning = false;
    }

    public String toString(){
        String card = cardType.toString()+"_"+suit.toString();
        String string = (this.selected ? "*"+card+"*" : card);
        return string;
    }

    /*** Getters And Setters ***/
    public TextureRegion getTrCard(){ return trCard; }
    public Suit getSuit(){ return suit; }
    public void setSelected(boolean selected){ this.selected = selected; } //ThreadBE
    public void setWinning(boolean winning){ this.winning = winning; }
    public void setHalfWinning(boolean halfWinning){ this.halfWinning = halfWinning; }
    public CardType getCardType(){ return this.cardType; }
    public boolean isSelected(){ return this.selected; }
    public boolean isWinning(){ return this.winning; }
    public boolean isHalfWinning(){ return this.halfWinning; }
}

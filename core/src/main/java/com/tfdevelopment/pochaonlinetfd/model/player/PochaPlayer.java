package com.tfdevelopment.pochaonlinetfd.model.player;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.PhasePlayer;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Suit;

import java.util.ArrayList;

public class PochaPlayer {
    private static final String TAG = PochaPlayer.class.getName();

    private String name;
    private boolean ai;
    private PhasePlayer phasePlayer;
    private int bet;
    private int win;
    private ArrayList<Card> alCards;
    private int selectedCard; //Index de la carta seleccionada

    public PochaPlayer(String name, boolean ai){
        this.name = name;
        this.ai = ai;
        this.alCards = new ArrayList<>();

        initHand();
    }

    public void initHand(){
        Gdx.app.log(TAG, "*Init HAND*");
        this.bet = -1;
        this.win = -1;

        for(int i=0; i<alCards.size(); i++)
            alCards.get(i).setSelected(false);
        alCards.clear();

        initChance();
    }

    /***** Thread Button Event *****/
    public void initChance(){
        Gdx.app.log(TAG, "*Init CHANCE*");
        this.phasePlayer = PhasePlayer.WAITING;
        this.selectedCard = -1;
    }

    /***** Thread Button Event *****/
    public void setBet(int bet){
        this.bet = bet;
        this.win = 0;
    }

    public void addCard(Card card){
        this.alCards.add(card);
    }

    public ArrayList<Card> getUnselectedCards(){
        ArrayList<Card> arrayList = new ArrayList<>();
        for(int i=0; i<alCards.size(); i++){
            if(!alCards.get(i).isSelected()) arrayList.add(alCards.get(i));
        }
        return arrayList;
    }
    public ArrayList<Integer> getFirstSuit(Suit suit){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i=0; i<alCards.size(); i++){
            if(!alCards.get(i).isSelected()){
                if(alCards.get(i).getSuit() == suit) arrayList.add(i);
            }
        }
        return arrayList;
    }
    public ArrayList<Integer> getTrumpSuit(Suit suit){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i=0; i<alCards.size(); i++){
            if(!alCards.get(i).isSelected()){
                if(alCards.get(i).getSuit() == suit) arrayList.add(i);
            }
        }
        return arrayList;
    }


    public void setAlCards(ArrayList<Card> _alCards) { this.alCards = _alCards; } /***** Thread Button Event *****/
    public void setSelectedCards(int index) { this.selectedCard = index; } /***** Thread Button Event *****/
    public void setWin(int win){ this.win = win; } /***** Thread Button Event *****/

    /*** Getters And Setters ***/
    public void setPhasePlayer(PhasePlayer phasePlayer){ this.phasePlayer = phasePlayer;}
    public PhasePlayer getPhasePlayer(){ return phasePlayer; }
    public boolean isAIPlayer(){ return ai; }
    public String getName(){ return name; }
    public int getBet(){ return bet; }
    public int getWin(){ return win; }
    public ArrayList<Card> getAlCards(){ return alCards; }
    public Card getCard(int index){ return alCards.get(index); }
    public int getSelectedCard(){ return selectedCard; }
}

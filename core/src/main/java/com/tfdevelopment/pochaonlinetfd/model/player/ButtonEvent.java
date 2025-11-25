package com.tfdevelopment.pochaonlinetfd.model.player;

import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.DecisionB;

public class ButtonEvent {
    private DecisionB decisionB;
    private int player;
    private int answer; //BET: Número de apuesta --- CARD: Carta seleccionada

    public ButtonEvent(DecisionB decisionB, int player, int answer){
        this.decisionB = decisionB;
        this.player = player;
        this.answer = answer;
    }

    public DecisionB getDecisionB() { return decisionB; }
    public void setDecisionB(DecisionB decisionB) { this.decisionB = decisionB; }
    public int getPlayer() { return player; }
    public void setPlayer(int player) { this.player = player; }
    public int getAnswer() { return answer; }
    public void setAnswer(int answer) { this.answer = answer; }
}

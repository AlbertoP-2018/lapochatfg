package com.tfdevelopment.pochaonlinetfd.model.thread;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.game.GameData;
import com.tfdevelopment.pochaonlinetfd.model.game.GameLogic;
import com.tfdevelopment.pochaonlinetfd.model.game.GameScore;
import com.tfdevelopment.pochaonlinetfd.model.player.PochaPlayer;
import com.tfdevelopment.pochaonlinetfd.screen.game.logic.OnlineGS;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.layout.CardButton;
import com.tfdevelopment.pochaonlinetfd.server.object.buttonevent.ButtonEventServer;
import com.tfdevelopment.pochaonlinetfd.server.object.game.ServerGameLogin;
import com.tfdevelopment.pochaonlinetfd.server.object.game.ServerGameData;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ThreadReloadGame extends Thread {
    private static final String TAG = ThreadReloadGame.class.getName();

    private Semaphore semaphore;

    private OnlineGS onlineGS;
    private GameLogic gameLogic;
    private ServerGameData dsg;

    public ThreadReloadGame(Semaphore semaphore, OnlineGS onlineGS, GameLogic gameLogic, ServerGameData dsg){
        super();
        this.semaphore = semaphore;
        this.onlineGS = onlineGS;
        this.gameLogic = gameLogic;
        this.dsg = dsg;
    }

    @Override
    public void run() {
        //super.run();
        try {
            semaphore.acquire();
            Gdx.app.log(TAG, "Starting thread \"ThreadReloadGame\"...");
            reloadGameStarted();
            onlineGS.setFlagUpdateCardsFromReloading();
            Gdx.app.log(TAG, "Thread \"ThreadReloadGame\" end.");
            semaphore.release();
        } catch (InterruptedException e) {
            Gdx.app.error(TAG,e.getMessage());
        }
    }

    /*
            ID: 7  --> continues=0   ::  firstId=0   ::   lastContinue=0
            ID: 8  --> continues=1   ::  firstId=-1   ::   lastContinue=8
            ID: 9  --> continues=2   ::  firstId=-1   ::   lastContinue=8
            ID: 10  --> continues=3   ::  firstId=-1   ::   lastContinue=8
            ID: 11  --> continues=4   ::  firstId=-1   ::   lastContinue=8
            ID: 12  --> continues=4   ::  firstId=12   ::   lastContinue=8
            ID: 13  --> continues=4   ::  firstId=12   ::   lastContinue=8
            ID: 19  --> continues=4   ::  firstId=12   ::   lastContinue=8
            ID: 20  --> continues=1   ::  firstId=-1   ::   lastContinue=20
            ID: 21  --> continues=2   ::  firstId=-1   ::   lastContinue=20
            ID: 22  --> continues=3   ::  firstId=-1   ::   lastContinue=20
            ID: 23  --> continues=4   ::  firstId=-1   ::   lastContinue=20
            ID: 24  --> continues=4   ::  firstId=24   ::   lastContinue=20
         */

    /**
     *
     */
    private void reloadGameStarted(){
        final int TOTAL_PLAYERS = onlineGS.getTOTAL_PLAYERS();
        ServerGameLogin dsgLogin = dsg.getDataSGLogin();

        ArrayList<ButtonEventServer> alButtonES = dsgLogin.getAlButtonEventServer();
        int continues = 0;
        int lastContinue = -1;
        int firstId = -1;
        for (int i=alButtonES.size()-1; i>0; i--) {
            ButtonEventServer bes = alButtonES.get(i);
            if (bes.getButtonEvent().getDecisionB() == PochaEnum.DecisionB.CONTINUE) {
                if (lastContinue==-1 || lastContinue==(i+1)){
                    continues+=1;
                    lastContinue = i;

                    if (continues==TOTAL_PLAYERS) break;
                } else break;
            } else {
                if (continues>0) break;
                firstId = i;
            }
        }

        //P1: Ejecutamos to-do
        GameData gameData = gameLogic.getGameData();
        GameScore gameScore = gameLogic.getGameScore();
        PochaPlayer[] alPochaPlayers = gameLogic.getPlayers();
        int firstPlayer = dsg.getFirstPlayer();
        int handPlayer = dsgLogin.getHandPlayer();
        int numHand = dsgLogin.getNumHand();
        int numChance = dsgLogin.getNumChance();
        int numChancesHand = dsgLogin.getNumChancesHand();
        int[][][] scoreboard = dsgLogin.getScoreboard();

        for (int i=0; i<alPochaPlayers.length; i++) {
            alPochaPlayers[i].setPhasePlayer(PochaEnum.PhasePlayer.WAITING);

            // Set cards
            int[] cardsIndex = dsg.getGamePlayers().get(i).getCards();
            ArrayList<Card> newAlCards = new ArrayList<>();
            for (int j=0; j<numChancesHand; j++) {
                newAlCards.add(gameLogic.getShufflerCards().getAlAllCards().get(cardsIndex[j]));
            }
            alPochaPlayers[i].setAlCards(newAlCards);
        }

        if (continues==0) {
            alPochaPlayers[firstPlayer].setPhasePlayer(PochaEnum.PhasePlayer.BET);
            for (int i=0; i<alButtonES.size(); i++){
                ButtonEventServer.runButtonEventServer(onlineGS, alButtonES.get(i));
            }
        }

        //P2: Aplicamos marcador y esperamos
        if (continues>0 && continues<TOTAL_PLAYERS) {
            int lastPlayer = alButtonES.get(alButtonES.size()-1).getButtonEvent().getPlayer();
            int currentPlayer = gameData.getNextPlayer(lastPlayer);
            gameData.reloadGame(PochaEnum.Phase.CONTINUE, currentPlayer, handPlayer, firstPlayer);
            gameScore.reloadGame(numHand, numChance, numChancesHand);
            updatePlaqueData(TOTAL_PLAYERS, alButtonES);
            gameScore.setScoreboard(scoreboard);
            hideCards();

            alPochaPlayers[currentPlayer].setPhasePlayer(PochaEnum.PhasePlayer.CONTINUE);
        }

        if (continues==TOTAL_PLAYERS) {
            //P3: El último ID es un continue, empieza la baza:  Aplicamos jugador mano, asignamos cartas y marcador y empieza
            if (firstId==-1) {
                int currentPlayer = handPlayer;
                gameData.reloadGame(PochaEnum.Phase.BET, currentPlayer, handPlayer, firstPlayer);
                gameScore.reloadGame(numHand, 1, numChancesHand);
                gameScore.setScoreboard(scoreboard);

                alPochaPlayers[handPlayer].setPhasePlayer(PochaEnum.PhasePlayer.BET);
            //P4: Existe un evento más, la baza ya ha empezado, ejecutamos todos los siguientes
            } else {
                int currentPlayer = handPlayer;
                firstPlayer = handPlayer;
                gameData.reloadGame(PochaEnum.Phase.BET, currentPlayer, handPlayer, firstPlayer);
                gameScore.reloadGame(numHand, 1, numChancesHand);
                gameScore.setScoreboard(scoreboard);

                alPochaPlayers[handPlayer].setPhasePlayer(PochaEnum.PhasePlayer.BET);
                for (int i=firstId; i<alButtonES.size(); i++){
                    ButtonEventServer.runButtonEventServer(onlineGS, alButtonES.get(i));
                }
            }
        }

        if (!alButtonES.isEmpty()) dsg.setIdLastEvent(alButtonES.get(alButtonES.size()-1).getIdEvent());
    }

    private void updatePlaqueData(int totalPlayers, ArrayList<ButtonEventServer> _alButtonES) {
        ArrayList<ButtonEventServer> alBEPlaqueData = new ArrayList<>();

        for (int i=_alButtonES.size()-1; i>=0; i--) {
            ButtonEventServer bes = _alButtonES.get(i);
            if (bes.getButtonEvent().getDecisionB() == PochaEnum.DecisionB.BET) {
                alBEPlaqueData.add(bes);
                if (alBEPlaqueData.size() == totalPlayers) break;
            }
        }

        for (ButtonEventServer bes : alBEPlaqueData) {
            int indexPlayer = bes.getButtonEvent().getPlayer();
            PochaPlayer[] pochaPlayers = gameLogic.getPlayers();
            pochaPlayers[indexPlayer].setBet(bes.getButtonEvent().getAnswer());
            pochaPlayers[indexPlayer].setWin(-1);
        }
    }

    private void hideCards(){
        ArrayList<CardButton> alButton = gameLogic.getGuiBuilder().getGuiBuilderCards().getAlCards();
        for (int i=0; i<alButton.size(); i++) {
            gameLogic.getGuiBuilder().getGuiBuilderCards().hideCard(i);
        }
    }
}

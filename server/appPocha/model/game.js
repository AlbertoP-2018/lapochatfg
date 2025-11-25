import Data from './data.js';
import CrupierJS from '../events/game/crupier.js';

import { getUserOnlineByName } from '../events/utils.js';
import { TAGS, FLAGS, writeInfo } from '../utils/logs.js';

class Game {
    TOTAL_PLAYERS; #TOTAL_CARDS; #NUM_MAX_CARDS; #TOTAL_HANDS;

    #id; #hostName; #numBots; #mode;
    #players; #firstPlayer; #arrayEvents;
    #numHand; #numChance; #numChancesHand; #handPlayer;

    #cardsPile; #trump;
    #scoreboard;

    #numValidatedUser = 0;
    #numContinues = 0;
    #numSelection = 0;
    #winningPlayers = [];
    #gameStarted = false;           //Partida empezada
    #gameCanceled = false;          //Partida cancelada
    #gameFinished = false;          //Partida finalizada (Para poder actualizar)
    #gameCompleted = false;         //Partida completada (Se ha llegado al número de juegos sin abandonar)
    #gameUpdated = false;           //Partida actualizada en la BD

    constructor(id, user, totalPlayers, numBots, mode){
        this.#id = id;
        this.#hostName = user.name;
        this.TOTAL_PLAYERS = totalPlayers;
        this.#mode = mode;

        this.#players = [user.getPlayerData()]; //new DataJS.PlayerData
        this.#numBots = numBots;

        this.#numHand = 1; //Número actual de la mano
        this.#numChance = 1; //Número actual de la baza
        this.#numChancesHand = 1; //Número de bazas en dicha mano

        this.#arrayEvents = []; //Eventos de botón
        this.#cardsPile = []; //Montón de cartas a repartir
        this.#trump = -1;
    }
    
    initGame() {
        for (let i=0; i<this.#players.length; i++) {
            const player = this.#players[i];

            player.setPlayerIndex(i);
            const auxPlayer = getUserOnlineByName(player.getName());
            auxPlayer.playerIndex = i;
        }
        this.#calculateValues();
        this.#firstPlayer = getNewFirstPlayer(this.TOTAL_PLAYERS);
        this.#handPlayer = this.#firstPlayer;
        this.#initScoreboard();
        this.#initCardsPlayer();
        CrupierJS.dealCards(this);
        this.#gameStarted = true;
    }

    initHand() {
        this.#numHand+=1;
        this.#numChance=1;
        this.#handPlayer = (this.#handPlayer+1) < this.TOTAL_PLAYERS ? this.#handPlayer+1 : 0;
        this.#firstPlayer = this.#handPlayer;
        

        if(this.#numHand<=this.TOTAL_PLAYERS) this.#numChancesHand = 1;
        else if(this.#numHand<=(this.#TOTAL_HANDS-this.TOTAL_PLAYERS)) this.#numChancesHand = (this.#numHand-this.TOTAL_PLAYERS+1);
        else this.#numChancesHand = this.#NUM_MAX_CARDS;

        writeInfo(TAGS.LOG, FLAGS.GAMES, "InitHand(): "+this.#id+" - NHand: "+this.#numHand+" - NChancesHand: "+this.#numChancesHand+" - HandPlayer: "+this.#handPlayer+" - FirstPlayer: "+this.#firstPlayer);
    }

    initChance() {
        this.#numChance+=1;
        // this.#firstPlayer = this.#calculateNewFirstPlayer();

        writeInfo(TAGS.LOG, FLAGS.GAMES, "InitChance(): "+this.#id+" - NumChance: "+this.#numChance+" - HandPlayer: "+this.#handPlayer+" - FirstPlayer: "+this.#firstPlayer);
    }

    initCardsPile(){
        const totalCards = this.TOTAL_PLAYERS * this.#NUM_MAX_CARDS;
        const excludeTwos = totalCards == 35 || totalCards == 36;

        this.#cardsPile = [];
        for(let i=0; i<this.#TOTAL_CARDS; i++){
            if (!excludeTwos || ((i != 1 || i != 11 || i != 21 || i != 31))) 
                this.#cardsPile.push(i); 
        }
    }
    #initCardsPlayer(){
        for(let i=0; i<this.TOTAL_PLAYERS; i++){
            this.#players[i].initCards(this.#NUM_MAX_CARDS);
        }
    }
    #initScoreboard() {
        this.#scoreboard = [];
        for (let i=0; i<this.#TOTAL_HANDS; i++) {
            this.#scoreboard.push([]);
            for (let j=0; j<this.TOTAL_PLAYERS; j++) {
                this.#scoreboard[i].push([0,0]);
            }
        }
    }
    addValidatedUser(){ this.#numValidatedUser+=1; }
    addNumContinues(){ this.#numContinues+=1; }
    addNumSelection(){ this.#numSelection+=1; }
    resetContinues(){ this.#numContinues=0; }
    resetSelection(){ this.#numSelection=0; }

    getNewGameData() { 
        const playerNames = [];
        this.#players.forEach(player => {
            playerNames.push(player.getName());
        });
        return new Data.NewGameData(this.#id, this.#hostName, this.TOTAL_PLAYERS, this.#numBots, this.#mode, playerNames);
    }

    getGameData() {                           //DataServerGame
        return new Data.GameData(this.#id, this.#players, this.#trump, this.#firstPlayer); 
    }

    getGameReconnectData() { // DataServerGame with GameReconnectData
        const gameReconnectData = 
            new Data.GameReconnectData(this.#numHand, this.#numChance, this.#numChancesHand, this.#handPlayer, this.#arrayEvents, this.#scoreboard);
        let gameData = this.getGameData();
        gameData.setGameReconnectData(gameReconnectData);
        return gameData;
    }

    updatePlayerId(chair, newId){
        this.#players[chair].setId(newId);
    }

    addPlayer(user) {
        this.#players.push(user);
    }

    removePlayer(userName) {
        for (let i=0; i<this.#players.length; i++) {
            if (this.#players[i].getName() === userName) {
                this.#players.splice(i, 1);
                break;
            }
        }
    }

    #calculateNewFirstPlayer() {
        const trumpCard = CrupierJS.getCardTrump(trump);
        let winIndex = -1;
        let winValue = -1;

        const selectionCards = [];
        for (let i=0; i<this.#players.length; i++) {

        }
    }

    //GamePlayer
    getPlayerName(index){ return this.#players[index].getName(); }
    getPlayerId(index){ return this.#players[index].getId(); }
    getCards(){
        let cards = [];
        for(let i=0; i<this.TOTAL_PLAYERS; i++){
            const aux = this.#players[i].gCards();
            cards.push(aux);
        }
        return cards;
    }

    isUserInGame(userName) {
        for (let i=0; i<this.#players.length; i++) {
            if (this.#players[i].getName() === userName) return true;
        }
        return false;
    }

        //Getters
    getId(){ return this.#id; }
    getMode(){ return this.#mode; }
    getHostName() { return this.#hostName; }
    getPlayers(){ return this.#players; } //PlayerData
    getArrayEvents(){ return this.#arrayEvents; }
    getCardsPile(){ return this.#cardsPile; }
    getNumHand(){ return this.#numHand; }
    getNumChance(){ return this.#numChance; }
    getNumChancesHand(){ return this.#numChancesHand; }
    getNumMaxCards(){ return this.#NUM_MAX_CARDS; }
    getHandPlayer(){ return this.#handPlayer; }
    getFirstPlayer() { return this.#firstPlayer; }
    getTotalHands(){ return this.#TOTAL_HANDS; }
    getTrump(){ return this.#trump; } //DataServerGame
    isGameStarted(){ return this.#gameStarted; }
    isGameCanceled(){ return this.#gameCanceled; }
    isGameFinished(){ return this.#gameFinished; }
    isGameCompleted(){ return this.#gameCompleted; }
    isGameUpdated(){ return this.#gameUpdated; }
    getScoreboard(){ return this.#scoreboard; }
    getNumSelection(){ return this.#numSelection; }
    getNumContinues(){ return this.#numContinues; }
        //Setters
    setSettings(numValidatedUser){ this.#numValidatedUser = numValidatedUser; } //Publicas y Clasificación (Settings)
    setGameStarted(started){ this.#gameStarted = started; }
    setTrump(trump){ this.#trump = trump; }
    setScoreboard(scoreboard){ this.#scoreboard = scoreboard; }
    setFirstPlayer(firstPlayer){ this.#firstPlayer = firstPlayer; }
    setWinningPlayers(winningPlayers){ this.#winningPlayers = winningPlayers; }
    setNumHand(numHand){ this.#numHand = numHand; }
    setNumChance(numChance){ this.#numChance = numChance; }
    setNumChancesHand(numChancesHand){ this.#numChancesHand = numChancesHand; }
    setHandPlayer(handPlayer){ this.#handPlayer = handPlayer; }
    setGameCompleted(completed) { this.#gameCompleted = completed; }
    setGameFinished(finished){ this.#gameFinished = finished; }
    setGameUpdated(updated){ this.#gameUpdated = updated; }

    #calculateValues() {
        switch (this.TOTAL_PLAYERS) {
            case 3: // 39 Cartas, sin triunfo       // 36 cartas, sin los doses
                this.#TOTAL_HANDS = 17;             // this.#TOTAL_HANDS = 16;
                this.#NUM_MAX_CARDS = 13;           // this.#NUM_MAX_CARDS = 12;
                break;
            case 4: // 40 Cartas
                this.#TOTAL_HANDS = 16;
                this.#NUM_MAX_CARDS = 10;
                break;
            case 5: // 40 Cartas
                this.#TOTAL_HANDS = 16;
                this.#NUM_MAX_CARDS = 8;
                break;
            case 6: // 36 Cartas, sin doses
                this.#TOTAL_HANDS = 16;
                this.#NUM_MAX_CARDS = 6;
                break;
            case 7: // 35 Cartas, sin doses ni triunfo
                this.#TOTAL_HANDS = 17;
                this.#NUM_MAX_CARDS = 5;
                break;
            case 8: // 40 Cartas
                this.#TOTAL_HANDS = 19;
                this.#NUM_MAX_CARDS = 5;
                break;
        }

        this.#TOTAL_CARDS = this.#NUM_MAX_CARDS*this.TOTAL_PLAYERS;
    }
}

class ButtonEvent{
    constructor(_idButtonEvent, _decisionB, _player, _answer){
        this.idButtonEvent = _idButtonEvent;
        this.decisionB = _decisionB;
        this.player = _player;
        this.answer = _answer;
    }
}
/************ FIN - OBJETOS ************/

/************ INI - MÉTODOS ************/
function getNewFirstPlayer(NUM_PLAYERS){
    let min=0;
    let max=NUM_PLAYERS; //4 no incluido
    return Math.floor(Math.random()*(max-min)+min);
}
/************ FIN - MÉTODOS PRIVADOS ************/

const GameJS = {};
GameJS.Game = Game;
GameJS.ButtonEvent = ButtonEvent;
export default GameJS;

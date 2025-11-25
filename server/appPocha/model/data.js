/*******************************************************************************************************/
/****** Dichos objetos serán enviados al cliente, por lo que no pueden existir atributos privados ******/
/*******************************************************************************************************/
class UserData {
    constructor(id, name, gameId, playerIndex, userIcon, online){
        this.id = id;
        this.name = name;
        this.gameId = gameId;
        this.playerIndex = playerIndex;
        this.userIcon = userIcon;
        this.online = online;
    }
}

class RankingUserData {
    constructor(name, points, ratio, nWon, nLost){
        this.name=name; 
        this.points=points;
        this.ratio=ratio;
        this.nWon=nWon;
        this.nLost=nLost;
    }
}

class PlayerData {
    constructor(id, name, gameId, playerIndex, userIcon, ai){
        this.id = id;
        this.name = name;
        this.gameId = gameId;
        this.playerIndex = playerIndex;
        this.userIcon = userIcon;
        this.ai = ai;
        this.cardsData = [];
    }

    getId(){ return this.id; }
    setId(newId) { this.id = newId; }
    getName(){ return this.name; }
    isAi(){ return this.ai; }
    gCardsData() { return this.cardsData; }
    gCards() { return this.cardsData[0]; }
    setSelectedCard(index, selected) { this.cardsData[1][index] = selected; }
    setPlayerIndex(index) { this.playerIndex = index; }
    setCard(index, card, selected) { 
        this.cardsData[0][index] = card; 
        this.cardsData[1][index] = selected;
    }
    initCards(NUM_MAX_CARDS){
        this.cardsData[0] = [];
        this.cardsData[1] = [];
        for(let i=0; i<NUM_MAX_CARDS; i++){
            this.cardsData[0].push(-1);
            this.cardsData[1].push(false);
        }
    }
}

class NewGameData {
    constructor(id, hostName, numPlayers, numBots, mode, playerNames) {
        this.id = id;
        this.hostName = hostName;
        this.numPlayers = numPlayers;
        this.numBots = numBots;
        this.mode = mode;
        this.playerNames = playerNames;
    }
}

class GameData {
    constructor(_id, _players, _trump, _firstPlayer){
        this.id = _id;
        this.players = _players;
        this.trump = _trump;
        this.firstPlayer = _firstPlayer;
        this.gameReconnectData = null;
    }

    getId(){ return this.id; }
    gPlayers(){ return this.players; }
    gTrump(){ return this.trump; }
    getFirstPlayer(){ return this.firstPlayer; }
    getGameReconnectData(){ return this.gameReconnectData; }
    setGameReconnectData(gameReconnectData){ this.gameReconnectData = gameReconnectData; }
}

class GameReconnectData {
    constructor(numHand, numChance, numChancesHand, handPlayer, arrayEvents, scoreboard) {
        this.numHand = numHand;
        this.numChance = numChance;
        this.numChancesHand = numChancesHand;
        this.handPlayer = handPlayer;
        this.arrayEvents = arrayEvents;
        this.scoreboard = scoreboard;
    }
}

class NewsData {
    constructor () {
        this.version = '0.0.0.0';
        this.news = [];
    }

    getVersion() { return this.version; }
    getNews() { return this.news; }
    setVersion(version) { if (version) this.version = version; }
    setNews(news) { if (news) this.news = news; }
}

class RankingData {
    constructor () {
        this.dateText = '';
        this.currentSeason = [[]];
        this.lastSeasons = [[]];
        this.flagInterval = true;
    }
    
    setCurrentSeason(order, data) { this.currentSeason[order] = data; }
    setLastSeasons(season, order, data) { this.lastSeasons[season][order] = data; }

    getDateText() { return this.dateText; }
    getCurrentSeason(order) { return this.currentSeason[order]; }
    getLastSeasons(season, order) { return this.lastSeasons[season][order]; }
    isFlagInterval() { return this.flagInterval; }

    changeFlagInterval() { 
        this.flagInterval = !this.flagInterval; 
        console.log(`*Update Ranking Enabled: ${this.flagInterval}*`);
    }

    updateDate() {
        const date = new Date();
        const options = { timeZone: "Europe/Madrid", hour: "2-digit", minute: "2-digit", hour12: false };
        const [hours, minutes] = new Intl.DateTimeFormat("es-ES", options).format(date).split(":");
        this.dateText = `Última actualización: ${hours}h ${minutes}min`;
    }
}

const DataJS = {};
DataJS.UserData = UserData;
DataJS.RankingUserData = RankingUserData;
DataJS.PlayerData = PlayerData;
DataJS.NewGameData = NewGameData;
DataJS.GameData = GameData;
DataJS.GameReconnectData = GameReconnectData;
DataJS.NewsData = NewsData;
DataJS.RankingData = RankingData;
export default DataJS;

import Config from '../server/config.js';
import DatabaseJS from '../db/database.js';

import { users, games } from '../server/init.js';

export const TAGS = {
    ERROR:"ERROR",
    LOG:"LOG"
};

export const FLAGS = {
    SIGN_IN: "SIGN-IN", 
    LOG_OUT:"LOG-OUT",
    HALL_MESSAGE:"HALL-MESSAGE",
    GAME_MESSAGE:"GAME-MESSAGE",
    DEAL_CARDS:"DEAL-CARDS",
    BUTTON_EVENT:"BUTTON-EVENT",
    ERROR_BUTTON_EVENT:"ERROR-BUTTON-EVENT",
    PLAYERS_CHAIRS:"PLAYERS-CHAIR",
    SOCKET_CONNECTED:"SOCKET-CONNECTED",
    TOURNAMENTS:"TOURNAMENTS",
    GAME_TO_HALL:"GAME-TO-HALL",
    OBSERVER_MODE:"OBSERVER-MODE",
    GAMES:"GAMES",
    PAUSE:"PAUSE",
    USERS_ONLINE:"USERS-ONLINE",
    TOURNAMENT_FINISHED:"TOURNAMENT-FINISHED",
    SOCKET_EXPELED:"SOCKET-EXPELED",
    BUTTON_EVENT_TOURNAMENT:"BUTTON-EVENT-TOURNAMENT",
    ERROR_BUTTON_EVENT_TOURNAMENT:"ERROR-BUTTON-EVENT-TOURNAMENT",
    GAMES_TOURNAMENT:"GAMES-TOURNAMENT",
    DEAL_CARDS_TOURNAMENT:"DEAL-CARDS-TOURNAMENT",
    OTHERS:"OTHERS",
    ERRORS:"ERRORS",
    INI_FIN_GAMES:"INI-FIN-GAMES", 
    RECONNECT_TO_GAME:"RECONNECT-TO-GAME",
    RELOAD_GAME:"RELOAD-GAME",
    DISCONNECT:"DISCONNECT",
    EVENT: "I/O Event",
    LOGIN_EVENTS: 'LOGIN_EVENTS',
    USER_EVENTS: 'USER_EVENTS',
    GAME_EVENTS: 'GAME_EVENTS'
};

let logsEnabled = [true];

let logFlags = [
    [FLAGS.SIGN_IN, true], //0 - true
    [FLAGS.LOG_OUT, true], //1 - false
    [FLAGS.HALL_MESSAGE, true], //2 - false
    [FLAGS.GAME_MESSAGE, true], //3 - true
    [FLAGS.DEAL_CARDS, true], //4 - false
    [FLAGS.BUTTON_EVENT, true], //5 - false
    [FLAGS.ERROR_BUTTON_EVENT, true], //6 - false
    [FLAGS.PLAYERS_CHAIRS, true], //7 - false
    [FLAGS.SOCKET_CONNECTED, true], //8 - false
    [FLAGS.TOURNAMENTS, true], //9 - true
    [FLAGS.GAME_TO_HALL, true], //10 - false
    [FLAGS.OBSERVER_MODE, true], //11 - false
    [FLAGS.GAMES, true], //12 - false
    [FLAGS.PAUSE, true], //13 - true
    [FLAGS.USERS_ONLINE, true], //14 - false
    [FLAGS.TOURNAMENT_FINISHED, true], //15 - true
    [FLAGS.SOCKET_EXPELED, true], //16 - true
    [FLAGS.BUTTON_EVENT_TOURNAMENT, true], //17 - false
    [FLAGS.ERROR_BUTTON_EVENT_TOURNAMENT, true], //18 - false
    [FLAGS.GAMES_TOURNAMENT, true], //19 - false
    [FLAGS.DEAL_CARDS_TOURNAMENT, true], //20 - false
    [FLAGS.OTHERS, true], //21 - true
    [FLAGS.ERRORS, true], //22 - true
    [FLAGS.INI_FIN_GAMES, true], //23 - true
    [FLAGS.RECONNECT_TO_GAME, true], //24 - true
    [FLAGS.RELOAD_GAME, true], //25 - true
    [FLAGS.DISCONNECT, true], //26 - false
    [FLAGS.EVENT, true] //27 - false
];

/************ INI - MÉTODOS DE LOGS ************/
export function writeInfo(tag, type, text){    
    if(logsEnabled){
        if(tag==TAGS.ERROR) tag="*****"+tag+"*****";
        else tag="LOG -";

        let typeEnabled = true;
        for(let i=0; i<logFlags.length; i++){
            if(logFlags[i][0]==type){ 
                typeEnabled = logFlags[i][1];
                break;
            }
        }

        if(typeEnabled) console.log(tag+" "+type+" --> "+text);
    }
}

export function serverHandler(logIndex) {
    const usersOnline = users;
    const _games = games;

    if(logIndex==0) {
        console.log("****SHOW TESTING - Memory****");            
        const _rss = Math.round((process.memoryUsage()["rss"]/1024/1024*100)/100)+ "MB";
        const _heapTotal = Math.round((process.memoryUsage()["heapTotal"]/1024/1024*100)/100)+ "MB";
        const _heapUsed = Math.round((process.memoryUsage()["heapUsed"]/1024/1024*100)/100)+ "MB";
        const _external = Math.round((process.memoryUsage()["external"]/1024/1024*100)/100)+ "MB";
        const _arrayBuffers = Math.round((process.memoryUsage()["arrayBuffers"]/1024/1024*100)/100)+ "MB";
        console.log("--> Memoria: "+_heapUsed+" / "+_heapTotal+"\tRSS: "+_rss+" || HeapTotal: "+_heapTotal+" || HeapUsed: "+_heapUsed+" || External: "+_external+" || ArrayBuffers: "+_arrayBuffers);
        console.log("*********************************");
        
    } else if(logIndex==1) {
        console.log("****SHOW TESTING - UsersOnline: "+usersOnline.length+"****");
        for(let i=0; i<usersOnline.length; i++){
            console.log(".Player "+i+": "+usersOnline[i].name+"\t\t"+usersOnline[i].gameId+"/"+usersOnline[i].playerIndex+"\t||\t"+
                        usersOnline[i].id+" Online: "+usersOnline[i].online+" Pause: "+usersOnline[i].pause);
        }
    } else if(logIndex==2){
        console.log("****SHOW TESTING - Games****");
        for(let i=0; i<_games.length; i++){
            const game = _games[i];
            if (game) {
                console.log("----> Game "+ i + ": " + game.getId() + " - PLAYERS: " + game.TOTAL_PLAYERS);
                console.log("** Started: "+game.isGameStarted() + " | Canceled: "+game.isGameCanceled()+" | Finished: "+game.isGameFinished()
                    +" | Completed: "+game.isGameCompleted()+" | Updated: "+game.isGameCompleted());
                console.log("** NumHand: "+game.getNumHand()+" | NumChance: "+game.getNumChance()+" | NumChancesHand: "+game.getNumChancesHand()
                    +" | HandPlayer: "+game.getHandPlayer()+" | FirstPlayer: "+game.getFirstPlayer());
                console.log("** Players: "+JSON.stringify(game.getPlayers()));
            } else console.log("*******Partida "+i+" --> null");
        }
    } else if(logIndex==3){ 
        //LogsJS.showAllDetailsPlayersLog(socketsOnline, usersOnline, NUM_PLAYERS_IA);
    
    } else if(logIndex==20){
        console.log("****LOGS ENABLED - "+!logsEnabled[0]+"***");
        logsEnabled[0] = !logsEnabled[0];
    } else if(logIndex==21){
        console.log("****LOGS SignIN - "+!logFlags[0][1]+"***");
        logFlags[0][1] = !logFlags[0][1];
    } else if(logIndex==22){
        console.log("****LOGS LogOut - "+!logFlags[1][1]+"***");
        logFlags[1][1] = !logFlags[1][1];
    } else if(logIndex==23){
        console.log("****LOGS HallMessage - "+!logFlags[2][1]+"***");
        logFlags[2][1] = !logFlags[2][1];
    } else if(logIndex==24){
        console.log("****LOGS GameMessage - "+!logFlags[3][1]+"***");
        logFlags[3][1] = !logFlags[3][1];
    } else if(logIndex==25){
        console.log("****LOGS DealCards - "+!logFlags[4][1]+"***");
        logFlags[4][1] = !logFlags[4][1];
    } else if(logIndex==26){
        console.log("****LOGS ButtonEvent - "+!logFlags[5][1]+"***");
        logFlags[5][1] = !logFlags[5][1];
    } else if(logIndex==27){
        console.log("****LOGS ErrorButtonEvent - "+!logFlags[6][1]+"***");
        logFlags[6][1] = !logFlags[6][1];
    } else if(logIndex==28){
        console.log("****LOGS PlayersChairs - "+!logFlags[7][1]+"***");
        logFlags[7][1] = !logFlags[7][1];
    } else if(logIndex==29){
        console.log("****LOGS Socket-Connected - "+!logFlags[8][1]+"***");
        logFlags[8][1] = !logFlags[8][1];    
    } else if(logIndex==30){
        console.log("****LOGS Tournaments - "+!logFlags[9][1]+"***");
        logFlags[9][1] = !logFlags[9][1]; 
    } else if(logIndex==31){
        console.log("****LOGS GameToHall - "+!logFlags[10][1]+"***");
        logFlags[10][1] = !logFlags[10][1];   
    } else if(logIndex==32){
        console.log("****LOGS ObserverMode - "+!logFlags[11][1]+"***");
        logFlags[11][1] = !logFlags[11][1];    
    } else if(logIndex==33){   
        console.log("****LOGS Games - "+!logFlags[12][1]+"***");
        logFlags[12][1] = !logFlags[12][1];   
    } else if(logIndex==34){
        console.log("****LOGS Pause - "+!logFlags[13][1]+"***");
        logFlags[13][1] = !logFlags[13][1];    
    } else if(logIndex==35){
        console.log("****LOGS UsersOnline - "+!logFlags[14][1]+"***");
        logFlags[14][1] = !logFlags[14][1]; 
    } else if(logIndex==36){
        console.log("****LOGS TournamentsFinished - "+!logFlags[15][1]+"***");
        logFlags[15][1] = !logFlags[15][1];     
    } else if(logIndex==37){
        console.log("****LOGS Socket Expeled - "+!logFlags[16][1]+"***");
        logFlags[16][1] = !logFlags[16][1]; 
    } else if(logIndex==38){
        console.log("****LOGS ButtonEventT - "+!logFlags[17][1]+"***");
        logFlags[17][1] = !logFlags[17][1]; 
    } else if(logIndex==39){
        console.log("****LOGS ErrorButtonEventT - "+!logFlags[18][1]+"***");
        logFlags[18][1] = !logFlags[18][1];     
    } else if(logIndex==40){
        console.log("****LOGS GamesT - "+!logFlags[19][1]+"***");
        logFlags[19][1] = !logFlags[19][1]; 
    } else if(logIndex==41){
        console.log("****LOGS DealCardsT - "+!logFlags[20][1]+"***");
        logFlags[20][1] = !logFlags[20][1];
    } else if(logIndex==42){
        console.log("****LOGS OTHERS - "+!logFlags[21][1]+"***");
        logFlags[21][1] = !logFlags[21][1];
    } else if(logIndex==43){
        console.log("****LOGS ERRORS - "+!logFlags[22][1]+"***");
        logFlags[22][1] = !logFlags[22][1];
    } else if(logIndex==44){
        console.log("****LOGS Ini-Fin Games - "+!logFlags[23][1]+"***");
        logFlags[23][1] = !logFlags[23][1];
    } else if(logIndex==45){
        console.log("****LOGS RECONNECT_TO_GAME - "+!logFlags[24][1]+"***");
        logFlags[24][1] = !logFlags[24][1];
    } else if(logIndex==46){
        console.log("****LOGS RELOAD_GAME - "+!logFlags[25][1]+"***");
        logFlags[25][1] = !logFlags[25][1];
    } else if(logIndex==47){
        console.log("****LOGS DISCONNECT - "+!logFlags[26][1]+"***");
        logFlags[26][1] = !logFlags[26][1];
    } else if(logIndex==48){
        console.log("****LOGS EVENT - "+!logFlags[27][1]+"***");
        logFlags[27][1] = !logFlags[27][1];
        
        
    } else if(logIndex==155){
        console.log("****UPDATE RANKING****");
        DatabaseJS.updateSeason();
    } else if(logIndex==157){
        console.log("****UPDATE NEWS****");
        DatabaseJS.updateNews();
    } else if(logIndex==158){
        console.log("****SERVER RESTARTED TO false****");
        Config.setServerRestarted(false);
    } else if(logIndex==159){
        console.log("****SERVER ENABLED - "+!Config.serverEnabled+"****");
        Config.setServerEnabled(!Config.serverEnabled);
    } else if(logIndex==160){
        console.log("****UPDATE GAMES ENABLED - "+!Config.gameUpdatesEnabled+"****");
        Config.setGameUpdatesEnabled(!Config.gameUpdatesEnabled);
    } else if(logIndex==161){
        console.log(`****SET UPDATE RANKING ENABLED ****`);
        DatabaseJS.getRankingData().changeFlagInterval();
    }
    
    console.log("");
}

import Config from '../server/config.js';
import UserJS from '../model/user.js';
import DatabaseJS from '../db/database.js';

import UserIndexJS from './user/userIndex.js';
import LoginEventsJS from './login/loginEvents.js';
import UserEventsJS from './user/userEvents.js';
import GameEventsJS from './game/gameEvents.js';
import GameIndexJS from './game/gameIndex.js';

import { io, users } from '../server/init.js';
import { TAGS, FLAGS, writeInfo, serverHandler } from '../utils/logs.js';

import { getGame, getUserOnlineByName, getUsername, getUserOnline, deleteUser, eoSendMyUser, eoDataObject, isUserOnline } from './utils.js';

const { VERSION, NOT_NAME_PLAYER } = Config;

async function initSocket() {
    io.on('connection', function(socket){
        console.log(`Nuevo socket conectado - ID: ${socket.id}`);
        users.push(new UserJS.User(socket.id, NOT_NAME_PLAYER, 0, false));
        socket.emit(Config.EO_INIT_PLAYER, {id: socket.id, version: VERSION});
    
    /************ INI - EVENTOS DE ENTRADA ************/
        LoginEventsJS.initEvents(socket);
        UserEventsJS.initEvents(socket);
        GameEventsJS.initEvents(socket);

        otherEvents(socket);
        disconnectionEvent(socket);
    });
}

function otherEvents(socket) {
    socket.on(Config.EX_DATA_OBJECT, function(){        
        eoSendMyUser(socket.id);
        eoDataObject(socket.id);
    });

    socket.on(Config.EX_NEWS, function(){
        if (!isUserOnline(socket)) {
            writeInfo(TAGS.LOG, FLAGS.OTHERS,"El usuario ("+socket.id+") no existe. Se procede a expulsarle. - EX_NEWS");
            socket.disconnect();
        } else socket.emit(Config.EX_NEWS, {newsVersion: DatabaseJS.getNewsData().getVersion(), arrayNews: DatabaseJS.getNewsData().getNews()});
    });

    socket.on(Config.EX_PLAYER_RANKING, function(iRanking, iOrder) {
        if (!isUserOnline(socket)) {
            writeInfo(TAGS.LOG, FLAGS.OTHERS,"El usuario ("+socket.id+") no existe. Se procede a expulsarle. - EX_PLAYER_RANKING");
            socket.disconnect();
        } else UserIndexJS.sendPlayerRanking(socket.id, iRanking, iOrder);
    });

    socket.on(Config.EI_USER_PAUSE, function(playerName, pause) {
        if (!isUserOnline(socket)) {
            writeInfo(TAGS.LOG, FLAGS.OTHERS,"El usuario ("+socket.id+") no existe. Se mantiene sin ser expulsado - EI_USER_PAUSE");
            // socket.disconnect();
        } else UserIndexJS.updateUserPause(playerName, pause);
    });

    socket.on(Config.EI_SERVER_HANDLER, function(logIndex) {
        serverHandler(logIndex);
    });

    socket.on(Config.EI_USER_LEAVE, function(fromMenu) {
        const user = getUserOnline(socket.id);
        if (user) writeInfo(TAGS.LOG, FLAGS.DISCONNECT, " El usuario "+user.name+" ha solicitado abandonar. :: "+user.id+" -- "+user.gameId);
        else writeInfo(TAGS.LOG, FLAGS.DISCONNECT, " El usuario *null* ha solicitado abandonar. :: " + socket.id);
        socket.disconnect();
    });
}

function disconnectionEvent(socket) {
    socket.on('disconnect',function() {
        console.log(`Socket desconectado - ID: ${socket.id}`);
        
        let user = getUserOnline(socket.id);
        if (user != false) {
            if (user.name != NOT_NAME_PLAYER) {
                user.online = false;

                const game = getGame(user.gameId);
                if (game) {
                    const chair = user.playerIndex;

                    // Delete game
                    if (!game.isGameStarted() && game.getHostName() === user.name) {
                        GameIndexJS.deleteGame(game);
                    }

                    if (!game.isGameStarted() && game.getHostName() !== user.name) {
                        GameIndexJS.leaveGame(user);
                    }

                    // Delete game (BEFORE 'deleteGame' when it has not finished yet)
                    if (game.isGameStarted() && game.isGameFinished()) {
                        GameIndexJS.updateFinishedGame(user.gameId);
                        GameIndexJS.deleteGame(game);
                    }

                    // Check if there is someone in the game to finish it or wait for it
                    if (game.isGameStarted() && !game.isGameFinished()) {
                        let someOneInGame = false;
                        for (let i=0; i<game.TOTAL_PLAYERS; i++) {
                            const auxUser =  game.getPlayers()[i];
                            const ai = auxUser.isAi();
                            const online = getUserOnlineByName(auxUser.getName()).online;

                            if (!ai && online) someOneInGame = true;
                        }

                        if (someOneInGame) {
                            writeInfo(TAGS.LOG, FLAGS.RECONNECT_TO_GAME, "****** "+getUsername(socket.id)+" espera reconectarse a partida... ID: "+game.getId());
                            user.online = false;

                            for(let i=0; i<game.TOTAL_PLAYERS; i++){
                                const auxPlayer = game.getPlayers()[i];
                                if (auxPlayer && chair!=i) {
                                    io.to(auxPlayer.getId()).emit(Config.EO_PLAYER_GAME_RECONNECTED, {playerOut: true, chair: chair});
                                }
                            }

                            return;
                        } else {
                            game.setGameFinished(true);
                            GameIndexJS.updateFinishedGame(user.gameId);
                            GameIndexJS.deleteGame(game);
                        }
                    }

                    deleteUser(socket.id);
                } else deleteUser(socket.id);
            } else deleteUser(socket.id);
        } else deleteUser(socket.id);
    });
}

/***************************************************************/
const SocketJS = {};
SocketJS.initSocket = initSocket;
export default SocketJS;

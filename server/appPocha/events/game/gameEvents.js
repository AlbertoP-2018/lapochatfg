import Config from '../../server/config.js';
import ButtonEventJS from './buttonEvent.js';
import GameIndexJS from './gameIndex.js';
import DatabaseJS from '../../db/database.js';

import { TAGS, FLAGS, writeInfo } from '../../utils/logs.js';
import { getGame, getUserOnline, eoDataObject, isUserOnline } from '../utils.js';

const { NOT_NAME_PLAYER } = Config;

function initEvents(socket) {
    socket.on(Config.EX_NEW_GAME, function(numPlayers, numBots, mode) {
        const user = getUserOnline(socket.id)
        if (user && user.name!=NOT_NAME_PLAYER) GameIndexJS.createGame(user, numPlayers, numBots, mode);
        else socket.disconnect();
    });

    socket.on(Config.EX_ADD_PLAYER_GAME, function(gameId) {
        const user = getUserOnline(socket.id);
        if (user && user.name!=NOT_NAME_PLAYER) GameIndexJS.joinGame(user, gameId);
        else socket.disconnect();
    });

    socket.on(Config.EX_REMOVE_PLAYER_GAME, function() {
        const user = getUserOnline(socket.id);
        if (user && user.gameId) GameIndexJS.leaveGame(user);
        else socket.disconnect();
    });

    socket.on(Config.EX_GAME_INVITATION, function(playerName, gameId, userName, remainingPlayers) {
        if (!isUserOnline(socket)) {
            writeInfo(TAGS.LOG, FLAGS.OTHERS,"El usuario ("+socket.id+") no existe. Se procede a expulsarle. - EX_GAME_INVITATION");
            socket.disconnect();
        } else GameIndexJS.gameInvitation(playerName, gameId, userName, remainingPlayers);
    });

    socket.on(Config.EX_BUTTON_EVENT, function(
                _gameId, _idButtonEvent, _decisionB,  _player, _answer, 
                _numHand, _numChance, _numChancesHand, _handPlayer,
                _firstPlayer, _scoreboard, callback) {
        ButtonEventJS.eoButtonEvent(socket, _gameId, _idButtonEvent, _decisionB, _player, _answer,
            _numHand, _numChance, _numChancesHand, _handPlayer, _firstPlayer, _scoreboard, callback
        );
    });

    socket.on(Config.EX_RECONNECT_TO_GAME, function(_userName, fromPaused){
        GameIndexJS.reconnectUserToGame(socket.id, _userName, 0, fromPaused);
    });

    socket.on(Config.EX_FINISHED_GAME, function(gameId, _scoreboard){
        const game = getGame(gameId);
        if(game && !game.isGameFinished()){
            game.setGameCompleted(true);
            game.setGameFinished(true);
            game.setScoreboard(JSON.parse(_scoreboard));
            
            GameIndexJS.updateFinishedGame(gameId);
        }
    });

    socket.on(Config.EX_GAME_TO_HALL, function (){
        const user = getUserOnline(socket.id);
        if (user && user.name!=NOT_NAME_PLAYER) {
            const game = getGame(user.gameId);
            if (game) GameIndexJS.deleteGame(game);
            socket.emit(Config.EX_GAME_TO_HALL, {newsVersion: DatabaseJS.getNewsData().getVersion()});
            eoDataObject(socket.id);
        } else {
            socket.emit(Config.EX_NOTICE_SERVER, {title: "Fin de partida", notice: "La partida ha finalizado"});
            socket.disconnect();
        }
    });
}

const GameEventsJS = {};
GameEventsJS.initEvents = initEvents;
export default GameEventsJS;
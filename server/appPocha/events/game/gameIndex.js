import Config from '../../server/config.js';
import GameJS from '../../model/game.js';
import UniqueId from '../../utils/uniqueId.js';
import UserJS from '../../model/user.js';
import LoginJS from '../login/loginIndex.js';

import { io, users, games } from '../../server/init.js';
import { eoDataObject, getGame, getUserOnlineByName, getUserOnline, resetUser, deleteUser, eoSendMyUser } from '../utils.js';
import { TAGS, FLAGS, writeInfo } from '../../utils/logs.js';
import { findUsers, addGame } from '../../db/userDB.js';
import { insertGame } from '../../db/gameDB.js';

const { NOT_NAME_PLAYER } = Config;

function createGame(user, numPlayers, numBots, mode) {
    writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "Creating new game by "+user.name+"...");

    if (user.gameId) {
        writeInfo(TAGS.ERROR, FLAGS.INI_FIN_GAMES, "User " + user.name+" is in game " +  user.gameId);
        eoDataObject(user.id);
        return;
    }

    const newId = UniqueId.newId(UniqueId.Type.Game);
    user.gameId = newId;
    const newGame = new GameJS.Game(newId, user, numPlayers, numBots, mode);
    for (let i = 1; i <= numBots; i++) {
        const bot = new UserJS.User(null, i, 0, true);
        writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "\tCreating new bot: " + bot.name  +" ...");
        bot.gameId = newId;
        users.push(bot);
        newGame.getPlayers().push(bot.getPlayerData());

        LoginJS.eoNewConnectedUser(bot);
    }
    const newGameData = newGame.getNewGameData();
    games.push(newGame);
    
    
    for (let i=0; i<users.length; i++) {
        const user = users[i];
        if(user.name != NOT_NAME_PLAYER){
            const game = getGame(user.gameId);
            if(!game || !game.isGameStarted()){
                io.to(user.id).emit(Config.EX_NEW_GAME, { newGameData });
                continue;
            }
        }
    }

    writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "New game created: " +  newGame.getId());

    if (numPlayers == numBots+1) startGame(newGame);
}

function deleteGame(game){
    if (!game) return;

    writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "Delete game "+game.getId()+"...");

    const started = game.isGameStarted();
    const title = !started ? "Partida cancelada" : "Partida finalizada";
    const text = !started ? "El host canceló la partida" : "La partida ha finalizado";
    const event = !started ? Config.EO_CANCEL_GAME : Config.EX_FINISHED_GAME;

    for (let i=0; i<game.getPlayers().length; i++) {
        const auxUser = game.getPlayers()[i];
        if (started || auxUser.getName() != game.getHostName()) {
            io.to(auxUser.getId()).emit(Config.EX_NOTICE_SERVER, {title: title, notice: text});
        }
        io.to(auxUser.getId()).emit(event);
    }

    resetGame(game);
    
    for (let i=0; i<users.length; i++) {
        const user = users[i];
        if(user.name != NOT_NAME_PLAYER){
            const _game = getGame(user.gameId);
            if(!_game || !_game.isGameStarted()){
                io.to(user.id).emit(Config.EX_DELETE_GAME, { id: game.getId() });
                continue;
            }
        }
    }

    writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "Game deleted: "+game.getId());
}

function joinGame(user, gameId) {
    writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, user.name+" is joining game "+gameId+"...");

    const game = getGame(gameId);
    if (game) {
        const gameStarted = game.isGameStarted();
        const totalPlayers = game.TOTAL_PLAYERS;
        const currentPlayers = game.getPlayers().length;

        if (!gameStarted && (currentPlayers<totalPlayers)) {
            user.gameId = gameId;
            game.addPlayer(user.getPlayerData());
        } else {
            writeInfo(TAGS.ERROR, FLAGS.INI_FIN_GAMES, "Game" + gameId+" is completed. ");
            eoDataObject(user.id);
            return;
        }

        for (let i=0; i<users.length; i++) {
            const auxUser = users[i];
            if(auxUser.name != NOT_NAME_PLAYER){
                const auxGame = getGame(auxUser.gameId);
                if(!auxGame || !auxGame.isGameStarted()){
                    io.to(auxUser.id).emit(Config.EX_ADD_PLAYER_GAME, { id: game.getId(), playerName: user.name });
                    continue;
                }
            }
        }

        writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, user.name+" has joined the game "+gameId);

        if (game.getPlayers().length === totalPlayers) startGame(game);
    } else writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "Game "+user.gameId+" not found.");
}

function gameInvitation(playerName, gameId, userName, remainingPlayers) {
    const user = getUserOnlineByName(userName);
    io.to(user.id).emit(Config.EX_GAME_INVITATION, { playerName: playerName, gameId: gameId, remainingPlayers: remainingPlayers});
}

function leaveGame(user) {
    writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, user.name+" is leaving game "+user.gameId+"...");

    const game = getGame(user.gameId);
    if (game) {
        if (game.getHostName() === user.name) {
            deleteGame(game);
        } else if (game.isUserInGame(user.name)) {
            game.removePlayer(user.name);

            for (let i=0; i<users.length; i++) {
                const auxUser = users[i];
                if(auxUser.name != NOT_NAME_PLAYER){
                    const auxGame = getGame(auxUser.gameId);
                    if(!auxGame || !auxGame.isGameStarted()){
                        io.to(auxUser.id).emit(Config.EX_REMOVE_PLAYER_GAME, { id: game.getId(), playerName: user.name });
                        continue;
                    }
                }
            }
        }

        writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, user.name+" has left the game "+user.gameId);
    } else writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "Game "+user.gameId+" not found.");

    user.gameId = undefined;
}

function resetGame(game){
    if (!game) return;
 
    writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "Reset game " + game.getId() + "...");
    
    for(let i=0; i<game.getPlayers().length; i++){
        const auxPlayer = game.getPlayers()[i];
        if(auxPlayer!=null) resetUser(auxPlayer.getName());
    }

    // Delete offline players and bot players
    for(let i=0; i<game.getPlayers().length; i++){
        const auxPlayer = game.getPlayers()[i];
        const auxUser = getUserOnlineByName(auxPlayer.name);
        if (auxUser && !auxUser.online) deleteUser(auxUser.id);
        if (auxUser && auxUser.ai) deleteUser(auxUser.id);
    }

    
    const id = game.getId();
    const index = games.findIndex(elem => elem.getId() == id);
    if (index != -1) {
        games.splice(index, 1);
        UniqueId.deleteId(UniqueId.Type.Game, id);

        writeInfo(TAGS.INFO, FLAGS.INI_FIN_GAMES, "Game reset: "+id);
    } else writeInfo(TAGS.INFO, FLAGS.INI_FIN_GAMES, "Index of game " + id + " not found.");
}

function startGame(game) {
    if (!game) return;

    writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "Starting game " + game.getId() + "...");

    game.initGame();

    const players = game.getPlayers();
    players.forEach(player => {
        io.to(player.getId()).emit(Config.EX_START_GAME, {game: game.getGameData()});
    });

    for (let i=0; i<users.length; i++) {
        const auxUser = users[i];
        if(auxUser.name != NOT_NAME_PLAYER){
            const auxGame = getGame(auxUser.gameId);
            if(!auxGame || !auxGame.isGameStarted()){
                io.to(auxUser.id).emit(Config.EO_HIDE_STARTED_GAME, { id: game.getId() });
                continue;
            }
        }
    }

    writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "Game started: " + game.getId());
}

/* 
    Tipos de llamadas a reconnectUserToGame(.., .., requestFrom, fromPaused)
        -requestFrom: Integer (0 ó 1)
        -fromPaused:  boolean (true o false)
        
     1.     0, ?        --> ReconnectToGame (desde GSOnline)
     2.     1, false    --> SignIn (desde Login)
     3.     1, true     --> ReloadGame (desde GSOnline-Login-SignIn)
     
     Por lo tanto, siempre que sea 1 y True, será una recarga de partida
    
*/
function reconnectUserToGame(socketId, playerName, requestFrom, fromPaused){
    const auxRequest = (requestFrom==0 ? "GameToGame" : "LoginToGame");

    const user = getUserOnlineByName(playerName);
    if (user != false) {
        const game = getGame(user.gameId);
        if (game) {
            writeInfo(TAGS.LOG, FLAGS.RECONNECT_TO_GAME,"Usuario ("+user.name+" - "+socketId+") Reconectado. "+
                " Mesa:"+user.gameId+" Silla:"+user.playerIndex+". "+auxRequest+"("+fromPaused+")");

            // if (!fromPaused) {

            //Almacena la referencia del usuario actual (sin nombre) para eliminarlo posteriormente
            let auxUser = getUserOnline(socketId);

            //Modifica el usuario conectado previamente para seguir usándolo
            user.online = true;

            //Si es el mismo socket, ha "iniciado sesión" cuando estaba conectado. Inicio de sesión doble con el mismo socket.
            if(user.id != socketId){
                const oldId = user.id;
                user.id = socketId;

                //Se desconecta el ID anterior
                auxUser.id = oldId; //Se establece al usuario actual el ID anterior para eliminar el usuario
                io.to(oldId).emit(Config.EX_NOTICE_SERVER, {title: "Usuario desconectado", notice: "Se inició sesión desde otro dispositivo."});
                deleteUser(oldId);

                //Actualizamos el ID
                if (game.isGameStarted()) game.getPlayers()[user.playerIndex].setId(user.id);
            }

            if (requestFrom == 0) eoSendMyUser(user.id);

            //Partidas
            eoReconnectToGame(socketId, user, game, requestFrom);
            return;
        } else writeInfo(TAGS.LOG, FLAGS.RECONNECT_TO_GAME,"La partida ("+user.gameId+") no existe. Usuario: "+user.name+" ("+socketId+"). "
            +auxRequest+"("+fromPaused+")");
    } else writeInfo(TAGS.LOG, FLAGS.RECONNECT_TO_GAME,"El usuario "+playerName+" ("+socketId+") no existe. "
            +auxRequest+"("+fromPaused+")");
}

//Llamada ÚNICAMENTE desde reconnectUserToGame()
function eoReconnectToGame(socketId, user, game, requestFrom){ //requestFrom: 0-->gameToGame   1-->loginToGame
    if (!user || !game) return;

    //Envía evento de reconexión
    for(let i=0; i<game.getPlayers().length; i++){
        const auxPlayer = game.getPlayers()[i];
        if (auxPlayer.getName() != user.name) io.to(auxPlayer.getId()).emit(Config.EO_PLAYER_GAME_RECONNECTED, {playerOut: false, chair: user.playerIndex });
    }

    eoDataObject(user.id);

    const gameReconnectData = game.getGameReconnectData();
    if (requestFrom == 0) { // Game to game
        io.to(user.id).emit(Config.EX_RECONNECT_TO_GAME, gameReconnectData);
    }

    if (requestFrom == 1) { // Login to game      
        io.to(socketId).emit(Config.EO_LOGIN_TO_GAME, gameReconnectData);
    }

    // if (requestFrom == 1) {
    //     if(!game.isGameStarted()){
    //         writeInfo(TAGS.LOG, FLAGS.GAMES,"eoReconnectToGame() - Reconexión a partida no empezada.\n"+
    //             "Nombre usuario: "+user.name+" - Partida: "+user.gameId+" - Silla: "+user.playerIndex);
    //         eoCancelSettingsGame(socket, _table);
    //         socket.emit(EX_SIGN_IN, {signIn: false, messageError: "Partida en configuración cancelada.\nSalga al menú principal y vuelva a iniciar sesión."});
    //         socket.disconnect();
    //         return;
    //     }

    //     io.to(socket.id).emit(ConstantJS.EO_LOGIN_TO_GAME, {game: game, reconnectData: gameReconnectData});
    // }
}

function updateFinishedGame(gameId) {
    const game = getGame(gameId);

    if (!game || !Config.gameUpdatesEnabled) return;
    if (!game.isGameStarted()) return;
    if (game.isGameCanceled()) return;
    if (game.isGameUpdated()) return;
    if (!game.isGameCompleted()) return;

    writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "Updating game " + game.getId() + "...");

    const mode = game.getMode();
    const scoreboard =  game.getScoreboard();
    const points = [];
    const winningPlayers = [];
    const playersName = [];
    

    let maxPoints = -Infinity;
    for (let i=0; i<game.TOTAL_PLAYERS; i++) {
        points.push(scoreboard[game.getTotalHands()-1][i][0]);
        if (points[i]>maxPoints) maxPoints = points[i];
    }

    for (let i=0; i<game.TOTAL_PLAYERS; i++) {
        const name = game.getPlayers()[i].getName();
        playersName.push(name);
        if (points[i]==maxPoints) winningPlayers.push(name);
    }

    game.setWinningPlayers(winningPlayers);
    saveGame(mode, winningPlayers, maxPoints, playersName);
    writeInfo(TAGS.LOG,FLAGS.GAMES,"\t\t***Partida actualizada en BD (ID: "+game.getId()+")*** Completed:"+game.isGameCompleted()+" Mode:"+mode+" WinningPlayers:"+winningPlayers+" Points: "+maxPoints);
    game.setGameUpdated(true);

    return writeInfo(TAGS.LOG, FLAGS.INI_FIN_GAMES, "Game updated: " + game.getId());
}

async function saveGame(mode, winningPlayers, maxPoints, playersName) {
    const users = await findUsers(playersName);

    if (!users) return;

    await Promise.all(users.map(async (user) => {
        const nWon = winningPlayers.includes(user.name) ? user.nWon_TT + 1 : user.nWon_TT;
        const nLost = !winningPlayers.includes(user.name) ? user.nLost_TT + 1 : user.nLost_TT;
        const points = (nWon * Config.POINTS_FOR_WINNING) + (nLost * Config.POINTS_FOR_LOST);
        const ratio = (nWon == 0 && nLost == 0) ? 0 : nWon / (nWon + nLost);

        const userResult = await addGame(user.name, points, ratio, nWon, nLost);

        if (userResult.modifiedCount == 0) {
            writeInfo(TAGS.ERROR, FLAGS.GAME_EVENTS, `Error saving the game for user ${user.name}`);
            return null;
        }

        const _user = getUserOnlineByName(user.name);
        if (!user) return null;
        _user.points_TT = points;
        _user.ratio_TT = ratio;
        _user.nWon_TT = nWon;
        _user.nLost_TT = nLost;
        eoSendMyUser(_user.id);
    }));

    const gameResult = await insertGame(mode, winningPlayers, maxPoints, playersName);

    if (!gameResult.acknowledged) writeInfo(TAGS.ERROR, FLAGS.GAME_EVENTS, `Error saving the game`);
}

const GameIndex = {};
GameIndex.createGame = createGame;
GameIndex.deleteGame = deleteGame;
GameIndex.gameInvitation = gameInvitation;
GameIndex.joinGame = joinGame;
GameIndex.leaveGame = leaveGame;
GameIndex.reconnectUserToGame = reconnectUserToGame;
GameIndex.updateFinishedGame = updateFinishedGame;
export default GameIndex;

import Config from '../server/config.js';
import LoginJS from './login/loginIndex.js';
import UniqueId from '../utils/uniqueId.js';
import DataJS from '../model/data.js';

import { io, users, games } from '../server/init.js';
import { TAGS, FLAGS, writeInfo } from '../utils/logs.js';

const { NOT_NAME_PLAYER } = Config;

export function eoSendMyUser(_id) {
    let myUser = getUserOnline(_id);
    if (myUser != false) io.to(_id).emit(Config.EO_MY_USER, { myUser:myUser });
}

export function eoDataObject(id) {
    const aDataUser = getArrayUserData();
    const _games = getArrayNewGamesData();
    io.to(id).emit(Config.EX_DATA_OBJECT, { aDataUser:aDataUser, newGamesData: _games });
}

export function deleteUser(_id) {
    const index = users.findIndex(elem => elem.id == _id);
    if (index != -1) {
        const user = users[index];
        users.splice(index, 1);
        if (user && user.ai) UniqueId.deleteId(UniqueId.Type.Bot, _id);
        io.to(_id).emit(Config.EO_FORCED_DISCONNECTION);
        writeInfo(TAGS.LOG, FLAGS.LOG_OUT, `Deleted user ${user.name} :: ${_id} -- Index: ${index}`);
        LoginJS.eoDeleteConnectedUser(user);
    } else writeInfo(TAGS.LOG, FLAGS.LOG_OUT, `User not found with id ${_id}. Error at delete user`);
}

export function getUserOnlineByName(_name) {
    if (_name === NOT_NAME_PLAYER) return false;

    for (let i=0; i<users.length; i++) {
        if (users[i].name == _name) {
            return users[i];
        }
    }
    
    return false;
}

export function getUserOnline(_id) {
    for (let i=0; i<users.length; i++) {
        if (users[i].id == _id) {
            return users[i];
        }
    }

    return false;
}

export function getArrayUserData() {
    const aUserData = [];
    users.forEach(user => aUserData.push(user.getUserData()));    
    return aUserData;
}

export function getNewUserData(user) {
    if (user) {
        const id = user.id;
        const name = user.name;
        const gameId = user.gameId;
        const playerIndex = user.playerIndex;
        const userIcon = user.userIcon;
        const online = user.online;

        return new DataJS.UserData(id, name, gameId, playerIndex, userIcon, online);
    }
}

export function getArrayNewGamesData() {
    const _games = [];
    games.forEach(game => _games.push(game.getNewGameData()));
    return _games;
}

export function getUsername(_id) {
    for (let i=0; i<users.length; i++){
        if (users[i].id == _id) {
            return users[i].name;
        }
    }
}

//Comprueba si el socket está conectado (tiene un nombre de usuario), para cuando se minimiza la aplicación y se expulsa dicho socket
export function isUserOnline(socket) {
    let socketEncontrado = false;
    for (let i=0; i<users.length; i++) {
        if (users[i].id == socket.id) {
            if (users[i].name == NOT_NAME_PLAYER) {
                socketEncontrado = true;
                break;
            } else return true;
        }
    }
    //NO BORRAR
    if (socketEncontrado == false) writeInfo(TAGS.LOG, FLAGS.USERS_ONLINE, 'No se ha encontrado el ID. Nombre de usuario NULL.');
    
    return false;
}

export function resetUser(name) {
    const user = getUserOnlineByName(name);
    if (user != false) { 
        if (!user.ai) {
            user.gameId = undefined;
            user.playerIndex = -1;
        }
        
        writeInfo(TAGS.LOG, FLAGS.EVENT, `Reset user ${user.name}`);
    }
}

export function getGame(id) {
    return games.find(elem => elem.getId() == id);
}

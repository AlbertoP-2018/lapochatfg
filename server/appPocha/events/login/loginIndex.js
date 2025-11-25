import Config from '../../server/config.js';
import DatabaseJS from '../../db/database.js';
import GameIndexJS from '../game/gameIndex.js';
import UserJS from '../../model/user.js';

import { io, users } from '../../server/init.js';
import { getGame, getUserOnlineByName, getUserOnline, deleteUser, getNewUserData } from '../utils.js';
import { TAGS, FLAGS, writeInfo } from '../../utils/logs.js';

import { findUser, userExists, insertUser } from '../../db/userDB.js';

const { serverRestarted, NOT_NAME_PLAYER } = Config;

function getStatus(code) { return `CODE: LE_${code}` }

async function eiSignIn(socketId, userName, pass) {
    try {
        const _user = await findUser(userName);

        if (!_user) return eoSignIn(socketId, '', false, 'El nombre de usuario no existe');   
        if (_user.pass !== pass) return eoSignIn(socketId, '', false, 'Usuario y/o contraseña incorrectos');

        const newUser = new UserJS.User(socketId, userName, _user.userIcon, false);
        newUser.setDataClassifications(_user.position_TT, _user.points_TT, _user.ratio_TT, _user.nWon_TT, _user.nLost_TT);
        return eoSignIn(socketId, userName, true, `Usuario correcto. Cargando datos...`, newUser);
    } catch (e) {
        writeInfo(TAGS.ERROR, FLAGS.LOGIN_EVENTS, `Ocurrió un error inesperado: eiSignIn.\n${e}`);
        eoSignIn(socketId, '', false, `Ocurrió un error inesperado - ${getStatus('02')}`);
        return false;
    }
}

function eoSignIn(socketId, userName, signIn, messageError, objPlayer) {
    if (!signIn) {
        io.to(socketId).emit(Config.EX_SIGN_IN, {signIn: signIn, userName: userName, messageError: messageError });
        return;
    }

    const _size = users.length;
    const _name = objPlayer.name;
    const _id = objPlayer.id;
    writeInfo(TAGS.LOG, FLAGS.SIGN_IN, "SIGN-IN("+_size+"): "+_name+" - "+_id+"\t\t RESTARTED: " + serverRestarted);
    io.to(socketId).emit(Config.EX_SIGN_IN, {signIn: signIn, userName: userName, messageError: messageError });

    const user = getUserOnlineByName(_name); //Previously connected user
    if (user && user.gameId) {
        writeInfo(TAGS.LOG, FLAGS.SIGN_IN, "Usuario "+user.name+" conectado previamente. En partida. Mesa: "+user.gameId);
        GameIndexJS.reconnectUserToGame(socketId, _name, 1, false);
        return;
    }
    if (user && !user.gameId) writeInfo(TAGS.LOG, FLAGS.SIGN_IN, "Usuario "+user.name+" conectado previamente.");
    if (user && socketId != user.id) deleteUser(user.id);

    //Se obtiene el objeto user creado al unirse al servidor (con id y nombre --null--) y sincronizan los datos con los de la BD
    const auxUser = getUserOnline(socketId);
    if (auxUser != false) {
        auxUser.synchroniseDataPlayers(objPlayer);
        eoLoginToHall(socketId);
        eoNewConnectedUser(auxUser);
    } else deleteUser(socketId);
}

async function eiSignUp(socketId, userName, pass) {
    try {
        const regExName = new RegExp(['^', userName, '$'].join(''), 'i');
        const exists = await userExists(regExName);
        if (exists) return eoSignUp(socketId, false, 'El nombre de usuario ya existe');

        const newUser = await insertUser(userName, pass);
        if (!newUser) return eoSignUp(socketId, false, `Ocurrió un error inesperado - ${getStatus('03')}`);

        eoSignUp(socketId, true, 'Usuario registrado');
    } catch (e) {
        writeInfo(TAGS.ERROR, FLAGS.LOGIN_EVENTS, `Ocurrió un error inesperado - eiSignUp.\n${e}`);
        eoSignUp(socketId, false, `Ocurrió un error inesperado - ${getStatus('04')}`);
        return false;
    }
}

function eoSignUp(socketId, signUp, messageError){
    io.to(socketId).emit(Config.EX_SIGN_UP, { signUp: signUp, messageError: messageError });
}

function eoLoginToHall(_id) {
    io.to(_id).emit(Config.EO_LOGIN_TO_HALL, {newsVersion: DatabaseJS.getNewsData().getVersion()});
}

function eoNewConnectedUser(user) {
    if (!user) return;

    const newUserData = getNewUserData(user);
    for (let i=0; i<users.length; i++) {
        const userOnline = users[i];
        if (userOnline) {
            if (userOnline.name != NOT_NAME_PLAYER && userOnline.name != user.name) {
                const game = getGame(userOnline.gameId);
                if (!game || !game.isGameStarted()) {
                    io.to(userOnline.id).emit(Config.EO_ADD_USER, {newUser: newUserData});
                }
            }
        }
    }
}

function eoDeleteConnectedUser(user) {
    if (!user) return;

    for (let i=0; i<users.length; i++) {
        const userOnline = users[i];
        if (userOnline) {
            if (userOnline.name != NOT_NAME_PLAYER && userOnline.name != user.name) {
                const game = getGame(userOnline.gameId);
                if (!game || !game.isGameStarted()) {
                    io.to(userOnline.id).emit(Config.EO_DELETE_USER, {userName: user.name});
                }
            }
        }
    }
}

const LoginEvents = {};
LoginEvents.eiSignIn = eiSignIn;
LoginEvents.eoSignIn = eoSignIn;
LoginEvents.eiSignUp = eiSignUp;
LoginEvents.eoSignUp = eoSignUp;
LoginEvents.eoNewConnectedUser = eoNewConnectedUser;
LoginEvents.eoDeleteConnectedUser = eoDeleteConnectedUser;
export default LoginEvents;

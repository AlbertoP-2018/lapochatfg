import Config from '../../server/config.js';
import LoginIndexJS from './loginIndex.js'

import { TAGS, FLAGS, writeInfo } from '../../utils/logs.js';

function initEvents(socket) {
    socket.on(Config.EX_SIGN_IN, function(playerName, pass, admin) {
        if (!Config.serverEnabled && !admin) {
            writeInfo(TAGS.LOG, FLAGS.EVENT, `Server is NOT enabled ---> Socket: ${socket.id}`);
            socket.emit(Config.EO_SERVER_NOT_AVAILABLE, {available: false, message: 'Nueva actualización en curso.\nInténtelo más tarde.'});
        } else LoginIndexJS.eiSignIn(socket.id, playerName, pass);
    });

    socket.on(Config.EX_SIGN_UP, function(playerName, pass, admin) {
        if (!Config.serverEnabled && !admin) {
            writeInfo(TAGS.LOG, FLAGS.EVENT, `Server is NOT enabled ---> Socket: ${socket.id}`);
            socket.emit(Config.EO_SERVER_NOT_AVAILABLE, {available: false});
        } else LoginIndexJS.eiSignUp(socket.id, playerName, pass);
    });
}

const LoginEventsJS = {}
LoginEventsJS.initEvents = initEvents;
export default LoginEventsJS;
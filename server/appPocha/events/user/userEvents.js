import Config from '../../server/config.js';
import UserIndexJS from './userIndex.js';

import { TAGS, FLAGS, writeInfo } from '../../utils/logs.js';
import { isUserOnline } from '../utils.js';

function initEvents(socket) {
    socket.on(Config.EX_USER_ICON, function(userName, icon) {
        if (!isUserOnline(socket)) {
            writeInfo(TAGS.LOG, FLAGS.OTHERS, `El usuario (${socket.id}) no existe. Se procede a expulsarle. - EX_USER_ICON`);
            socket.disconnect();
        } else UserIndexJS.eiUpdateIcon(userName, icon);
    });
}

const UserEventsJS = {};
UserEventsJS.initEvents = initEvents;
export default UserEventsJS;
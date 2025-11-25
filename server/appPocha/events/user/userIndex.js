import DatabaseJS from '../../db/database.js';
import Config from '../../server/config.js';

import { io, users } from '../../server/init.js';
import { getGame, getUserOnlineByName } from '../utils.js';
import { TAGS, FLAGS, writeInfo } from '../../utils/logs.js';
import { updateIcon } from '../../db/userDB.js';

const { NOT_NAME_PLAYER } = Config;

async function eiUpdateIcon(userName, icon) {
    try {
        const updated = await updateIcon(userName, icon);
        if (updated) eoUpdateIcon(userName, icon);
    } catch (e) {
        writeInfo(TAGS.ERROR, FLAGS.USER_EVENTS, `Ocurrió un error inesperado: eiUpdateIcon`);
        return false;
    }
}

function eoUpdateIcon(userName, icon) {
    const iconUser = getUserOnlineByName(userName);

    if (!iconUser) return;
    iconUser.userIcon = icon;

    users.forEach(user => {
        if (user.name !== NOT_NAME_PLAYER) {
            const game = getGame(user.gameId);
            if (!game || !game.isGameStarted()) {
                io.to(user.id).emit(Config.EX_USER_ICON, { playerName: userName, icon: icon });
            }
        }
    });
}

function sendPlayerRanking(id, iRanking, iOrder) {
    let listPlayerRanking = [];
    if (iRanking === (Config.LAST_SEASON-1)) listPlayerRanking = DatabaseJS.getRankingData().getLastSeasons(iRanking, iOrder);
    else listPlayerRanking = DatabaseJS.getRankingData().getCurrentSeason(iOrder); 

    io.to(id).emit(Config.EX_PLAYER_RANKING, 
        {iRanking: iRanking, iOrder: iOrder, lastRankingUpdate: DatabaseJS.getRankingData().getDateText(), listPlayerRanking: listPlayerRanking});
}

function updateUserPause(playerName, pause) {
    const user = getUserOnlineByName(playerName);
    if (user) user.pause = pause;
}

const UserEvents = {};
UserEvents.eiUpdateIcon = eiUpdateIcon;
UserEvents.eoUpdateIcon = eoUpdateIcon;
UserEvents.sendPlayerRanking = sendPlayerRanking;
UserEvents.updateUserPause = updateUserPause;
export default UserEvents;

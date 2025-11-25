import { database } from '../server/init.js';

const GAMES_COLLECTION = "games";

export async function insertGame(mode, winningPlayers, maxPoints, playersName) {
    return database.collection(GAMES_COLLECTION).insertOne({
        'date': new Date(), 'mode': mode, 'winningPlayers': winningPlayers,
        'maxPoints': maxPoints, 'playersName': playersName
    });
}

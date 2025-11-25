import { database } from '../server/init.js';

const USERS_COLLECTION = "users";

export async function findUser(userName) {
    return database.collection(USERS_COLLECTION).findOne({ name: userName });
}

export async function findUsers(players) {
    return database.collection(USERS_COLLECTION).find(
        { name: {$in: players} }, 
        { projection: { name: 1, nWon_TT: 1, nLost_TT: 1 } }
    ).toArray();
}

export async function addGame(userName, points, ratio, nWon, nLost) {
    return database.collection(USERS_COLLECTION).updateOne(
        { name: userName }, 
        { $set: { 
            position_TT: 0, points_TT: points,
            ratio_TT: ratio, nWon_TT: nWon,  nLost_TT: nLost 
        }
    });
}

export async function userExists(userName) {
    const count = await database.collection(USERS_COLLECTION).countDocuments({ name:userName });
    return count >= 1;
}

export async function insertUser(userName, pass) {
    const result = await database.collection(USERS_COLLECTION).insertOne({
        'name': userName, 'pass': pass, 'userIcon': 0,
        'dateSignUp':new Date(), 'dateLastAccess': null,
        'position_TT': 0, 'points_TT': 0, 'ratio_TT': 0, 'nWon_TT': 0, 'nLost_TT': 0
    });

    return result.acknowledged;
}

export async function updateIcon(userName, icon) {
    const result = await database.collection(USERS_COLLECTION).updateOne({ name: userName }, { $set: {userIcon: icon} });
    return result.modifiedCount >= 1;
}

export async function getSeasonUsers(param) {
    return database.collection(USERS_COLLECTION).find(
        {}, 
        { projection: { name: 1, points_TT: 1, ratio_TT: 1, nWon_TT: 1, nLost_TT: 1 } }
    ).sort( {[`${param}_TT`]: -1} ).toArray();
}

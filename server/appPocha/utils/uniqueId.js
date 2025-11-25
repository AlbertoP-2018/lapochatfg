import { TAGS, FLAGS, writeInfo } from '../utils/logs.js';

const charset = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopkrstuvwxyz01234567890';
const allBotIDs = new Set();
const allGameIDs = new Set();

const Type = Object.freeze({ Bot: 'Bot', Game: 'Game' });

function newId(type) {
    const { array, size } = getDataType(type);

    while (true) {
        let randomID = '';
        for (let i=0; i<size; i++) {
            randomID += charset.charAt(Math.floor(Math.random()*charset.length));
        }

        if (!array.has(randomID)) {
            array.add(randomID);
            return randomID;
        }
    }
}

function deleteId(type, id) {
    const { array } = getDataType(type);
    array.delete(id);
    writeInfo(TAGS.LOG, FLAGS.OTHERS, `${type} id ${id} deleted`);
}

function getDataType(type) {
    let array, size;
    switch (type) {
        case Type.Bot:
            array = allBotIDs;
            size = 4;
            break;
        case Type.Game:
            array = allGameIDs;
            size = 9;
            break;
    }

    return { array, size }
}

const UniqueId = {};
UniqueId.Type = Type;
UniqueId.newId = newId;
UniqueId.deleteId = deleteId;
export default UniqueId;

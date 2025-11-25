import express from 'express';
import http from 'http';
import { Server } from 'socket.io';
import { MongoClient } from 'mongodb';
import Config from './config.js';

export { io, listener, database, users, games };

const DDBB_PASS = process.env.DDBB_PASS || "1234";

let io, listener, database, users, games;

async function initData() {
    users = [];
    games = [];

    const app = express();
    const server = http.createServer(app);
    io = new Server(server);
    listener = server.listen(Config.PORT);

    database = await initDatabase();

    return listener.address().port;
}

async function initDatabase() {
    const config = Config.REMOTE_DATABASE ? JSON.parse(process.env.APP_CONFIG) : undefined;
    const nameDB = Config.REMOTE_DATABASE ? config.mongo.db : 'dbPruebaPocha';
    const uri = Config.REMOTE_DATABASE ? `mongodb://${config.mongo.user}:${encodeURIComponent(DDBB_PASS)}@${config.mongo.hostString}` : 'mongodb://127.0.0.1:27017';

    try {
        const mongoClient = new MongoClient(uri);
        await mongoClient.connect();
        const database = mongoClient.db(nameDB);
        console.log('Mongo Database - Successful connection!');
        return database;
    } catch (e) {
        console.error(e);
        console.log('Mongo Database - ** ERROR **');
    }
}

const Model = {};
Model.initData = initData;
export default Model;

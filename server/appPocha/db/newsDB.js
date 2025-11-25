import { database } from '../server/init.js';

const NEWS_COLLECTION = "news";

export async function getNews() {
    return database.collection(NEWS_COLLECTION).findOne({name:'-news-'});
}

import DataJS from '../model/data.js';

import { getNews } from './newsDB.js';
import { getSeasonUsers } from './userDB.js';

const newsData = new DataJS.NewsData();
const rankingData = new DataJS.RankingData();

const Order = Object.freeze({
    POINTS: 'points',
    RATIO: 'ratio'
});

async function initData() {
    updateNews();
    updateSeason();
    setInterval(() => { if (rankingData.isFlagInterval()) updateSeason(); }, 5*60*1000);
}

/************************************************/
async function updateNews(){
    const result = await getNews();

    newsData.setVersion(result.version);
    newsData.setNews(result.data);

    console.log(`*NOTICIAS ACTUALIZADAS: ${newsData.getVersion()}*`);
}

async function updateSeason() {
    updateCurrentSeason(Order.POINTS);
    updateCurrentSeason(Order.RATIO);   
    rankingData.updateDate();
}

async function updateCurrentSeason(order) {
    const users = await getSeasonUsers(order);

    const ranking = [];
    users.every(user => {
        const nGames = (user.nWon_TT + user.nLost_TT);
        if (order == Order.POINTS && nGames == 0) return true;
        if (order == Order.RATIO && nGames < 20) return true;

        ranking.push(new DataJS.RankingUserData(user.name, user.points_TT, user.ratio_TT, user.nWon_TT, user.nLost_TT));
        return ranking.length < 100; // Return 'true' to continue, 'false' to break
    });

    const index = Object.values(Order).indexOf(order);
    rankingData.setCurrentSeason(index, ranking);

    console.log(`*Temporada actual OrderBy: ${order}.  ACTUALIZADA* --> Tamaño: ${ranking.length}`);
}

/************ FIN - MÉTODOS PRIVADOS ************/

function getNewsData() { return newsData; }
function getRankingData() { return rankingData; }

/******************************************/
const DatabaseJS = {}
DatabaseJS.initData = initData;
DatabaseJS.updateNews = updateNews;
DatabaseJS.getNewsData = getNewsData;
DatabaseJS.getRankingData = getRankingData;
DatabaseJS.updateSeason = updateSeason;


export default DatabaseJS;

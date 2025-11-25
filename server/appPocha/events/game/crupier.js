import { TAGS, FLAGS, writeInfo } from '../../utils/logs.js';

/* 
Phase 0: Reparto INICIO_PARTIDA
Phase 1: Reparto INICIO_RONDA
*/
function dealCards(game){
    let indexCard, valueCard, valueTrump;

    game.initCardsPile();

    const numHands = game.getNumHand();
    const numCards = game.getNumChancesHand();
    for(let iPlayer=0; iPlayer<game.TOTAL_PLAYERS; iPlayer++){
        for(let iCard=0; iCard<numCards; iCard++){
            indexCard = Math.floor(Math.random()*game.getCardsPile().length);
            valueCard = game.getCardsPile()[indexCard];

            game.getPlayers()[iPlayer].setCard(iCard, valueCard, false);
            game.getCardsPile().splice(indexCard,1);
        }
    }

    if (numHands<=(game.getTotalHands()-game.TOTAL_PLAYERS)) {
        const indexTrump = Math.floor(Math.random()*game.getCardsPile().length);
        valueTrump = game.getCardsPile()[indexTrump];
        game.getCardsPile().splice(indexTrump,1);
    } else valueTrump = valueCard;

    game.setTrump(valueTrump);
    
    //Muestra las cartas en el servidor
    let dealCards = "\tMesa:"+game.getId()+"\t\t";
    for(let numPlayer=0; numPlayer<game.TOTAL_PLAYERS; numPlayer++){
        dealCards = dealCards+game.getPlayerName(numPlayer)+"-->";
        for(let numCard=0; numCard<game.getNumMaxCards(); numCard++){
            dealCards = dealCards+getCard(game.getPlayers()[numPlayer].gCardsData()[0][numCard]);
        }
        dealCards = dealCards+"\t";
    }
    dealCards = dealCards+"\tTrump: "+getCard(game.getTrump());
    writeInfo(TAGS.LOG, FLAGS.DEAL_CARDS,dealCards);

    return game;
}

/************ INI - MÉTODOS PRIVADOS ************/
function getCard(indexCard){
    let carta = "?";
    if(indexCard==0 || indexCard==10 || indexCard==20 || indexCard==30) carta="A";
    if(indexCard==1 || indexCard==11 || indexCard==21 || indexCard==31) carta="2";
    if(indexCard==2 || indexCard==12 || indexCard==22 || indexCard==32) carta="3";
    if(indexCard==3 || indexCard==13 || indexCard==23 || indexCard==33) carta="4";
    if(indexCard==4 || indexCard==14 || indexCard==24 || indexCard==34) carta="5";
    if(indexCard==5 || indexCard==15 || indexCard==25 || indexCard==35) carta="6";
    if(indexCard==6 || indexCard==16 || indexCard==26 || indexCard==36) carta="7";
    if(indexCard==7 || indexCard==17 || indexCard==27 || indexCard==37) carta="S";
    if(indexCard==8 || indexCard==18 || indexCard==28 || indexCard==38) carta="Q";
    if(indexCard==9 || indexCard==19 || indexCard==29 || indexCard==39) carta="R";
    
    if(indexCard>=0 && indexCard<=9) carta=carta+"O-";
    if(indexCard>=10 && indexCard<=19) carta=carta+"C-";
    if(indexCard>=20 && indexCard<=29) carta=carta+"E-";
    if(indexCard>=30 && indexCard<=39) carta=carta+"B-";
    
    return carta;
}

function getCardValue(indexCard) {
    if(indexCard==0 || indexCard==10 || indexCard==20 || indexCard==30) return 10;
    if(indexCard==1 || indexCard==11 || indexCard==21 || indexCard==31) return 1;
    if(indexCard==2 || indexCard==12 || indexCard==22 || indexCard==32) return 9;
    if(indexCard==3 || indexCard==13 || indexCard==23 || indexCard==33) return 2;
    if(indexCard==4 || indexCard==14 || indexCard==24 || indexCard==34) return 3;
    if(indexCard==5 || indexCard==15 || indexCard==25 || indexCard==35) return 4;
    if(indexCard==6 || indexCard==16 || indexCard==26 || indexCard==36) return 5;
    if(indexCard==7 || indexCard==17 || indexCard==27 || indexCard==37) return 6;
    if(indexCard==8 || indexCard==18 || indexCard==28 || indexCard==38) return 7;
    if(indexCard==9 || indexCard==19 || indexCard==29 || indexCard==39) return 8;
}

function getCardTrump(indexCard) {
    if(indexCard>=0 && indexCard<=9) return 0; // Oros
    if(indexCard>=10 && indexCard<=19) return 1; // Copas
    if(indexCard>=20 && indexCard<=29) return 2; // Espadas
    if(indexCard>=30 && indexCard<=39) return 3; // Bastos
}
/************ FIN - MÉTODOS PRIVADOS ************/

/***********************************************/
const CrupierJS = {}
CrupierJS.dealCards = dealCards;
CrupierJS.getCardValue = getCardValue;
CrupierJS.getCardTrump = getCardTrump;
export default CrupierJS;

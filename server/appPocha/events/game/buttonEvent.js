import GameJS from '../../model/game.js';
import CrupierJS from './crupier.js';
import Config from '../../server/config.js';

import { io } from '../../server/init.js';
import { getUserOnline, getGame } from '../utils.js';
import { TAGS, FLAGS, writeInfo } from '../../utils/logs.js';

const { NOT_NAME_PLAYER } = Config;

function eoButtonEvent(socket, _gameId, _idButtonEvent, _decisionB,  _indexPlayer, _answer,
        _numHand, _numChance, _numChancesHand, _handPlayer,
        _firstPlayer, _scoreboard, callback) {

    if (typeof callback == 'function') callback("ack");
    else writeInfo(TAGS.LOG, FLAGS.ERROR_BUTTON_EVENT, `** Error: Callback is not a function... Table: ${_gameId} :: Player ${namePlayer} :: IdButton ${_idButtonEvent}`);

    const user = getUserOnline(socket.id);
    if (!user || user.name==NOT_NAME_PLAYER) {
        socket.emit(Config.EX_NOTICE_SERVER, {title: "Fin de partida", notice: "La partida ha finalizado"});
        writeInfo(TAGS.LOG, FLAGS.OTHERS, `El usuario (${socket.id}) no existe o no tiene nombre - EX_BUTTON_EVENT`);
        return;
    }

    const game = getGame(_gameId);
    if(!game) {
        socket.emit(Config.EX_NOTICE_SERVER, {title: "Fin de partida", notice: "La partida ha finalizado"});
        writeInfo(TAGS.LOG, FLAGS.OTHERS, `El usuario (${user.name}) envió un evento a una mesa nula (${_gameId})  - EX_BUTTON_EVENT `);
        return;
    }

    let cards = null;
    let trump = null;

    const namePlayer = game.getPlayers()[_indexPlayer].getName();

    let sizeArrayBE = game.getArrayEvents().length;
    let sameEvent = false;
    if(sizeArrayBE>0){
        sameEvent = (
            game.getArrayEvents()[sizeArrayBE-1].decisionB==_decisionB && 
            game.getArrayEvents()[sizeArrayBE-1].player==_indexPlayer && 
            game.getArrayEvents()[sizeArrayBE-1].answer==_answer
        );
    }

    const decisionName = getDecisionButtonName(_decisionB);
    if(_idButtonEvent==(sizeArrayBE+1) && !sameEvent){              
        writeInfo(TAGS.LOG, FLAGS.BUTTON_EVENT, " ********** Mesa: "+_gameId+" :: Player: "+namePlayer+" (By:"+user.name+") *************** ");
        writeInfo(TAGS.LOG, FLAGS.BUTTON_EVENT, " ** ID:  "+_idButtonEvent+" :: Decision: "+decisionName+ " :: Answer: "+_answer);
        writeInfo(TAGS.LOG, FLAGS.BUTTON_EVENT, " ********************************************************");

        let objButtonEvent = new GameJS.ButtonEvent(_idButtonEvent, _decisionB, _indexPlayer, _answer);

        game.getArrayEvents().push(objButtonEvent);

        //Se actualiza el marcador
        game.setScoreboard(JSON.parse(_scoreboard));
        game.setFirstPlayer(_firstPlayer);

        if (_decisionB==1) { // Phase: CARD
            game.getPlayers()[_indexPlayer].setSelectedCard(_answer, true);
            game.addNumSelection();

            if (game.getNumSelection() == game.TOTAL_PLAYERS) {
                if(game.getNumChance()!=game.getNumChancesHand()) {
                    game.initChance();
                }
                game.resetSelection();
            }
        }

        if (_decisionB==3 ) { // Phase: CONTINUE
            game.addNumContinues();

            if(game.getNumContinues()==game.TOTAL_PLAYERS){
                game.initHand();
                //game = CrupierJS.dealCards(game, 1);
                //CrupierJS.dealCards(game, 1);
                CrupierJS.dealCards(game);
                cards = game.getCards();
                trump = game.getTrump();
                game.resetContinues();
            }
        }

        //Se envía el evento
        for(let i=0; i<game.TOTAL_PLAYERS; i++){
            io.to(game.getPlayerId(i)).emit(Config.EX_BUTTON_EVENT, {
                idButtonEvent: _idButtonEvent, decisionB: _decisionB, player: _indexPlayer, answer: _answer, trump: trump, cards: cards});
        }
    } else {
        if ( (_idButtonEvent+1) == (sizeArrayBE+1) && user.name != namePlayer) {
            writeInfo(TAGS.LOG, FLAGS.ERROR_BUTTON_EVENT, _gameId+" - Respuesta por timeOut o AI.");
        } else {
            writeInfo(TAGS.LOG, FLAGS.ERROR_BUTTON_EVENT, _gameId+" - ID: "+_idButtonEvent+" de "+(sizeArrayBE+1)+
            " - "+decisionName+": "+_answer+" - Socket: "+user.name+" - Jugador del evento: "+namePlayer);
        }
    }
}

function getDecisionButtonName(index) {
    if (index == 0) return "BET";
    if (index == 1) return "CARD";
    if (index == 2) return "WAITING";
    if (index == 3) return "CONTINUE";
    return "-NULL-";
}

const ButtonEvent = {};
ButtonEvent.eoButtonEvent = eoButtonEvent;
export default ButtonEvent;

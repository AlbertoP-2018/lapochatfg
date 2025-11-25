import DataJS from '../model/data.js';
import UniqueId from '../utils/uniqueId.js';

class User {
    constructor(id, name, userIcon, ai){
        //Constructor
        this.id = !ai ? id : UniqueId.newId(UniqueId.Type.Bot);
        this.name = !ai ? name : `Bot_${this.id}`; //_${name}`;
        this.userIcon = userIcon;
        this.ai = ai;

        //Atributos de partida
        this.gameId = undefined;
        this.playerIndex = -1;
        this.pause = false;

        //Otros atributos
        this.online = true;

        //Database
        this.position_TT = 0;
        this.points_TT = 0;
        this.ratio_TT = 0;
        this.nWon_TT = 0;
        this.nLost_TT = 0;
    }

    setDataClassifications(position_TT, points_TT, ratio_TT, nWon_TT, nLost_TT){
        this.position_TT = position_TT;
        this.points_TT = points_TT;
        this.ratio_TT = ratio_TT;
        this.nWon_TT = nWon_TT;
        this.nLost_TT = nLost_TT;
    }

    //Llamado cuando el usuario inicia sesión y ya estaba conectado
        //Si se encuentra en partida no se llama a este método
    synchroniseDataPlayers(objPlayer){
        this.name = objPlayer.name;
        this.gameId = objPlayer.gameId;
        this.playerIndex = objPlayer.playerIndex;
        this.userIcon = objPlayer.userIcon;
        this.ai = objPlayer.ai;
        
        //Temporada actual
        this.position_TT = objPlayer.position_TT;  this.points_TT = objPlayer.points_TT;      this.ratio_TT = objPlayer.ratio_TT;
        this.nWon_TT = objPlayer.nWon_TT;          this.nLost_TT = objPlayer.nLost_TT;
    }

    getUserData() {
        return new DataJS.UserData(this.id, this.name, this.gameId, this.playerIndex, this.userIcon, this.online);
    }

    getPlayerData() {
        return new DataJS.PlayerData(this.id, this.name, this.gameId, this.playerIndex, this.userIcon, this.ai);
    }
}

const UserJS = {}
UserJS.User = User;
export default UserJS;

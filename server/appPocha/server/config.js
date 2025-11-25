const Config = {};

/********************************* CONSTANTS *********************************/
Config.REMOTE_DATABASE = false;
Config.PORT = process.env.PORT || 8080;
Config.VERSION = "v2.0.0";
Config.LAST_SEASON = 0

Config.NOT_NAME_PLAYER = '--null--';
Config.NOT_IDSERVER_PLAYER = '--idServerNull--';
Config.POINTS_FOR_WINNING = 5;
Config.POINTS_FOR_LOST = 1;

/********************************* GLOBAL *********************************/
Config.serverEnabled = true;
Config.setServerEnabled = function (value) {
    Config.serverEnabled = value;
}

Config.gameUpdatesEnabled = true;
Config.setGameUpdatesEnabled = function (value) {
    Config.gameUpdatesEnabled = value;
}

Config.serverRestarted = true;
Config.setServerRestarted = function (value) {
    Config.serverRestarted = value;
}

/********************************* EVENTS ************************************/

Config.EO_SERVER_NOT_AVAILABLE = 'notAvailable';
Config.EO_FORCED_DISCONNECTION = 'forcedDisconnection';
Config.EO_INIT_PLAYER = 'initPlayer';
Config.EI_SERVER_HANDLER = 'serverHandler';

/** Others **/
Config.EX_DATA_OBJECT = 'dataObject';
Config.EI_USER_LEAVE = 'userLeave';

/** Login **/
Config.EX_SIGN_IN = 'signIn';
Config.EX_SIGN_UP = 'signUp';
Config.EO_LOGIN_TO_HALL = 'loginToHall';
Config.EO_LOGIN_TO_GAME = 'loginToGame';
Config.EO_ADD_USER = 'addUser';
Config.EO_DELETE_USER = 'deleteUser';

/** User **/
Config.EO_MY_USER = 'myUser';
Config.EX_USER_ICON = 'updateUserIcon';
Config.EI_USER_PAUSE = 'userPause';
Config.EX_GAME_INVITATION = 'gameInvitation';

/** Hall **/
Config.EX_NEWS = 'news';
Config.EX_NOTICE_SERVER = 'noticeServer';
Config.EX_PLAYER_RANKING = 'playerRanking';

/** Game **/
Config.EX_NEW_GAME = 'newGame';
Config.EX_DELETE_GAME = 'deleteGame';
Config.EX_ADD_PLAYER_GAME = 'addPlayerGame';
Config.EX_REMOVE_PLAYER_GAME = 'removePlayerGame';

Config.EX_START_GAME = 'startGame';
Config.EO_HIDE_STARTED_GAME = 'hideStartedGame';

Config.EO_GAME_DATA = 'gameData';
Config.EO_CANCEL_GAME = 'cancelGame';
Config.EX_BUTTON_EVENT = 'buttonEvent';
Config.EO_PLAYER_GAME_RECONNECTED = 'playerGameReconnected';
Config.EX_RECONNECT_TO_GAME = 'reconnectToGame';  
Config.EX_FINISHED_GAME = 'finishedGame';

Config.EX_GAME_TO_HALL = 'gameToHall';

export default Config;

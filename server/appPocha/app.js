import Config from './server/config.js';
import Init from './server/init.js';
import SocketJS from './events/socket.js';
import DatabaseJS from './db/database.js';

async function initApp() {
    const _port = await Init.initData();
    await SocketJS.initSocket();
    await DatabaseJS.initData();

    console.log('\x1b[36m%s\x1b[0m',"\t\t\t*********************************************") ;
    console.log('\x1b[36m%s\x1b[0m',"\t\t\t*********************************************") ;
    console.log('\x1b[36m%s\x1b[0m',"\t\t\t***** SERVER POCHA ONLINE IS NOW RUNNING ****") ;
    console.log('\x1b[36m%s\x1b[0m',"\t\t\t*********************************************") ;
    console.log('\x1b[36m%s\x1b[0m',"\t\t\t*********************************************") ;
    console.log('\t\t\t**PORT: ' + _port);
    console.log('\t\t\t**JS VERSION: ' + Config.VERSION);
    console.log('\t\t\t**SERVER ENABLED: ' + Config.serverEnabled);
    console.log('\t\t\t*********************************************');
    console.log('\n');
}

initApp();

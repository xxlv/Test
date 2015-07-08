
var mysql = require('mysql');
var conn = mysql.createConnection({
    host: '127.0.0.1',
    user: 'root',
    password: '',
    database:'mysql',
    port: 3306
});
conn.connect();
conn.query('show tables', function(err, rows, fields) {
    if (err) throw err;
    //do something
});
conn.end();

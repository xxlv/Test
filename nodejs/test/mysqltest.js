
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
 	name="Tables_in_"+"mysql";   
    console.log(name);
    console.log(rows[0].'Tables_in_mysql');
    console.log(rows[1].name);


});
conn.end();

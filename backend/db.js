const mysql = require('mysql2')

const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'ja123@mysql',
    database: 'notaria'
});

db.connect((err) => {
    if (err) throw err;
    console.log("Conexion hecha con el mysql")
});

module.exports = db;

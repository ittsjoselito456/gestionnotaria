const express = require('express');
const bodyParser = require('body-parser');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const db = require('./db');

const app = express();

app.use(bodyParser.json());

// Aqui registramos usuario mediante los datos enviados por post
app.post('/register', (req, res) => {
    const { nombre, apellido, email, password, rol } = req.body;
    const hashedPassword = bcrypt.hashSync(password, 8);
    let sql = 'INSERT INTO Usuarios (nombre, apellido, email, password, rol) VALUES (?, ?, ?, ?, ?)';
    db.query(sql, [nombre, apellido, email, hashedPassword, rol], (err, result) => {
        if (err) throw err;
        res.send('Usuario registrado');
    });
});

// Aqui enviamos los datos por post de inicio de sesion
app.post('/login', (req, res) => {
    const { email, password } = req.body;
    let sql = 'SELECT * FROM Usuarios WHERE email = ?';
    db.query(sql, [email], (err, results) => {
        if (err) throw err;
        if (results.length > 0) {
            const user = results[0];
            const isMatch = bcrypt.compareSync(password, user.password);
            if (isMatch) {
                const token = jwt.sign({ id: user.id, rol: user.rol }, 'your_jwt_secret', { expiresIn: 86400 });
                res.send({ auth: true, token: token, rol: user.rol, id: user.id }); // Incluir el rol y el ID en la respuesta
            } else {
                res.status(401).send('Contraseña incorrecta');
            }
        } else {
            res.status(404).send('Usuario no encontrado');
        }
    });
});


// Obtenemos todos los notarios disponibles
app.get('/notarios', (req, res) => {
    let sql = 'SELECT * FROM Usuarios WHERE rol = "notario"';
    db.query(sql, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

// Obtenemos todos los clientes disponibles
app.get('/clientes', (req, res) => {
    let sql = 'SELECT * FROM Usuarios WHERE rol = "cliente"';
    db.query(sql, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

// Obtenemos todos los despachos que hay
app.get('/despachos', (req, res) => {
    let sql = 'SELECT * FROM Despachos';
    db.query(sql, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

// Obtenemos todas las citas
app.get('/citas', (req, res) => {
    let sql = 'SELECT * FROM Citas';
    db.query(sql, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

// Agregamos una cita
app.post('/citas', (req, res) => {
    const { cliente_id, notario_id, despacho_id, fecha_cita } = req.body;
    let sql = 'INSERT INTO Citas (cliente_id, notario_id, despacho_id, fecha_cita) VALUES (?, ?, ?, ?)';
    db.query(sql, [cliente_id, notario_id, despacho_id, fecha_cita], (err, result) => {
        if (err) throw err;
        res.send('Cita agregada');
    });
});

// Actualizamos una cita
app.put('/citas/:id', (req, res) => {
    const { cliente_id, notario_id, despacho_id, fecha_cita } = req.body;
    let sql = 'UPDATE Citas SET cliente_id = ?, notario_id = ?, despacho_id = ?, fecha_cita = ? WHERE id = ?';
    db.query(sql, [cliente_id, notario_id, despacho_id, fecha_cita, req.params.id], (err, result) => {
        if (err) throw err;
        res.send('Cita modificada');
    });
});

// Eliminamos la cita
app.delete('/citas/:id', (req, res) => {
    let sql = 'DELETE FROM Citas WHERE id = ?';
    db.query(sql, [req.params.id], (err, result) => {
        if (err) throw err;
        res.send('Cita eliminada');
    });
});

// Obtenemos las citas por cliente
app.get('/citas/cliente', (req, res) => {
    const { cliente_id } = req.query;
    let sql = 'SELECT * FROM Citas WHERE cliente_id = ?';
    db.query(sql, [cliente_id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

// Obtenemos las citas por notario
app.get('/citas/notario', (req, res) => {
    const { notario_id } = req.query;
    let sql = 'SELECT * FROM Citas WHERE notario_id = ?';
    db.query(sql, [notario_id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

// Agregamos un despacho
app.post('/despachos', (req, res) => {
    const { nombre, direccion } = req.body;
    let sql = 'INSERT INTO Despachos (nombre, direccion) VALUES (?, ?)';
    db.query(sql, [nombre, direccion], (err, result) => {
        if (err) throw err;
        res.send('Despacho agregado');
    });
});

// Eliminamos un despacho
app.delete('/despachos/:id', (req, res) => {
    let sql = 'DELETE FROM Despachos WHERE id = ?';
    db.query(sql, [req.params.id], (err, result) => {
        if (err) throw err;
        res.send('Despacho eliminado');
    });
});

const PORT = process.env.PORT || 5000;

app.listen(PORT, () => console.log(`Server started on port ${PORT}`));

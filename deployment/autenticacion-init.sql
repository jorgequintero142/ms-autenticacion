/***
** db autenticacion
*/ 

/* HU-01 */
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    documento_identidad VARCHAR(50) NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	direccion VARCHAR(50) NOT NULL,
	telefono VARCHAR(10) NOT NULL,
    salario_base NUMERIC(15,2) NOT NULL
);

/* HU-03 */
CREATE TABLE rol( 
	id_rol INTEGER PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT );

INSERT INTO rol (id_rol, nombre, descripcion)
	VALUES
		(1, 'Administrador','Crea usuarios, lista y aprueba solicitudes'),
		(2, 'Asesor','Crea usuarios, lista , aprueba solicitudes y genera reportes'),
		(3, 'Cliente','Crea solicitudes de prestamo');

ALTER TABLE usuario 
 ADD id_rol INTEGER NULL,
 ADD contrasenia TEXT NULL;

INSERT INTO usuario 
			(id_usuario,nombre, apellido,email,documento_identidad, fecha_nacimiento, direccion, telefono, salario_base, id_rol,contrasenia) 
    VALUES (0,'Admin', 'Admin','admin@admin.com','000000', '1986-01-01', 'Localhost', '3001231212', 0, 1, '123456');
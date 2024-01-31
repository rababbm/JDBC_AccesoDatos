--1) Crear bbdd
CREATE DATABASE cine;
--2) Crear las tablas de las clases:

CREATE TABLE Pelicula (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255),
    año INTEGER,
    duracion VARCHAR(10),
    enlace VARCHAR(7000),
);

CREATE TABLE Director (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255)
);

CREATE TABLE Reparto (
    pelicula_id INTEGER REFERENCES Pelicula(id),
    actor_principal_1 VARCHAR(255),
    actor_principal_2 VARCHAR(255),
    actor_principal_3 VARCHAR(255),
    PRIMARY KEY (pelicula_id)
);


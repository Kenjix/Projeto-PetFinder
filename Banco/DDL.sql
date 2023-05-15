-- DROP DATABASE petfinder;
CREATE DATABASE petfinder;
USE petfinder;

CREATE TABLE usuario
(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(30) NOT NULL,
    dataNasc DATE,
    email VARCHAR(80) NOT NULL,
    senha VARCHAR(80) NOT NULL,
    genero CHAR,
    telefone VARCHAR(11),
    avatar BLOB,
    nivelAcesso INT DEFAULT 0,
    tentativasAcesso INT DEFAULT 0,
    dataCadastro DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE publicacao
(
	id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(60) NOT NULL,
    descricao VARCHAR(150) NOT NULL,
    nomePet VARCHAR(60),
    genero CHAR,
    especie VARCHAR(60) NOT NULL,
    porte VARCHAR(60) NOT NULL,
    idade INT,
    vacinas VARCHAR(255),
    fk_usuario INT,
    dataPublicacao DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id)
);
CREATE DATABASE PetBnB;
\c PetBnB;

CREATE TABLE IF NOT EXISTS Usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(256) NOT NULL,
    login VARCHAR(20) NOT NULL UNIQUE,
    senha VARCHAR(64) NOT NULL,
    papel VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS Casa (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(256) NOT NULL,
    endereco VARCHAR(256) NOT NULL,
    descricao VARCHAR(512),
    diaria NUMERIC(10,2) NOT NULL,
    capacidade INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Pet (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(256) NOT NULL,
    raca VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    porte VARCHAR(20) NOT NULL,
    castrado BOOLEAN NOT NULL,
    descricao VARCHAR(512),
    usuario_id BIGINT NOT NULL REFERENCES Usuario(id) ON DELETE CASCADE
);

INSERT INTO Usuario (nome, login, senha, papel) VALUES ('Administrador', 'admin', 'admin', 'ADMIN');
INSERT INTO Usuario (nome, login, senha, papel) VALUES ('Cliente', 'user', 'user', 'USER');

INSERT INTO Casa (nome, endereco, descricao, diaria, capacidade) VALUES
('Casa do Luar', 'Rua das Flores, 123', 'Ambiente acolhedor para cães de pequeno e médio porte.', 120.00, 3),
('Refúgio Pet Feliz', 'Av. dos Pinheiros, 45', 'Casa ampla com quintal seguro para passeios diários.', 180.00, 5),
('Recanto Canino', 'Rua do Sol, 78', 'Estadia tranquila com área coberta e cuidados especializados.', 150.00, 4);

INSERT INTO Pet (nome, raca, idade, porte, castrado, descricao, usuario_id) VALUES
('Luna', 'Shih Tzu', 4, 'Pequeno', true, 'Adora carinho e tem energia tranquila.', 2),
('Bongo', 'Labrador', 3, 'Grande', false, 'Muito amigável e gosta de brincar com crianças.', 2);

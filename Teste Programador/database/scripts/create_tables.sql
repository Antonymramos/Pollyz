-- 📦 Criando o banco de dados
CREATE DATABASE gerenciador_patrimonios;
\c gerenciador_patrimonios; -- Conectando ao banco

-- 🗄️ Tabela base para todos os patrimônios
CREATE TABLE patrimonio (
    id SERIAL PRIMARY KEY,
    modelo VARCHAR(50) NOT NULL,
    numero_patrimonio VARCHAR(50) UNIQUE NOT NULL,
    subgrupo VARCHAR(50) NOT NULL
);

-- 🚗 Tabela de veículos (herdando de patrimonio)
CREATE TABLE veiculo (
    id INT PRIMARY KEY,
    fabricante VARCHAR(50) NOT NULL,
    ano INT NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    cor VARCHAR(50) DEFAULT 'Indefinido',
    CONSTRAINT fk_veiculo_patrimonio FOREIGN KEY (id) REFERENCES patrimonio(id) ON DELETE CASCADE
);

-- 🚘 Tabela de carros
CREATE TABLE carro (
    id INT PRIMARY KEY,
    quantidade_portas INT NOT NULL,
    tipo_combustivel VARCHAR(20) NOT NULL,
    CONSTRAINT fk_carro_veiculo FOREIGN KEY (id) REFERENCES veiculo(id) ON DELETE CASCADE
);

-- 🏍️ Tabela de motos
CREATE TABLE moto (
    id INT PRIMARY KEY,
    cilindrada INT NOT NULL,
    CONSTRAINT fk_moto_veiculo FOREIGN KEY (id) REFERENCES veiculo(id) ON DELETE CASCADE
);

-- 🪑 Tabela de equipamentos (herdando de patrimonio)
CREATE TABLE equipamento (
    id INT PRIMARY KEY,
    descricao VARCHAR(100),
    CONSTRAINT fk_equipamento_patrimonio FOREIGN KEY (id) REFERENCES patrimonio(id) ON DELETE CASCADE
);

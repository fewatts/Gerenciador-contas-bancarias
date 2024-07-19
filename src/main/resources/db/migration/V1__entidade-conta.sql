CREATE TABLE contas (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    agencia VARCHAR(5),
    tipo_de_conta ENUM('CORRENTE', 'POUPANCA'),
    titular VARCHAR(255) NOT NULL,
    saldo DECIMAL(10,2),
    ativa BOOLEAN
);

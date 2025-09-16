-- Requisitos: extensões para UUID e função de timestamp
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Enum de status da empresa
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'status_empresa') THEN
CREATE TYPE status_empresa AS ENUM ('PENDING', 'READY_FOR_LEADS', 'ACTIVE', 'INACTIVE');
END IF;
END$$;

CREATE TABLE IF NOT EXISTS empresa (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cnpj             CHAR(14)     NOT NULL,                           -- apenas dígitos, 14 chars
    razao_social     VARCHAR(255) NOT NULL,
    nome_responsavel VARCHAR(150) NOT NULL,
    email            VARCHAR(150) NOT NULL,
    senha_hash       VARCHAR(255) NOT NULL,                           -- hash (bcrypt/argon2)
    telefone         VARCHAR(20)  NOT NULL,
    status           status_empresa NOT NULL DEFAULT 'PENDING',
    data_criacao     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    data_atualizacao TIMESTAMPTZ  NULL,
    CONSTRAINT uq_empresa_cnpj  UNIQUE (cnpj),
    CONSTRAINT uq_empresa_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS cliente (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cpf              CHAR(11)     NOT NULL,                           -- apenas dígitos, 14 chars
    nome             VARCHAR(150) NOT NULL,
    email            VARCHAR(150) NOT NULL,
    senha_hash       VARCHAR(255) NOT NULL,                           -- hash (bcrypt/argon2)
    telefone         VARCHAR(20)  NOT NULL,

    -- ENDEREÇO (embutido)
    endereco_cep         VARCHAR(8)   NOT NULL,
    endereco_logradouro  VARCHAR(120) NOT NULL,
    endereco_numero      VARCHAR(10)  NOT NULL,
    endereco_complemento VARCHAR(60),
    endereco_bairro      VARCHAR(80)  NOT NULL,
    endereco_cidade      VARCHAR(120) NOT NULL,
    endereco_uf          CHAR(2)      NOT NULL,

    data_criacao     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    data_atualizacao TIMESTAMPTZ  NULL,
    CONSTRAINT uq_cliente_cpf  UNIQUE (cpf),
    CONSTRAINT uq_cliente_email UNIQUE (email)
    );
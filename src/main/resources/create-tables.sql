-- Requisitos: extensões para UUID e função de timestamp
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Enum de status da empresa
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'company_status') THEN
CREATE TYPE company_status AS ENUM ('PENDING', 'READY_FOR_LEADS', 'ACTIVE', 'INACTIVE');
END IF;
END$$;

-- Tabela principal
CREATE TABLE IF NOT EXISTS empresa (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cnpj             CHAR(14)     NOT NULL,                           -- apenas dígitos, ex: 14 chars
    razao_social     VARCHAR(255) NOT NULL,
    nome_responsavel VARCHAR(150) NOT NULL,
    email            VARCHAR(150) NOT NULL,
    senha_hash       VARCHAR(255) NOT NULL,                           -- hash (bcrypt/argon2)
    telefone         VARCHAR(20)  NOT NULL,
    status           company_status NOT NULL DEFAULT 'PENDING',
    data_criacao     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    data_atualizacao TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT uq_empresa_cnpj  UNIQUE (cnpj),
    CONSTRAINT uq_empresa_email UNIQUE (email)
);
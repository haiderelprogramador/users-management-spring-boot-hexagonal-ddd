-- =============================================
-- Script de creación de la base de datos (PostgreSQL / Supabase)
-- Gestión de Usuarios - Arquitectura Hexagonal
-- =============================================
-- PostgreSQL local:  CREATE DATABASE crud_usuarios;  y luego ejecutar este script conectado a ella.
-- Supabase:          pegar este script en "SQL Editor" (usa la base "postgres" que ya existe).
-- Docker Compose:    se ejecuta automáticamente al crear el contenedor (docker-compose.postgres.yml).
--
-- Se usan VARCHAR + CHECK en lugar de ENUM nativos de PostgreSQL para que el adaptador
-- JDBC pueda enviar role/status como texto plano (igual que con MySQL).

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL
                CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'MEMBER', 'REVIEWER')),
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING'
                CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED')),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Usuario administrador inicial (password: Admin1234!)
INSERT INTO users (id, name, email, password, role, status)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Administrador',
    'admin@example.com',
    '$2a$12$n68g/Q5lspJWhmsddcMhHePe.AjmmCxWFLG3Dieme/y/LHN1iOSuC',
    'ADMIN',
    'ACTIVE'
)
ON CONFLICT (id) DO NOTHING;

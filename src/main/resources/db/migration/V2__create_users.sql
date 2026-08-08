-- simple users table for supervisor login
CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    role          VARCHAR(50) NOT NULL DEFAULT 'SUPERVISOR',
    created_at    TIMESTAMP NOT NULL DEFAULT now()
);

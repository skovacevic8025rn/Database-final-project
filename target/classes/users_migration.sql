-- ============================================================
--  Medicinska Istraživanja – SQL skripta za kreiranje baze
-- ============================================================

CREATE DATABASE IF NOT EXISTS labaratorijski_eksperimenti_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE labaratorijski_eksperimenti_db;

-- ------------------------------------------------------------
--  Tabela: users
--  Koristi se za autentifikaciju (login i registracija)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
                                     id               INT          NOT NULL AUTO_INCREMENT,
                                     ime              VARCHAR(50)  NOT NULL,
    prezime          VARCHAR(50)  NOT NULL,
    email            VARCHAR(100) NOT NULL UNIQUE,
    korisnicko_ime   VARCHAR(50)  NOT NULL UNIQUE,
    lozinka          VARCHAR(255) NOT NULL,
    uloga            ENUM('Korisnik', 'Moderator', 'Administrator') NOT NULL DEFAULT 'Korisnik',
    datum_registracije DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
--  Test podaci – nekoliko korisnika za probni login
-- ------------------------------------------------------------
INSERT INTO users (ime, prezime, email, korisnicko_ime, lozinka, uloga) VALUES
                                                                            ('Admin',    'Sistem',    'admin@med.rs',       'admin',    'Admin123!',  'Administrator'),
                                                                            ('Marko',    'Marković',  'marko@med.rs',        'marko',    'Marko123!',  'Moderator'),
                                                                            ('Ana',      'Anić',      'ana@med.rs',           'ana',      'Ana12345!',  'Korisnik');

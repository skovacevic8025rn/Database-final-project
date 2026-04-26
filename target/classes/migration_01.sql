USE labaratorijski_eksperimenti_db;

-- Drop tabela (redosled je bitan zbog foreign key-eva)
DROP TABLE IF EXISTS odgovori;
DROP TABLE IF EXISTS ucesce;
DROP TABLE IF EXISTS pitanja;
DROP TABLE IF EXISTS ankete;
DROP TABLE IF EXISTS eksperimenti;
DROP TABLE IF EXISTS ucesnici;

-- Učesnici
CREATE TABLE ucesnici (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          ime VARCHAR(50) NOT NULL,
                          prezime VARCHAR(50) NOT NULL,
                          datum_rodjenja DATE,
                          pol ENUM('M', 'Z', 'Drugo'),
                          email VARCHAR(100) UNIQUE,
                          datum_registracije DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Eksperimenti
CREATE TABLE eksperimenti (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              naziv VARCHAR(100) NOT NULL,
                              opis TEXT,
                              datum_pocetka DATE,
                              datum_kraja DATE,
                              istrazivac VARCHAR(100)
);

-- Ankete
CREATE TABLE ankete (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        naziv VARCHAR(100) NOT NULL,
                        opis TEXT,
                        eksperiment_id INT,
                        FOREIGN KEY (eksperiment_id) REFERENCES eksperimenti(id)
);

-- Pitanja u anketi
CREATE TABLE pitanja (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         anketa_id INT,
                         tekst_pitanja TEXT NOT NULL,
                         tip ENUM('višestruki izbor', 'tekst', 'skala') NOT NULL,
                         FOREIGN KEY (anketa_id) REFERENCES ankete(id)
);

-- Odgovori učesnika
CREATE TABLE odgovori (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          ucesnik_id INT,
                          pitanje_id INT,
                          odgovor TEXT,
                          datum_odgovora DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (ucesnik_id) REFERENCES ucesnici(id),
                          FOREIGN KEY (pitanje_id) REFERENCES pitanja(id)
);

-- Učešće u eksperimentima
CREATE TABLE ucesce (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        ucesnik_id INT,
                        eksperiment_id INT,
                        datum_prijave DATETIME DEFAULT CURRENT_TIMESTAMP,
                        status ENUM('prijavljen', 'aktivan', 'završen', 'odustao') DEFAULT 'prijavljen',
                        FOREIGN KEY (ucesnik_id) REFERENCES ucesnici(id),
                        FOREIGN KEY (eksperiment_id) REFERENCES eksperimenti(id)
);

-- ============================================================
--  Medicinska Istraživanja – SQL skripta za kreiranje baze
-- ============================================================


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

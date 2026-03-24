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
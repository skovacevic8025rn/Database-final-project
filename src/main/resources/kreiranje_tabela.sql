
CREATE TABLE Laboratorija (
    laboratorija_id      INT             NOT NULL AUTO_INCREMENT,
    naziv                VARCHAR(200)    NOT NULL,
    opis_lokacije        TEXT,
    kapacitet            INT,
    akreditacioni_status VARCHAR(50),
    CONSTRAINT pk_laboratorija PRIMARY KEY (laboratorija_id)
);

CREATE TABLE Istrazivac (
    istrazivac_id          INT             NOT NULL AUTO_INCREMENT,
    ime                    VARCHAR(100)    NOT NULL,
    prezime                VARCHAR(100)    NOT NULL,
    titula                 VARCHAR(10),                  -- BSc, MSc, PhD
    orcid                  VARCHAR(20),
    oblast_specijalizacije VARCHAR(200),
    email                  VARCHAR(150),
    telefon                VARCHAR(30),
    aktivan                BOOLEAN         NOT NULL DEFAULT TRUE,
    supervizor_id          INT,                          -- samo-referenca
    CONSTRAINT pk_istrazivac    PRIMARY KEY (istrazivac_id),
    CONSTRAINT uq_orcid         UNIQUE      (orcid),
    CONSTRAINT fk_supervizor    FOREIGN KEY (supervizor_id)
                                REFERENCES  Istrazivac(istrazivac_id)
                                ON DELETE SET NULL
);


CREATE TABLE ProfilDizajnera (
    istrazivac_id           INT             NOT NULL,
    teorijski_pravac        VARCHAR(200),
    broj_objavljenih_radova INT             DEFAULT 0,
    eticka_licenca          VARCHAR(100),
    CONSTRAINT pk_profil_dizajnera PRIMARY KEY (istrazivac_id),
    CONSTRAINT fk_pd_istrazivac    FOREIGN KEY (istrazivac_id)
                                   REFERENCES  Istrazivac(istrazivac_id)
                                   ON DELETE CASCADE
);

CREATE TABLE ProfilIzvodjaca (
    istrazivac_id                  INT             NOT NULL,
    nivo_obuke_za_osetljive_grupe  VARCHAR(50),
    beleske                        TEXT,
    CONSTRAINT pk_profil_izvodjaca PRIMARY KEY (istrazivac_id),
    CONSTRAINT fk_pi_istrazivac    FOREIGN KEY (istrazivac_id)
                                   REFERENCES  Istrazivac(istrazivac_id)
                                   ON DELETE CASCADE
);

CREATE TABLE SertifikatIzvodjaca (
    sertifikat_id     INT             NOT NULL AUTO_INCREMENT,
    istrazivac_id     INT             NOT NULL,
    naziv_sertifikata VARCHAR(200)    NOT NULL,
    datum_izdavanja   DATE,
    datum_isteka      DATE,
    CONSTRAINT pk_sertifikat       PRIMARY KEY (sertifikat_id),
    CONSTRAINT fk_si_istrazivac    FOREIGN KEY (istrazivac_id)
                                   REFERENCES  Istrazivac(istrazivac_id)
                                   ON DELETE CASCADE
);


CREATE TABLE Eksperiment (
    eksperiment_id       INT             NOT NULL AUTO_INCREMENT,
    sifra                VARCHAR(20)     NOT NULL,       -- npr. PSY-2024-001
    naziv                VARCHAR(300)    NOT NULL,
    oblast               VARCHAR(50)     NOT NULL,
    teorijski_okvir      VARCHAR(200),
    hipoteza             TEXT            NOT NULL,
    ciljevi_istrazivanja TEXT,
    irb_broj_odobrenja   VARCHAR(100)    NOT NULL,
    CONSTRAINT pk_eksperiment      PRIMARY KEY (eksperiment_id),
    CONSTRAINT uq_sifra            UNIQUE      (sifra),
    CONSTRAINT uq_irb              UNIQUE      (irb_broj_odobrenja)
);


CREATE TABLE EksperimentDizajner (
    eksperiment_id INT NOT NULL,
    istrazivac_id  INT NOT NULL,
    CONSTRAINT pk_eksperiment_dizajner PRIMARY KEY (eksperiment_id, istrazivac_id),
    CONSTRAINT fk_ed_eksperiment       FOREIGN KEY (eksperiment_id)
                                       REFERENCES  Eksperiment(eksperiment_id)
                                       ON DELETE CASCADE,
    CONSTRAINT fk_ed_istrazivac        FOREIGN KEY (istrazivac_id)
                                       REFERENCES  Istrazivac(istrazivac_id)
                                       ON DELETE CASCADE
);


CREATE TABLE Resurs (
    resurs_id   INT             NOT NULL AUTO_INCREMENT,
    tip_resursa VARCHAR(30)     NOT NULL,
    naziv       VARCHAR(300)    NOT NULL,
    opis        TEXT,
    CONSTRAINT pk_resurs    PRIMARY KEY (resurs_id),
    CONSTRAINT chk_tip_resursa CHECK (tip_resursa IN (
        'PsiholoskiTest', 'Upitnik', 'StimulusniMaterijal',
        'ObrazacPristanka', 'Ucesnik'
    ))
);


CREATE TABLE PsiholoskiTest (
    resurs_id      INT             NOT NULL,
    tip_testa      VARCHAR(100),
    verzija        VARCHAR(20),
    standardizovan BOOLEAN         DEFAULT TRUE,
    CONSTRAINT pk_psiholoski_test PRIMARY KEY (resurs_id),
    CONSTRAINT fk_pt_resurs       FOREIGN KEY (resurs_id)
                                  REFERENCES  Resurs(resurs_id)
                                  ON DELETE CASCADE
);


CREATE TABLE Upitnik (
    resurs_id       INT             NOT NULL,
    broj_stavki     INT,
    format_odgovora VARCHAR(100),
    CONSTRAINT pk_upitnik   PRIMARY KEY (resurs_id),
    CONSTRAINT fk_u_resurs  FOREIGN KEY (resurs_id)
                            REFERENCES  Resurs(resurs_id)
                            ON DELETE CASCADE
);


CREATE TABLE StimulusniMaterijal (
    resurs_id     INT             NOT NULL,
    tip_stimulusa VARCHAR(100),
    format        VARCHAR(50),
    CONSTRAINT pk_stimulusni_mat PRIMARY KEY (resurs_id),
    CONSTRAINT fk_sm_resurs      FOREIGN KEY (resurs_id)
                                 REFERENCES  Resurs(resurs_id)
                                 ON DELETE CASCADE
);


CREATE TABLE ObrazacPristanka (
    resurs_id INT             NOT NULL,
    jezik     VARCHAR(50),
    verzija   VARCHAR(20),
    CONSTRAINT pk_obrazac_pristanka PRIMARY KEY (resurs_id),
    CONSTRAINT fk_op_resurs         FOREIGN KEY (resurs_id)
                                    REFERENCES  Resurs(resurs_id)
                                    ON DELETE CASCADE
);


CREATE TABLE Ucesnik (
    resurs_id           INT             NOT NULL,
    tip_ucesnika        VARCHAR(30)     NOT NULL,
    starosna_grupa      VARCHAR(50),
    profesija_ili_sport VARCHAR(200),
    CONSTRAINT pk_ucesnik   PRIMARY KEY (resurs_id),
    CONSTRAINT fk_uc_resurs FOREIGN KEY (resurs_id)
                            REFERENCES  Resurs(resurs_id)
                            ON DELETE CASCADE,
    CONSTRAINT chk_tip_ucesnika CHECK (tip_ucesnika IN (
        'Pojedinac', 'BracniPar', 'Porodica', 'GrupaZaposlenih', 'SportskiTim'
    ))
);


CREATE TABLE LaboratorijaResurs (
    laboratorija_id   INT         NOT NULL,
    resurs_id         INT         NOT NULL,
    dostupna_kolicina INT         DEFAULT 0,
    status            VARCHAR(50),
    CONSTRAINT pk_lab_resurs    PRIMARY KEY (laboratorija_id, resurs_id),
    CONSTRAINT fk_lr_lab        FOREIGN KEY (laboratorija_id)
                                REFERENCES  Laboratorija(laboratorija_id)
                                ON DELETE CASCADE,
    CONSTRAINT fk_lr_resurs     FOREIGN KEY (resurs_id)
                                REFERENCES  Resurs(resurs_id)
                                ON DELETE CASCADE,
    CONSTRAINT chk_lr_status CHECK (status IN ('Dostupan', 'U upotrebi', 'Potrosten'))
);


CREATE TABLE EksperimentResurs (
    eksperiment_id INT NOT NULL,
    resurs_id      INT NOT NULL,
    CONSTRAINT pk_eksperiment_resurs PRIMARY KEY (eksperiment_id, resurs_id),
    CONSTRAINT fk_er_eksperiment     FOREIGN KEY (eksperiment_id)
                                     REFERENCES  Eksperiment(eksperiment_id)
                                     ON DELETE CASCADE,
    CONSTRAINT fk_er_resurs          FOREIGN KEY (resurs_id)
                                     REFERENCES  Resurs(resurs_id)
                                     ON DELETE CASCADE
);


CREATE TABLE Alat (
    alat_id              INT             NOT NULL AUTO_INCREMENT,
    laboratorija_id      INT             NOT NULL,
    inventarni_broj      VARCHAR(100)    NOT NULL,
    tip_alata            VARCHAR(100)    NOT NULL,
    naziv                VARCHAR(200),
    datum_nabavke        DATE,
    datum_kalibracije    DATE,
    status_ispravnosti   VARCHAR(30),
    CONSTRAINT pk_alat              PRIMARY KEY (alat_id),
    CONSTRAINT uq_inventarni_broj   UNIQUE      (inventarni_broj),
    CONSTRAINT fk_alat_lab          FOREIGN KEY (laboratorija_id)
                                    REFERENCES  Laboratorija(laboratorija_id)
                                    ON DELETE CASCADE,
    CONSTRAINT chk_status_ispravnosti CHECK (status_ispravnosti IN (
        'Ispravan', 'Neispravan', 'Na servisiranju'
    ))
);


CREATE TABLE Izvodjenje (
    izvodjenje_id      INT             NOT NULL AUTO_INCREMENT,
    eksperiment_id     INT             NOT NULL,
    laboratorija_id    INT             NOT NULL,
    redni_broj         INT             NOT NULL,
    datum_pokretanja   DATE,
    status             VARCHAR(30)     NOT NULL,
    razlog_otkazivanja TEXT,
    CONSTRAINT pk_izvodjenje        PRIMARY KEY (izvodjenje_id),
    CONSTRAINT uq_izv_redni_broj    UNIQUE      (eksperiment_id, redni_broj),
    CONSTRAINT fk_izv_eksperiment   FOREIGN KEY (eksperiment_id)
                                    REFERENCES  Eksperiment(eksperiment_id)
                                    ON DELETE CASCADE,
    CONSTRAINT fk_izv_lab           FOREIGN KEY (laboratorija_id)
                                    REFERENCES  Laboratorija(laboratorija_id)
                                    ON DELETE CASCADE,
    CONSTRAINT chk_status_izvodjenja CHECK (status IN (
        'Planirano', 'Zapoceto', 'Otkazano', 'ZavrsenoUspesno', 'ZavrsenoNeuspesno'
    )),
    -- Pravilo 5: razlog_otkazivanja mora biti popunjen kada je status = 'Otkazano'
    CONSTRAINT chk_razlog_otkazivanja CHECK (
        status <> 'Otkazano' OR razlog_otkazivanja IS NOT NULL
    )
);


CREATE TABLE IzvodjenjeIzvodjac (
    izvodjenje_id INT NOT NULL,
    istrazivac_id INT NOT NULL,
    CONSTRAINT pk_izv_izvodjac  PRIMARY KEY (izvodjenje_id, istrazivac_id),
    CONSTRAINT fk_ii_izvodjenje FOREIGN KEY (izvodjenje_id)
                                REFERENCES  Izvodjenje(izvodjenje_id)
                                ON DELETE CASCADE,
    CONSTRAINT fk_ii_istrazivac FOREIGN KEY (istrazivac_id)
                                REFERENCES  Istrazivac(istrazivac_id)
                                ON DELETE CASCADE
);

CREATE TABLE ClanTimaIzvodjenja (
    clan_tima_id  INT             NOT NULL AUTO_INCREMENT,
    izvodjenje_id INT             NOT NULL,
    istrazivac_id INT             NOT NULL,
    uloga         VARCHAR(50)     NOT NULL,
    opis_uloge    TEXT,
    CONSTRAINT pk_clan_tima     PRIMARY KEY (clan_tima_id),
    CONSTRAINT uq_clan_uloga    UNIQUE      (izvodjenje_id, istrazivac_id, uloga),
    CONSTRAINT fk_ct_izvodjenje FOREIGN KEY (izvodjenje_id)
                                REFERENCES  Izvodjenje(izvodjenje_id)
                                ON DELETE CASCADE,
    CONSTRAINT fk_ct_istrazivac FOREIGN KEY (istrazivac_id)
                                REFERENCES  Istrazivac(istrazivac_id)
                                ON DELETE CASCADE,
    CONSTRAINT chk_uloga CHECK (uloga IN (
        'Glavni ispitivac', 'Posmatrac', 'Tehnicar', 'Koordinator ucesnika'
    ))
);


CREATE TABLE MaterijaliClanovaTima (
    materijal_id        INT             NOT NULL AUTO_INCREMENT,
    clan_tima_id        INT             NOT NULL,
    tip_materijala      VARCHAR(30),
    sadrzaj_ili_putanja TEXT,
    vreme_kreiranja     TIMESTAMP,
    CONSTRAINT pk_materijal     PRIMARY KEY (materijal_id),
    CONSTRAINT fk_mct_clan      FOREIGN KEY (clan_tima_id)
                                REFERENCES  ClanTimaIzvodjenja(clan_tima_id)
                                ON DELETE CASCADE,
    CONSTRAINT chk_tip_materijala CHECK (tip_materijala IN (
        'Dokument', 'PisanaBeleska', 'VideoEvidencija'
    ))
);


CREATE TABLE Sesija (
    sesija_id          INT             NOT NULL AUTO_INCREMENT,
    izvodjenje_id      INT             NOT NULL,
    laboratorija_id    INT             NOT NULL,
    datum_odrzavanja   DATE            NOT NULL,
    vreme_pocetka      TIME            NOT NULL,
    vreme_zavrsetka    TIME            NOT NULL,
    tip_sesije         VARCHAR(20)     NOT NULL,
    format             VARCHAR(20)     NOT NULL,
    status             VARCHAR(20)     NOT NULL,
    url_platforme      VARCHAR(500),
    kapacitet_online   INT,
    CONSTRAINT pk_sesija        PRIMARY KEY (sesija_id),
    CONSTRAINT fk_ses_izvodjenje FOREIGN KEY (izvodjenje_id)
                                 REFERENCES  Izvodjenje(izvodjenje_id)
                                 ON DELETE CASCADE,
    CONSTRAINT fk_ses_lab       FOREIGN KEY (laboratorija_id)
                                REFERENCES  Laboratorija(laboratorija_id)
                                ON DELETE CASCADE,
    CONSTRAINT chk_tip_sesije CHECK (tip_sesije IN ('Uzivo', 'Online', 'Teren')),
    CONSTRAINT chk_format_sesije CHECK (format IN ('Individualna', 'Grupna')),
    CONSTRAINT chk_status_sesije CHECK (status IN (
        'Zakazana', 'UToku', 'Zavrsena', 'Otkazana'
    )),
    CONSTRAINT chk_vreme CHECK (vreme_zavrsetka > vreme_pocetka)
);

CREATE TABLE PotrosnjaResursaSesije (
    potrosnja_id         INT     NOT NULL AUTO_INCREMENT,
    sesija_id            INT     NOT NULL,
    resurs_id            INT     NOT NULL,
    iskoriscena_kolicina INT     NOT NULL DEFAULT 0,
    CONSTRAINT pk_potrosnja     PRIMARY KEY (potrosnja_id),
    CONSTRAINT fk_prs_sesija    FOREIGN KEY (sesija_id)
                                REFERENCES  Sesija(sesija_id)
                                ON DELETE CASCADE,
    CONSTRAINT fk_prs_resurs    FOREIGN KEY (resurs_id)
                                REFERENCES  Resurs(resurs_id)
                                ON DELETE CASCADE
);


CREATE TABLE UpotrebaAlataSesije (
    upotreba_id     INT     NOT NULL AUTO_INCREMENT,
    sesija_id       INT     NOT NULL,
    alat_id         INT     NOT NULL,
    trajanje_minuta INT,
    CONSTRAINT pk_upotreba_alata PRIMARY KEY (upotreba_id),
    CONSTRAINT fk_uas_sesija     FOREIGN KEY (sesija_id)
                                 REFERENCES  Sesija(sesija_id)
                                 ON DELETE CASCADE,
    CONSTRAINT fk_uas_alat       FOREIGN KEY (alat_id)
                                 REFERENCES  Alat(alat_id)
                                 ON DELETE CASCADE
);


CREATE TABLE RezultatTesta (
    rezultat_id         INT             NOT NULL AUTO_INCREMENT,
    izvodjenje_id       INT             NOT NULL,
    sesija_id           INT,
    ucesnik_id          INT             NOT NULL,
    resurs_id           INT             NOT NULL,
    numericka_ocena     DECIMAL(10,3),
    vreme_evidentiranja TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_rezultat      PRIMARY KEY (rezultat_id),
    CONSTRAINT fk_rt_izvodjenje FOREIGN KEY (izvodjenje_id)
                                REFERENCES  Izvodjenje(izvodjenje_id)
                                ON DELETE CASCADE,
    CONSTRAINT fk_rt_sesija     FOREIGN KEY (sesija_id)
                                REFERENCES  Sesija(sesija_id)
                                ON DELETE SET NULL,
    CONSTRAINT fk_rt_ucesnik    FOREIGN KEY (ucesnik_id)
                                REFERENCES  Ucesnik(resurs_id)
                                ON DELETE CASCADE,
    CONSTRAINT fk_rt_resurs     FOREIGN KEY (resurs_id)
                                REFERENCES  Resurs(resurs_id)
                                ON DELETE CASCADE
);


CREATE TABLE Feedback (
    feedback_id             INT             NOT NULL AUTO_INCREMENT,
    rezultat_id             INT             NOT NULL,
    ocenjivac_id            INT             NOT NULL,
    oblasti_za_unapredjenje TEXT,
    procena_spremnosti      TEXT,
    predlog_daljeg_rada     TEXT,
    vreme_kreiranja         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_feedback      PRIMARY KEY (feedback_id),
    CONSTRAINT fk_fb_rezultat   FOREIGN KEY (rezultat_id)
                                REFERENCES  RezultatTesta(rezultat_id)
                                ON DELETE CASCADE,
    CONSTRAINT fk_fb_ocenjivac  FOREIGN KEY (ocenjivac_id)
                                REFERENCES  Istrazivac(istrazivac_id)
                                ON DELETE CASCADE
);

CREATE TABLE MedjuocenjivackaSaglasnost (
    saglasnost_id       INT             NOT NULL AUTO_INCREMENT,
    rezultat_id         INT             NOT NULL,
    ocenjivac1_id       INT             NOT NULL,
    ocenjivac2_id       INT             NOT NULL,
    stepen_saglasnosti  DECIMAL(5,4),
    metod_saglasnosti   VARCHAR(100),
    vreme_izracunavanja TIMESTAMP,
    CONSTRAINT pk_saglasnost        PRIMARY KEY (saglasnost_id),
    CONSTRAINT fk_ms_rezultat       FOREIGN KEY (rezultat_id)
                                    REFERENCES  RezultatTesta(rezultat_id)
                                    ON DELETE CASCADE,
    CONSTRAINT fk_ms_ocenjivac1     FOREIGN KEY (ocenjivac1_id)
                                    REFERENCES  Istrazivac(istrazivac_id)
                                    ON DELETE CASCADE,
    CONSTRAINT fk_ms_ocenjivac2     FOREIGN KEY (ocenjivac2_id)
                                    REFERENCES  Istrazivac(istrazivac_id)
                                    ON DELETE CASCADE,
    CONSTRAINT chk_razliciti_ocenjivaci CHECK (ocenjivac1_id <> ocenjivac2_id)
);



-- ------------------------------------------------------------
-- Pravilo 3: Sesije u istoj laboratoriji se ne smeju preklapati
-- ------------------------------------------------------------
DELIMITER $$

CREATE TRIGGER trg_provera_preklapanja_sesija
BEFORE INSERT ON Sesija
FOR EACH ROW
BEGIN
    DECLARE preklapanje INT;

    SELECT COUNT(*) INTO preklapanje
    FROM Sesija
    WHERE laboratorija_id    = NEW.laboratorija_id
      AND datum_odrzavanja   = NEW.datum_odrzavanja
      AND status NOT IN ('Otkazana')
      AND vreme_pocetka      < NEW.vreme_zavrsetka
      AND vreme_zavrsetka    > NEW.vreme_pocetka;

    IF preklapanje > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Greška: Sesija se preklapa sa postojećom sesijom u istoj laboratoriji.';
    END IF;
END$$

-- ------------------------------------------------------------
-- Pravilo 4: Nakon INSERT u PotrosnjaResursaSesije,
--            automatski smanji dostupnu količinu u LaboratorijaResurs
-- ------------------------------------------------------------
CREATE TRIGGER trg_azuriraj_kolicinu_resursa
AFTER INSERT ON PotrosnjaResursaSesije
FOR EACH ROW
BEGIN
    DECLARE lab_id INT;

    SELECT laboratorija_id INTO lab_id
    FROM Sesija
    WHERE sesija_id = NEW.sesija_id;

    UPDATE LaboratorijaResurs
    SET dostupna_kolicina = dostupna_kolicina - NEW.iskoriscena_kolicina
    WHERE laboratorija_id = lab_id
      AND resurs_id       = NEW.resurs_id;
END$$

-- ------------------------------------------------------------
-- Pravilo 1: Istraživač ne sme biti i dizajner i izvođač
--            istog eksperimenta
-- ------------------------------------------------------------
CREATE TRIGGER trg_provera_dizajner_izvodjac
BEFORE INSERT ON IzvodjenjeIzvodjac
FOR EACH ROW
BEGIN
    DECLARE je_dizajner INT;

    SELECT COUNT(*) INTO je_dizajner
    FROM EksperimentDizajner ed
    JOIN Izvodjenje i ON i.eksperiment_id = ed.eksperiment_id
    WHERE i.izvodjenje_id = NEW.izvodjenje_id
      AND ed.istrazivac_id = NEW.istrazivac_id;

    IF je_dizajner > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Greška: Istraživač ne može biti i dizajner i izvođač istog eksperimenta.';
    END IF;
END$$

-- ------------------------------------------------------------
-- Pravilo 2: Dizajner mora imati PhD ili biti pod supervizijom
-- ------------------------------------------------------------
CREATE TRIGGER trg_provera_kvalifikacije_dizajnera
BEFORE INSERT ON EksperimentDizajner
FOR EACH ROW
BEGIN
    DECLARE ima_phd     BOOLEAN;
    DECLARE ima_sup     BOOLEAN;

    SELECT (titula = 'PhD') INTO ima_phd
    FROM Istrazivac
    WHERE istrazivac_id = NEW.istrazivac_id;

    SELECT (supervizor_id IS NOT NULL) INTO ima_sup
    FROM Istrazivac
    WHERE istrazivac_id = NEW.istrazivac_id;

    IF NOT ima_phd AND NOT ima_sup THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Greška: Dizajner mora imati titulu PhD ili biti pod supervizijom.';
    END IF;
END$$

DELIMITER ;

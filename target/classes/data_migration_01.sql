-- Ucesnici
INSERT INTO ucesnici (ime, prezime, datum_rodjenja, pol, email) VALUES
                                                                    ('Marko', 'Petrović', '1995-03-15', 'M', 'marko.petrovic@gmail.com'),
                                                                    ('Ana', 'Jovanović', '1998-07-22', 'Z', 'ana.jovanovic@gmail.com'),
                                                                    ('Stefan', 'Nikolić', '1990-11-05', 'M', 'stefan.nikolic@gmail.com'),
                                                                    ('Jelena', 'Đorđević', '2000-01-30', 'Z', 'jelena.djordjevic@gmail.com'),
                                                                    ('Nikola', 'Stojanović', '1993-06-18', 'M', 'nikola.stojanovic@gmail.com');

-- Eksperimenti
INSERT INTO eksperimenti (naziv, opis, datum_pocetka, datum_kraja, istrazivac) VALUES
                                                                                   ('Stres i pamćenje', 'Ispitivanje uticaja stresa na kratkoročno pamćenje', '2024-01-10', '2024-06-10', 'Dr. Milena Kovač'),
                                                                                   ('Socijalni pritisak', 'Analiza ponašanja pod uticajem grupe', '2024-03-01', '2024-09-01', 'Dr. Ivan Lazić'),
                                                                                   ('Emocionalna inteligencija', 'Merenje EQ kod različitih starosnih grupa', '2024-05-15', '2024-12-15', 'Dr. Milena Kovač');

-- Ankete
INSERT INTO ankete (naziv, opis, eksperiment_id) VALUES
                                                     ('Anketa o stresu', 'Procena nivoa stresa pre i posle eksperimenta', 1),
                                                     ('Grupno ponašanje', 'Pitanja o odlukama u grupnim situacijama', 2),
                                                     ('EQ upitnik', 'Standardizovani upitnik emocionalne inteligencije', 3),
                                                     ('Zadovoljstvo učešćem', 'Opšta anketa o iskustvu učesnika', 1);

-- Pitanja
INSERT INTO pitanja (anketa_id, tekst_pitanja, tip) VALUES
                                                        (1, 'Na skali 1-10, koliko ste stresni trenutno?', 'skala'),
                                                        (1, 'Da li imate poteškoće sa koncentracijom?', 'višestruki izbor'),
                                                        (1, 'Opišite svoje trenutno emocionalno stanje.', 'tekst'),
                                                        (2, 'Da li biste promenili odluku pod pritiskom grupe?', 'višestruki izbor'),
                                                        (2, 'Na skali 1-10, koliko vam je važno tuđe mišljenje?', 'skala'),
                                                        (3, 'Kako reagujete kada je neko uznemiren pored vas?', 'tekst'),
                                                        (3, 'Na skali 1-10, koliko lako prepoznajete emocije kod drugih?', 'skala'),
                                                        (4, 'Da li biste ponovo učestvovali u eksperimentu?', 'višestruki izbor');

-- Odgovori
INSERT INTO odgovori (ucesnik_id, pitanje_id, odgovor) VALUES
                                                           (1, 1, '7'),
                                                           (1, 2, 'Da'),
                                                           (1, 3, 'Umoran sam i malo uznemiren'),
                                                           (2, 1, '4'),
                                                           (2, 2, 'Ne'),
                                                           (2, 3, 'Relativno mirna, malo nervozna'),
                                                           (3, 4, 'Da, ponekad'),
                                                           (3, 5, '6'),
                                                           (4, 6, 'Pokušavam da ih podržim'),
                                                           (4, 7, '8'),
                                                           (5, 4, 'Ne'),
                                                           (5, 5, '3'),
                                                           (1, 8, 'Da, svakako'),
                                                           (2, 8, 'Verovatno da');

-- Ucesce
INSERT INTO ucesce (ucesnik_id, eksperiment_id, status) VALUES
                                                            (1, 1, 'završen'),
                                                            (2, 1, 'završen'),
                                                            (3, 2, 'aktivan'),
                                                            (4, 3, 'aktivan'),
                                                            (5, 2, 'prijavljen'),
                                                            (1, 3, 'prijavljen'),
                                                            (2, 3, 'aktivan');
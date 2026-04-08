SET CLIENT_ENCODING="UTF8";
BEGIN;

CREATE TABLE IF NOT EXISTS projekt(
    projekt_id SERIAL,
    nazwa VARCHAR(50) NOT NULL,
    opis VARCHAR(1000),
    dataczas_utworzenia TIMESTAMP DEFAULT now(),
    dataczas_modyfikacji TIMESTAMP,
    data_oddania DATE,
    CONSTRAINT projekt_pk PRIMARY KEY (projekt_id)
    );

CREATE TABLE IF NOT EXISTS zadanie(
    zadanie_id SERIAL,
    nazwa VARCHAR(50) NOT NULL,
    opis VARCHAR(1000),
    kolejnosc INTEGER,
    dataczas_utworzenia TIMESTAMP DEFAULT now(),
    dataczas_modyfikacji TIMESTAMP,
    projekt_id INTEGER NOT NULL,
    CONSTRAINT zadanie_pk PRIMARY KEY (zadanie_id)
    );

CREATE TABLE IF NOT EXISTS student(
    student_id SERIAL,
    imie VARCHAR (50) NOT NULL,
    nazwisko VARCHAR (100) NOT NULL,
    nr_indeksu VARCHAR (20) UNIQUE NOT NULL,
    email VARCHAR (50)UNIQUE NOT NULL,
    stacjonarny boolean DEFAULT true,
    CONSTRAINT student_pk PRIMARY KEY (student_id)
    );

ALTER TABLE zadanie DROP CONSTRAINT IF EXISTS zadanie_projekt_fk;
ALTER TABLE zadanie ADD CONSTRAINT zadanie_projekt_fk FOREIGN KEY (projekt_id) REFERENCES projekt (projekt_id);

ALTER TABLE zadanie DROP CONSTRAINT IF EXISTS unique_kolejnosc;
ALTER TABLE zadanie ADD CONSTRAINT unique_kolejnosc UNIQUE (kolejnosc, projekt_id);

CREATE INDEX IF NOT EXISTS projekt_nazwa_idx ON projekt(nazwa);
CREATE INDEX IF NOT EXISTS zadanie_nazwa_idx ON zadanie(nazwa);
CREATE INDEX IF NOT EXISTS student_nr_indeksu_idx ON student(nr_indeksu);
CREATE INDEX IF NOT EXISTS student_email_idx ON student(email);

COMMIT;
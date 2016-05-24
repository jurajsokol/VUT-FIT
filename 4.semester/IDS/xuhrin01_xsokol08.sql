-- xuhrin01, xsokol08

DROP TABLE Vyuzil cascade constraints;
DROP TABLE Izba cascade constraints;
DROP TABLE Platba_za_sluzbu cascade constraints;
DROP TABLE Platba_za_pobyt cascade constraints;
DROP TABLE Fyzicka_osoba cascade constraints;
DROP TABLE Firma cascade constraints;
DROP TABLE Pobyt cascade constraints;
DROP TABLE Doplnkove_sluzby cascade constraints;
DROP TABLE Platba cascade constraints;
DROP TABLE Klient cascade constraints;
DROP TABLE Zamestnanec cascade constraints;
DROP SEQUENCE ID_kl_seq;

--odstranenie prav
--revoke all on Fyzicka_osoba from xsokol08;
--revoke all on Pobyt from xsokol08;

--vytvorenie tabuliek
CREATE TABLE Klient(
ID_zakaznika int PRIMARY KEY,
c_domu int,
ulica nchar(100),
mesto nchar(100),
PSC int
);

CREATE TABLE Zamestnanec(
cislo_zamestnanca INT PRIMARY KEY,
meno nchar(50),
priezvisko nchar(50)
);

CREATE TABLE Doplnkove_sluzby(
ID_sluzby int PRIMARY KEY,
nazov nchar(100),
typ nchar(100),
cena int,
poskytol int,
FOREIGN KEY (poskytol) REFERENCES Zamestnanec(cislo_zamestnanca)
);

CREATE TABLE Vyuzil(
ID_sluzby int,
ID_zakaznika int,
FOREIGN KEY (ID_sluzby) REFERENCES Doplnkove_sluzby(ID_sluzby),
FOREIGN KEY (ID_zakaznika) REFERENCES Klient(ID_zakaznika)
);

CREATE TABLE Platba(
variabilny_symbol INT PRIMARY KEY,
ciastka INT,
datum date,
spÙsob_platby CHAR(100),
uskutocnil INT,
prijal_platbu INT,
FOREIGN KEY (prijal_platbu) REFERENCES Zamestnanec(cislo_zamestnanca),
FOREIGN KEY (uskutocnil) REFERENCES Klient(ID_zakaznika)
);

CREATE TABLE Platba_za_sluzbu(
platba int,
sluzba int,
FOREIGN KEY (platba) REFERENCES Platba(variabilny_symbol),
FOREIGN KEY (sluzba) REFERENCES Doplnkove_sluzby(ID_sluzby)
);

CREATE TABLE Fyzicka_osoba(
ID_zakaznika int PRIMARY KEY,
datum_narodenia date,
rodne_cislo int CHECK( MOD(rodne_cislo, 11) = 0 ),
cislo_platobnej_karty int,
meno nchar(100),
priezvisko nchar(100),
FOREIGN KEY (ID_zakaznika) REFERENCES Klient(ID_zakaznika)
);

CREATE TABLE Firma(
ID_zakaznika int PRIMARY KEY,
nazov nchar(100),
bankove_spojenie int,
ICO int,
FOREIGN KEY (ID_zakaznika) REFERENCES Klient(ID_zakaznika)
);

CREATE TABLE Pobyt(
ID_pobytu int PRIMARY KEY,
datum_prichodu date,
datum_odchodu date,
celkova_cena int,
ID_zakaznika int,
rezervoval date,
zamestnanec int,
FOREIGN KEY (ID_zakaznika) REFERENCES Klient(ID_zakaznika),
FOREIGN KEY (zamestnanec) REFERENCES Zamestnanec(cislo_zamestnanca)
);

CREATE TABLE Izba(
cislo_izby int PRIMARY KEY,
typ char(100),
pocet_lozok int,
cena int,
stav char,
ID_pobytu int,
FOREIGN KEY (ID_pobytu) REFERENCES Pobyt(ID_pobytu)
);

CREATE TABLE Platba_za_pobyt(
platba int,
pobyt int,
FOREIGN KEY (platba) REFERENCES Platba(variabilny_symbol),
FOREIGN KEY (pobyt) REFERENCES Pobyt(ID_pobytu)
);

-- vytvori sekvenciu, z nej sa bude vytv·ratù ID zakaznika
CREATE SEQUENCE ID_kl_seq START WITH 1 INCREMENT BY 1 NOMAXVALUE;

-- trigger, kory priradi cislo zo sekvencie zakaznikovi
CREATE TRIGGER ID_kl_trigger
BEFORE INSERT ON Klient
FOR EACH ROW
BEGIN
  :new.ID_zakaznika := ID_kl_seq.nextval;
END;
/

--trigger, ktory odstreni zakaznika zo vsetkych tabuliek
CREATE OR REPLACE TRIGGER Odstranenie_kienta
AFTER DELETE ON Klient
BEGIN
  SELECT ID_zakaznika INTO ID FROM DUAL;
   DELETE FROM Fyzicka_osoba  WHERE ID_zakaznika IN (select ID_zakaznika from deleted.ID_zakaznika);
   DELETE FROM Firma WHERE ID_zakaznika IN (select ID_zakaznika from deleted.ID_zakaznika);
END;
/

--procedura
DECLARE
   ID_zak Klient.ID_zakaznika%type;
   k_mesto Klient.mesto%type;

   -- user defined exception
   ex_neznamy_klient EXCEPTION;
BEGIN
   IF k_mesto <= NULL THEN
      RAISE ex_neznamy_klient;
   ELSE
      SELECT mesto INTO  k_mesto
      FROM Klient
      WHERE ID_zak = ID_zakaznika;
     
      DBMS_OUTPUT.PUT_LINE ('ID: '||  ID_zak);
      DBMS_OUTPUT.PUT_LINE ('Mesto : ' || k_mesto);
   END IF;
EXCEPTION
   WHEN ex_neznamy_klient THEN
      dbms_output.put_line('ID must be greater than zero!');
   WHEN no_data_found THEN
      dbms_output.put_line('No such customer!');
   WHEN others THEN
      dbms_output.put_line('Error!'); 
END;
/

--naplnenie tabuliek hodnotami
INSERT INTO Klient
VALUES (0, 203, 'Hlavn·', 'NovÈ Z·mky', 93568);

INSERT INTO Klient
VALUES (1, 500, 'Hlavn·', 'Nitra', 98745);

INSERT INTO Klient
VALUES (2, 32, 'Kom·rÚansk·', 'NovÈ Z·mky', 93568);

INSERT INTO Zamestnanec
VALUES (0, 'Oto', 'Nov·k');

INSERT INTO Zamestnanec
VALUES (1, 'J·n', 'Vesel˝');

INSERT INTO Zamestnanec
VALUES (2, 'Anna', 'Novotn·');

INSERT INTO Doplnkove_sluzby
VALUES (0, 'bazÈn', 'standard plus', 250, 0);

INSERT INTO Doplnkove_sluzby
VALUES (1, 'sauna', 'standard plus', 200, 2);

INSERT INTO Doplnkove_sluzby
VALUES (2, 'masaz', 'standard plus', 100, 2);

INSERT INTO Vyuzil
VALUES (0, 1);

INSERT INTO Platba
VALUES (036, 2000, '23-04-2016', 'kartou', 1, 0);

INSERT INTO Platba
VALUES (223, 3000, '08-04-2016', 'kartou', 1, 1);

INSERT INTO Platba_za_sluzbu
VALUES (036, 1);

INSERT INTO Fyzicka_osoba
VALUES (1, '02-02-1990', 9553044787, 9658223036, 'Ondrej', 'Novck');

INSERT INTO Fyzicka_osoba
VALUES (2, '09-10-1987', 9402025644, 8854284109, 'Michal', 'Sova');

INSERT INTO Fyzicka_osoba
VALUES (3, '23-05-1976', 9453223604, 4179275363596870 , 'Jana', 'Perez');

INSERT INTO Firma
VALUES (1, 'IB Mont', 5555, 879);

INSERT INTO Pobyt
VALUES (0, '22-03-2016', '26-03-2016', 2000, 1, '14-03-2016', 0);

INSERT INTO Pobyt
VALUES (1, '30-03-2016', '09-04-2016', 5000, 2, null, 0);

INSERT INTO Pobyt
VALUES (2, '08-04-2016', '11-04-2016', 3000, 2, '05-04-2016', 1);

INSERT INTO Izba
VALUES (10, 'apartm·n klasik', 2, 2500, 'p', 1);

INSERT INTO Izba
VALUES (11, 'apartm·n klasik', 3, 3000, 'p', 2);

INSERT INTO Izba
VALUES (23, 'apartm·n de lux', 2, 4500, 'p', 2);

INSERT INTO Izba
VALUES (18, 'dvojlozkova izba', 2, 2000, 'p', null);

INSERT INTO Izba
VALUES (33, 'jednolozkova izba', 1, 1800, 'n', null);

INSERT INTO Platba_za_pobyt
VALUES (036, 2);

INSERT INTO Platba_za_pobyt
VALUES (223, 1);

-- JOIN dvoch tabuliek
-- vypÌöe zamestnancov, ktorÌ poskytli sluûbu drahöiu ako 50 CZK
SELECT meno, priezvisko, nazov
FROM Zamestnanec JOIN Doplnkove_sluzby ON (cislo_zamestnanca=poskytol)
WHERE cena > 100;

-- JOIN dvoch tabuliek
-- vypÌöe izby, ktorÈ s˙ obsadenÈ/rezervovanÈ a do kedy
SELECT cislo_izby, datum_odchodu
FROM Izba JOIN Pobyt ON (Pobyt.ID_pobytu=Izba.ID_pobytu)
WHERE cena > 50;

-- JOIN troch tabuliek
-- vypise meno, priezvysko, rodne cislo a cislo platobnej karty klienta
SELECT meno, priezvisko, rodne_cislo, cislo_platobnej_karty 
FROM Fyzicka_osoba 
NATURAL JOIN Klient 
NATURAL JOIN Platba;

-- dotaz s klauzulou GROUP BY
-- vypÌöe kolko ktor˝ zamestnanec rezervoval
SELECT cislo_zamestnanca, priezvisko, COUNT(Pobyt.ID_POBYTU) AS PocetRezerv·cii
FROM Zamestnanec JOIN Pobyt ON (Pobyt.zamestnanec=Zamestnanec.cislo_zamestnanca)
GROUP BY priezvisko, cislo_zamestnanca;

-- dotaz s klauzulou GROUP BY a agregacnou funkciou
-- platby s vyssou sumou ako 500 a pocet platieb jednotliveho typu
SELECT spÙsob_platby, COUNT(*)
FROM platba
WHERE ciastka>500
GROUP BY spÙsob_platby;

-- predik·t EXISTS
-- variabilny symbol platby a ciastka ktor˙ prijal zamestnanec
SELECT variabilny_symbol, ciastka
FROM Platba
WHERE EXISTS (SELECT meno FROM zamestnanec WHERE Platba.prijal_platbu = zamestnanec.cislo_zamestnanca);

-- predik·t IN s vnoren˝m selectem
-- ktoru izbu rezervoval zamestnanec
SELECT cislo_izby, stav
FROM Izba
WHERE Izba.ID_pobytu IN (SELECT ID_pobytu FROM Pobyt WHERE zamestnanec LIKE 0 );

--explain plan bez indexu
explain plan for SELECT rodne_cislo, count(*) FROM Fyzicka_osoba 
NATURAL JOIN Platba WHERE ciastka > 2500 GROUP BY rodne_cislo;

SELECT plan_table_output FROM table(dbms_output.display());

--explain plan s indexom
create index platba_ciastka on Platba(Ciastka); 

explain plan for SELECT rodne_cislo, count(*) FROM Fyzicka_osoba 
NATURAL JOIN Platba WHERE ciastka > 2500 GROUP BY rodne_cislo;

SELECT plan_table_output FROM table(dbms_xplan.display());

DROP index platba_ciastka;

--pristupove prava pre druheho uzivatela
GRANT ALL ON Fyzicka_osoba to xsokol08;
GRANT ALL ON Pobyt to xsokol08;

--materializovany pohlad

DROP materialized view pohlad; 
DROP synonym Fyzicka_osoba;
DROP synonym Pobyt;

create or replace synonym Fyzicka_osoba for xuhrin01.fyzicka_osoba@vutfit;
create or replace synonym Pobyt for xuhrin01.pobyt@vutfit;

create materialized view pohlad
   nologging
   cache
   build immediate
   refresh ON COMMIT 
   enable query rewrite
   as
      SELECT rodne_cislo, meno, priezvisko, pobyt.celkova_cena
      FROM Fyzicka_osoba NATURAL JOIN Pobyt;

alter session SET query_rewrite_enabled = false;
explain plan for SELECT rodne_cislo, meno, priezvisko, Pobyt.celkova_cena
FROM Fyzicka_osoba NATURAL JOIN Pobyt;
SELECT plan_table_output FROM table(dbms_xplan.display(null, null, 'basic'));

alter session set query_rewrite_enabled = true;
explain plan for SELECT rodne_cislo, meno, priezvisko, Pobyt.celkova_cena
FROM Fyzicka_osoba NATURAL JOIN Pobyt;
SELECT plan_table_output FROM table(dbms_xplan.display(null, null, 'basic'));

grant all on pohlad to xuhrin01


COMMIT;
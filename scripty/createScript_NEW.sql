-- we don't know how to generate schema ST55431 (class Schema) :(
create sequence INCREMENT_HODNOCENI
/

create sequence INCREMENT_UZIVATELE
/

create sequence INCREMENT_PREDMETY
/

create sequence INCREMENT_SKUPINY
/

create sequence INCREMENT_OBORY
/

create sequence INCREMENT_ZPRAVY
/

create sequence INCREMENT_SOUBORY
/

create sequence INCREMENT_KONTA
/

create sequence INCREMENT_TRANSAKCE
/

create sequence INCREMENT_PRODUKTY
/

create sequence INCREMENT_JIDELNI_LISTKY
/

create table PREDMETY
(
    ID_PREDMET NUMBER       not null
        constraint PREDMET_PK
            primary key,
    NAZEV      VARCHAR2(50) not null,
    POPIS      VARCHAR2(250)
)
/

create or replace trigger PREDMETY_TRIGGER
    before insert or update
    on PREDMETY
    for each row
BEGIN

    if (LENGTH(:NEW.nazev) < 3 or LENGTH(:NEW.nazev) > 50) then
        raise_application_error(-20005, 'Název musí být v rozsahu 3 až 50 znaků');
    end if;

    if (inserting) then
        SELECT increment_predmety.nextval
        INTO :new.id_predmet
        FROM dual;
    end if;
END;
/

create table SKUPINY
(
    ID_SKUPINA NUMBER       not null
        constraint SKUPINA_PK
            primary key,
    NAZEV      VARCHAR2(50) not null,
    POPIS      VARCHAR2(250)
)
/

create or replace trigger SKUPINY_TRIGGER
    before insert or update
    on SKUPINY
    for each row
BEGIN
    if (LENGTH(:NEW.nazev) < 3 or LENGTH(:NEW.nazev) > 30) then
        raise_application_error(-20002, 'Název musí být v rozsahu 3 až 30 znaků');
    end if;

    if (inserting) then
        SELECT increment_skupiny.nextval
        INTO :new.id_skupina
        FROM dual;
    end if;
END;
/

create table STUDIJNI_OBORY
(
    ID_OBOR NUMBER       not null
        constraint STUDIJNI_OBOR_PK
            primary key,
    NAZEV   VARCHAR2(50) not null,
    POPIS   VARCHAR2(250)
)
/

create table OBOR_PREDMET
(
    STUDIJNI_OBOR_ID_OBOR NUMBER not null
        constraint OBOR_PREDMET_STUDIJNI_OBORY_ID_OBOR_FK
            references STUDIJNI_OBORY,
    PREDMET_ID_PREDMET    NUMBER not null
        constraint OBOR_PREDMET_PREDMETY_ID_PREDMET_FK
            references PREDMETY
)
/

create or replace trigger OBORY_TRIGGER
    before insert or update
    on STUDIJNI_OBORY
    for each row
BEGIN
    if (LENGTH(:NEW.nazev) < 3 or LENGTH(:NEW.nazev) > 50) then
        raise_application_error(-20002, 'Název musí být v rozsahu 3 až 50 znaků');
    end if;

    if (inserting) then
        SELECT increment_obory.nextval
        INTO :new.id_obor
        FROM dual;
    end if;
END;
/

create table UZIVATELE
(
    ID_UZIVATEL     NUMBER       not null
        constraint UZIVATEL_PK
            primary key,
    JMENO           VARCHAR2(50) not null,
    PRIJMENI        VARCHAR2(50) not null,
    EMAIL           VARCHAR2(50) not null,
    HESLO           VARCHAR2(50) not null,
    DATUM_VYTVORENI DATE         not null,
    UZIVATEL_TYP    VARCHAR2(50),
    AVATAR          BLOB
)
/

create table HODNOCENI
(
    ID_HODNOCENI      NUMBER not null
        constraint HODNOCENI_PK
            primary key,
    HODNOTA_HODNOCENI NUMBER not null,
    POPIS             VARCHAR2(100),
    ID_UZIVATEL       NUMBER not null
        constraint UZIVATEL_HODNOCENI
            references UZIVATELE,
    ID_SKUPINA        NUMBER not null
        constraint HODNOCENI_SKUPINA
            references SKUPINY
)
/

create or replace trigger HODNOCENI_TRIGGER
    before insert or update
    on HODNOCENI
    for each row
BEGIN
    IF (:NEW.hodnota_hodnoceni < 1 OR :NEW.hodnota_hodnoceni > 5) then
        raise_application_error(-200001, 'Hodnota hodnocení musí být v rozmezí 1-5');
    end if;

    if (inserting) then
        SELECT increment_hodnoceni.nextval
        INTO :new.id_hodnoceni
        FROM dual;
    end if;
END;
/

create table STUDENTI
(
    ID_UZIVATEL NUMBER       not null
        constraint STUDENT_PK
            primary key
        constraint UZIVATEL_STUDENT
            references UZIVATELE,
    ROK_STUDIA  VARCHAR2(50) not null,
    ID_OBOR     NUMBER       not null
        constraint STUDENT_OBOR
            references STUDIJNI_OBORY
)
/

create table UCITELE
(
    ID_UZIVATEL NUMBER       not null
        constraint UCITEL_PK
            primary key
        constraint UZIVATEL_UCITEL
            references UZIVATELE,
    KATEDRA     VARCHAR2(50) not null
)
/

create table UCITELE_PREDMETY
(
    UCITELE_ID_UCITEL  NUMBER
        constraint UCITELE_PREDMETY_UCITELE_ID_UZIVATEL_FK
            references UCITELE,
    PREDMET_ID_PREDMET NUMBER
        constraint UCITELE_PREDMETY_PREDMETY_ID_PREDMET_FK
            references PREDMETY
)
/

create or replace trigger UZIVATELE_TRIGGER
    before insert or update
    on UZIVATELE
    for each row
BEGIN
    if (LENGTH(:NEW.jmeno) < 3 or LENGTH(:NEW.jmeno) > 30) then
        raise_application_error(-20002, 'Jméno musí být v rozsahu 3 až 30 znaků');
    elsif (LENGTH(:NEW.prijmeni) < 3 or LENGTH(:NEW.prijmeni) > 30) then
        raise_application_error(-20003, 'Přijmení musí být v rozsahu 3 až 30 znaků');
    elsif (LENGTH(:NEW.heslo) < 2) then
        raise_application_error(-20004, 'Příliš slabé heslo. Minimální počet znaků je 2');
    end if;


    if (inserting) then
        :NEW.heslo := fnc_zahashuj_uzivatele(:NEW.email, :NEW.heslo); /*Na update profilu nebude fungovat heslo */
        :NEW.datum_vytvoreni := sysdate;
        SELECT increment_uzivatele.nextval
        INTO :NEW.id_uzivatel
        FROM dual;

    end if;
END;
/

create table ZPRAVY_BACKUP
(
    ID_ZPRAVA              NUMBER        not null
        constraint ZPRAVA_BACKUP_PK
            primary key,
    NAZEV                  VARCHAR2(50)  not null,
    TELO                   VARCHAR2(250) not null,
    DATUM_VYTVORENI        DATE          not null,
    ID_UZIVATEL_ODESILATEL NUMBER        not null,
    ID_UZIVATEL_PRIJEMCE   NUMBER,
    ID_SKUPINA_PRIJEMCE    NUMBER
)
/

create table SKUPINY_PREDMETY
(
    SKUPINY_ID_SKUPINA  NUMBER not null
        constraint SKUPINY_PREDMETY_SKUPINY_ID_SKUPINA_FK
            references SKUPINY,
    PREDMETY_ID_PREDMET NUMBER not null
        constraint SKUPINY_PREDMETY_PREDMETY_ID_PREDMET_FK
            references PREDMETY
)
/

create table SOUBORY
(
    ID_SOUBORU    NUMBER        not null
        constraint SOUBORY_PK
            primary key,
    NAZEV_SOUBORU VARCHAR2(255) not null,
    TYP_SOUBORU   VARCHAR2(255) not null,
    PRIPONA       VARCHAR2(255) not null,
    DATA          BLOB          not null,
    UPRAVENO      DATE          not null,
    NAHRANO       DATE          not null
)
/

create table ZPRAVY
(
    ID_ZPRAVA              NUMBER        not null
        constraint ZPRAVA_PK
            primary key,
    NAZEV                  VARCHAR2(50)  not null,
    TELO                   VARCHAR2(250) not null,
    DATUM_VYTVORENI        DATE          not null,
    ID_UZIVATEL_ODESILATEL NUMBER        not null
        constraint ODESILATEL_ZPRAVA
            references UZIVATELE,
    ID_UZIVATEL_PRIJEMCE   NUMBER
        constraint PRIJEMCE_ZPRAVA
            references UZIVATELE,
    ID_SKUPINA_PRIJEMCE    NUMBER
        constraint PRIJEMCE_SKUPINA
            references SKUPINY,
    ID_RODIC               NUMBER,
    ID_SOUBORU             NUMBER
        constraint ZPRAVY_SOUBORY_ID_SOUBORU_FK
            references SOUBORY
)
/

create or replace trigger ZPRAVY_TRIGGER
    before insert or update or delete
    on ZPRAVY
    for each row
BEGIN
    if (deleting) then
        INSERT INTO ZPRAVY_BACKUP(id_zprava, nazev, telo, datum_vytvoreni, id_uzivatel_odesilatel, id_uzivatel_prijemce,
                                  id_skupina_prijemce)
        VALUES (:old.id_zprava, :old.nazev, :old.telo, :old.datum_vytvoreni, :old.id_uzivatel_odesilatel,
                :old.id_uzivatel_prijemce, :old.id_skupina_prijemce);
    else
        if (LENGTH(:NEW.nazev) <= 0 or LENGTH(:NEW.nazev) > 30) then
            raise_application_error(-20006, 'Název zprávy nesmí být prázdný nebo větší jak 30 znaků');
        elsif (LENGTH(:NEW.telo) <= 0 or LENGTH(:NEW.telo) > 250) then
            raise_application_error(-20007, 'Tělo zprávy nesmí být prázdné nebo větší jak 150 znaků');
        end if;

        if (inserting) then
            :new.datum_vytvoreni := sysdate;
            SELECT increment_zpravy.nextval
            INTO :new.id_zprava
            FROM dual;
        end if;
    end if;

END;
/

create or replace trigger SOUBORY_TRIGGER
    before insert or update
    on SOUBORY
    for each row
BEGIN
    if (updating) then
        :new.upraveno := sysdate;
    end if;

    if (inserting) then
        :new.nahrano := sysdate;
        SELECT increment_soubory.nextval
        INTO :NEW.id_souboru
        FROM dual;
    end if;
END;
/

create table UZIVATELE_SKUPINY
(
    UZIVATELE_ID_UZIVATEL NUMBER not null
        constraint UZIVATELE_SKUPINY_UZIVATELE_ID_UZIVATEL_FK
            references UZIVATELE,
    SKUPINY_ID_SKUPINA    NUMBER not null
        constraint UZIVATELE_SKUPINY_SKUPINY_ID_SKUPINA_FK
            references SKUPINY,
    constraint UZIVATELE_SKUPINY_PK
        unique (UZIVATELE_ID_UZIVATEL, SKUPINY_ID_SKUPINA)
)
/

create table PRODUKTY
(
    ID_PRODUKTU NUMBER        not null
        constraint PRODUKT_PK
            primary key,
    NAZEV       VARCHAR2(255) not null,
    POPIS       VARCHAR2(255) not null,
    SKLADEM     NUMBER,
    TYP         VARCHAR2(100),
    CENA        NUMBER
)
/

create or replace trigger PRODUKTY_TRIGGER
    before insert or update
    on PRODUKTY
    for each row
BEGIN
    if (inserting) then
        SELECT increment_produkty.nextval
        INTO :NEW.id_produktu
        FROM dual;
    end if;
END;
/

create table TRANSAKCE
(
    ID_TRANSAKCE  NUMBER        not null
        constraint TRANSAKCE_PK
            primary key,
    ID_UZIVATELE  NUMBER        not null
        constraint TRANSAKCE_UZIVATELE_ID_UZIVATEL_FK
            references UZIVATELE,
    ID_PRODUKTU   NUMBER
        constraint TRANSAKCE_PRODUKT_ID_PRODUKTU_FK
            references PRODUKTY,
    TYP_TRANSAKCE VARCHAR2(100) not null,
    CASTKA        FLOAT         not null,
    DATUM         DATE          not null,
    POPIS         VARCHAR2(255) not null
)
/

create or replace trigger TRANSAKCE_TRIGGER
    before insert or update
    on TRANSAKCE
    for each row
BEGIN
    if (inserting) then
        SELECT increment_transakce.nextval
        INTO :NEW.id_transakce
        FROM dual;
    end if;
END;
/

create table JIDELNI_LISTKY
(
    ID_LISTKU NUMBER not null
        constraint JIDELNI_LISTEK_PK
            primary key,
    DATUM     DATE   not null
)
/

create unique index JIDELNI_LISTKY_DATUM_UINDEX
    on JIDELNI_LISTKY (DATUM)
/

create or replace trigger JIDELNI_LISTKY_TRIGGER
    before insert or update
    on JIDELNI_LISTKY
    for each row
BEGIN
    if (inserting) then
        SELECT increment_jidelni_listky.nextval
        INTO :NEW.id_listku
        FROM dual;
    end if;
END;
/

create table LISTEK_PRODUKT
(
    ID_PRODUKT NUMBER not null
        constraint LISTEK_PRODUKT_PRODUKTY_ID_PRODUKTU_FK
            references PRODUKTY
                on delete cascade,
    ID_LISTEK  NUMBER not null
        constraint LISTEK_PRODUKT_JIDELNI_LISTKY_ID_LISTKU_FK
            references JIDELNI_LISTKY
                on delete cascade
)
/

create or replace view GETOBORY as
SELECT o.id_obor    "id_obor",
       o.nazev      "nazev_obor",
       o.popis      "popis_obor",
       p.id_predmet "id_predmet",
       p.nazev      "nazev_predmet",
       p.popis      "popis_predmet"
from OBOR_PREDMET ob
         JOIN STUDIJNI_OBORY o ON o.id_obor = ob.studijni_obor_id_obor
         JOIN PREDMETY p on ob.predmet_id_predmet = p.id_predmet
/

create or replace view GETUZIVATELE as
SELECT u.id_uzivatel,
       u.jmeno,
       u.prijmeni,
       u.heslo,
       u.email,
       u.datum_vytvoreni,
       u.uzivatel_typ,
       u.avatar,
       s.rok_studia,
       so.id_obor,
       so.nazev "nazev_obor",
       so.popis "popis_obor",
       uc.katedra
FROM UZIVATELE u
         LEFT JOIN STUDENTI s ON u.id_uzivatel = s.id_uzivatel
         LEFT JOIN UCITELE uc on u.id_uzivatel = uc.id_uzivatel
         LEFT JOIN STUDIJNI_OBORY so ON s.id_obor = so.id_obor
/

create or replace view GETSTUDENTI as
SELECT u.id_uzivatel "id_uzivatel",
       u.jmeno,
       u.prijmeni,
       u.email,
       u.datum_vytvoreni,
       u.uzivatel_typ,
       u.avatar,
       s.rok_studia,
       so.id_obor,
       so.nazev,
       so.popis
FROM UZIVATELE u
         JOIN STUDENTI s ON u.id_uzivatel = s.id_uzivatel
         JOIN STUDIJNI_OBORY so ON s.id_obor = so.id_obor
/

create or replace view GETUCITELE as
SELECT u.id_uzivatel,
       u.jmeno,
       u.prijmeni,
       u.email,
       u.datum_vytvoreni,
       u.uzivatel_typ,
       u.avatar,
       uc.katedra
FROM UZIVATELE u
         JOIN UCITELE uc ON u.id_uzivatel = uc.id_uzivatel
/

create or replace view GETSKUPINY as
SELECT s.id_skupina,
       s.nazev "nazev_skupina",
       s.popis "popis_skupina"
FROM SKUPINY S
/

create or replace view GETUZIVATELEVESKUPINE as
SELECT u.id_uzivatel,
       u.jmeno,
       u.prijmeni,
       u.heslo,
       u.email,
       u.datum_vytvoreni,
       u.uzivatel_typ,
       u.avatar,
       u.rok_studia,
       u.id_obor,
       so.nazev "nazev_obor",
       so.popis "popis_obor",
       u.katedra,
       s.id_skupina
FROM getUzivatele u
         left join UZIVATELE_SKUPINY us on u.ID_UZIVATEL = us.UZIVATELE_ID_UZIVATEL
         LEFT JOIN STUDIJNI_OBORY so ON u.id_obor = so.id_obor
         join SKUPINY s on us.SKUPINY_ID_SKUPINA = s.ID_SKUPINA
/

create or replace view GETHODNOCENI as
SELECT h.id_hodnoceni,
       h.hodnota_hodnoceni,
       h.popis,
       u."ID_UZIVATEL",
       u."JMENO",
       u."PRIJMENI",
       u."HESLO",
       u."EMAIL",
       u."DATUM_VYTVORENI",
       u."UZIVATEL_TYP",
       u."AVATAR",
       u."ROK_STUDIA",
       u."ID_OBOR",
       u."nazev_obor",
       u."popis_obor",
       u."KATEDRA",
       g."ID_SKUPINA",
       g."nazev_skupina",
       g."popis_skupina"
FROM HODNOCENI h
         JOIN (select * from getUzivatele) u ON u.id_uzivatel = h.id_uzivatel
         JOIN (select * from getSkupiny) g ON g.id_skupina = h.id_skupina
/

create or replace view GETVYUCOVANEPREDMETY as
SELECT uc.ID_UZIVATEL,
       p.ID_PREDMET,
       p.NAZEV,
       p.POPIS
from UCITELE uc
         join UCITELE_PREDMETY U on uc.ID_UZIVATEL = U.UCITELE_ID_UCITEL
         join PREDMETY P on U.PREDMET_ID_PREDMET = P.ID_PREDMET
/

create or replace view GETPREDMETYVESKUPINE as
SELECT s.id_skupina,
       p.ID_PREDMET,
       p.NAZEV,
       p.POPIS
from SKUPINY s
         join SKUPINY_PREDMETY sp on s.id_skupina = sp.SKUPINY_ID_SKUPINA
         join PREDMETY P on sp.PREDMETY_ID_PREDMET = P.ID_PREDMET
/

create or replace view GETZPRAVYHIERARCHICKY as
SELECT ID_ZPRAVA,
       nazev,
       telo,
       ID_UZIVATEL_ODESILATEL,
       ID_UZIVATEL_PRIJEMCE,
       ID_SKUPINA_PRIJEMCE,
       DATUM_VYTVORENI,
       ID_RODIC,
       ID_SOUBORU,
       level as Uroven
FROM ZPRAVY
CONNECT BY ID_RODIC = PRIOR ID_ZPRAVA
START WITH ID_RODIC IS NULL
/

create or replace view GETSKUPINYUZIVATELE as
SELECT UZIVATELE_ID_UZIVATEL, id_skupina, nazev "nazev_skupina", popis "popis_skupina"
FROM UZIVATELE_SKUPINY
         JOIN SKUPINY S on UZIVATELE_SKUPINY.SKUPINY_ID_SKUPINA = S.ID_SKUPINA
/

create or replace view GETUCITELESPREDMETEM as
SELECT u.id_uzivatel,
       u.jmeno,
       u.prijmeni,
       u.heslo,
       u.email,
       u.datum_vytvoreni,
       u.uzivatel_typ,
       u.avatar,
       u.rok_studia,
       u.id_obor,
       so.nazev "nazev_obor",
       so.popis "popis_obor",
       u.katedra,
       s.id_predmet
FROM getUzivatele u
         left join UCITELE_PREDMETY up on u.ID_UZIVATEL = up.UCITELE_ID_UCITEL
         LEFT JOIN STUDIJNI_OBORY so ON u.id_obor = so.id_obor
         join predmety s on up.PREDMET_ID_PREDMET = s.ID_PREDMET
/

create or replace view GETOBORYWITHSUBJECT as
SELECT o.id_obor    "id_obor",
       o.nazev      "nazev_obor",
       o.popis      "popis_obor",
       p.id_predmet "id_predmet"
from OBOR_PREDMET ob
         JOIN STUDIJNI_OBORY o ON o.id_obor = ob.studijni_obor_id_obor
         JOIN PREDMETY p on ob.predmet_id_predmet = p.id_predmet
/

create or replace view GETOBORYSPREDMETEM as
SELECT o.id_obor,
       o.nazev,
       o.popis,
       p.id_predmet
from OBOR_PREDMET ob
         JOIN STUDIJNI_OBORY o ON o.id_obor = ob.studijni_obor_id_obor
         JOIN PREDMETY p on ob.predmet_id_predmet = p.id_predmet
/

create or replace view GETSKUPINYPREDMETU as
SELECT id_skupina, nazev "nazev_skupina", popis "popis_skupina", PREDMETY_ID_PREDMET
FROM SKUPINY_PREDMETY
         JOIN SKUPINY S on SKUPINY_ID_SKUPINA = S.ID_SKUPINA
/

create or replace view GETUZIVATELODESILATEL as
SELECT u.id_uzivatel     "id_odesilatel",
       u.jmeno           "jmeno_odesilatel",
       u.prijmeni        "prijmeni_odesilatel",
       u.heslo           "heslo_odesilatel",
       u.email           "email_odesilatel",
       u.datum_vytvoreni "datum_vytvoreni_odesilatel",
       u.uzivatel_typ    "uzivatel_typ_odesilatel",
       u.avatar          "avatar_odesilatel",
       s.rok_studia      "rok_studia_odesilatel",
       so.id_obor        "id_obor_odesilatel",
       so.nazev          "nazev_obor_odesilatel",
       so.popis          "popis_obor_odesilatel",
       uc.katedra        "katedra_odesilatel"
FROM UZIVATELE u
         LEFT JOIN STUDENTI s ON u.id_uzivatel = s.id_uzivatel
         LEFT JOIN UCITELE uc on u.id_uzivatel = uc.id_uzivatel
         LEFT JOIN STUDIJNI_OBORY so ON s.id_obor = so.id_obor
/

create or replace view GETUZIVATELPRIJEMCE as
SELECT u.id_uzivatel     "id_prijemce",
       u.jmeno           "jmeno_prijemce",
       u.prijmeni        "prijmeni_prijemce",
       u.heslo           "heslo_prijemce",
       u.email           "email_prijemce",
       u.datum_vytvoreni "datum_vytvoreni_prijemce",
       u.uzivatel_typ    "uzivatel_typ_prijemce",
       u.avatar          "avatar_prijemce",
       s.rok_studia      "rok_studia_prijemce",
       so.id_obor        "id_obor_prijemce",
       so.nazev          "nazev_obor_prijemce",
       so.popis          "popis_obor_prijemce",
       uc.katedra        "katedra_prijemce"
FROM UZIVATELE u
         LEFT JOIN STUDENTI s ON u.id_uzivatel = s.id_uzivatel
         LEFT JOIN UCITELE uc on u.id_uzivatel = uc.id_uzivatel
         LEFT JOIN STUDIJNI_OBORY so ON s.id_obor = so.id_obor
/

create or replace view GETVSECHNYZPRAVY as
SELECT uo."id_odesilatel",
       up."id_prijemce"
FROM ZPRAVY z
         left join getUzivatelOdesilatel uo ON z.ID_UZIVATEL_ODESILATEL = uo."id_odesilatel"
         left join getUzivatelPrijemce up ON z.ID_UZIVATEL_PRIJEMCE = up."id_prijemce"
         left join GETSKUPINY sk on z.ID_SKUPINA_PRIJEMCE = sk.ID_SKUPINA
         left join ZPRAVY zp on z.ID_ZPRAVA = zp.ID_RODIC
         left join SOUBORY s on z.ID_SOUBORU = s.ID_SOUBORU
/

create or replace PROCEDURE insert_hodnoceni(hodnoceni_in in INTEGER, popis_in in VARCHAR2, id_uzivatel_in in INTEGER,
                                             id_skupina_in in INTEGER)
    IS
BEGIN
    INSERT INTO HODNOCENI(hodnota_hodnoceni, popis, id_uzivatel, id_skupina)
    VALUES (hodnoceni_in, popis_in, id_uzivatel_in, id_skupina_in);
END;
/

create or replace PROCEDURE insert_zprava(nazev_in in VARCHAR2, telo_in in VARCHAR2, datum_vytvoreni_in in DATE,
                                          id_odesilatel_in INTEGER, id_prijemce_in INTEGER, id_skupina_in INTEGER)
    IS
BEGIN
    INSERT INTO ZPRAVY(nazev, telo, datum_vytvoreni, id_uzivatel_odesilatel, id_uzivatel_prijemce,
                       id_skupina_prijemce)
    VALUES (nazev_in, telo_in, datum_vytvoreni_in, id_odesilatel_in, id_prijemce_in, id_skupina_in);
END;
/

create or replace PROCEDURE insert_predmet(nazev_in in VARCHAR2, popis_in in VARCHAR2)
    IS
BEGIN
    INSERT INTO PREDMETY(nazev, popis)
    VALUES (nazev_in, popis_in);
END;
/

create or replace PROCEDURE insert_studijni_obor(nazev_in in VARCHAR2, popis_in in VARCHAR2)
    IS
BEGIN
    INSERT INTO STUDIJNI_OBORY(nazev, popis)
    VALUES (nazev_in, popis_in);
END;
/

create or replace PROCEDURE insert_obor_predmet(id_obor_in INTEGER, id_predmet_in INTEGER)
    IS
BEGIN
    INSERT INTO OBOR_PREDMET(studijni_obor_id_obor, predmet_id_predmet)
    VALUES (id_obor_in, id_predmet_in);
END;
/

create or replace PROCEDURE insert_skupina(nazev_in in VARCHAR2, popis_in in VARCHAR2)
    IS
BEGIN
    INSERT INTO SKUPINY(nazev, popis)
    VALUES (nazev_in, popis_in);
END;
/

create or replace PROCEDURE delete_predmet(id_in IN NUMBER)
    IS
BEGIN
    DELETE FROM SKUPINY_PREDMETY WHERE PREDMETY_ID_PREDMET = id_in;
    DELETE FROM UCITELE_PREDMETY WHERE PREDMET_ID_PREDMET = id_in;
    DELETE FROM obor_predmet WHERE PREDMET_ID_PREDMET = id_in;
    DELETE FROM PREDMETY WHERE PREDMETY.ID_PREDMET = id_in;
END;
/

create or replace PROCEDURE delete_hodnoceni(id_in IN NUMBER)
    IS
BEGIN
    DELETE FROM HODNOCENI WHERE HODNOCENI.id_hodnoceni = id_in;
END;
/

create or replace PROCEDURE delete_zprava(id_in IN NUMBER)
    IS
BEGIN
    DELETE FROM ZPRAVY WHERE ZPRAVY.ID_ZPRAVA = id_in;
END;
/

create or replace PROCEDURE delete_obor_predmet(id_obor_in integer, id_predmet_in integer)
    IS
BEGIN
    DELETE
    FROM OBOR_PREDMET
    WHERE OBOR_PREDMET.PREDMET_ID_PREDMET = id_predmet_in
      AND OBOR_PREDMET.studijni_obor_id_obor = id_obor_in;
END;
/

create or replace PROCEDURE delete_uzivatel_skupina(user_id_in IN NUMBER, group_id_in IN NUMBER)
    IS
BEGIN
    DELETE
    FROM UZIVATELE_SKUPINY us
    WHERE us.UZIVATELE_ID_UZIVATEL = user_id_in
      and us.SKUPINY_ID_SKUPINA = group_id_in;
END;
/

create or replace PROCEDURE insert_student(jmeno_in IN VARCHAR2, prijmeni_in IN VARCHAR2, email_in VARCHAR2,
                                           heslo_in VARCHAR2, datum_vytvoreni_in DATE, rok_studia_in VARCHAR2,
                                           id_obor_in INTEGER)
    IS
    user_id INTEGER;
BEGIN
    INSERT INTO UZIVATELE(jmeno, prijmeni, email, heslo, datum_vytvoreni, uzivatel_typ)
    VALUES (jmeno_in, prijmeni_in, email_in, heslo_in, datum_vytvoreni_in, 'student');
    SELECT MAX(id_uzivatel) into user_id from UZIVATELE;
    INSERT INTO STUDENTI(id_uzivatel, rok_studia, id_obor)
    VALUES (user_id, rok_studia_in, id_obor_in);

END;
/

create or replace PROCEDURE insert_ucitel(jmeno_in IN VARCHAR2, prijmeni_in IN VARCHAR2, email_in VARCHAR2,
                                          heslo_in VARCHAR2, datum_vytvoreni_in DATE, katedra_in VARCHAR2)
    IS
    user_id INTEGER;
BEGIN
    INSERT INTO UZIVATELE(jmeno, prijmeni, email, heslo, datum_vytvoreni, uzivatel_typ)
    VALUES (jmeno_in, prijmeni_in, email_in, heslo_in, datum_vytvoreni_in, 'ucitel');
    SELECT max(id_uzivatel) into user_id from UZIVATELE;
    INSERT INTO UCITELE(id_uzivatel, katedra)
    VALUES (user_id, katedra_in);
END;
/

create or replace PROCEDURE delete_ucitel(id_in IN NUMBER)
    IS
BEGIN
    DELETE FROM UCITELE WHERE UCITELE.id_uzivatel = id_in;
    DELETE FROM ZPRAVY WHERE zpravy.id_uzivatel_odesilatel = id_in OR id_uzivatel_prijemce = id_in;
    DELETE FROM UZIVATELE_SKUPINY WHERE UZIVATELE_ID_UZIVATEL = id_in;
    DELETE FROM HODNOCENI WHERE HODNOCENI.ID_UZIVATEL = id_in;
    DELETE FROM UZIVATELE WHERE UZIVATELE.id_uzivatel = id_in;
END;
/

create or replace PROCEDURE delete_student(id_in IN NUMBER)
    IS
BEGIN
    DELETE FROM STUDENTI WHERE STUDENTI.id_uzivatel = id_in;
    DELETE FROM ZPRAVY WHERE zpravy.id_uzivatel_odesilatel = id_in OR id_uzivatel_prijemce = id_in;
    DELETE FROM UZIVATELE_SKUPINY WHERE UZIVATELE_ID_UZIVATEL = id_in;
    DELETE FROM HODNOCENI WHERE HODNOCENI.ID_UZIVATEL = id_in;
    DELETE FROM UZIVATELE WHERE UZIVATELE.id_uzivatel = id_in;
END;
/

create or replace PROCEDURE delete_admin(id_in IN NUMBER)
    IS
BEGIN
    DELETE FROM ZPRAVY WHERE zpravy.id_uzivatel_odesilatel = id_in OR id_uzivatel_prijemce = id_in;
    DELETE FROM UZIVATELE_SKUPINY WHERE UZIVATELE_ID_UZIVATEL = id_in;
    DELETE FROM HODNOCENI WHERE HODNOCENI.ID_UZIVATEL = id_in;
    DELETE FROM UZIVATELE WHERE UZIVATELE.id_uzivatel = id_in;
END;
/

create or replace PROCEDURE delete_field(id_in IN NUMBER)
    IS
BEGIN
    DELETE FROM STUDENTI WHERE STUDENTI.id_obor = id_in;
    DELETE FROM OBOR_PREDMET WHERE OBOR_PREDMET.studijni_obor_id_obor = id_in;
    DELETE FROM STUDIJNI_OBORY WHERE STUDIJNI_OBORY.id_obor = id_in;
END;
/

create or replace PROCEDURE insert_uzivatele_skupiny(id_uzivatel_in INTEGER, id_skupina_in INTEGER)
    IS
BEGIN
    INSERT INTO UZIVATELE_SKUPINY(uzivatele_id_uzivatel, skupiny_id_skupina)
    VALUES (id_uzivatel_in, id_skupina_in);
END;
/

create or replace FUNCTION fnc_zahashuj_uzivatele(uziv_jmeno_in in varchar2, heslo_in in varchar2) return varchar2
    IS
BEGIN
    RETURN ltrim(to_char(dbms_utility.get_hash_value(upper(uziv_jmeno_in) || '/' || upper(heslo_in),
                                                     1000000000, power(2, 30)),
                         rpad('X', 29, 'X') || 'X'));
END;
/

create or replace FUNCTION fnc_prumer_hodnoceni(id integer)
    RETURN number
    IS
    prumer number;
BEGIN
    SELECT AVG(hodnota_hodnoceni) into prumer FROM getHodnoceni where id_skupina = id;
    return prumer;
END;
/

create or replace FUNCTION fnc_get_nejlepe_hodnocenou_skupinu
    RETURN integer
    IS
    max_hodnota  integer := 0;
    max_id       integer;
    temp_hodnota integer;
begin
    FOR id IN (SELECT id_skupina from skupiny)
        LOOP
            temp_hodnota := fnc_prumer_hodnoceni(id.id_skupina);
            if (temp_hodnota > max_hodnota) then
                max_hodnota := temp_hodnota; max_id := id.id_skupina;
            end if;
        end loop;
    return max_id;
end;
/

create or replace PROCEDURE insert_skupiny_predmety(id_skupina_in in INTEGER, id_predmet_in in INTEGER)
    IS
BEGIN
    INSERT INTO SKUPINY_PREDMETY(SKUPINY_ID_SKUPINA, PREDMETY_ID_PREDMET)
    VALUES (id_skupina_in, id_predmet_in);
END;
/

create or replace FUNCTION fnc_pocet_uzivatelu_ve_skupine(id_in integer)
    RETURN number
    IS
    pocet number;
BEGIN
    select count(*) into pocet from UZIVATELE_SKUPINY where SKUPINY_ID_SKUPINA = id_in;
    return pocet;
END;
/

create or replace view GETPOCTYVESKUPINACH as
SELECT id_skupina,
       nazev                                         "nazev_skupina",
       popis                                         "popis_skupina",
       fnc_pocet_uzivatelu_ve_skupine(id_skupina) as "pocet_skupina"
from SKUPINY
/

create or replace PROCEDURE insert_predmet_ucitel(id_ucitel_in INTEGER, id_predmet_in INTEGER)
    IS
BEGIN
    INSERT INTO UCITELE_PREDMETY(UCITELE_ID_UCITEL, PREDMET_ID_PREDMET)
    VALUES (id_ucitel_in, id_predmet_in);
END;
/

create or replace PROCEDURE delete_skupina(id_in IN NUMBER)
    IS
BEGIN
    DELETE FROM ZPRAVY WHERE zpravy.id_skupina_prijemce = id_in;
    DELETE FROM UZIVATELE_SKUPINY WHERE skupiny_id_skupina = id_in;
    DELETE FROM SKUPINY_PREDMETY WHERE skupiny_id_skupina = id_in;
    DELETE FROM HODNOCENI WHERE HODNOCENI.ID_SKUPINA = id_in;
    DELETE FROM SKUPINY WHERE SKUPINY.id_skupina = id_in;
END;
/

create or replace PROCEDURE update_skupina(id integer, nazev_in in VARCHAR2, popis_in in VARCHAR2)
    IS
BEGIN
    UPDATE SKUPINY s SET s.nazev = nazev_in, s.popis = popis_in WHERE s.id_skupina = id;
END;
/

create or replace PROCEDURE update_predmet(id integer, nazev_in in VARCHAR2, popis_in in VARCHAR2)
    IS
BEGIN
    UPDATE predmety p SET p.nazev = nazev_in, p.popis = popis_in WHERE p.id_predmet = id;
END;
/

create or replace PROCEDURE delete_skupiny_predmety(id_predmet_in integer, id_skupina_in integer)
    IS
BEGIN
    DELETE FROM SKUPINY_PREDMETY WHERE PREDMETY_ID_PREDMET = id_predmet_in AND SKUPINY_ID_SKUPINA = id_skupina_in;
END;
/

create or replace PROCEDURE delete_ucitele_predmety(id_predmet_in integer, id_ucitel_in integer)
    IS
BEGIN
    DELETE FROM UCITELE_PREDMETY WHERE PREDMET_ID_PREDMET = id_predmet_in AND UCITELE_ID_UCITEL = id_ucitel_in;
END;
/

create or replace PROCEDURE update_hodnoceni(id_hodnoceni_in in INTEGER, hodnoceni_in in INTEGER, popis_in in VARCHAR2,
                                             id_uzivatel_in in INTEGER,
                                             id_skupina_in in INTEGER)
    IS
BEGIN
    UPDATE hodnoceni h
    SET h.hodnota_hodnoceni = hodnoceni_in,
        h.popis             = popis_in,
        h.id_uzivatel       = id_uzivatel_in,
        h.id_skupina        = id_skupina_in
    WHERE h.id_hodnoceni = id_hodnoceni_in;
END;
/

create or replace PROCEDURE update_zprava(id_in integer, nazev_in in VARCHAR2, obsah_in in VARCHAR2, datum_in date,
                                          id_odesilatel_in integer, id_prijemce_uzivatel_in integer,
                                          id_prijemce_skupina_in integer,
                                          id_rodic_in integer, id_soubor_in integer)
    IS
BEGIN
    UPDATE ZPRAVY z
    SET z.nazev                  = nazev_in,
        z.telo                   = obsah_in,
        z.datum_vytvoreni        = datum_in,
        z.id_uzivatel_odesilatel = id_odesilatel_in,
        z.id_uzivatel_prijemce   = id_prijemce_uzivatel_in,
        z.id_skupina_prijemce    = id_prijemce_skupina_in,
        z.id_rodic               = id_rodic_in,
        z.id_souboru             = id_soubor_in
    WHERE z.id_zprava = id_in;
END;
/

create or replace PROCEDURE getLikes(id_msg integer)
    IS
BEGIN
    select COUNT(*) FROM OBLIBENE_ZPRAVY where ID_ZPRAVA = id_msg;
END;
/

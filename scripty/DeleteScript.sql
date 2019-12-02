drop table 	UZIVATEL_SKUPINA	cascade constraints;
drop table 	ZPRAVY	cascade constraints;
drop table 	UZIVATELE	cascade constraints;
drop table 	STUDIJNI_OBORY	cascade constraints;
drop table 	UCITELE	cascade constraints;
drop table 	STUDENTI	cascade constraints;
drop table 	PREDMETY	cascade constraints;
drop table 	SKUPINY	cascade constraints;
drop table 	HODNOCENI	cascade constraints;
drop table 	OBOR_PREDMET	cascade constraints;

drop sequence INCREMENT_HODNOCENI;
drop sequence INCREMENT_OBORY;
drop sequence INCREMENT_PREDMETY;
drop sequence INCREMENT_SKUPINY;
drop sequence INCREMENT_UZIVATELE;
drop sequence INCREMENT_ZPRAVY;

drop view getUsers;
drop view getStudents;
drop view getTeachers;
drop view getGroups;
drop view getUsersInGroups;
drop view getRatings;
drop view getFields;

drop PROCEDURE delete_student;
drop PROCEDURE delete_ucitel;
drop PROCEDURE delete_admin;
drop PROCEDURE delete_group;
drop PROCEDURE insert_student;
drop PROCEDURE insert_ucitel;
drop PROCEDURE delete_field;

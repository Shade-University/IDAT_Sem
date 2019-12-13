
package model;

import data.OracleConnection;
import java.sql.Date;

import java.time.LocalDateTime;

/**
 *
 * @author Tomáš Vondra
 */
public class Message {

    private final int id;
    private final String nazev;
    private final String obsah;
    private final Date datum_vytvoreni;
    private final User odesilatel;
    
    private User prijemce_uzivatel;
    private Group prijemce_skupina;

    private Message rodic;
    private File soubor;

    public Message(String nazev, String obsah, User odesilatel, User prijemce_uzivatel, Group prijemce_skupina){
        this(-1, nazev, obsah, odesilatel, prijemce_uzivatel, prijemce_skupina,
                OracleConnection.parseDate(LocalDateTime.now().toString(), "yyyy-MM-dd'T'HH:mm:ss"));
    }
    public Message(String nazev, String obsah, User odesilatel, User prijemce_uzivatel, Group prijemce_skupina, Date datum_vytvoreni){
        this(-1, nazev, obsah, odesilatel, prijemce_uzivatel, prijemce_skupina,
                datum_vytvoreni);
    }
    public Message(int id, String nazev, String obsah, User odesilatel, User prijemce_uzivatel, Group prijemce_skupina, Date datum_vytvoreni) {
        this.id = id;
        this.nazev = nazev;
        this.obsah = obsah;
        this.datum_vytvoreni = datum_vytvoreni;
        this.odesilatel = odesilatel;
        this.prijemce_uzivatel = prijemce_uzivatel;
        this.prijemce_skupina = prijemce_skupina;
    }

    public Message(int id, String nazev, String obsah, User odesilatel, User prijemce_uzivatel, Group prijemce_skupina, Date datum_vytvoreni, Message rodic, File soubor) {
        this.id = id;
        this.nazev = nazev;
        this.obsah = obsah;
        this.datum_vytvoreni = datum_vytvoreni;
        this.odesilatel = odesilatel;
        this.prijemce_uzivatel = prijemce_uzivatel;
        this.prijemce_skupina = prijemce_skupina;
        this.rodic = rodic;
        this.soubor = soubor;
    }

    public int getId() {
        return id;
    }

    public String getNazev() {
        return nazev;
    }

    public String getObsah() {
        return obsah;
    }

    public Date getDatum_vytvoreni() {
        return datum_vytvoreni;
    }

    public User getOdesilatel() {
        return odesilatel;
    }

    public User getPrijemce_uzivatel() {
        return prijemce_uzivatel;
    }

    public Group getPrijemce_skupina() {
        return prijemce_skupina;
    }

    @Override
    public String toString() {
        return getOdesilatel() + " => " + getObsah();
    }
    
    
}


package model;

import data.OracleConnection;
import java.sql.Date;

import java.time.LocalDateTime;

/**
 *
 * @author Tomáš Vondra
 */
public class Zprava {

    private final int id;
    private final String nazev;
    private final String obsah;
    private final Date datum_vytvoreni;
    private final Uzivatel odesilatel;
    
    private Uzivatel prijemce_uzivatel;
    private Skupina prijemce_skupina;

    public Zprava(String nazev, String obsah, Uzivatel odesilatel, Uzivatel prijemce_uzivatel, Skupina prijemce_skupina){
        this(-1, nazev, obsah, odesilatel, prijemce_uzivatel, prijemce_skupina,
                OracleConnection.parseDate(LocalDateTime.now().toString(), "yyyy-MM-dd'T'HH:mm:ss"));
    }
    public Zprava(String nazev, String obsah, Uzivatel odesilatel, Uzivatel prijemce_uzivatel, Skupina prijemce_skupina, Date datum_vytvoreni){
        this(-1, nazev, obsah, odesilatel, prijemce_uzivatel, prijemce_skupina,
                datum_vytvoreni);
    }
    public Zprava(int id, String nazev, String obsah, Uzivatel odesilatel, Uzivatel prijemce_uzivatel, Skupina prijemce_skupina, Date datum_vytvoreni) {
        this.id = id;
        this.nazev = nazev;
        this.obsah = obsah;
        this.datum_vytvoreni = datum_vytvoreni;
        this.odesilatel = odesilatel;
        this.prijemce_uzivatel = prijemce_uzivatel;
        this.prijemce_skupina = prijemce_skupina;
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

    public Uzivatel getOdesilatel() {
        return odesilatel;
    }

    public Uzivatel getPrijemce_uzivatel() {
        return prijemce_uzivatel;
    }

    public Skupina getPrijemce_skupina() {
        return prijemce_skupina;
    }

    @Override
    public String toString() {
        return getOdesilatel() + " => " + getObsah();
    }
    
    
}

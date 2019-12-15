
package model;

import data.OracleConnection;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Tomáš Vondra
 */
public class Message {

    private int id;
    private String nazev;
    private String obsah;
    private Date datum_vytvoreni;
    private User odesilatel;

    private User prijemce_uzivatel;
    private Group prijemce_skupina;

    private int rodic;
    private File soubor;

    public Message(String nazev, String obsah, User odesilatel, User prijemce_uzivatel, Group prijemce_skupina) {
        this(-1, nazev, obsah, odesilatel, prijemce_uzivatel, prijemce_skupina,
                OracleConnection.parseDate(LocalDateTime.now().toString(), "yyyy-MM-dd'T'HH:mm:ss"));
    } //Konstruktor pro vytváření

    public Message(String nazev, String obsah, User odesilatel, User prijemce_uzivatel, Group prijemce_skupina, Date datum_vytvoreni) {
        this(-1, nazev, obsah, odesilatel, prijemce_uzivatel, prijemce_skupina,
                datum_vytvoreni);
    } //Konstruktor pro vytváření s datumem

    public Message(int id, String nazev, String obsah, User odesilatel, User prijemce_uzivatel, Group prijemce_skupina, Date datum_vytvoreni) {
        this.id = id;
        this.nazev = nazev;
        this.obsah = obsah;
        this.datum_vytvoreni = datum_vytvoreni;
        this.odesilatel = odesilatel;
        this.prijemce_uzivatel = prijemce_uzivatel;
        this.prijemce_skupina = prijemce_skupina;
    } //Konstrutor pro načítání

    public Message(int id, String nazev, String obsah, User odesilatel, User prijemce_uzivatel, Group prijemce_skupina, Date datum_vytvoreni, int rodic, File soubor) {
        this.id = id;
        this.nazev = nazev;
        this.obsah = obsah;
        this.datum_vytvoreni = datum_vytvoreni;
        this.odesilatel = odesilatel;
        this.prijemce_uzivatel = prijemce_uzivatel;
        this.prijemce_skupina = prijemce_skupina;
        this.rodic = rodic;
        this.soubor = soubor;
    } //Konstruktor pro načítání se zanořením

    public Message(){}

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

    public int getRodic() {
        return rodic;
    }

    public File getSoubor() {
        return soubor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public void setObsah(String obsah) {
        this.obsah = obsah;
    }

    public void setOdesilatel(User us) {
        this.odesilatel = us;
    }

    public void setDatum_vytvoreni(Date date) {
        this.datum_vytvoreni = date;
    }

    public void setPrijemce_uzivatel(User us) {
        this.prijemce_uzivatel = us;
    }

    public void setPrijemce_skupina(Group gp) {
        this.prijemce_skupina = gp;
    }

    public void setRodic(Message msg) {
        if (msg == null) {
            this.rodic = 0;
        } else {
            this.rodic = msg.getId();
        }
    }

    public void setSoubor(File fl) {
        this.soubor = fl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id &&
                rodic == message.rodic &&
                nazev.equals(message.nazev) &&
                obsah.equals(message.obsah) &&
                datum_vytvoreni.equals(message.datum_vytvoreni) &&
                odesilatel.equals(message.odesilatel) &&
                prijemce_uzivatel.equals(message.prijemce_uzivatel) &&
                prijemce_skupina.equals(message.prijemce_skupina) &&
                soubor.equals(message.soubor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nazev, obsah, datum_vytvoreni, odesilatel, prijemce_uzivatel, prijemce_skupina, rodic, soubor);
    }

    @Override
    public String toString() {
        return getOdesilatel() + " => " + getObsah();
    }


}

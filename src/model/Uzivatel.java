package model;

import data.OracleConnection;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 *
 * @author Tomáš Vondra
 */
public class Uzivatel {

    private int id;
    private String jmeno;
    private String heslo;
    private String prijmeni;
    private String email;
    private final Date datum_vytvoreni;
    private final String uzivatelskyTyp; //TODO Enum ?

    public Uzivatel(String jmeno, String prijmeni, String email, String typ, String heslo) {
        this(-1, jmeno, prijmeni, email,
                OracleConnection.parseDate(LocalDateTime.now().toString(), "yyyy-MM-dd'T'HH:mm:ss"),
                typ);
        this.heslo = heslo;
        //Pokud vytvářím uživatele, ještě neznám jeho id, takže nastavím -1 a při insertu ho nastavím
        //Také automaticky vytvořím datum vytvoření
        //TODO Přemejšlím nad tím, že bych volal insert do databáze už tady
    }

    public Uzivatel(int id, String jmeno, String prijmeni, String email, Date datum_vytvoreni, String typ) {
        this.id = id;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.datum_vytvoreni = datum_vytvoreni;
        this.uzivatelskyTyp = typ;

        //TODO Zde nemám nastavování hesla. Nezískávám heslo z databáze kvůli bezpečnosti
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeslo() {
        return heslo;
    }

    public void setHeslo(String heslo) {
        this.heslo = heslo;
    } //Není v konstruktoru, můžeme načítat uživatele i bez hesla, bezpečnost

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public String getEmail() {
        return email;
    }

    public Date getDatum_vytvoreni() {
        return datum_vytvoreni;
    }

    public String getUzivatelskyTyp() {
        return uzivatelskyTyp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Uzivatel other = (Uzivatel) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getUzivatelskyTyp() + ": " + getJmeno() + " " + getPrijmeni();
    }

}


package model;

import java.sql.Date;

/**
 *
 * @author Tomáš Vondra
 */
public class Ucitel extends Uzivatel {

    private final Predmet vyucujici_Predmet;
    private final String katedra;

    public String getKatedra() {
        return katedra;
    }

    public Predmet getVyucujici_Predmet() {
        return vyucujici_Predmet;
    }
    
    public Ucitel(Predmet vyucujici_Predmet, String katedra, String jmeno, String prijmeni, String email, String heslo){
        super(jmeno, prijmeni, email, "ucitel", heslo);
        this.vyucujici_Predmet = vyucujici_Predmet;
        this.katedra = katedra;
    } //Konstruktor pro vytváření

    public Ucitel(Predmet vyucujici_Predmet, String katedra, int id, String jmeno, String prijmeni, String email, Date datum_vytvoreni) {
        super(id, jmeno, prijmeni, email, datum_vytvoreni, "ucitel");
        this.vyucujici_Predmet = vyucujici_Predmet;
        this.katedra = katedra;
    } //Konstruktor pro načítání
}

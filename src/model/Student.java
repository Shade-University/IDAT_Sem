
package model;

import java.sql.Date;

/**
 *
 * @author Tomáš Vondra
 */
public class Student extends Uzivatel {

   private final Obor obor;
   private final String rokStudia;

    public String getRokStudia() {
        return rokStudia;
    }

    public Obor getObor() {
        return obor;
    }
    
    public Student(Obor obor, String rok_studia, String jmeno, String prijmeni, String email, String heslo){
        super(jmeno, prijmeni, email, "student", heslo);
        this.obor = obor;
        this.rokStudia = rok_studia;
    } //Konstruktor pro vytváření

    public Student(Obor obor, String rok_studia, int id, String jmeno, String prijmeni, String email, Date datum_vytvoreni) {
        super(id, jmeno, prijmeni, email, datum_vytvoreni, "student");
        this.obor = obor;
        this.rokStudia = rok_studia;
    } //Konstruktor pro načítání
}


package model;

/**
 *
 * @author Tomáš Vondra
 */
public class Obor {

    private int id;
    private String nazev;
    private String popis;
    
    public Obor(String nazev, String popis){
        this(-1, nazev, popis);
    }

    public Obor(int id, String nazev, String popis) {
        this.id = id;
        this.nazev = nazev;
        this.popis = popis;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }
    
    

    public int getId() {
        return id;
    }

    public String getNazev() {
        return nazev;
    }

    public String getPopis() {
        return popis;
    }

    @Override
    public String toString() {
        //return getNazev();
        return getId() + " " + getNazev();
    }
    
}

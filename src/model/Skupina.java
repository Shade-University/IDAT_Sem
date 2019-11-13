package model;


/**
 *
 * @author Tomáš Vondra
 */
public class Skupina {

    private int id;
    private String nazev;
    private String popis;
    private Predmet predmet;

    public Skupina(String nazev, String popis, Predmet predmet){
        this(-1, nazev, popis, predmet);
    }
    public Skupina(int id, String nazev, String popis, Predmet predmet) {
        this.id = id;
        this.nazev = nazev;
        this.popis = popis;
        this.predmet = predmet;

    }

    public int getId() {
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }

    public String getNazev() {
        return nazev;
    }

    public String getPopis() {
        return popis;
    }

    public Predmet getPredmet() {
        return predmet;
    }

    @Override
    public String toString() {
        return getNazev();
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public void setPredmet(Predmet predmet) {
        this.predmet = predmet;
    }
    

}

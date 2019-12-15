
package model;


public class Rating {

    private int id;
    private final int hodnota;
    private final String popis;
    private final User hodnoticiUzivatel;
    private final Group hodnoticiSkupina;
    
    public Rating(int hodnota_hodnoceni, String popis, User hodnotici_Uzivatel, Group hodnocena_skupina) {
        this(-1, hodnota_hodnoceni, popis, hodnotici_Uzivatel, hodnocena_skupina);
    } //Konstruktor pro vytváření

    public Rating(int id, int hodnota_hodnoceni, String popis, User hodnotici_Uzivatel, Group hodnocena_skupina) {
        this.id = id;
        this.hodnota = hodnota_hodnoceni;
        this.popis = popis;
        this.hodnoticiUzivatel = hodnotici_Uzivatel;
        this.hodnoticiSkupina = hodnocena_skupina;
    } //Konstruktor pro načítání

    public int getId() {
        return id;
    }

    public int getHodnota() {
        return hodnota;
    }

    public String getPopis() {
        return popis;
    }

    public void setId(int id){this.id = id;}

    public User getHodnoticiUzivatel() {
        return hodnoticiUzivatel;
    }

    public Group getHodnoticiSkupina() {
        return hodnoticiSkupina;
    }

    @Override
    public String toString() {
        return "[hodnocení: "+hodnota+"] skupina - " + id;
    }
    
    
    
}

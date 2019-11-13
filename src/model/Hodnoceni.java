
package model;

/**
 *
 * @author Tomáš Vondra
 */
public class Hodnoceni {

    private final int id;
    private final int hodnota;
    private final String popis;
    private final Uzivatel hodnoticiUzivatel;
    private final Skupina hodnoticiSkupina;
    
    public Hodnoceni(int hodnota_hodnoceni, String popis, Uzivatel hodnotici_Uzivatel, Skupina hodnocena_skupina) {
        this(-1, hodnota_hodnoceni, popis, hodnotici_Uzivatel, hodnocena_skupina);
    }

    public Hodnoceni(int id, int hodnota_hodnoceni, String popis, Uzivatel hodnotici_Uzivatel, Skupina hodnocena_skupina) {
        this.id = id;
        this.hodnota = hodnota_hodnoceni;
        this.popis = popis;
        this.hodnoticiUzivatel = hodnotici_Uzivatel;
        this.hodnoticiSkupina = hodnocena_skupina;
    }

    public int getId() {
        return id;
    }

    public int getHodnota() {
        return hodnota;
    }

    public String getPopis() {
        return popis;
    }

    public Uzivatel getHodnoticiUzivatel() {
        return hodnoticiUzivatel;
    }

    public Skupina getHodnoticiSkupina() {
        return hodnoticiSkupina;
    }

    @Override
    public String toString() {
        return "Hodnocení";
        //TODO Hodnocení
    }
    
    
    
}

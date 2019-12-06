
package model;

/**
 *
 * @author Tomáš Vondra
 */
public class Subject {

    private final int id;
    private final String nazevString;
    private final String popis;

    public Subject(int id, String nazevString, String popis) {
        this.id = id;
        this.nazevString = nazevString;
        this.popis = popis;
    }

    public int getId() {
        return id;
    }

    public String getNazevString() {
        return nazevString;
    }

    public String getPopis() {
        return popis;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
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
        final Subject other = (Subject) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString() {
        return getNazevString();
    }
    

   
}

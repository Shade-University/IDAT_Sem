
package model;

/**
 * Subject model
 */
public class Subject {

    private int id;
    private String name;
    private String description;

    public Subject(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    } //Konstruktor pro načítání

    public Subject(String name, String description) {
        this(-1, name, description);
    } //Konstruktor pro vytváření

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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
        return this.id == other.id;
    }


    @Override
    public String toString() {
        return getName();
    }


}

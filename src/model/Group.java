package model;


import java.util.List;

/**
 * Group model
 */
public class Group {

    private int id;
    private String name;
    private String description;
    private int quantity;
    private List<Subject> subjects;

    public Group(String name, String description, List<Subject> subjects) {
        this(-1, name, description, subjects);
    } //Konstruktor pro vytváření

    public Group(int id, String name, String description, List<Subject> subjects) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subjects = subjects;
    } //Konsturktor pro načítání s kvantitou

    public Group(int id, String name, String description, int quantity, List<Subject> subjects) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.subjects = subjects;
    } //Konstruktor pro načítání

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public List<Subject> getSubject() {
        return subjects;
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
        final Group other = (Group) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubject(List<Subject> subjects) {
        this.subjects = subjects;
    }


}

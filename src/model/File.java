package model;

import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

/**
 * File model
 */
public class File {

    private Integer id;
    private String name;
    private String extension;
    private String type;
    private byte[] data;
    private Date date_created;
    private Date date_updated;

    public File(Integer id, String name, String extension, String type, byte[] data,  Date date_created, Date date_updated) {
        this.id = id;
        this.name = name;
        this.extension = extension;
        this.type = type;
        this.data = data;
        this.date_created = date_created;
        this.date_updated = date_updated;
    } //Konstruktor pro načítání

    public File(String name, String extension, String type, byte[] data, Date date_created, Date date_updated) {
        this(-1, name, extension, type, data, date_created, date_updated);
    } //Konstruktor pro vytváření

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return id.equals(file.id) &&
                name.equals(file.name) &&
                extension.equals(file.extension) &&
                type.equals(file.type) &&
                Arrays.equals(data, file.data) &&
                date_created.equals(file.date_created) &&
                date_updated.equals(file.date_updated);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, extension, type, date_created, date_updated);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString(){
        return "[" + this.type + "] " + this.name + this.getExtension();
    }
}

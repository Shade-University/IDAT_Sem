package model;

import java.sql.Date;

public class FoodMenu {
    private int id;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public FoodMenu(int id,  Date date) {
        this.id = id;
        this.date = date;
    }

    @Override
    public String toString() {
        return date.toString();
    }
}

package model;

import controller.enums.TRANSACTION_TYPE;

import java.sql.Date;
import java.util.Objects;

/**
 * Order model
 */
public class Order {
    private int id;
    private User user;
    private Product product;
    private TRANSACTION_TYPE typeOfTransaction;
    private float price;
    private Date date;

    public Order(int id, User user, Product product, TRANSACTION_TYPE typeOfTransaction, float price, Date date, String description) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.typeOfTransaction = typeOfTransaction;
        this.price = price;
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public TRANSACTION_TYPE getTypeOfTransaction() {
        return typeOfTransaction;
    }

    public void setTypeOfTransaction(TRANSACTION_TYPE typeOfTransaction) {
        this.typeOfTransaction = typeOfTransaction;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        if(product!=null){
            return id == order.id &&
                    Float.compare(order.price, price) == 0 &&
                    user.equals(order.user) &&
                    product.equals(order.product) &&
                    typeOfTransaction == order.typeOfTransaction &&
                    date.equals(order.date) &&
                    description.equals(order.description);
        } else {
            return id == order.id &&
                    Float.compare(order.price, price) == 0 &&
                    user.equals(order.user) &&
                    typeOfTransaction == order.typeOfTransaction &&
                    date.equals(order.date) &&
                    description.equals(order.description);
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, product, typeOfTransaction, price, date, description);
    }

    @Override
    public String toString() {
        int toInt = (int) price;
        return "["+ date + "] " + description + " - " + toInt + ",-";
    }
}

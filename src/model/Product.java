package model;

import controller.enums.TRANSACTION_TYPE;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private String description;
    private int inStock;
    private TRANSACTION_TYPE type;
    private float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public TRANSACTION_TYPE getType() {
        return type;
    }

    public void setType(TRANSACTION_TYPE type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Product(int id, String name, String description, int inStock, TRANSACTION_TYPE type, float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.inStock = inStock;
        this.type = type;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                inStock == product.inStock &&
                Float.compare(product.price, price) == 0 &&
                name.equals(product.name) &&
                description.equals(product.description) &&
                type == product.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, inStock, type, price);
    }

    @Override
    public String toString() {
        int toInt = (int) price;
        return  "["+ toInt + ",-] " + name;
    }
}

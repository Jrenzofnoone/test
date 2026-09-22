package com.example.test;

import java.io.Serializable;

public class Product implements Serializable {
    int id;
    String name;
    double price;
    String desc;
    int quantity;
    String category;
    int userId;

    public Product(int id, String name, double price, String desc, int quantity, String category,int userId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.quantity = quantity;
        this.category = category;
        this.userId = userId;

    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

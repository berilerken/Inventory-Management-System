package com.example.javafxlogin;

public class stock {

    int stock_id;
    String location, product_name,product_brand;
    int quantity, product_price;


    public stock(int stock_id, String product_name, String product_brand, int product_price, int quantity, String location) {
        this.stock_id = stock_id;
        this.product_name = product_name;
        this.product_brand = product_brand;
        this.product_price = product_price;
        this.quantity = quantity;
        this.location = location;
    }

    public stock() {
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

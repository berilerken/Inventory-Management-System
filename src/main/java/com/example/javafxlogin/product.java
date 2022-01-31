package com.example.javafxlogin;

public class product {
    int id, quantity;
    String name, brand;
    int price;
    byte[] image;


    public product() {
    }

    public product(int id, int quantity, String name, String brand, int price,byte[] image) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.image= image;

    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}

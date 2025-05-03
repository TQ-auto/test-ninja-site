package com.helpclasses;

public class Product {

    private String name;
    private String pictureUrl;
    private String description;
    private double price;

    public Product(String name, String pictureUrl, String description, double price) {
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}

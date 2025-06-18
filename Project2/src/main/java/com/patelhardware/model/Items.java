/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.model;

/**
 *
 * @author miran
 */
public class Items {
    private int itemId;
    private String name;
    private String color;
    private String description;
    private double price;
    private boolean available;

    /* Default constructor.*/
    public Items() {
    }

    /* Constructor for creating a new item (without itemId, as it's auto-generated).*/
    public Items(String name, String color, String description, double price, boolean available) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    /* Constructor for retrieving an item from the database.*/
    public Items(int itemId, String name, String color, String description, double price, boolean available) {
        this.itemId = itemId;
        this.name = name;
        this.color = color;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    // --- Getters ---
    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    // --- Setters ---
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Items{" + "itemId=" + itemId + ", name=" + name + ", color=" + color + 
               ", description=" + description + ", price=" + price + 
               ", available=" + available + '}';
    }
}

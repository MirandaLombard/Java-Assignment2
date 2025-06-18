/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.model;
import java.text.NumberFormat;
import java.util.Locale;
/**
 *
 * @author miran
 */
public class CartItem {
    private Items item;  
    private int quantity;  

    /* Default constructor.*/
    public CartItem() {
    }

    /* Constructor for a CartItem.*/
    public CartItem(Items item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    // --- Getters ---
    public Items getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    // --- Setters ---
    public void setItem(Items item) {
        this.item = item;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /*Calculates the total price for this specific cart item (item price * quantity).*/
    public double getTotalPrice() {
        if (item != null) {
            return item.getPrice() * quantity;
        }
        return 0.0;
    }
    
    public String getFormattedTotalPrice() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "ZA")); // For South African Rand (R)
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);
        return currencyFormat.format(getTotalPrice());
    }

    @Override
    public String toString() {
        return "CartItem{" + "item=" + item + ", quantity=" + quantity + '}';
    }
}

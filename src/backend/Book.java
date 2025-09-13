/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

/**
 *
 * @author Khondaker Shaian & Sadit Joarder
 */
public class Book {
    private String name;
    private double price;

    //Book is created with a price and unique name
    public Book(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Book name cannot be null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Book price cannot be negative");
        }
        this.name = name.trim();
        this.price = price;
    }

    //Getters and Setters Methods 
    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name + " " + this.price;
    }
}

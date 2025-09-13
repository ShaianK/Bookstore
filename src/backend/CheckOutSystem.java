/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;

/**
 *
 * @author Khondaker Shaian & Sadit Joarder
 */
public class CheckOutSystem {
    
    //Singleton Pattern to have one instance 
    //of a CheckOutSystem
    private static CheckOutSystem instance;

    private CheckOutSystem() {}

    public static CheckOutSystem getInstance() {
        if (instance == null) {
            instance = new CheckOutSystem();
        }
        return instance;
    }

    //Able to buy books without using points, gain 10 points per 1 CAD
    public double buyBooks(ArrayList<Book> selectedBooks, Customer currentUser) {
        double totalPrice = 0.0;
        for (Book boughtBooks: selectedBooks){
            totalPrice += boughtBooks.getPrice();
        }
        
        currentUser.setPoints((int) (currentUser.getPoints() + totalPrice*10));
        
        return totalPrice;
    }

    
    //Buys books with points, where every 100 points is 1 CAD deducted from final price
    public double buyBookWithPoints(ArrayList<Book> selectedBooks, Customer currentUser) {
        double totalPrice = 0.0;
        for (Book boughtBooks: selectedBooks){
            totalPrice += boughtBooks.getPrice();
        }
        
   
        int userPoints = currentUser.getPoints();
        
        for (int i = 100; i < userPoints; i += 100){
            if (totalPrice >= 100){
                totalPrice--;
                currentUser.setPoints(currentUser.getPoints() - 100);  
            }            
            else{
                return totalPrice;
            }
            
        }   
        
        return totalPrice;
    }
}

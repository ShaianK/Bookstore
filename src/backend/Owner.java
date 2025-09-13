/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

/**
 *
 * @author Khondaker Shaian & Sadit Joarder
 */
public class Owner extends User {
    private static Owner instance;

    //Private constructor sets password and 
    //username of special Owner account
    private Owner() {
        this.username = "admin";
        this.password = "admin";
    }

    public static Owner getInstance() {
        if (instance == null) {
            instance = new Owner();
        }
        return instance;
    }
}

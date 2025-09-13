/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

/**
 *
 * @author Khondaker Shaian & Sadit Joarder
 */
public abstract class User {
    protected String username;
    protected String password;

    //Implements an authentication used to login into an account
    public boolean authenticateUser(String username, String password){
        
        return username.equals(this.username) && password.equals(this.password);
    }
}

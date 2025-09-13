/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

/**
 *
 * @author Khondaker Shaian & Sadit Joarder
 */
public class Customer extends User {
    private int points;
    private Status memberStatus;

    public Customer(String username, String password, int points) {
        super();
        this.username = username;
        this.password = password;
        this.points = points;
        updateStatus();
    }
    
    //Uses polymorphism to change memberStatus to match the current 
    //membership associated with remaining points for the accounnt
    public void updateStatus() {
        if (points < 1000){
            memberStatus = new SilverMember();            
        }
        else{
            memberStatus = new GoldMember();    
        }
    }
    
    //Getter and Setter Methods
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        if (points >= 0){
            this.points = points;
        }
    }
    
    public String getStatus() {
        return this.memberStatus.toString();
    }
    
    //UPDATE ON CLASS DIAGRAM
    public String getUsername(){
        return this.username;
        
    }
    
    public String getPassword() {
        return password;
    }
}

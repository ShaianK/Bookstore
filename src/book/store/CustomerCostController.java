package book.store;

import backend.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustomerCostController {
    // FXML Labels to display information
    @FXML private Label totalCostLabel;
    @FXML private Label pointsStatusLabel;
    
    // Bookstore related variables
    private BookStore store = BookStore.getInstance();
    private Customer currentCustomer;
    private double totalCost;

    public void initialize(double totalCost) {
        this.totalCost = totalCost; // Set the total cost
        currentCustomer = (Customer) store.getCurrentUser(); // Get the customer
        updateDisplay(); // Update the screen
    }

    // Updating the labels in the total cost screen
    private void updateDisplay() {
        totalCostLabel.setText(String.format("Total Cost: $%.2f", totalCost));
        pointsStatusLabel.setText(String.format(
            "Points: %d, Status: %s",
            currentCustomer.getPoints(),
            currentCustomer.getStatus()
        ));
    }
    
    // Logout/Exit Customer Total Cost Screen 
    @FXML
    private void handleLogout() {
        store.logout(); // Logs out from the bookstore
        store.saveData(); // Saves data
        BookStore.showLoginScreen(); // Switches to login screen
    }
}
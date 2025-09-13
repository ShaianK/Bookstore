package book.store;

import backend.Customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class OwnerCustomersController {
    // JavaFX Elements
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    // Store instance and ObservableList for JavaFX Table
    private BookStore storeInstance = BookStore.getInstance();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Get all the customers and add them to the JavaFX Table
        customers.addAll(storeInstance.getCustomers());
        customersTable.setItems(customers);
    }

    // Handler for adding a customer
    @FXML
    private void handleAddCustomer() {
        // Get the username and password 
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            // Add the customer to the store instance
            // If its a duplicate, the customer won't be added.
            if (!storeInstance.addCustomer(username, password)) {
                return;
            }
            customers.add(new Customer(username, password, 0));
            usernameField.clear();
            passwordField.clear();
        }
    }
    
    // Handler for deleting a customer
    @FXML
    private void handleDeleteCustomer() {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            customers.remove(selectedCustomer);
            storeInstance.deleteCustomer(selectedCustomer.getUsername());
        }
    }

    @FXML
    private void handleBack() {
        BookStore.showOwnerDashboard();
    }
}

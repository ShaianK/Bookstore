package book.store;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController {
    // JavaFX Elements
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;
    
    private BookStore store = BookStore.getInstance();

    // Login handler
    @FXML
    private void handleLoginAction() {
        // Get the username and password
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (store.login(username, password)) { // Does the user exist 
            if (store.checkAdminPrivileges()) { // Is it the owner
                BookStore.showOwnerDashboard();
            } else { // It's a customer
                BookStore.showCustomerStartScreen();
            }
        }
    }
}
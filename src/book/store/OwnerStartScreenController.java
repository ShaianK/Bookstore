package book.store;

import javafx.fxml.FXML;

public class OwnerStartScreenController {
    
    BookStore store = BookStore.getInstance();
    
    
    @FXML
    private void handleBooksButton() {
        BookStore.showOwnerBooksScreen();
    }

    @FXML
    private void handleCustomersButton() {
        BookStore.showOwnerCustomersScreen();
    }
    
    @FXML
    private void handleLogoutButton() {
        store.logout();
        BookStore.showLoginScreen();
    }
}
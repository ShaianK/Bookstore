package book.store;

import backend.Book;
import backend.Customer;
import java.io.IOException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class CustomerStartController {

    // FXML Labels/Table that display information
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, Boolean> selectColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label statusLabel;

    // Bookstore related variables
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private BookStore store = BookStore.getInstance();
    private Customer currentCustomer;
    private final Map<Book, SimpleBooleanProperty> selectionMap = new HashMap<>();

    @FXML
    public void initialize() {
        //Get customber and update welcome message
        currentCustomer = (Customer) store.getCurrentUser();
        updateWelcomeMessage();

        // Setup checkbox column
        setupCheckboxColumn();

        // Load books
        loadBooks();

        // Enable table editing
        booksTable.setEditable(true);
    }

    // Setting up checkbox column from list of books
    // Each book can either be selected or not
    private void setupCheckboxColumn() {
        // Set up cell value factory
        selectColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return selectionMap.computeIfAbsent(book, k -> new SimpleBooleanProperty(false));
        });

        // Set up cell factory with proper binding
        selectColumn.setCellFactory(col -> new CheckBoxTableCell<>(index
                -> selectionMap.get(booksTable.getItems().get(index))
        ));

        // Enable column editing
        selectColumn.setEditable(true);
    }

    // Update the labels containing the welcome message
    private void updateWelcomeMessage() {
        currentCustomer.updateStatus();
        welcomeLabel.setText(String.format(
                "Welcome, %s (Status: %s, Points: %d)",
                currentCustomer.getUsername(),
                currentCustomer.getStatus(),
                currentCustomer.getPoints()
        ));
    }

    // From the Bookstore, get the avaliable books.
    private void loadBooks() {
        books.setAll(store.getBooks());
        booksTable.setItems(books);

        // Initialize selection state for all books (for the checkboxes)
        for (Book book : books) {
            selectionMap.putIfAbsent(book, new SimpleBooleanProperty(false));
        }
    }

    // Purchasing a book
    @FXML
    private void handlePurchase() {
        // Get selected books
        
        List<Book> selectedBooks = getSelectedBooks();
        // If there are no selected books
        if (selectedBooks.isEmpty()) {
            statusLabel.setText("Please select at least one book!");
            return;
        }
        
        // Process the purchase and get total cost
        double totalCost = store.checkOut(false, currentCustomer);

        // Pass the total cost to the customer cost screen
        showCostScreen(totalCost);
    }

    @FXML
    private void handleRedeemPoints() {
        // Get selected books
        List<Book> selectedBooks = getSelectedBooks();
        
        // If there are no selected books
        if (selectedBooks.isEmpty()) {
            statusLabel.setText("Please select at least one book!");
            return;
        }

        // Process the purchase and get total cost
        double totalCost = store.checkOut(true, currentCustomer);

        // Pass the total cost to the customer cost screen
        showCostScreen(totalCost);
    }

    private List<Book> getSelectedBooks() {
        List<Book> selected = new ArrayList<>();
        
        // If the book is selected (checkboxed), then add it to the selected books array
        for (Map.Entry<Book, SimpleBooleanProperty> entry : selectionMap.entrySet()) {
            if (entry.getValue().get()) {
                selected.add(entry.getKey());
            }
        }

        // Update the bookstore's list of selected books
        for (Book book : selected) {
            store.selectBook(book.getName());
        }
        
        // Return the selected books
        return selected;
    }

    // Switch the scene to the total cost screen
    private void showCostScreen(double totalCost) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("CustomerCostScreen.fxml"));
            Parent root = loader.load();

            CustomerCostController controller = loader.getController();
            controller.initialize(totalCost);

            Stage stage = (Stage) booksTable.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading cost screen");
        }
    }

    // Logout and switch to the login screen
    @FXML
    private void handleLogout() {
        store.logout();
        BookStore.showLoginScreen();
    }
}

package book.store;

import backend.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class OwnerBooksScreenController {
    // JavaFX elements
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;

    // Bookstore instance and ObservableList for JavaFX table
    private BookStore storeInstance = BookStore.getInstance();
    private ObservableList<Book> books = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize with books from BookStore
        books.addAll(storeInstance.getBooks());
        booksTable.setItems(books);
    }

    // Handler for Owner adding a book 
    @FXML
    private void handleAddBook() {
        try {
            // Get the information for the book and create it
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            Book newBook = new Book(name, price);

            if (!name.isEmpty() && price > 0) {
                // Add the book to the bookstore
                // If it returns false it is a duplicate, and the book isnt added.
                if (!storeInstance.addBook(name, price)) {
                    return;
                }
                books.add(newBook);              // Add to observable list
                nameField.clear();
                priceField.clear();
            }
        } catch (NumberFormatException e) {
            // Handle invalid price input
            System.err.println("Invalid price format: " + e.getMessage());
        }
    }

    // Handler for deleting selected books
    @FXML
    private void handleDeleteBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            storeInstance.deleteBook(selectedBook.getName());  // Remove from backend
            books.remove(selectedBook);             // Remove from observable list
        }
    }

    // Switch to owner dashboard/start screen
    @FXML
    private void handleBack() {
        BookStore.showOwnerDashboard();
    }
}

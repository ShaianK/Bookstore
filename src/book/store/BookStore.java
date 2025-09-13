package book.store;

import backend.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BookStore extends Application {

    private static Stage primaryStage;

    private User currentUser;
    private static BookStore instance;

    private ArrayList<Customer> customers = new ArrayList<Customer>();
    private ArrayList<Book> books = new ArrayList<Book>();
    private ArrayList<Book> selectedBooks = new ArrayList<Book>();

    private boolean adminPrivileges = false;
    
    
    //Reads data from save files containing previous customers or books
    public BookStore() {
        readData();
    }

    
    //Singleton method to call BookStore in other files
    public static BookStore getInstance() {

        if (instance == null) {
            instance = new BookStore();
        }
        return instance;
    }

    //Login checks if user has admin privileges and authenticates
    //the entered password and username
    public boolean login(String name, String password) {
        Owner currentOwner = Owner.getInstance();
        if (currentOwner.authenticateUser(name, password)) {
            adminPrivileges = true;
            currentUser = currentOwner;
            return true;
        } else {
            for (Customer currentCustomer : customers) {
                if (currentCustomer.authenticateUser(name, password)) {
                    currentUser = currentCustomer;
                    adminPrivileges = false;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkAdminPrivileges() {
        return adminPrivileges;
    }
    
    
    public User getCurrentUser() {
        return this.currentUser;
    }

    public void logout() {
        this.currentUser = null;
        this.adminPrivileges = false;
        saveData();
    }

    //Opens default books ad customers files to check and for previously
    //stored books and customers
    public void readData() {
        File bookList = new File("books.txt");
        File customerList = new File("customers.txt");

        try {
            Scanner bookTextFile = new Scanner(bookList);
            while (bookTextFile.hasNextLine()) {
                String bookName = bookTextFile.nextLine();
                double bookPrice = Double.valueOf(bookTextFile.nextLine());
                books.add(new Book(bookName, bookPrice));
            }

            Scanner customerTextFile = new Scanner(customerList);
            while (customerTextFile.hasNextLine()) {
                String customerName = customerTextFile.nextLine();
                String customerPassword = customerTextFile.nextLine();
                int customerPoints = Integer.valueOf(customerTextFile.nextLine());
                customers.add(new Customer(customerName, customerPassword, customerPoints));
            }
        } catch (IOException e) {
            System.out.println("Unable to write to file.");

        }
    }

    //Saves the data by writing to two save files, one for books and another for customers
    public void saveData() {

        try {
            FileWriter writeBookList = new FileWriter("books.txt");
            FileWriter writeCustomerList = new FileWriter("customers.txt");

            for (Book availableBooks : books) {
                writeBookList.write(availableBooks.getName() + "\n" + availableBooks.getPrice() + "\n");

            }
            writeBookList.close();

            for (Customer previousCustomers : customers) {
                writeCustomerList.write(previousCustomers.getUsername() + "\n" + previousCustomers.getPassword() + "\n" + previousCustomers.getPoints() + "\n");

            }
            writeCustomerList.close();

        } catch (IOException e) {
            System.out.println("Unable to write to file.");
        }
    }

    //Adds a non duplicate customer to the customers list
    public boolean addCustomer(String name, String password) {
        for (Customer currentCustomer : customers) {
            if (currentCustomer.getUsername().equals(name)) {
                return false;
            }
        }
        customers.add(new Customer(name, password, 0));
        return true;
    }

    //Deletes a customer based on their username
    public void deleteCustomer(String name) {
        int i = 0;
        for (Customer currentCustomer : customers) {
            if (currentCustomer.getUsername().equals(name)) {
                customers.remove(i);
                return;
            }
            i++;
        }
    }

    //Returns list of all customers
    public ArrayList<Customer> getCustomers() {
        return this.customers;
    }

    //Returns a customer with a specific username
    public Customer getCustomer(String name) {
        for (Customer currentCustomer : customers) {
            if (currentCustomer.getUsername().equals(name)) {
                return currentCustomer;
            }
        }

        return new Customer("null", "null", 0);
    }

    //Adds a book with a price and unique name to list of books
    public boolean addBook(String name, double price) {
        for (Book availableBook : books) {
            if (availableBook.getName().equals(name)) {
                return false;
            }
        }
        books.add(new Book(name, price));
        return true;
    }

    //Deletes a book from the list with a specific name
    public void deleteBook(String name) {
        int i = 0;
        for (Book availableBook : books) {
            if (availableBook.getName().equals(name)) {
                books.remove(i);
                break;
            }
            i++;
        }
    }
    
    //Returns list of all books
    public ArrayList<Book> getBooks() {
        return this.books;
    }

    //Selects specific book from the list
    public void selectBook(String name) {
        for (Book availableBook : books) {
            if (availableBook.getName().equals(name)) {
                selectedBooks.add(availableBook);
            }
        }
    }

    //Checks out with books and selects whether or not the customer would like
    //to spend their points or not
    public double checkOut(boolean withPoints, Customer currentCustomer) {
        CheckOutSystem transaction = CheckOutSystem.getInstance();
        double finalPrice;

        if (withPoints) {
            finalPrice = transaction.buyBookWithPoints(selectedBooks, currentCustomer);
        } else {
            finalPrice = transaction.buyBooks(selectedBooks, currentCustomer);
        }

        for (Book boughtBooks : selectedBooks) {
            deleteBook(boughtBooks.getName());

        }

        currentCustomer.updateStatus();

        selectedBooks.removeAll(selectedBooks);

        return finalPrice;
    }

    // GUI --------------------------------------------------------------------- 
    
    // Start function for JavaFX, since this is the main file
    @Override
    public void start(Stage stage) throws Exception {
        BookStore store = BookStore.getInstance();
        primaryStage = stage;
        
        stage.setOnCloseRequest(event -> {
            store.saveData();
        });
        
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        showLoginScreen();
        primaryStage.show();
    }

    // Switch to login screen
    public static void showLoginScreen() {
        try {
            Parent root = FXMLLoader.load(BookStore.class.getResource("LoginPage.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Book Store - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Switch to owner start screen
    public static void showOwnerDashboard() {
        try {
            Parent root = FXMLLoader.load(BookStore.class.getResource("OwnerStartScreen.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Owner Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Switch to owners book screen
    public static void showOwnerBooksScreen() {
        try {
            Parent root = FXMLLoader.load(BookStore.class.getResource("OwnerBooksScreen.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Manage Books");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Switch to owner customers screen
    public static void showOwnerCustomersScreen() {
        try {
            Parent root = FXMLLoader.load(BookStore.class.getResource("OwnerCustomersScreen.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Manage Customers");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Switch to customer start screen
    public static void showCustomerStartScreen() {
        try {
            Parent root = FXMLLoader.load(BookStore.class.getResource("CustomerStartScreen.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Customers Start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

package com.library_management_system.Controller;

import com.library_management_system.Connection.DatabaseConnection;
import com.library_management_system.DAO.BookDAO;
import com.library_management_system.Model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageBooksController implements Initializable {
    @FXML
    private TextField txt_bookID;

    @FXML
    private TextField txt_bookName;

    @FXML
    private TextField txt_author;

    @FXML
    private TextField txt_quantity;
    @FXML
    private TableView<Book> manageBookTable;

    @FXML
    private TableColumn<Book, Integer> bookIdColumn;

    @FXML
    private TableColumn<Book, String> bookNameColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, Integer> quantityColumn;
    @FXML
    private TextField txt_keyword;
    private static final Logger LOGGER = Logger.getLogger(ManageBooksController.class.getName());

    public void backHomePage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/HomeGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load Home GUI", e);
        }
    }

    public void exitApp() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        showBooks();
        // Add listener for row selection
        manageBookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateTextFields(newValue);
            }
        });
    }

    private void showBooks() {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");

                bookList.add(new Book(bookId, bookName, author, quantity));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to show books", e);
        }
        manageBookTable.setItems(bookList);
    }

    private void populateTextFields(Book book) {
        txt_bookID.setText(String.valueOf(book.getBookId()));
        txt_bookName.setText(book.getBookName());
        txt_author.setText(book.getAuthor());
        txt_quantity.setText(String.valueOf(book.getQuantity()));
    }

    public void addBook() {
        try {
            if (txt_bookID.getText().isEmpty() || txt_bookName.getText().isEmpty() || txt_author.getText().isEmpty() || txt_quantity.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
                return;
            }
            int bookId = Integer.parseInt(txt_bookID.getText());
            String bookName = txt_bookName.getText();
            String author = txt_author.getText();
            int quantity = Integer.parseInt(txt_quantity.getText());
            if (BookDAO.bookExists(bookId)) {
                showAlert(Alert.AlertType.ERROR, "Book ID already exists. Please enter a different ID.");
                return;
            }
            if (BookDAO.addBook(bookId, bookName, author, quantity)) {
                showAlert(Alert.AlertType.INFORMATION, "Book added successfully.");
                showBooks();
            } else showAlert(Alert.AlertType.ERROR, "Book added failed.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input. Please enter valid data.");
        }
    }

    public void updateBook() {
        try {
            if (txt_bookID.getText().isEmpty() || txt_bookName.getText().isEmpty() || txt_author.getText().isEmpty() || txt_quantity.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
                return;
            }
            int bookId = Integer.parseInt(txt_bookID.getText());
            String bookName = txt_bookName.getText();
            String author = txt_author.getText();
            int quantity = Integer.parseInt(txt_quantity.getText());
            if (BookDAO.updateBook(bookId, bookName, author, quantity)) {
                showAlert(Alert.AlertType.INFORMATION, "Book updated successfully.");
                showBooks();
            } else showAlert(Alert.AlertType.ERROR, "Book updated failed.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input. Please enter valid data.");
        }
    }

    public void deleteBook() {
        int bookId = Integer.parseInt(txt_bookID.getText());
        if (BookDAO.deleteBook(bookId)) {
            showAlert(Alert.AlertType.INFORMATION, "Book deleted successfully.");
            showBooks();
        } else showAlert(Alert.AlertType.ERROR, "Book deleted failed.");
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void searchBook() {
        String keyword = txt_keyword.getText().trim();
        List<Book> bookList = BookDAO.searchBook(keyword);
        ObservableList<Book> observableBookList = FXCollections.observableArrayList(bookList);
        manageBookTable.setItems(observableBookList);
    }
}



package com.library_management_system.Controller;

import com.library_management_system.Connection.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController implements Initializable {
    @FXML
    PieChart pieChartAllBooks;
    @FXML
    PieChart pieChartIssuedBooks;
    @FXML
    private TextField numberOfBooks;
    @FXML
    private TextField numberOfStudents;
    @FXML
    private TextField numberOfIssuedBooks;
    @FXML
    private TextField numberOfOverdueBooks;
    @FXML
    private TextField showUserName;
    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());

    public void exitApp() {
        System.exit(0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        numberOfBooks.setText(String.valueOf(getCount("SELECT COUNT(*) FROM books")));
        numberOfStudents.setText(String.valueOf(getCount("SELECT COUNT(*) FROM students")));
        numberOfIssuedBooks.setText(String.valueOf(getCount("SELECT COUNT(*) FROM issue_book_detail WHERE status = 'issued'")));
        numberOfOverdueBooks.setText(String.valueOf(getCount("SELECT COUNT(*) FROM issue_book_detail WHERE due_date < GETDATE() AND status = 'pending'")));
        String username = getLoggedInUsername();
        showUserName.setText(username);
        populatePieChartAllBooks();
        populatePieChartIssuedBooks();
        setupPieChartClickHandler(pieChartAllBooks);
        setupPieChartClickHandler(pieChartIssuedBooks);
    }

    private String getLoggedInUsername() {
        return LoginController.getLoggedInUsername(); // Example placeholder

    }

    public void goToManageBook(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/ManageBooksGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load ManageBook GUI", e);
        }
    }

    public void goToManageStudent(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/ManageStudentsGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load ManageStudent GUI", e);
        }
    }

    public void goToIssueBook(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/IssueBookGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load IssueBookGUI", e);
        }
    }

    public void goToReturnBook(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/ReturnBookGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load ReturnBookGUI", e);
        }
    }

    public void goToViewRecord(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/ViewRecordGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load ViewRecordGUI", e);
        }
    }

    public void goToViewIssuedBook(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/ViewIssueGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load ViewIssueBookGUI", e);
        }
    }

    public void goToViewOverdueBook(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/ViewOverdueGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load ViewIssueBookGUI", e);
        }
    }

    public void logOut(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/LoginGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load ViewIssueBookGUI", e);
        }
    }

    private int getCount(String query) {
        int count = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute query: " + query, e);
        }
        return count;
    }

private void populatePieChartAllBooks() {
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    String query = "SELECT book_id, quantity FROM books";
    int totalQuantity = 0;
    Map<String, Integer> bookQuantities = new HashMap<>();

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            String bookId = rs.getString("book_id");
            int quantity = rs.getInt("quantity");
            bookQuantities.put(bookId, quantity);
            totalQuantity += quantity;
        }
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Failed to execute query: " + query, e);
    }

    for (Map.Entry<String, Integer> entry : bookQuantities.entrySet()) {
        String bookId = entry.getKey();
        int quantity = entry.getValue();
        double percentage = (double) quantity / totalQuantity * 100;
        pieChartData.add(new PieChart.Data(bookId + " (" + String.format("%.2f", percentage) + "%)", quantity));
    }

    pieChartAllBooks.setData(pieChartData);
}


    private void populatePieChartIssuedBooks() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String query = "SELECT book_id, COUNT(*) AS issue_count FROM issue_book_detail WHERE status = 'pending' GROUP BY book_id";
        int totalIssuedBooks = 0;
        Map<String, Integer> bookIssueCounts = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String bookId = rs.getString("book_id");
                int issueCount = rs.getInt("issue_count");
                bookIssueCounts.put(bookId, issueCount);
                totalIssuedBooks += issueCount;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute query: " + query, e);
        }

        for (Map.Entry<String, Integer> entry : bookIssueCounts.entrySet()) {
            String bookId = entry.getKey();
            int issueCount = entry.getValue();
            double percentage = (double) issueCount / totalIssuedBooks * 100;
            pieChartData.add(new PieChart.Data(bookId + " (" + String.format("%.2f", percentage) + "%)", issueCount));
        }

        pieChartIssuedBooks.setData(pieChartData);
    }

    private void setupPieChartClickHandler(PieChart pieChart) {
        pieChart.getData().forEach(data -> data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            // Extract book ID from the data name
            String dataName = data.getName();
            String bookId = dataName.split(" ")[0]; // Assuming bookId is the first part before any space
            showBookDetails(bookId);
        }));
    }

    private void showBookDetails(String bookId) {
        String query = "SELECT * FROM books WHERE book_id = '" + bookId + "'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                // Assuming you have columns like title, author, etc.
                int book_id = Integer.parseInt(bookId);
                String book_name = rs.getString("book_name");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                // Display the book details in a dialog or new window
                showBookDetailsDialog(book_id, book_name, author, quantity);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute query: " + query, e);
        }
    }

    private void showBookDetailsDialog(int book_id, String book_name, String author, int quantity) {
        // Implement a dialog or new window to show the book details
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Book Details");
        alert.setHeaderText(null);
        alert.setContentText("ID: " + book_id + "\nName: " + book_name + "\nAuthor: " + author + "\nQuantity: " + quantity);
        alert.showAndWait();
    }

    public void goToUserDetail(MouseEvent event) {
    }
}

package com.library_management_system.Controller;

import com.library_management_system.DAO.BookDAO;
import com.library_management_system.DAO.IssueBookDAO;
import com.library_management_system.DAO.StudentDAO;
import com.library_management_system.Model.Book;
import com.library_management_system.Model.IssueBook;
import com.library_management_system.Model.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IssueBookController implements Initializable {
    @FXML
    private TextField txt_bookID;
    @FXML
    private TextField txt_bookName;
    @FXML
    private TextField txt_Author;
    @FXML
    private TextField txt_Quantity;
    @FXML
    private TextField enterBookID;

    @FXML
    private TextField txt_studentID;
    @FXML
    private TextField txt_studentName;
    @FXML
    private TextField txt_studentYear;
    @FXML
    private TextField txt_studentMajor;
    @FXML
    private TextField enterStudentID;

    @FXML
    private DatePicker issueDate;
    @FXML
    private DatePicker dueDate;

    private static final Logger LOGGER = Logger.getLogger(IssueBookController.class.getName());


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

    public void issueBook() {
        try {
            int bookID = Integer.parseInt(txt_bookID.getText());
            int quantity = Integer.parseInt(txt_Quantity.getText());
            int studentID = Integer.parseInt(txt_studentID.getText());
            String studentName = txt_studentName.getText();
            String bookName = txt_bookName.getText();
            LocalDate issue = issueDate.getValue();
            LocalDate due = dueDate.getValue();

            if (quantity < 1) {
                showAlert(Alert.AlertType.ERROR, "Book quantity is not sufficient.");
                return;
            }

            if (issue == null || due == null) {
                showAlert(Alert.AlertType.ERROR, "Issue date and due date must be entered.");
                return;
            }

            if (!issue.isBefore(due)) {
                showAlert(Alert.AlertType.ERROR, "Issue date must be before due date.");
                return;
            }

            // Check if the student has already borrowed the same book
            if (IssueBookDAO.isBookAlreadyIssued(bookID, studentID)) {
                showAlert(Alert.AlertType.ERROR, "Student has already borrowed this book.");
                return;
            }
            // Save the book issue details into the issue_book_detail table
            IssueBook issueBookDetail = new IssueBook(bookID, bookName, studentID, studentName, issue, due, "pending");
            IssueBookDAO.saveIssueBookDetail(issueBookDetail);

            // Update the book quantity in the database
            BookDAO.updateBookQuantity(bookID, - 1);
            handleBookIDEnter();
            showAlert(Alert.AlertType.INFORMATION, "Book issued successfully.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input format.");
            LOGGER.severe("Invalid input format: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "An error occurred while issuing the book.");
            LOGGER.log(Level.SEVERE, "An error occurred while issuing the book", e);
        }
    }

    public void handleBookIDEnter() {
        try {
            int bookID = Integer.parseInt(enterBookID.getText());
            LOGGER.info("Fetching details for Book ID: " + bookID);

            Book book = BookDAO.getBookDetails(bookID);
            if (book != null) {
                txt_bookID.setText(String.valueOf(book.getBookId()));
                txt_bookName.setText(book.getBookName());
                txt_Author.setText(book.getAuthor());
                txt_Quantity.setText(String.valueOf(book.getQuantity()));
                LOGGER.info("Book details populated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Book ID does not exist");
                LOGGER.warning("Book ID does not exist: " + bookID);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Book ID format");
            LOGGER.severe("Invalid Book ID format: " + enterBookID.getText());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "An error occurred while fetching book details");
            LOGGER.log(Level.SEVERE, "An error occurred while fetching book details", e);
        }
    }

    public void handleStudentIDEnter() {
        try {
            int studentID = Integer.parseInt(enterStudentID.getText());
            LOGGER.info("Fetching details for Student ID: " + enterStudentID);

            Student student = StudentDAO.getStudentDetails(studentID);
            if (student != null) {
                txt_studentID.setText(String.valueOf(student.getStudentId()));
                txt_studentName.setText(student.getStudentName());
                txt_studentYear.setText(String.valueOf(student.getYear()));
                txt_studentMajor.setText(student.getMajor());
                LOGGER.info("Book details populated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Student ID does not exist");
                LOGGER.warning("Student ID does not exist: " + studentID);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Student ID format");
            LOGGER.severe("Invalid Student ID format: " + enterBookID.getText());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "An error occurred while fetching student details");
            LOGGER.log(Level.SEVERE, "An error occurred while fetching student details", e);
        }
    }



    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enterBookID.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                handleBookIDEnter();
            }
        });

        enterStudentID.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                handleStudentIDEnter();
            }
        });
    }


}

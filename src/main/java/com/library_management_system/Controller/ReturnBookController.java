package com.library_management_system.Controller;

import com.library_management_system.DAO.BookDAO;
import com.library_management_system.DAO.IssueBookDAO;
import com.library_management_system.Model.IssueBook;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReturnBookController implements Initializable {

    @FXML
    private TextField enterBookID;
    @FXML
    private TextField enterStudentID;
    @FXML
    private TextField txt_issueID;
    @FXML
    private TextField txt_bookName;
    @FXML
    private TextField txt_studentName;
    @FXML
    private TextField txt_issueDate;
    @FXML
    private TextField txt_dueDate;

    private static final Logger LOGGER = Logger.getLogger(ReturnBookController.class.getName());


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

    public void returnBook() {
        try {
            int issueID = Integer.parseInt(txt_issueID.getText());
            IssueBookDAO.updateIssueBookDetail(issueID);
            int bookID = Integer.parseInt(enterBookID.getText());
            BookDAO.updateBookQuantity(bookID,1);
            showAlert(Alert.AlertType.INFORMATION, "Book returned successfully.");
            LOGGER.info("Book returned successfully for Issue ID: " + issueID);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input format");
            LOGGER.warning("Invalid input format");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to update issue status");
            LOGGER.log(Level.SEVERE, "Failed to update issue status", e);
        }
    }

    public void findIssue() {
        try {
            int bookId = Integer.parseInt(enterBookID.getText());
            int studentId = Integer.parseInt(enterStudentID.getText());
            IssueBook issueBook = IssueBookDAO.findIssue(studentId, bookId);
            if (issueBook != null) {
                txt_issueID.setText(String.valueOf(issueBook.getIssueID()));
                txt_bookName.setText(issueBook.getBookName());
                txt_studentName.setText(issueBook.getStudentName());
                txt_issueDate.setText(String.valueOf(issueBook.getIssueDate()));
                txt_dueDate.setText(String.valueOf(issueBook.getDueDate()));
                LOGGER.info("Issue details populated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "No issue found for the given Book ID and Student ID.");
                LOGGER.warning("No issue found for the given Book ID: " + bookId + " and Student ID: " + studentId);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input format");
            LOGGER.warning("Invalid input format");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching issue details", e);
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

    }
}

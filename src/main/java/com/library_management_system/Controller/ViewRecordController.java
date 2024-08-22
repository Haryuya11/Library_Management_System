package com.library_management_system.Controller;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewRecordController implements Initializable {
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private TableView<IssueBook> manageIssueTable;
    @FXML
    private TableColumn<IssueBook, Integer> issueIDColumn;
    @FXML
    private TableColumn<IssueBook, String> bookNameColumn;
    @FXML
    private TableColumn<IssueBook, String> studentNameColumn;
    @FXML
    private TableColumn<IssueBook, LocalDate> issueDateColumn;
    @FXML
    private TableColumn<IssueBook, LocalDate> dueDateColumn;
    @FXML
    private TableColumn<IssueBook, String> statusColumn;

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

    @FXML
    public void searchRecord() {
        try {
            LocalDate issue = fromDate.getValue();
            LocalDate due = toDate.getValue();

            if (issue == null || due == null) {
                showAlert(Alert.AlertType.ERROR, "Issue date and due date must be entered.");
                return;
            }

            List<IssueBook> issues = IssueBookDAO.findIssuesByDateRange(issue, due);
            if (issues.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No issues found for the given date range.");
            } else {
                manageIssueTable.getItems().setAll(issues);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "An error occurred while searching for issues.");
            LOGGER.log(Level.SEVERE, "An error occurred while searching for issues", e);
        }
    }

    @FXML
    public void showAllRecord() {
        try {

            List<IssueBook> issues = IssueBookDAO.showAllRecords();
            if (!issues.isEmpty()) {
                manageIssueTable.getItems().setAll(issues);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "An error occurred while searching for record.");
            LOGGER.log(Level.SEVERE, "An error occurred while searching for record", e);
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
        issueIDColumn.setCellValueFactory(new PropertyValueFactory<>("issueID"));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

}

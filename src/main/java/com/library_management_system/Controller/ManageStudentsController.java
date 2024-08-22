package com.library_management_system.Controller;

import com.library_management_system.Connection.DatabaseConnection;
import com.library_management_system.DAO.StudentDAO;
import com.library_management_system.Model.Book;
import com.library_management_system.Model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageStudentsController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(ManageStudentsController.class.getName());
    @FXML
    private TextField txt_StudentID;

    @FXML
    private TextField txt_studentName;

    @FXML
    private ComboBox<Integer> yearComboxBox;

    @FXML
    private ComboBox<String> majorComboxBox;
    @FXML
    private TableView<Student> manageStudentTable;

    @FXML
    private TableColumn<Student, Integer> studentIdColumn;

    @FXML
    private TableColumn<Book, String> studentNameColumn;

    @FXML
    private TableColumn<Book, Integer> yearColumn;

    @FXML
    private TableColumn<Book, String> majorColumn;

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

    public void addStudent() {
        try {
            if (txt_StudentID.getText().isEmpty() || txt_studentName.getText().isEmpty() || yearComboxBox.getItems().isEmpty() || majorComboxBox.getItems().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
                return;
            }
            int id = Integer.parseInt(txt_StudentID.getText());
            String name = txt_studentName.getText();
            int year = yearComboxBox.getValue();
            String major = majorComboxBox.getValue();
            if (StudentDAO.studentExists(id)) {
                showAlert(Alert.AlertType.ERROR, "Student ID already exists. Please enter a different ID.");
                return;
            }
            if(StudentDAO.addStudent(id, name, year, major))
            {
                showAlert(Alert.AlertType.INFORMATION, "Student added successfully.");
                showStudents();
            } else showAlert(Alert.AlertType.ERROR, "Student added failed.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input. Please enter valid data.");
        }
    }

    public void updateStudent() {
        try {
            if (txt_StudentID.getText().isEmpty() || txt_studentName.getText().isEmpty() || yearComboxBox.getItems().isEmpty() || majorComboxBox.getItems().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
                return;
            }
            int id = Integer.parseInt(txt_StudentID.getText());
            String name = txt_studentName.getText();
            int year = yearComboxBox.getValue();
            String major = majorComboxBox.getValue();
            if(StudentDAO.updateStudent(id, name, year, major)){
                showAlert(Alert.AlertType.INFORMATION, "Student updated successfully.");
                showStudents();
            } else showAlert(Alert.AlertType.ERROR, "Student updated failed.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input. Please enter valid data.");
        }
    }

    public void deleteStudent() {
        int id = Integer.parseInt(txt_StudentID.getText());
        if(StudentDAO.deleteStudent(id))
        {
            showAlert(Alert.AlertType.INFORMATION, "Student deleted successfully.");
            showStudents();
        } else showAlert(Alert.AlertType.ERROR, "Student deleted failed.");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));

        yearComboxBox.getItems().addAll(1,2,3,4);
        majorComboxBox.getItems().addAll("KHMT","MMT&TT","CNTT","BMTL","CNPM","HTTT","KTTT","P.DTDH","TTNN");

        showStudents();
        // Add listener for row selection
        manageStudentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateTextFields(newValue);
            }
        });
    }

    private void showStudents() {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            while (rs.next()) {
                int id = rs.getInt("student_id");
                String name = rs.getString("student_name");
                int year = rs.getInt("year");
                String major = rs.getString("major");

                studentList.add(new Student(id, name, year, major));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to show students", e);
        }
        manageStudentTable.setItems(studentList);
    }

    private void populateTextFields(Student student) {
        txt_StudentID.setText(String.valueOf(student.getStudentId()));
        txt_studentName.setText(student.getStudentName());
        yearComboxBox.setValue(student.getYear());
        majorComboxBox.setValue(String.valueOf(student.getMajor()));
    }
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package com.library_management_system.Controller;

import com.library_management_system.DAO.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {

    @FXML
    TextField txt_username;
    @FXML
    PasswordField txt_password;
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    public void exitApp() {
        System.exit(0);
    }

    public void loginAccount() {
        String username = txt_username.getText();
        String password = txt_password.getText();

        if (UserDAO.checkUser(username, password)) {
            goToHomePage();
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid username or password");
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Login Result");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();

    }

    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/HomeGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) txt_username.getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load Home GUI", e);
        }
    }

    public void goToSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/library_management_system/views/SignupGUI.fxml"));
            Parent load = loader.load();
            Stage stage = (Stage) txt_username.getScene().getWindow();
            stage.setScene(new Scene(load));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load Home GUI", e);
        }
    }
}

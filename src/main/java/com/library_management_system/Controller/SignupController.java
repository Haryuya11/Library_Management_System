package com.library_management_system.Controller;

import com.library_management_system.DAO.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupController implements Initializable {

    @FXML
    TextField txt_username;
    @FXML
    PasswordField txt_password;
    @FXML
    TextField txt_email;
    @FXML
    PasswordField txt_confirm_password;

    static Boolean checkUsername = false, checkPassword = false, checkConfirmPassword = false, checkEmail;
    private static final Logger LOGGER = Logger.getLogger(SignupController.class.getName());


    public void insertSignupDetail() {
        if (!checkUsername) {
            showAlert(Alert.AlertType.ERROR, "Username must be at least 6 characters long.");
        } else if (!checkPassword) {
            showAlert(Alert.AlertType.ERROR, "Password must be 6-30 characters long, include at least one digit, one lowercase letter, one uppercase letter, and one special character.");
        } else if (!checkConfirmPassword) {
            showAlert(Alert.AlertType.ERROR, "Passwords do not match.");
        } else if (!checkEmail) {
            showAlert(Alert.AlertType.ERROR, "Not a valid form");
        } else if (UserDAO.userExists(txt_username.getText())) {
            showAlert(Alert.AlertType.ERROR, "Username already exists.");
        } else {
            String username = txt_username.getText();
            String password = txt_password.getText();
            String email = txt_email.getText();
            UserDAO.insertUser(username, password, email);
            showAlert(Alert.AlertType.INFORMATION, "Registration successful!");
        }
    }

    public void exitApp() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txt_username.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateUsername();
            }
        });

        txt_password.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validatePassword();
            }
        });


        txt_confirm_password.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateConfirmPassword();
            }
        });

        txt_email.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateEmail();
            }
        });
    }

    private void validateUsername() {
        checkUsername = txt_username.getText().length() >= 6;
    }

    private void validatePassword() {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&-+=()<>~`])(?=\\S+$).{6,30}$";
        Pattern p = Pattern.compile(passwordRegex);
        Matcher m = p.matcher(txt_password.getText());
        checkPassword = m.find();
    }

    private void validateConfirmPassword() {
        checkConfirmPassword = txt_password.getText().equals(txt_confirm_password.getText());
    }

    private void validateEmail() {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern p = Pattern.compile(emailRegex);
        Matcher m = p.matcher(txt_email.getText());
        checkEmail = m.find();
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

    public void goToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/library_management_system/views/LoginGUI.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) txt_password.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Login GUI", e);
        }
    }
}

package com.patient.management.fx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    private final ConfigurableApplicationContext context;

    public ResetPasswordController(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @FXML
    private void handleResetPasswordAction() {
        // TODO: Implement password reset logic
        showAlert(Alert.AlertType.INFORMATION, "Not Implemented",
                "Password reset functionality not implemented yet");
    }

    @FXML
    private void handleBackToLoginAction() {
        loadLoginForm();
    }

    private void loadLoginForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Login");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Could not load login form: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

package com.patient.management.fx.controller;

import com.patient.management.service.authentication.AuthenticationService;
import com.patient.management.service.user.UserFxService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LoginFormController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final AuthenticationService authenticationService;
    private final UserFxService userFxService;
    private final ConfigurableApplicationContext context;

    @Autowired
    public LoginFormController(AuthenticationService authenticationService,
                               UserFxService userFxService,
                               ConfigurableApplicationContext context) {
        this.authenticationService = authenticationService;
        this.userFxService = userFxService;
        this.context = context;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (userFxService.getUserCount() == 0) {
                loadInitialUserRegistration();
            }
        });
    }

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter both username and password.");
            return;
        }

        try {
            if (authenticationService.authenticate(username, password)) {
                loadPatientManagementView();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed",
                        "Invalid username or password.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Login Error",
                    "An error occurred during login: " + e.getMessage());
        }
    }

    @FXML
    private void handleForgetPasswordAction() {
        loadView("/fxml/ResetPassword.fxml", "Reset Password");
    }

    private void loadPatientManagementView() {
        loadView("/fxml/PatientManagement.fxml", "Patient Management");
    }

    private void loadInitialUserRegistration() {
        loadView("/fxml/InitialUserRegistrationForm.fxml", "Initial Setup");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            Scene currentScene = usernameField.getScene();
            if (currentScene == null) {
                throw new IllegalStateException("Scene is not yet initialized");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(context::getBean);
            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Could not load " + title + " view: " + e.getMessage());
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
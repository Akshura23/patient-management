package com.patient.management.fx.controller;

import com.patient.management.payload.CreateUserDTO;
import com.patient.management.service.user.UserFxService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class InitialUserRegistrationController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker birthdayPicker;

    private final UserFxService userFxService;
    private final ConfigurableApplicationContext context;

    public InitialUserRegistrationController(UserFxService userFxService,
                                             ConfigurableApplicationContext context) {
        this.userFxService = userFxService;
        this.context = context;
    }

    @FXML
    private void handleCreateAdminAction() {
        try {
            CreateUserDTO dto = new CreateUserDTO();
            dto.setFirstName(firstNameField.getText());
            dto.setLastName(lastNameField.getText());
            dto.setUsername(usernameField.getText());
            dto.setPassword(passwordField.getText());
            dto.setBirthday(birthdayPicker.getValue());

            userFxService.createInitialAdmin(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Admin user created successfully");
            loadLoginForm();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to create admin: " + e.getMessage());
        }
    }

    private void loadLoginForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Stage stage = (Stage) firstNameField.getScene().getWindow();
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
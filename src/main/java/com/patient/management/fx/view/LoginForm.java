package com.patient.management.fx.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginForm extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = getButton(usernameField, passwordField);

        VBox layout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Scene scene = new Scene(layout, 300, 200);

        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button getButton(TextField usernameField, PasswordField passwordField) {
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Authenticate against Spring Boot backend
            String jwt = authenticate(username, password);
            if (jwt != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login Successful! JWT: " + jwt);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid credentials!");
                alert.showAndWait();
            }
        });
        return loginButton;
    }

    private String authenticate(String username, String password) {
        // Replace with actual REST API call to Spring Boot
        return "mock-jwt-token"; // Simulated JWT token
    }

    public static void main(String[] args) {
        launch(args);
    }
}
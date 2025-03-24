package com.patient.management.fx.controller;

import com.patient.management.entity.PatientEntity;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BaseController {

    protected final ApplicationContext applicationContext;
    @Autowired
    public BaseController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected void loadView(String fxmlPath, Node sourceNode, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlPath));
            loader.setControllerFactory(applicationContext::getBean);

            Parent root = loader.load();
            Stage stage = (Stage) sourceNode.getScene().getWindow();

            // If there's data to pass to the new controller
            if (data != null) {
                Object controller = loader.getController();
                if (controller instanceof PatientFormController) {
                    ((PatientFormController) controller).setPatient((PatientEntity) data);
                }
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Navigation Error", "Could not load view: " + e.getMessage());
        }
    }

    protected void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void maximizeWindow(Node node) {
        Platform.runLater(() -> {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setMaximized(true);
        });
    }
}
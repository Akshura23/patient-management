package com.patient.management.fx.view;

import com.patient.management.PatientManagementApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class LoginForm extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        context = new SpringApplicationBuilder(PatientManagementApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginForm.fxml"));
        loader.setControllerFactory(context::getBean);
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Login Form");
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
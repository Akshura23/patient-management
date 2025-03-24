package com.patient.management;

import com.patient.management.fx.view.LoginForm;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableEncryptableProperties
public class PatientManagementApplication {

	public static void main(String[] args) {
		Application.launch(LoginForm.class, args);
	}

}

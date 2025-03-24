package com.patient.management.fx.controller;

import com.patient.management.entity.PatientEntity;
import com.patient.management.enums.Gender;
import com.patient.management.service.patient.PatientService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PatientFormController extends BaseController {

    @FXML private Label titleLabel;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthdayPicker;
    @FXML private TextField mobileNoField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField emergencyContactNameField;
    @FXML private TextField emergencyContactPhoneField;
    @FXML private TextArea insuranceInfoField;

    private final PatientService patientService;
    private PatientEntity existingPatient;

    @Autowired
    public PatientFormController(ApplicationContext applicationContext, PatientService patientService) {
        super(applicationContext);
        this.patientService = patientService;
    }

    @FXML
    public void initialize() {
        setupGenderComboBox();
        setupValidation();
        setupFieldWidths();
        maximizeWindow(titleLabel);
    }

    private void setupGenderComboBox() {
        genderComboBox.getItems().addAll("Male", "Female");
        genderComboBox.setPromptText("Select Gender");
    }

    private void setupFieldWidths() {
        // Set minimum widths for text fields
        firstNameField.setMinWidth(200);
        lastNameField.setMinWidth(200);
        mobileNoField.setMinWidth(200);
        emailField.setMinWidth(200);
        addressField.setMinWidth(400);
        emergencyContactNameField.setMinWidth(200);
        emergencyContactPhoneField.setMinWidth(200);

        // Make fields grow with window
        firstNameField.setMaxWidth(Double.MAX_VALUE);
        lastNameField.setMaxWidth(Double.MAX_VALUE);
        mobileNoField.setMaxWidth(Double.MAX_VALUE);
        emailField.setMaxWidth(Double.MAX_VALUE);
        addressField.setMaxWidth(Double.MAX_VALUE);
        emergencyContactNameField.setMaxWidth(Double.MAX_VALUE);
        emergencyContactPhoneField.setMaxWidth(Double.MAX_VALUE);
        insuranceInfoField.setMaxWidth(Double.MAX_VALUE);

        // Set specific sizes for special controls
        birthdayPicker.setPrefWidth(200);
        genderComboBox.setPrefWidth(200);
        insuranceInfoField.setPrefRowCount(3);
        insuranceInfoField.setPrefColumnCount(40);
    }

    public void setPatient(PatientEntity patient) {
        this.existingPatient = patient;
        if (patient != null) {
            populateForm();
            titleLabel.setText("Update Patient");
        }
    }

    private void setupValidation() {
        firstNameField.getStyleClass().add("required");
        lastNameField.getStyleClass().add("required");
        birthdayPicker.getStyleClass().add("required");
        mobileNoField.getStyleClass().add("required");
        emailField.getStyleClass().add("required");
        addressField.getStyleClass().add("required");
        genderComboBox.getStyleClass().add("required");
        emergencyContactNameField.getStyleClass().add("required");
        emergencyContactPhoneField.getStyleClass().add("required");
        insuranceInfoField.getStyleClass().add("required");
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (validateForm()) {
            try {
                PatientEntity patient = getPatientEntity();
                if (existingPatient != null) {
                    patientService.updatePatient(patient);
                } else {
                    patientService.addPatient(patient);
                }
                showSuccess("Success", "Patient details saved successfully!");
                loadView("PatientManagement.fxml", ((Node) event.getSource()), null);
            } catch (Exception e) {
                showError("Save Error", "Could not save patient: " + e.getMessage());
            }
        }
    }

    private PatientEntity getPatientEntity() {
        PatientEntity patient = existingPatient != null ? existingPatient : new PatientEntity();
        patient.setFirstName(firstNameField.getText().trim());
        patient.setLastName(lastNameField.getText().trim());
        patient.setBirthday(birthdayPicker.getValue());
        patient.setMobileNo(mobileNoField.getText().trim());
        patient.setEmail(emailField.getText().trim());
        patient.setAddress(addressField.getText().trim());
        patient.setGender(Gender.valueOf(genderComboBox.getValue().toUpperCase()));
        patient.setEmergencyContactName(emergencyContactNameField.getText().trim());
        patient.setEmergencyContactPhone(emergencyContactPhoneField.getText().trim());
        patient.setInsuranceInfo(insuranceInfoField.getText().trim());
        return patient;
    }

    private void populateForm() {
        if (existingPatient != null) {
            firstNameField.setText(existingPatient.getFirstName());
            lastNameField.setText(existingPatient.getLastName());
            birthdayPicker.setValue(existingPatient.getBirthday());
            mobileNoField.setText(existingPatient.getMobileNo());
            emailField.setText(existingPatient.getEmail());
            addressField.setText(existingPatient.getAddress());
            genderComboBox.setValue(existingPatient.getGender().toString());
            emergencyContactNameField.setText(existingPatient.getEmergencyContactName());
            emergencyContactPhoneField.setText(existingPatient.getEmergencyContactPhone());
            insuranceInfoField.setText(existingPatient.getInsuranceInfo());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        loadView("PatientManagement.fxml", ((Node) event.getSource()), null);
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (firstNameField.getText().trim().isEmpty()) errors.append("First Name is required\n");
        if (lastNameField.getText().trim().isEmpty()) errors.append("Last Name is required\n");
        if (birthdayPicker.getValue() == null) errors.append("Birthday is required\n");
        if (mobileNoField.getText().trim().isEmpty()) errors.append("Mobile No is required\n");
        if (emailField.getText().trim().isEmpty()) errors.append("Email is required\n");
        if (addressField.getText().trim().isEmpty()) errors.append("Address is required\n");
        if (genderComboBox.getValue() == null) errors.append("Gender is required\n");
        if (emergencyContactNameField.getText().trim().isEmpty()) errors.append("Emergency Contact Name is required\n");
        if (emergencyContactPhoneField.getText().trim().isEmpty()) errors.append("Emergency Contact Phone is required\n");
        if (insuranceInfoField.getText().trim().isEmpty()) errors.append("Insurance Information is required\n");

        if (!errors.isEmpty()) {
            showError("Validation Error", errors.toString());
            return false;
        }
        return true;
    }
}
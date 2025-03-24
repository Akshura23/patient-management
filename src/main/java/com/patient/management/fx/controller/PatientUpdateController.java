package com.patient.management.fx.controller;

import com.patient.management.entity.PatientEntity;
import com.patient.management.enums.Gender;
import com.patient.management.service.patient.PatientService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PatientUpdateController extends BaseController {

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
    private PatientEntity patient;

    @Autowired
    public PatientUpdateController(ApplicationContext applicationContext, PatientService patientService) {
        super(applicationContext);
        this.patientService = patientService;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
        populateForm();
    }

    private void populateForm() {
        if (patient != null) {
            firstNameField.setText(patient.getFirstName());
            lastNameField.setText(patient.getLastName());
            birthdayPicker.setValue(patient.getBirthday());
            mobileNoField.setText(patient.getMobileNo());
            emailField.setText(patient.getEmail());
            addressField.setText(patient.getAddress());
            genderComboBox.setValue(patient.getGender().toString());
            emergencyContactNameField.setText(patient.getEmergencyContactName());
            emergencyContactPhoneField.setText(patient.getEmergencyContactPhone());
            insuranceInfoField.setText(patient.getInsuranceInfo());
        }
    }

    @FXML
    public void handleUpdatePatient(ActionEvent event) {
        if (!validateInput()) {
            showError("Validation Error", "Please fill in all required fields correctly.");
            return;
        }

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

        try {
            patientService.updatePatient(patient);
            showInformation("Update Successful", "Patient details updated successfully.");
            loadView("PatientManagement.fxml", ((Node) event.getSource()), null);
        } catch (Exception e) {
            showError("Update Error", "An error occurred while updating patient details: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        loadView("PatientManagement.fxml", ((Node) event.getSource()), null);
    }

    private boolean validateInput() {
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
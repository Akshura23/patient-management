package com.patient.management.fx.controller;

import com.patient.management.entity.MedicalHistoryEntity;
import com.patient.management.entity.PatientEntity;
import com.patient.management.service.patient.PatientService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PatientManagementController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField birthdayField;
    @FXML
    private TextField mobileNoField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField genderField;
    @FXML
    private TextField emergencyContactNameField;
    @FXML
    private TextField emergencyContactPhoneField;
    @FXML
    private TextField insuranceInfoField;

    @FXML
    private TableView<PatientEntity> patientTable;
    @FXML
    private TableColumn<PatientEntity, String> firstNameColumn;
    @FXML
    private TableColumn<PatientEntity, String> lastNameColumn;
    @FXML
    private TableColumn<PatientEntity, LocalDate> birthdayColumn;
    @FXML
    private TableColumn<PatientEntity, String> mobileNoColumn;
    @FXML
    private TableColumn<PatientEntity, String> emailColumn;
    @FXML
    private TableColumn<PatientEntity, String> addressColumn;
    @FXML
    private TableColumn<PatientEntity, String> genderColumn;
    @FXML
    private TableColumn<PatientEntity, String> emergencyContactNameColumn;
    @FXML
    private TableColumn<PatientEntity, String> emergencyContactPhoneColumn;
    @FXML
    private TableColumn<PatientEntity, String> insuranceInfoColumn;

    private final PatientService patientService;

    @Autowired
    public PatientManagementController(PatientService patientService) {
        this.patientService = patientService;
    }

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        mobileNoColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNo"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        emergencyContactNameColumn.setCellValueFactory(new PropertyValueFactory<>("emergencyContactName"));
        emergencyContactPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("emergencyContactPhone"));
        insuranceInfoColumn.setCellValueFactory(new PropertyValueFactory<>("insuranceInfo"));
        patientTable.setItems(patientService.getAllPatients());
    }

    @FXML
    private void handleAddButtonAction() {
        PatientEntity patient = new PatientEntity(
                firstNameField.getText(),
                lastNameField.getText(),
                LocalDate.parse(birthdayField.getText()),
                mobileNoField.getText(),
                emailField.getText(),
                addressField.getText(),
                genderField.getText(),
                emergencyContactNameField.getText(),
                emergencyContactPhoneField.getText(),
                insuranceInfoField.getText()
        );
        patientService.addPatient(patient);
        patientTable.setItems(patientService.getAllPatients());
    }

    @FXML
    private void handleUpdateButtonAction() {
        PatientEntity selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            selectedPatient.setFirstName(firstNameField.getText());
            selectedPatient.setLastName(lastNameField.getText());
            selectedPatient.setBirthday(LocalDate.parse(birthdayField.getText()));
            selectedPatient.setMobileNo(mobileNoField.getText());
            selectedPatient.setEmail(emailField.getText());
            selectedPatient.setAddress(addressField.getText());
            selectedPatient.setGender(genderField.getText());
            selectedPatient.setEmergencyContactName(emergencyContactNameField.getText());
            selectedPatient.setEmergencyContactPhone(emergencyContactPhoneField.getText());
            selectedPatient.setInsuranceInfo(insuranceInfoField.getText());
            patientService.updatePatient(selectedPatient);
            patientTable.refresh();
        } else {
            showAlert("Please select a patient to update.");
        }
    }

    @FXML
    private void handleDeleteButtonAction() {
        PatientEntity selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            patientService.deletePatient(selectedPatient);
            patientTable.setItems(patientService.getAllPatients());
        } else {
            showAlert("Please select a patient to delete.");
        }
    }
    @FXML
    private void handleViewButtonAction() {
        PatientEntity selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            List<MedicalHistoryEntity> medicalHistories = selectedPatient.getMedicalHistories();
            showMedicalHistories(medicalHistories);
        } else {
            showAlert("Please select a patient to view medical history.");
        }
    }

    private void showMedicalHistories(List<MedicalHistoryEntity> medicalHistories) {
        StringBuilder historyDetails = new StringBuilder();
        for (MedicalHistoryEntity history : medicalHistories) {
            historyDetails.append("Date: ").append(history.getDate()).append("\n")
                    .append("Description: ").append(history.getDescription()).append("\n\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Medical History");
        alert.setHeaderText(null);
        alert.setContentText(historyDetails.toString());
        alert.showAndWait();
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No patient selected");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

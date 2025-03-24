package com.patient.management.fx.controller;

import com.patient.management.entity.PatientEntity;
import com.patient.management.service.patient.PatientService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Component
public class PatientManagementController extends BaseController {

    @FXML
    private TableView<PatientEntity> patientTable;
    @FXML
    private TableColumn<PatientEntity, String> uidColumn;
    @FXML
    private TableColumn<PatientEntity, String> firstNameColumn;
    @FXML
    private TableColumn<PatientEntity, String> lastNameColumn;
    @FXML
    private TableColumn<PatientEntity, String> birthdayColumn;
    @FXML
    private TableColumn<PatientEntity, Integer> ageColumn;
    @FXML
    private TableColumn<PatientEntity, String> mobileNoColumn;
    @FXML
    private TableColumn<PatientEntity, String> emailColumn;
    @FXML
    private TableColumn<PatientEntity, String> genderColumn;
    @FXML
    private TableColumn<PatientEntity, Void> actionsColumn;

    private final PatientService patientService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public PatientManagementController(ApplicationContext applicationContext, PatientService patientService) {
        super(applicationContext);
        this.patientService = patientService;
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        refreshTable();
        setupColumnWidths();
        maximizeWindow(patientTable);
    }

    private void setupColumnWidths() {
        uidColumn.setVisible(false);
        firstNameColumn.prefWidthProperty().bind(patientTable.widthProperty().multiply(0.12));
        lastNameColumn.prefWidthProperty().bind(patientTable.widthProperty().multiply(0.12));
        birthdayColumn.prefWidthProperty().bind(patientTable.widthProperty().multiply(0.12));
        ageColumn.prefWidthProperty().bind(patientTable.widthProperty().multiply(0.08));
        mobileNoColumn.prefWidthProperty().bind(patientTable.widthProperty().multiply(0.12));
        emailColumn.prefWidthProperty().bind(patientTable.widthProperty().multiply(0.15));
        genderColumn.prefWidthProperty().bind(patientTable.widthProperty().multiply(0.08));
        actionsColumn.prefWidthProperty().bind(patientTable.widthProperty().multiply(0.21));
    }

    private void setupTableColumns() {
        uidColumn.setCellValueFactory(new PropertyValueFactory<>("uid"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        mobileNoColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNo"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Configure age column
        ageColumn.setCellValueFactory(cellData -> {
            String birthday = String.valueOf(cellData.getValue().getBirthday());
            try {
                LocalDate birthDate = LocalDate.parse(birthday, dateFormatter);
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                return new javafx.beans.property.SimpleObjectProperty<>(age);
            } catch (Exception e) {
                return new javafx.beans.property.SimpleObjectProperty<>(0);
            }
        });

        // Center align all columns
        firstNameColumn.setStyle("-fx-alignment: CENTER;");
        lastNameColumn.setStyle("-fx-alignment: CENTER;");
        birthdayColumn.setStyle("-fx-alignment: CENTER;");
        ageColumn.setStyle("-fx-alignment: CENTER;");
        mobileNoColumn.setStyle("-fx-alignment: CENTER;");
        emailColumn.setStyle("-fx-alignment: CENTER;");
        genderColumn.setStyle("-fx-alignment: CENTER;");
        actionsColumn.setStyle("-fx-alignment: CENTER;");

        patientTable.setFixedCellSize(40);

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateBtn = new Button("Update");
            private final Button deleteBtn = new Button("Delete");
            private final Button viewBtn = new Button("View History");
            private final HBox buttons = new HBox(5, updateBtn, deleteBtn, viewBtn);

            {
                buttons.setStyle("-fx-alignment: CENTER;");
                updateBtn.getStyleClass().addAll("action-button", "update-button");
                deleteBtn.getStyleClass().addAll("action-button", "delete-button");
                viewBtn.getStyleClass().addAll("action-button", "view-button");

                updateBtn.setOnAction(e -> {
                    PatientEntity patient = getTableView().getItems().get(getIndex());
                    handleUpdate(e, patient);
                });

                deleteBtn.setOnAction(e -> {
                    PatientEntity patient = getTableView().getItems().get(getIndex());
                    handleDelete(patient);
                });

                viewBtn.setOnAction(e -> {
                    PatientEntity patient = getTableView().getItems().get(getIndex());
                    handleViewMedicalHistory(e, patient);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    @FXML
    public void handleAddNewPatient(ActionEvent event) {
        loadView("PatientCreationForm.fxml", ((Node) event.getSource()), null);
    }

    private void handleUpdate(ActionEvent event, PatientEntity patient) {
        if (patient == null) {
            showError("Selection Error", "Please select a patient to update.");
            return;
        }
        PatientEntity existingPatient = patientService.getPatientById(patient.getUid());
        loadView("PatientUpdateForm.fxml", ((Node) event.getSource()), existingPatient);
    }

    private void handleDelete(PatientEntity patient) {
        if (patient == null) {
            showError("Selection Error", "Please select a patient to delete.");
            return;
        }

        if (showConfirmation("Delete Patient",
                "Are you sure you want to delete patient: " +
                        patient.getFirstName() + " " + patient.getLastName() + "?")) {
            try {
                patientService.deletePatient(patient);
                refreshTable();
            } catch (Exception e) {
                showError("Delete Error", "Could not delete the patient: " + e.getMessage());
            }
        }
    }

    private void handleViewMedicalHistory(ActionEvent event, PatientEntity patient) {
        if (patient == null) {
            showError("Selection Error", "Please select a patient to view medical history.");
            return;
        }
        PatientEntity existingPatient = patientService.getPatientById(patient.getUid());
        loadView("MedicalHistory.fxml", ((Node) event.getSource()), existingPatient);
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        return alert.showAndWait()
                .filter(response -> response == buttonTypeYes)
                .isPresent();
    }

    private void refreshTable() {
        patientTable.getItems().clear();
        patientTable.getItems().addAll(patientService.getAllPatients());
    }

    @FXML
    public void onPatientTableClicked() {
        PatientEntity selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            // Handle row selection if needed
        }
    }
}
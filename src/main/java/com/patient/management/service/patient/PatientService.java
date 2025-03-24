package com.patient.management.service.patient;

import com.patient.management.entity.PatientEntity;
import javafx.collections.ObservableList;

public interface PatientService {
    ObservableList<PatientEntity> getAllPatients();

    void addPatient(PatientEntity patient);

    void updatePatient(PatientEntity selectedPatient);

    void deletePatient(PatientEntity selectedPatient);

    PatientEntity getPatientById(Integer uid);
}

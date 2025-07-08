package com.patient.management.service.patient;

import com.patient.management.entity.PatientEntity;

import java.util.List;

public interface PatientService {
    List<PatientEntity> getAllPatients();

    PatientEntity addPatient(PatientEntity patient);

    PatientEntity updatePatient(PatientEntity selectedPatient);

    void deletePatient(PatientEntity selectedPatient);

    PatientEntity getPatientById(Integer uid);
}

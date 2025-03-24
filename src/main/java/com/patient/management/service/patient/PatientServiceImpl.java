package com.patient.management.service.patient;

import com.patient.management.entity.PatientEntity;
import com.patient.management.repository.PatientRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public ObservableList<PatientEntity> getAllPatients() {
        return FXCollections.observableArrayList(patientRepository.findAll());
    }

    @Override
    public void addPatient(PatientEntity patient) {
        patientRepository.save(patient);
    }

    @Override
    public void updatePatient(PatientEntity selectedPatient) {
        patientRepository.save(selectedPatient);
    }

    @Override
    public void deletePatient(PatientEntity selectedPatient) {
        patientRepository.delete(selectedPatient);
    }

    @Override
    public PatientEntity getPatientById(Integer uid) {
        return patientRepository.findByUid(uid);
    }
}
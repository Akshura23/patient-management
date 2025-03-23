package com.patient.management.service.patient;

import com.patient.management.entity.PatientEntity;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {
    @Override
    public ObservableList<PatientEntity> getAllPatients() {
        return null;
    }

    @Override
    public void addPatient(PatientEntity patient) {

    }

    @Override
    public void updatePatient(PatientEntity selectedPatient) {

    }

    @Override
    public void deletePatient(PatientEntity selectedPatient) {

    }
}

package com.patient.management.service.patient;

import com.patient.management.entity.PatientEntity;
import com.patient.management.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PatientEntity> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public PatientEntity addPatient(PatientEntity patient) {
        return patientRepository.save(patient);
    }

    @Override
    public PatientEntity updatePatient(PatientEntity selectedPatient) {
        return patientRepository.save(selectedPatient);
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
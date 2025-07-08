package com.patient.management.controller;

import com.patient.management.entity.PatientEntity;
import com.patient.management.service.patient.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientEntity>> getAllPatients() {
        List<PatientEntity> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientEntity> getPatientById(@PathVariable Integer id) {
        PatientEntity patient = patientService.getPatientById(id);
        if (patient != null) {
            return ResponseEntity.ok(patient);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PatientEntity> createPatient(@RequestBody PatientEntity patient) {
        PatientEntity savedPatient = patientService.addPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientEntity> updatePatient(@PathVariable Integer id, @RequestBody PatientEntity patient) {
        PatientEntity existingPatient = patientService.getPatientById(id);
        if (existingPatient != null) {
            patient.setUid(id);
            PatientEntity updatedPatient = patientService.updatePatient(patient);
            return ResponseEntity.ok(updatedPatient);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        PatientEntity patient = patientService.getPatientById(id);
        if (patient != null) {
            patientService.deletePatient(patient);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
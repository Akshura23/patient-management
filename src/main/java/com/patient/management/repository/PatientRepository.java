package com.patient.management.repository;

import com.patient.management.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PatientRepository extends JpaRepository<PatientEntity, Integer>, JpaSpecificationExecutor<PatientEntity> {

}

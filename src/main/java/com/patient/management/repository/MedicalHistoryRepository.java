package com.patient.management.repository;

import com.patient.management.entity.MedicalHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistoryEntity, Integer>, JpaSpecificationExecutor<MedicalHistoryEntity> {
}

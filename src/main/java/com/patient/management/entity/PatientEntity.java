package com.patient.management.entity;

import com.patient.management.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patient")
public class PatientEntity extends AuditableEntity {
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;
    @Column(name = "mobile_no", nullable = false)
    private String mobileNo;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "address", nullable = false)
    private String address;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;
    @Column(name = "emergency_contact_name", nullable = false)
    private String emergencyContactName;
    @Column(name = "emergency_contact_phone", nullable = false)
    private String emergencyContactPhone;
    @Column(name = "insurance_info", nullable = false)
    private String insuranceInfo;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalHistoryEntity> medicalHistories;

    public PatientEntity(String firstName, String lastName, LocalDate birthday, String mobileNo, String email,
                         String address, Gender gender, String emergencyContactName, String emergencyContactPhone,
                         String insuranceInfo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.mobileNo = mobileNo;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.insuranceInfo = insuranceInfo;
    }
}
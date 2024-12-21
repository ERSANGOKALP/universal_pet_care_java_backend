package com.ersandev.universalpetcare.repository;

import com.ersandev.universalpetcare.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Long> {
}

package com.ersandev.universalpetcare.repository;

import com.ersandev.universalpetcare.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarianRepository extends JpaRepository<Veterinarian,Long> {
}

package com.ersandev.universalpetcare.repository;

import com.ersandev.universalpetcare.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet,Long> {
}

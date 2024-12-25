package com.ersandev.universalpetcare.repository;

import com.ersandev.universalpetcare.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
}

package com.ersandev.universalpetcare.repository;

import com.ersandev.universalpetcare.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query("SELECT r FROM Review r WHERE r.patient.id =:userId OR r.veterinarian.id =:userId ")
    Page<Review> findAllByUserId(@Param("userId") Long userId, PageRequest pageRequest);

    List<Review> findByVeterinarianId(Long veterinarianId);

    Optional<Review> findByVeterinarianIdAndPatientId(Long veterinarianId, Long reviewerId);
}
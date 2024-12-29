package com.ersandev.universalpetcare.repository;

import com.ersandev.universalpetcare.enums.AppointmentStatus;
import com.ersandev.universalpetcare.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    Appointment findByAppointmentNo(String appointmentNo);

    boolean existsByVeterinarianIdAndPatientIdAndStatus(Long veterinarianId, Long reviewerId, AppointmentStatus appointmentStatus);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id =:userId OR a.veterinarian.id =:userId ")
    List<Appointment> findAllByUserId(@Param("userId") Long userId);
}

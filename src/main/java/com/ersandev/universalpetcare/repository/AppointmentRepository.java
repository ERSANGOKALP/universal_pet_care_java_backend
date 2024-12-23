package com.ersandev.universalpetcare.repository;

import com.ersandev.universalpetcare.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    Appointment findByAppointmentNo(String appointmentNo);
}

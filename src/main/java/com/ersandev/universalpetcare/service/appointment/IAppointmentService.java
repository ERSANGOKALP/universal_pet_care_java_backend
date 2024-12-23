package com.ersandev.universalpetcare.service.appointment;

import com.ersandev.universalpetcare.model.Appointment;
import com.ersandev.universalpetcare.request.AppointmentUpdateRequest;

import java.util.List;

public interface IAppointmentService {
    Appointment createAppointment(Appointment appointment,Long senderId,Long recipientId);
    List<Appointment> getAllAppointments();
    Appointment updateAppointment(Long id, AppointmentUpdateRequest request);
    void deleteAppointment(Long id);
    Appointment getAppointmentById(Long id);
    Appointment getAppointmentByNo(String appointmentNo);
}

package com.ersandev.universalpetcare.request;

import com.ersandev.universalpetcare.model.Appointment;
import com.ersandev.universalpetcare.model.Pet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookAppointmentRequest {
    private Appointment appointment;
    private List<Pet> pets;
}

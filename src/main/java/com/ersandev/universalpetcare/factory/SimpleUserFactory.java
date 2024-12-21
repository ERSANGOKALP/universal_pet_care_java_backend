package com.ersandev.universalpetcare.factory;

import com.ersandev.universalpetcare.exception.UserAlreadyExistException;
import com.ersandev.universalpetcare.model.User;
import com.ersandev.universalpetcare.repository.UserRepository;
import com.ersandev.universalpetcare.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleUserFactory implements UserFactory{

    private final UserRepository userRepository;
    private final VeterinarianFactory veterinarianFactory;
    private final PatientFactory patientFactory;
    private final AdminFactory adminFactory;

    @Override
    public User createUser(RegistrationRequest registrationRequest) {

        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistException("Opps! " + registrationRequest.getEmail() +" already exist");
        }

        switch (registrationRequest.getUserType()){
            case "VET" -> {return veterinarianFactory.createVeterinarian(registrationRequest);}
            case "PATIENT" -> {return patientFactory.createPatient(registrationRequest);}
            case "ADMIN" -> {return adminFactory.createAdmin(registrationRequest);}
            default -> {
                return null;
            }
        }


    }
}

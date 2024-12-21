package com.ersandev.universalpetcare.factory;

import com.ersandev.universalpetcare.model.Patient;
import com.ersandev.universalpetcare.model.User;
import com.ersandev.universalpetcare.repository.PatientRepository;
import com.ersandev.universalpetcare.request.RegistrationRequest;
import com.ersandev.universalpetcare.service.user.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientFactory {

    private final PatientRepository patientRepository;
    private final UserAttributesMapper userAttributesMapper;

    public Patient createPatient(RegistrationRequest request) {
        Patient patient = new Patient();
        userAttributesMapper.setCommonAttributes(request,patient);

        return patientRepository.save(patient);
    }
}

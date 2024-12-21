package com.ersandev.universalpetcare.factory;

import com.ersandev.universalpetcare.model.User;
import com.ersandev.universalpetcare.model.Veterinarian;
import com.ersandev.universalpetcare.repository.VeterinarianRepository;
import com.ersandev.universalpetcare.request.RegistrationRequest;
import com.ersandev.universalpetcare.service.user.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VeterinarianFactory {

    private final VeterinarianRepository veterinarianRepository;
    private final UserAttributesMapper userAttributesMapper;

    public User createVeterinarian(RegistrationRequest request) {
        Veterinarian veterinarian = new Veterinarian();
        userAttributesMapper.setCommonAttributes(request,veterinarian);
        veterinarian.setSpecialization(request.getSpecialization());
        return veterinarianRepository.save(veterinarian);
    }

}

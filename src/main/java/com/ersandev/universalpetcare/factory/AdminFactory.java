package com.ersandev.universalpetcare.factory;

import com.ersandev.universalpetcare.model.Admin;
import com.ersandev.universalpetcare.repository.AdminRepository;
import com.ersandev.universalpetcare.request.RegistrationRequest;
import com.ersandev.universalpetcare.service.user.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminFactory {

    private final AdminRepository adminRepository;
    private final UserAttributesMapper userAttributesMapper;

    public Admin createAdmin(RegistrationRequest request) {

        Admin admin = new Admin();
        userAttributesMapper.setCommonAttributes(request,admin);
        return adminRepository.save(admin);
    }
}

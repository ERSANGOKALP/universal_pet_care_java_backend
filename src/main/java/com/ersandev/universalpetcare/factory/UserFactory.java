package com.ersandev.universalpetcare.factory;

import com.ersandev.universalpetcare.model.User;
import com.ersandev.universalpetcare.request.RegistrationRequest;

public interface UserFactory {

    public User createUser(RegistrationRequest registrationRequest);
}

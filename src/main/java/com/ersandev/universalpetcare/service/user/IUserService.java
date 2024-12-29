package com.ersandev.universalpetcare.service.user;

import com.ersandev.universalpetcare.dto.UserDto;
import com.ersandev.universalpetcare.model.User;
import com.ersandev.universalpetcare.request.RegistrationRequest;
import com.ersandev.universalpetcare.request.UserUpdateRequest;

import java.sql.SQLException;
import java.util.List;

public interface IUserService {
    User register(RegistrationRequest request);

    User update(Long userId, UserUpdateRequest request);

    User findById(Long userId);

    void delete(Long userId);

    List<UserDto> getAllUsers();

    UserDto getUserWithDetails(Long userId) throws SQLException;
}

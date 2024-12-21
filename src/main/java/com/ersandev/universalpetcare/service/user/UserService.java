package com.ersandev.universalpetcare.service.user;

import com.ersandev.universalpetcare.dto.EntityConverter;
import com.ersandev.universalpetcare.dto.UserDto;
import com.ersandev.universalpetcare.exception.ResourceNotFoundException;
import com.ersandev.universalpetcare.factory.UserFactory;
import com.ersandev.universalpetcare.model.User;
import com.ersandev.universalpetcare.repository.UserRepository;
import com.ersandev.universalpetcare.request.RegistrationRequest;
import com.ersandev.universalpetcare.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final EntityConverter<User,UserDto> entityConverter;
    @Override
    public User register(RegistrationRequest request) {

       return userFactory.createUser(request);
    }

    @Override
    public User update(Long userId, UserUpdateRequest request){
        User user = findById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setSpecialization(request.getSpecialization());
        return userRepository.save(user);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void delete(Long userId){
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, ()-> {
                    throw new ResourceNotFoundException("User not found");
                });
    }

    @Override
    public List<UserDto> getAllUsers(){
      List<User> users = userRepository.findAll();
      return users.stream()
              .map(user -> entityConverter.mapEntityToDto(user, UserDto.class))
              .collect(Collectors.toList());
    }

}

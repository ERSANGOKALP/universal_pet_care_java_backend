package com.ersandev.universalpetcare.service.user;

import com.ersandev.universalpetcare.dto.AppointmentDto;
import com.ersandev.universalpetcare.dto.EntityConverter;
import com.ersandev.universalpetcare.dto.ReviewDto;
import com.ersandev.universalpetcare.dto.UserDto;
import com.ersandev.universalpetcare.exception.ResourceNotFoundException;
import com.ersandev.universalpetcare.factory.UserFactory;
import com.ersandev.universalpetcare.model.Review;
import com.ersandev.universalpetcare.model.User;
import com.ersandev.universalpetcare.repository.UserRepository;
import com.ersandev.universalpetcare.repository.VeterinarianRepository;
import com.ersandev.universalpetcare.request.RegistrationRequest;
import com.ersandev.universalpetcare.request.UserUpdateRequest;
import com.ersandev.universalpetcare.service.appointment.AppointmentService;
import com.ersandev.universalpetcare.service.pet.IPetService;
import com.ersandev.universalpetcare.service.photo.PhotoService;
import com.ersandev.universalpetcare.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final VeterinarianRepository veterinarianRepository;
    private final EntityConverter<User, UserDto> entityConverter;
    private final AppointmentService appointmentService;
    private final IPetService petService;
    private final PhotoService photoService;
    private final ReviewService reviewService;

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

    @Override
    public UserDto getUserWithDetails(Long userId) throws SQLException {
        //1. get the user
        User user = findById(userId);
        //2. convert the user to a userDto
        UserDto userDto = entityConverter.mapEntityToDto(user, UserDto.class);
        //3. get user appointments ( users ( patient and a vet))
        setUserAppointment(userDto);
        //.4 get users photo
        setUserPhoto(userDto, user);
        setUserReviews(userDto, userId);
        return  userDto;
    }

    private void setUserAppointment(UserDto userDto) {
        List<AppointmentDto> appointments = appointmentService.getUserAppointments(userDto.getId());
        userDto.setAppointments(appointments);
    }
    private void setUserPhoto(UserDto userDto, User user) throws SQLException {
        if (user.getPhoto() != null) {
            userDto.setPhotoId(user.getPhoto().getId());
            userDto.setPhoto(photoService.getImageData(user.getPhoto().getId()));
        }
    }


    private void setUserReviews(UserDto userDto, Long userId) {
        Page<Review> reviewPage = reviewService.findAllReviewsByUserId(userId, 0, Integer.MAX_VALUE);
        List<ReviewDto> reviewDto = reviewPage.getContent()
                .stream()
                .map(this::mapReviewToDto).toList();
        if(!reviewDto.isEmpty()) {
            double averageRating = reviewService.getAverageRatingForVet(userId);
        }
        userDto.setReviews(reviewDto);
    }

    private ReviewDto mapReviewToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setStars(review.getStars());
        reviewDto.setFeedback(review.getFeedback());
        mapVeterinarianInfo(reviewDto, review);
        mapPatientInfo(reviewDto, review);
        return reviewDto;
    }
    private void mapVeterinarianInfo(ReviewDto reviewDto, Review review){
        if (review.getVeterinarian() != null) {
            reviewDto.setVeterinarianId(review.getVeterinarian().getId());
            reviewDto.setVeterinarianName(review.getVeterinarian().getFirstName()+ " " + review.getVeterinarian().getLastName());
            // set the photo
            setVeterinarianPhoto(reviewDto, review);
        }
    }

    private void mapPatientInfo(ReviewDto reviewDto, Review review) {
        if (review.getPatient() != null) {
            reviewDto.setPatientId(review.getPatient().getId());
            reviewDto.setPatientName(review.getPatient().getFirstName()+ " " + review.getPatient().getLastName());
            // set the photo
            setReviewerPhoto(reviewDto, review);
        }
    }

    private void setReviewerPhoto(ReviewDto reviewDto, Review review) {
        if(review.getPatient().getPhoto() != null){
            try {
                reviewDto.setPatientImage(photoService.getImageData(review.getPatient().getPhoto().getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }else {
            reviewDto.setPatientImage(null);
        }
    }

    private void setVeterinarianPhoto(ReviewDto reviewDto, Review review) {
        if(review.getVeterinarian().getPhoto() != null){
            try {
                reviewDto.setVeterinarianImage(photoService.getImageData(review.getVeterinarian().getPhoto().getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }else {
            reviewDto.setVeterinarianImage(null);
        }
    }

}

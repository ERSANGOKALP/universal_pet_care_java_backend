package com.ersandev.universalpetcare.service.review;

import com.ersandev.universalpetcare.enums.AppointmentStatus;
import com.ersandev.universalpetcare.exception.AlreadyExistsException;
import com.ersandev.universalpetcare.exception.ResourceNotFoundException;
import com.ersandev.universalpetcare.model.Review;
import com.ersandev.universalpetcare.model.User;
import com.ersandev.universalpetcare.repository.AppointmentRepository;
import com.ersandev.universalpetcare.repository.ReviewRepository;
import com.ersandev.universalpetcare.repository.UserRepository;
import com.ersandev.universalpetcare.request.ReviewUpdateRequest;
import com.ersandev.universalpetcare.utils.FeedBackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    public Review saveReview(Review review, Long reviewerId, Long veterinarianId) {
        // 1. Check if the reviewer is same as the doctor being reviewed
        if (veterinarianId.equals(reviewerId)){
            throw new IllegalArgumentException(FeedBackMessage.CANNOT_REVIEW);
        }

        //2. Check if the reviewer has previously submitted a review for this doctor.
        Optional<Review> existingReview = reviewRepository.findByVeterinarianIdAndPatientId(veterinarianId,reviewerId);
        if (existingReview.isPresent()){
            throw new AlreadyExistsException(FeedBackMessage.ALREADY_REVIEWED);
        }

        //3.Check if the reviewer has gotten a completed appointment with this doctor.
        boolean hadCompletedAppointments = appointmentRepository
                .existsByVeterinarianIdAndPatientIdAndStatus(veterinarianId,reviewerId, AppointmentStatus.COMPLETED);

        if (hadCompletedAppointments){
            throw new IllegalStateException(FeedBackMessage.NOT_ALLOWED);
        }
        //4 Get the veterinarian  from the database
        User veterinarian = userRepository.findById(veterinarianId)
                .orElseThrow(()-> new  ResourceNotFoundException(FeedBackMessage.VET_OR_PATIENT_NOT_FOUND));

        //5 Get the patient from the database
        User patient = userRepository.findById(reviewerId)
                .orElseThrow(()-> new ResourceNotFoundException(FeedBackMessage.VET_OR_PATIENT_NOT_FOUND));

        //6. Set both to the review
        review.setVeterinarian(veterinarian);
        review.setPatient(patient);

        //7. Save the review.
        return reviewRepository.save(review);

    }

    @Transactional
    @Override
    public double getAverageRatingForVet(Long veterinarianId) {
        List<Review> reviews = reviewRepository.findByVeterinarianId(veterinarianId);
        return reviews.isEmpty() ? 0 : reviews.stream()
                .mapToInt(Review :: getStars)
                .average()
                .orElse(0.0);
    }

    @Override
    public Review updateReview(Long reviewerId, ReviewUpdateRequest review) {
        return reviewRepository.findById(reviewerId)
                .map(existingReview -> {
                    existingReview.setStars(review.getStars());
                    existingReview.setFeedback(review.getFeedback());
                    return reviewRepository.save(existingReview);
                }).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.RESOURCE_NOT_FOUND));
    }

    @Override
    public Page<Review> findAllReviewsByUserId(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        return reviewRepository.findAllByUserId(userId,pageRequest);
    }

    @Override
    public void deleteReview(Long reviewerId) {
        reviewRepository.findById(reviewerId)
                .ifPresentOrElse(Review::removeRelationShip, ()-> {
                    throw new ResourceNotFoundException(FeedBackMessage.RESOURCE_NOT_FOUND);
                });
        reviewRepository.deleteById(reviewerId);

    }
}

package com.ersandev.universalpetcare.service.review;

import com.ersandev.universalpetcare.model.Review;
import com.ersandev.universalpetcare.request.ReviewUpdateRequest;
import org.springframework.data.domain.Page;

public interface IReviewService {
    Review saveReview(Review review,Long reviewerId,Long veterinarianId);
    double getAverageRatingForVet(Long veterinarianId);
    Review updateReview(Long reviewerId, ReviewUpdateRequest review);
    Page<Review> findAllReviewsByUserId(Long userId, int page, int size);

    void deleteReview(Long reviewerId);
}

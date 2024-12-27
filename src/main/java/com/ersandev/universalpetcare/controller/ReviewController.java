package com.ersandev.universalpetcare.controller;

import com.ersandev.universalpetcare.dto.ReviewDto;
import com.ersandev.universalpetcare.exception.AlreadyExistsException;
import com.ersandev.universalpetcare.exception.ResourceNotFoundException;
import com.ersandev.universalpetcare.model.Review;
import com.ersandev.universalpetcare.request.ReviewUpdateRequest;
import com.ersandev.universalpetcare.response.ApiResponse;
import com.ersandev.universalpetcare.service.review.IReviewService;
import com.ersandev.universalpetcare.utils.FeedBackMessage;
import com.ersandev.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(UrlMapping.REVIEWS)
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;
    private final ModelMapper modelMapper;

    @PostMapping(UrlMapping.SUBMIT_REVIEW)
    public ResponseEntity<ApiResponse> saveReview(@RequestBody Review review,
                                                  @RequestParam Long reviewerId,
                                                  @RequestParam Long vetId) {
        try {
            Review savedReview = reviewService.saveReview(review, reviewerId, vetId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.CREATE_SUCCESS, savedReview.getId()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new ApiResponse(e.getMessage(), null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMapping.GET_USER_REVIEWS)
    public ResponseEntity<ApiResponse> getReviewsByUserId(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        Page<Review> reviewPage = reviewService.findAllReviewsByUserId(userId, page, size);
        Page<ReviewDto> reviewDtos = reviewPage.map((element) -> modelMapper.map(element, ReviewDto.class));
        return ResponseEntity.status(FOUND).body(new ApiResponse(FeedBackMessage.RESOURCE_FOUND, reviewDtos));
    }

    @PutMapping(UrlMapping.UPDATE_REVIEW)
    public ResponseEntity<ApiResponse> updateReview(@RequestBody ReviewUpdateRequest updateRequest,
                                                    @PathVariable Long reviewId) {

        try {
            Review updateReview = reviewService.updateReview(reviewId, updateRequest);
            return ResponseEntity.ok().body(new ApiResponse(FeedBackMessage.UPDATE_SUCCESS, updateReview.getId()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping(UrlMapping.DELETE_REVIEW)
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId){
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok(new ApiResponse(FeedBackMessage.DELETE_SUCCESS, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping(UrlMapping.GET_AVERAGE_RATING)
    public ResponseEntity<ApiResponse> getAverageRatingForVet(@PathVariable Long vetId){
        double averageRating = reviewService.getAverageRatingForVet(vetId);
        return ResponseEntity.ok(new ApiResponse(FeedBackMessage.RESOURCE_FOUND, averageRating));
    }

}

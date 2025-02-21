package com.project.hotel.service;

import com.project.hotel.dto.request.ReviewRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.ReviewResponse;
import com.project.hotel.entity.UserEntity;
import org.springframework.http.ResponseEntity;

public interface ReviewService {

    ResponseEntity<ReviewResponse> add(ReviewRequest request);
    PageResponse<ReviewResponse> getAllReviews(int page, int size);
    ResponseEntity<ReviewResponse> findReviewById(Long reviewId);
    ResponseEntity<ReviewResponse> update(Long reviewId, ReviewRequest request);
    void delete(Long reviewId);
    UserEntity getCurrentUser();
}

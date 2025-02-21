package com.project.hotel.controller;

import com.project.hotel.dto.request.ReviewRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.ReviewResponse;
import com.project.hotel.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADD_REVIEW')")
    public ResponseEntity<ReviewResponse> add(@RequestBody @Valid ReviewRequest request) {
        return reviewService.add(request);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('GET_ALL_REVIEWS')")
    public PageResponse<ReviewResponse> getAllReviews(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return reviewService.getAllReviews(page, size);
    }

    @GetMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('FIND_REVIEW_BY_ID')")
    public ResponseEntity<ReviewResponse> findReviewById(@PathVariable Long reviewId) {
        return reviewService.findReviewById(reviewId);
    }

    @PutMapping("/update/{reviewId}")
    @PreAuthorize("hasAuthority('UPDATE_REVIEW')")
    public ResponseEntity<ReviewResponse> update(@PathVariable Long reviewId,
                                                 @RequestBody @Valid ReviewRequest request) {
        return reviewService.update(reviewId, request);
    }

    @DeleteMapping("/delete/{reviewId}")
    @PreAuthorize("hasAuthority('DELETE_REVIEW')")
    public void delete(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
    }
}

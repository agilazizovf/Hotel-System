package com.project.hotel.mapper;

import com.project.hotel.dto.response.ReviewResponse;
import com.project.hotel.entity.ReviewEntity;

public class ReviewMapper {

    public static ReviewResponse toReviewDTO(ReviewEntity review) {
        ReviewResponse response = new ReviewResponse();
        response.setTitle(review.getTitle());
        response.setComment(review.getComment());
        response.setRating(review.getRating());
        response.setHotelName(review.getHotel().getName());
        response.setRoomNumber(review.getRoom().getRoomNumber());

        return response;
    }
}

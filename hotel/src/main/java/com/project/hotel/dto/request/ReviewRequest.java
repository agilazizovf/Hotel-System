package com.project.hotel.dto.request;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class ReviewRequest {

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Comment cannot be blank")
    @Size(min = 10, max = 500, message = "Comment must be between 10 and 500 characters")
    private String comment;

    @NotNull(message = "Hotel ID is required")
    @Positive(message = "Hotel ID must be a positive number")
    private Long hotelId;

    @Positive(message = "Room ID must be a positive number")
    private Long roomId;
}

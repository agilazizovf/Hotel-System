package com.project.hotel.dto.response;

import lombok.Data;

@Data
public class ReviewResponse {

    private String title;
    private Integer rating;
    private String comment;
    private String hotelName;
    private Integer roomNumber;
}

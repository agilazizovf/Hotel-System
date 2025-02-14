package com.project.hotel.dto.response;

import com.project.hotel.enums.RoomType;
import lombok.Data;

@Data
public class RoomInfoResponse {

    private Long id;
    private RoomType roomType;
    private Integer roomNumber;
    private double pricePerNight;
    private Boolean isAvailable;
    private Integer capacity;
    private String hotelName;

}

package com.project.hotel.dto.request;

import com.project.hotel.enums.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomRequest {

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @Min(value = 0, message = "Price per night must be non-negative")
    private double pricePerNight;

    @Min(value = 1, message = "Room number must be non-negative")
    private Integer roomNumber;

    @NotNull(message = "Hotel ID is required")
    private Long hotelId;
}

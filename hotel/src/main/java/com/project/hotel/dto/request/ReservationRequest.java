package com.project.hotel.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationRequest {

    @NotNull(message = "Hotel ID cannot be null")
    private Long hotelId;

    @NotNull(message = "Room ID cannot be null")
    private Long roomId;

    @NotNull(message = "Check-in date cannot be null")
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date cannot be null")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    @NotNull(message = "Number of guests cannot be null")
    @Min(value = 1, message = "Number of guests must be at least 1")
    private Integer numberOfGuests;
}

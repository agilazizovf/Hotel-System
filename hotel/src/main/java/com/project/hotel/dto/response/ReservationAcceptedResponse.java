package com.project.hotel.dto.response;

import com.project.hotel.enums.ReservationStatus;
import com.project.hotel.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationAcceptedResponse {

    private Long id;
    private String hotelName;
    private RoomType roomType;
    private Integer roomNumber;
    private Integer numberOfGuests;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private ReservationStatus status;
}

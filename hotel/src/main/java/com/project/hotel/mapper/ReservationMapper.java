package com.project.hotel.mapper;

import com.project.hotel.dto.response.ReservationAcceptedResponse;
import com.project.hotel.entity.ReservationEntity;

public class ReservationMapper {

    public static ReservationAcceptedResponse toReservationDTO(ReservationEntity reservation) {
        ReservationAcceptedResponse response = new ReservationAcceptedResponse();
        response.setId(reservation.getId());
        response.setHotelName(reservation.getHotel().getName());
        response.setRoomType(reservation.getRoom().getRoomType());
        response.setRoomNumber(reservation.getRoom().getRoomNumber());
        response.setNumberOfGuests(reservation.getNumberOfGuests());
        response.setCheckInDate(reservation.getCheckInDate());
        response.setCheckOutDate(reservation.getCheckOutDate());
        response.setTotalPrice(reservation.getTotalPrice());
        response.setStatus(reservation.getStatus());

        return response;
    }
}

package com.project.hotel.service;

import com.project.hotel.dto.request.ReservationRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.ReservationAcceptedResponse;
import com.project.hotel.dto.response.ReservationPendingResponse;
import com.project.hotel.entity.UserEntity;
import com.project.hotel.enums.ReservationStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReservationService {

    ResponseEntity<ReservationPendingResponse> add(ReservationRequest request);
    PageResponse<ReservationAcceptedResponse> getAllReservations(int page, int size);
    ResponseEntity<ReservationAcceptedResponse> getReservationById(Long reservationId);
    ResponseEntity<List<ReservationAcceptedResponse>> findReservationsByUser();
    ResponseEntity<ReservationPendingResponse> update(Long reservationId, ReservationRequest request);
    ResponseEntity<String> confirmReservation(Long reservationId);
    ResponseEntity<String> completeReservation(Long reservationId);
    ResponseEntity<String> checkedInReservation(Long reservationId);
    ResponseEntity<String> cancelReservation(Long reservationId);
    UserEntity getCurrentUser();
}

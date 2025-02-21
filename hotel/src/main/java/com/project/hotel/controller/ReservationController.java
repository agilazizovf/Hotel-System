package com.project.hotel.controller;

import com.project.hotel.dto.request.ReservationRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.ReservationAcceptedResponse;
import com.project.hotel.dto.response.ReservationPendingResponse;
import com.project.hotel.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADD_RESERVATION')")
    public ResponseEntity<ReservationPendingResponse> addReservation(@RequestBody ReservationRequest request) {
        return reservationService.add(request);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('GET_ALL_RESERVATIONS')")
    public PageResponse<ReservationAcceptedResponse> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return reservationService.getAllReservations(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FIND_RESERVATION_BY_ID')")
    public ResponseEntity<ReservationAcceptedResponse> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('FIND_RESERVATION_BY_USER')")
    public ResponseEntity<List<ReservationAcceptedResponse>> findReservationsByUser() {
        return reservationService.findReservationsByUser();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('UPDATE_RESERVATION')")
    public ResponseEntity<ReservationPendingResponse> updateReservation(
            @PathVariable Long id, @RequestBody ReservationRequest request) {
        return reservationService.update(id, request);
    }

    @PutMapping("/confirm/{id}")
    @PreAuthorize("hasAuthority('CONFIRM_RESERVATION')")
    public ResponseEntity<String> confirmReservation(@PathVariable Long id) {
        return reservationService.confirmReservation(id);
    }

    @PutMapping("/complete/{id}")
    @PreAuthorize("hasAuthority('COMPLETE_RESERVATION')")
    public ResponseEntity<String> completeReservation(@PathVariable Long id) {
        return reservationService.completeReservation(id);
    }

    @PutMapping("/check-in/{id}")
    @PreAuthorize("hasAuthority('CHECK_IN_RESERVATION')")
    public ResponseEntity<String> checkedInReservation(@PathVariable Long id) {
        return reservationService.checkedInReservation(id);
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasAuthority('CANCEL_RESERVATION')")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }
}

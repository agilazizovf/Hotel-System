package com.project.hotel.controller;

import com.project.hotel.dto.request.ReservationRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.ReservationAcceptedResponse;
import com.project.hotel.dto.response.ReservationPendingResponse;
import com.project.hotel.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity<ReservationPendingResponse> addReservation(@RequestBody ReservationRequest request) {
        return reservationService.add(request);
    }

    @GetMapping("/get-all")
    public PageResponse<ReservationAcceptedResponse> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return reservationService.getAllReservations(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationAcceptedResponse> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReservationAcceptedResponse>> findReservationsByUser() {
        return reservationService.findReservationsByUser();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReservationPendingResponse> updateReservation(
            @PathVariable Long id, @RequestBody ReservationRequest request) {
        return reservationService.update(id, request);
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<String> confirmReservation(@PathVariable Long id) {
        return reservationService.confirmReservation(id);
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<String> completeReservation(@PathVariable Long id) {
        return reservationService.completeReservation(id);
    }

    @PutMapping("/check-in/{id}")
    public ResponseEntity<String> checkedInReservation(@PathVariable Long id) {
        return reservationService.checkedInReservation(id);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }
}

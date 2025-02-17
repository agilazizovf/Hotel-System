package com.project.hotel.service.impl;

import com.project.hotel.dto.request.ReservationRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.ReservationAcceptedResponse;
import com.project.hotel.dto.response.ReservationPendingResponse;
import com.project.hotel.entity.HotelEntity;
import com.project.hotel.entity.ReservationEntity;
import com.project.hotel.entity.RoomEntity;
import com.project.hotel.entity.UserEntity;
import com.project.hotel.enums.ReservationStatus;
import com.project.hotel.exception.CustomException;
import com.project.hotel.mapper.ReservationMapper;
import com.project.hotel.repository.*;
import com.project.hotel.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ModelMapper mapper;

    @Override
    public ResponseEntity<ReservationPendingResponse> add(ReservationRequest request) {
        UserEntity user = getCurrentUser();

        HotelEntity hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new CustomException("Hotel tapılmadı", "Hotel not found", "Not found",
                        404, null));

        RoomEntity room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new CustomException("Otaq tapılmadı", "Room not found", "Not found",
                        404, null));

        if (!isRoomAvailable(room, request.getCheckInDate(), request.getCheckOutDate())) {
            throw new CustomException("Otaq mövcud deyil", "The selected room is not available for the requested dates", "Conflict", 409, null);
        }
        long totalNights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        double totalPrice = totalNights * room.getPricePerNight();

        ReservationEntity reservation = new ReservationEntity();
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setNumberOfGuests(request.getNumberOfGuests());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setHotel(hotel);
        reservation.setRoom(room);
        reservation.setTotalPrice(totalPrice);
        reservation.setUser(user);
        reservationRepository.save(reservation);

        ReservationPendingResponse response = new ReservationPendingResponse();
        response.setMessage("Reservation is pending");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public PageResponse<ReservationAcceptedResponse> getAllReservations(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new CustomException("Səhifə və ya ölçü etibarsızdır", "Invalid page or size",
                    "Bad Request", 400, null);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationEntity> reservationEntities = reservationRepository.findAll(pageable);

        List<ReservationAcceptedResponse> responses = reservationEntities
                .stream()
                .map(ReservationMapper::toReservationDTO)
                .collect(Collectors.toList());

        return getReservationPageResponse(responses, reservationEntities);
    }

    private static PageResponse<ReservationAcceptedResponse> getReservationPageResponse(
            List<ReservationAcceptedResponse> responses, Page<ReservationEntity> reservationEntities) {

        PageResponse<ReservationAcceptedResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(responses);
        pageResponse.setPage(reservationEntities.getPageable().getPageNumber());
        pageResponse.setSize(reservationEntities.getPageable().getPageSize());
        pageResponse.setTotalElements(reservationEntities.getTotalElements());
        pageResponse.setTotalPages(reservationEntities.getTotalPages());
        pageResponse.setLast(reservationEntities.isLast());
        pageResponse.setFirst(reservationEntities.isFirst());

        return pageResponse;
    }


    @Override
    public ResponseEntity<ReservationAcceptedResponse> getReservationById(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException("Rezervasiya tapılmadı", "Reservation not found", "Not found",
                        404, null));
        ReservationAcceptedResponse res = new ReservationAcceptedResponse();
        mapper.map(reservation, res);
        res.setHotelName(reservation.getHotel().getName());
        res.setRoomType(reservation.getRoom().getRoomType());
        res.setRoomNumber(reservation.getRoom().getRoomNumber());

        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<List<ReservationAcceptedResponse>> findReservationsByUser() {
        UserEntity user = getCurrentUser();

        List<ReservationEntity> reservations = reservationRepository.findByUserEmail(user.getEmail());
        List<ReservationAcceptedResponse> response = reservations.stream()
                .map(reservation -> {
                    ReservationAcceptedResponse res = new ReservationAcceptedResponse();
                    res.setId(reservation.getId());
                    res.setHotelName(reservation.getHotel().getName());
                    res.setRoomType(reservation.getRoom().getRoomType());
                    res.setRoomNumber(reservation.getRoom().getRoomNumber());
                    res.setCheckInDate(reservation.getCheckInDate());
                    res.setCheckOutDate(reservation.getCheckOutDate());
                    res.setNumberOfGuests(reservation.getNumberOfGuests());
                    res.setStatus(reservation.getStatus());
                    res.setTotalPrice(reservation.getTotalPrice());
                    return res;
                })
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @Override
    public ResponseEntity<ReservationPendingResponse> update(Long reservationId, ReservationRequest request) {
        UserEntity user = getCurrentUser();
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException("Rezervasiya tapılmadı", "Reservation not found", "Not found",
                        404, null));
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Reservation cannot be updated as it is not in 'PENDING' status.");
        }

        HotelEntity hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new CustomException("Hotel tapılmadı", "Hotel not found", "Not found",
                        404, null));

        RoomEntity room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new CustomException("Otaq tapılmadı", "Room not found", "Not found",
                        404, null));

        if (!isRoomAvailable(room, request.getCheckInDate(), request.getCheckOutDate())) {
            throw new CustomException("Otaq mövcud deyil", "The selected room is not available for the requested dates", "Conflict", 409, null);
        }

        long totalNights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        double totalPrice = totalNights * room.getPricePerNight();

        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setNumberOfGuests(request.getNumberOfGuests());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setHotel(hotel);
        reservation.setRoom(room);
        reservation.setTotalPrice(totalPrice);
        reservation.setUser(user);
        reservationRepository.save(reservation);

        ReservationPendingResponse response = new ReservationPendingResponse();
        response.setMessage("Reservation is pending");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<String> confirmReservation(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException("Rezervasiya tapılmadı", "Reservation not found", "Not found",
                        404, null));

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        return ResponseEntity.status(HttpStatus.OK).body("Reservation status "+ReservationStatus.CONFIRMED);
    }

    @Override
    public ResponseEntity<String> completeReservation(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException("Rezervasiya tapılmadı", "Reservation not found", "Not found",
                        404, null));

        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new IllegalStateException("Reservation must be in 'CHECKED_IN' status to be completed.");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservationRepository.save(reservation);

        return ResponseEntity.status(HttpStatus.OK).body("Reservation status "+ReservationStatus.COMPLETED);
    }

    @Override
    public ResponseEntity<String> checkedInReservation(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException("Rezervasiya tapılmadı", "Reservation not found", "Not found",
                        404, null));

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Reservation must be in 'CONFIRMED' status to be checked-in.");
        }

        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservationRepository.save(reservation);

        return ResponseEntity.status(HttpStatus.OK).body("Reservation status "+ReservationStatus.CHECKED_IN);
    }

    @Override
    public ResponseEntity<String> cancelReservation(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException("Rezervasiya tapılmadı", "Reservation not found", "Not found",
                        404, null));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Reservation status "+ReservationStatus.CANCELLED);
    }

    @Override
    public UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("İstifadəçi tapılmadı", "User not found",
                        "Not found", 404, null));
    }

    private boolean isRoomAvailable(RoomEntity room, LocalDate checkInDate, LocalDate checkOutDate) {
        Optional<ReservationEntity> existingReservation = reservationRepository
                .findByRoomAndCheckInDateBeforeAndCheckOutDateAfter(
                room, checkOutDate, checkInDate);

        return existingReservation.isEmpty();
    }
}

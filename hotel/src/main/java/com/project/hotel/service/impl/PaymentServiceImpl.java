package com.project.hotel.service.impl;

import com.project.hotel.dto.request.PaymentRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.PaymentResponse;
import com.project.hotel.dto.response.ReservationAcceptedResponse;
import com.project.hotel.entity.HotelEntity;
import com.project.hotel.entity.PaymentEntity;
import com.project.hotel.entity.ReservationEntity;
import com.project.hotel.entity.UserEntity;
import com.project.hotel.enums.PaymentStatus;
import com.project.hotel.exception.CustomException;
import com.project.hotel.mapper.PaymentMapper;
import com.project.hotel.mapper.ReservationMapper;
import com.project.hotel.repository.HotelRepository;
import com.project.hotel.repository.PaymentRepository;
import com.project.hotel.repository.ReservationRepository;
import com.project.hotel.repository.UserRepository;
import com.project.hotel.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    @Override
    public ResponseEntity<PaymentResponse> processPayment(PaymentRequest request) {
        UserEntity user = getCurrentUser();

        ReservationEntity reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new CustomException("Rezervasiya tapılmadı", "Reservation not found", "Not found",
                        404, null));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new CustomException("İcazəsiz giriş", "Unauthorized access",
                    "Forbidden", 403, null);
        }

        double totalPrice = reservation.getTotalPrice();
        double totalPaid = paymentRepository.findByReservation(reservation)
                .stream()
                .mapToDouble(PaymentEntity::getAmount)
                .sum();

        double remainingAmount = totalPrice - totalPaid;
        if (request.getAmount() > remainingAmount) {
            throw new CustomException("Ödəniş məbləği çoxdur", "Payment amount exceeds remaining balance", "Bad Request", 400, null);
        }


        PaymentEntity payment = new PaymentEntity();
        payment.setReservation(reservation);
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(LocalDateTime.now());

        totalPaid += request.getAmount();
        remainingAmount = totalPrice - totalPaid;

        if (request.getAmount() <= 0) {
            payment.setStatus(PaymentStatus.FAILED);
        } else if (remainingAmount == 0) {
            payment.setStatus(PaymentStatus.COMPLETED);
        } else {
            payment.setStatus(PaymentStatus.PARTIAL);
        }

        paymentRepository.save(payment);

        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setReservationId(reservation.getId());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setPaymentDate(payment.getPaymentDate());

        return ResponseEntity.ok(response);
    }

    @Override
    public PageResponse<PaymentResponse> getAllPayments(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new CustomException("Səhifə və ya ölçü etibarsızdır", "Invalid page or size",
                    "Bad Request", 400, null);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentEntity> paymentEntities = paymentRepository.findAll(pageable);

        List<PaymentResponse> responses = paymentEntities
                .stream()
                .map(PaymentMapper::toPaymentDTO)
                .collect(Collectors.toList());

        return getPaymentPageResponse(responses, paymentEntities);
    }

    @Override
    public ResponseEntity<PaymentResponse> findPaymentById(Long paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException("Ödəniş tapılmadı", "Payment not found", "Not found",
                        404, null));
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setReservationId(payment.getReservation().getId());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setPaymentDate(payment.getPaymentDate());

        return ResponseEntity.ok(response);
    }

    private static PageResponse<PaymentResponse> getPaymentPageResponse(
            List<PaymentResponse> responses, Page<PaymentEntity> reservationEntities) {

        PageResponse<PaymentResponse> pageResponse = new PageResponse<>();
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
    public ResponseEntity<List<PaymentResponse>> findPaymentByHotelId(Long hotelId) {
        // Ensure the hotel exists
        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new CustomException("Otel tapılmadı", "Hotel not found",
                        "Not Found", 404, null));

        // Retrieve payments associated with reservations for this hotel
        List<PaymentEntity> payments = paymentRepository.findByReservation_Hotel(hotel);

        if (payments.isEmpty()) {
            throw new CustomException("Bu otel üçün ödəniş tapılmadı", "No payments found for this hotel",
                    "Not Found", 404, null);
        }

        // Convert to PaymentResponse DTO
        List<PaymentResponse> responses = payments.stream().map(payment -> {
            PaymentResponse response = new PaymentResponse();
            response.setId(payment.getId());
            response.setReservationId(payment.getReservation().getId());
            response.setAmount(payment.getAmount());
            response.setStatus(payment.getStatus());
            response.setPaymentDate(payment.getPaymentDate());
            return response;
        }).toList();

        return ResponseEntity.ok(responses);
    }


    @Override
    public UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("İstifadəçi tapılmadı", "User not found",
                        "Not found", 404, null));
    }
}

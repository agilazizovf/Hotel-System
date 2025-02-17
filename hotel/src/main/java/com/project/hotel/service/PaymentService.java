package com.project.hotel.service;

import com.project.hotel.dto.request.PaymentRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.PaymentResponse;
import com.project.hotel.entity.ReservationEntity;
import com.project.hotel.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {

    ResponseEntity<PaymentResponse> processPayment(PaymentRequest request);
    PageResponse<PaymentResponse> getAllPayments(int page, int size);
    ResponseEntity<PaymentResponse> findPaymentById(Long paymentId);
    ResponseEntity<List<PaymentResponse>> findPaymentByHotelId(Long hotelId);
    UserEntity getCurrentUser();
}

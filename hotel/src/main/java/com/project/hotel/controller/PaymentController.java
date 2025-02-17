package com.project.hotel.controller;

import com.project.hotel.dto.request.PaymentRequest;
import com.project.hotel.dto.response.PageResponse;
import com.project.hotel.dto.response.PaymentResponse;
import com.project.hotel.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody @Valid PaymentRequest request) {
        return paymentService.processPayment(request);
    }

    @GetMapping("/get-all")
    public PageResponse<PaymentResponse> getAllPayments(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return paymentService.getAllPayments(page, size);
    }

    @GetMapping("/by-payment/{paymentId}")
    public ResponseEntity<PaymentResponse> findPaymentById(@PathVariable Long paymentId) {
        return paymentService.findPaymentById(paymentId);
    }

    @GetMapping("/by-hotel/{hotelId}")
    public ResponseEntity<List<PaymentResponse>> findPaymentByHotelId(@PathVariable Long hotelId) {
        return paymentService.findPaymentByHotelId(hotelId);
    }
}

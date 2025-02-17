package com.project.hotel.dto.response;

import com.project.hotel.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {

    private Long id;
    private Long reservationId;
    private double amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
}

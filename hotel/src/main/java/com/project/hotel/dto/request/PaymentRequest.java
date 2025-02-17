package com.project.hotel.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long reservationId;
    private double amount;
}

package com.project.hotel.mapper;

import com.project.hotel.dto.response.PaymentResponse;
import com.project.hotel.entity.PaymentEntity;

public class PaymentMapper {

    public static PaymentResponse toPaymentDTO(PaymentEntity payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setPaymentDate(payment.getPaymentDate());
        response.setReservationId(payment.getReservation().getId());
        response.setStatus(payment.getStatus());

        return response;
    }
}

package com.project.hotel.service;

import com.project.hotel.dto.request.CustomerRequest;
import com.project.hotel.dto.response.CustomerResponse;
import org.springframework.http.ResponseEntity;

public interface CustomerService {

    ResponseEntity<CustomerResponse> register(CustomerRequest request);
}

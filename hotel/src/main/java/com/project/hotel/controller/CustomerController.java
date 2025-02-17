package com.project.hotel.controller;

import com.project.hotel.dto.request.CustomerRequest;
import com.project.hotel.dto.response.CustomerResponse;
import com.project.hotel.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> registration(@RequestBody @Valid CustomerRequest request) {
        return customerService.register(request);
    }
}

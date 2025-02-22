package com.project.hotel.service;

import com.project.hotel.dto.request.EmployeeRequest;
import com.project.hotel.dto.response.EmployeeResponse;
import org.springframework.http.ResponseEntity;

public interface EmployeeService {

    ResponseEntity<EmployeeResponse> register(EmployeeRequest request);
}

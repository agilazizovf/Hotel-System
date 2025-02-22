package com.project.hotel.controller;

import com.project.hotel.dto.request.EmployeeRequest;
import com.project.hotel.dto.response.EmployeeResponse;
import com.project.hotel.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADD_EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> registration(@RequestBody @Valid EmployeeRequest request) {
        return employeeService.register(request);
    }
}

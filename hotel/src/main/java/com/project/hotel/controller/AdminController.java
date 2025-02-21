package com.project.hotel.controller;

import com.project.hotel.dto.request.AdminRequest;
import com.project.hotel.dto.response.AdminResponse;
import com.project.hotel.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADD_ADMIN')")
    public ResponseEntity<AdminResponse> registration(@RequestBody @Valid AdminRequest request) {
        return adminService.register(request);
    }
}

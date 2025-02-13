package com.project.hotel.service;

import com.project.hotel.dto.request.AdminRequest;
import com.project.hotel.dto.response.AdminResponse;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<AdminResponse> register(AdminRequest request);
}

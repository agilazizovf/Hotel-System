package com.project.hotel.service;

import com.project.hotel.dto.request.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> login(LoginRequest request);
}

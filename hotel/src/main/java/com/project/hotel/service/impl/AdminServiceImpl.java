package com.project.hotel.service.impl;

import com.project.hotel.dto.request.AdminRequest;
import com.project.hotel.dto.response.AdminResponse;
import com.project.hotel.entity.AdminEntity;
import com.project.hotel.entity.UserEntity;
import com.project.hotel.exception.CustomException;
import com.project.hotel.repository.AdminRepository;
import com.project.hotel.repository.RoleRepository;
import com.project.hotel.repository.UserRepository;
import com.project.hotel.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public ResponseEntity<AdminResponse> register(AdminRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("İstifadəçi artıq mövcuddur", "User already exists", "Already exists",
                    400, null);
        }
        UserEntity user = new UserEntity(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        AdminEntity admin = new AdminEntity();
        mapper.map(request, admin);
        admin.setRegisterDate(LocalDateTime.now());
        admin.setUpdateDate(LocalDateTime.now());
        admin.setUser(user);
        adminRepository.save(admin);

        roleRepository.addAdminRoles(user.getId());

        AdminResponse response = new AdminResponse();
        response.setName(admin.getName());
        response.setSurname(admin.getSurname());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

package com.project.hotel.service.impl;

import com.project.hotel.dto.request.EmployeeRequest;
import com.project.hotel.dto.response.EmployeeResponse;
import com.project.hotel.entity.EmployeeEntity;
import com.project.hotel.entity.UserEntity;
import com.project.hotel.exception.CustomException;
import com.project.hotel.repository.EmployeeRepository;
import com.project.hotel.repository.RoleRepository;
import com.project.hotel.repository.UserRepository;
import com.project.hotel.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Override
    public ResponseEntity<EmployeeResponse> register(EmployeeRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("İstifadəçi artıq mövcuddur", "User already exists", "Already exists",
                    400, null);
        }
        UserEntity user = new UserEntity(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        EmployeeEntity employee = new EmployeeEntity();
        mapper.map(request, employee);
        employee.setRegisterDate(LocalDateTime.now());
        employee.setUpdateDate(LocalDateTime.now());
        employee.setUser(user);

        employeeRepository.save(employee);

        roleRepository.addEmployeeRoles(user.getId());

        EmployeeResponse response = new EmployeeResponse();
        response.setName(employee.getName());
        response.setSurname(employee.getSurname());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

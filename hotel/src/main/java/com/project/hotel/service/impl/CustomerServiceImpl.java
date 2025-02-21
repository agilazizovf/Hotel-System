package com.project.hotel.service.impl;

import com.project.hotel.dto.request.CustomerRequest;
import com.project.hotel.dto.response.CustomerResponse;
import com.project.hotel.entity.CustomerEntity;
import com.project.hotel.entity.UserEntity;
import com.project.hotel.exception.CustomException;
import com.project.hotel.repository.CustomerRepository;
import com.project.hotel.repository.RoleRepository;
import com.project.hotel.repository.UserRepository;
import com.project.hotel.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<CustomerResponse> register(CustomerRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("İstifadəçi artıq mövcuddur", "User already exists", "Already exists",
                    400, null);
        }
        UserEntity user = new UserEntity(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        CustomerEntity customer = new CustomerEntity();
        mapper.map(request, customer);
        customer.setRegisterDate(LocalDateTime.now());
        customer.setUpdateDate(LocalDateTime.now());
        customer.setUser(user);

        customerRepository.save(customer);

        roleRepository.addCustomerRoles(user.getId());

        CustomerResponse response = new CustomerResponse();
        response.setName(customer.getName());
        response.setSurname(customer.getSurname());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

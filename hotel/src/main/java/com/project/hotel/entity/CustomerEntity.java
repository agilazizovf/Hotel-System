package com.project.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String email;
    private String phone;
    private LocalDate birthday;

    private String city;
    private String state;
    private String country;

    private LocalDateTime registerDate;
    private LocalDateTime updateDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}

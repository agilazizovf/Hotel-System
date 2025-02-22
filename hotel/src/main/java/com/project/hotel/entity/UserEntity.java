package com.project.hotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private AdminEntity admin;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private CustomerEntity customer;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private EmployeeEntity employee;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<HotelEntity> hotels;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private ReviewEntity review;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ReservationEntity> reservations;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<RoomEntity> rooms;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    public UserEntity(String username, String password) {
        this.email = username;
        this.password = password;
    }
}

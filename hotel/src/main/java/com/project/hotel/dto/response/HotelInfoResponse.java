package com.project.hotel.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class HotelInfoResponse {
    private Long id;
    private String name;
    private String location;
    private double rating; // Needs calculation
    private String description;
    private Integer numberOfRooms; // Needs calculation
    private double pricePerNight;
    private int availableRooms; // Needs calculation or something else
    private boolean hasPool;
    private boolean hasGym;
    private boolean hasWiFi;
    private boolean hasRestaurant;
    private boolean hasParking;
    private boolean hasSpa;
    private String contactNumber;
    private String email;
    private String website;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String city;
    private String state;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

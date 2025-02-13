package com.project.hotel.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalTime;

@Data
public class HotelRequest {

    @NotBlank(message = "Hotel name is required")
    @Size(min = 2, max = 100, message = "Hotel name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Positive(message = "Price per night must be a positive number")
    private double pricePerNight;

    private boolean hasPool;
    private boolean hasGym;
    private boolean hasWiFi;
    private boolean hasRestaurant;
    private boolean hasParking;
    private boolean hasSpa;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid contact number format")
    private String contactNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 255, message = "Website URL cannot exceed 255 characters")
    private String website;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime checkInTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime checkOutTime;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;
}

package com.project.hotel.dto.request;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
public class CustomerRequest {

    @NotBlank(message = "Name is required and cannot be blank.")
    @Size(max = 50, message = "Name must not exceed 50 characters.")
    private String name;

    @NotBlank(message = "Surname is required and cannot be blank.")
    @Size(max = 50, message = "Surname must not exceed 50 characters.")
    private String surname;

    @NotBlank(message = "Phone number is required and cannot be blank.")
    @Pattern(regexp = "^(\\+994|0)(50|51|55|70|77|99)\\d{7}$", message = "Phone number must be valid. Example: +994000000000 or 0000000000")
    private String phone;

    @NotBlank(message = "Email is required and cannot be blank.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email must be valid.Example: firstname-lastname@example.com  ")
    private String email;

    @NotBlank(message = "Password is required and cannot be blank.")
    private String password;

    @NotNull(message = "Birthday is required.")
    @Past(message = "Birthday must be in the past.")
    private LocalDate birthday;

    @NotBlank(message = "Country is required and cannot be blank.")
    @Size(max = 50, message = "Country name must not exceed 50 characters.")
    private String country;

    @Size(max = 50, message = "State name must not exceed 50 characters.")
    private String state;

    @NotBlank(message = "City is required and cannot be blank.")
    @Size(max = 50, message = "City name must not exceed 50 characters.")
    private String city;
}

package com.project.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminRequest {

    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters.")
    private String name;

    @NotBlank(message = "Surname is required.")
    @Size(min = 2, max = 50, message = "Surname should be between 2 and 50 characters.")
    private String surname;

    @NotBlank(message = "Email is required and cannot be blank.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email must be valid.Example: firstname-lastname@example.com  ")
    private String email;

    @NotBlank(message = "Username cannot be empty or null")
    @Size(min = 3)
    @Pattern(regexp = "[A-Za-z0-9_.]+")
    private String password;
}

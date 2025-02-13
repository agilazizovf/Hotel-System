package com.project.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginRequest {

    @NotBlank(message = "Email is required and cannot be blank.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email must be valid.Example: firstname-lastname@example.com  ")
    private String email;

    @NotBlank(message = "Username cannot be empty or null")
    @Size(min = 3)
    @Pattern(regexp = "[A-Za-z0-9_.]+")
    private String password;
}

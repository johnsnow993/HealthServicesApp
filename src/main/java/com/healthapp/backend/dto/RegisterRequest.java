package com.healthapp.backend.dto;

import com.healthapp.backend.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Password must be at least 8 characters with 1 uppercase letter and 1 number"
    )
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Gender is required")
    private String gender;

    // Patient-specific fields
    private LocalDate dob;
    private String address;
    private String profilePhotoBase64;
    private String insuranceInfo;
    private Map<String, Object> medicalQuestionnaire;

    // Doctor-specific fields
    private String licenseNumber;
    private String specialization;
    private Integer experience;
    private String education;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    private List<String> languages;
    private String clinicAddress;
}
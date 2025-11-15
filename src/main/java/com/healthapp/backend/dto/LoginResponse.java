package com.healthapp.backend.dto;

import com.healthapp.backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private UUID userId;
    private String email;
    private Role role;
    private String message;

    public LoginResponse(String token, UUID userId, String email, Role role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.message = "Login successful";
    }
}
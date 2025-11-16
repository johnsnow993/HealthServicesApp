package com.healthapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper for consistent response format.
 * Used for success/error responses across all endpoints (except login which returns LoginResponse).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private Boolean success; // true for successful operations, false for errors
    private String message; // Human-readable message describing the result
    private Object data; // Optional data payload (rarely used, mostly just success + message)

    /**
     * Constructor for simple success/error responses without data payload.
     */
    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
package com.smartcart.userservice.dto;

import com.smartcart.userservice.entity.Role;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        Role role
) {
}

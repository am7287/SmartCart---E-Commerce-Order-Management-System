package com.smartcart.userservice.dto;

import com.smartcart.userservice.entity.Role;

public record UserResponse(Long id, String username, Role role) {
}

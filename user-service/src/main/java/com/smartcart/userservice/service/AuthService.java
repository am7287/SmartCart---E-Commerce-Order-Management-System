package com.smartcart.userservice.service;

import com.smartcart.userservice.dto.AuthResponse;
import com.smartcart.userservice.dto.LoginRequest;
import com.smartcart.userservice.dto.RegisterRequest;
import com.smartcart.userservice.dto.UserResponse;
import com.smartcart.userservice.entity.AppUser;
import com.smartcart.userservice.entity.Role;
import com.smartcart.userservice.exception.DuplicateUserException;
import com.smartcart.userservice.exception.InvalidCredentialsException;
import com.smartcart.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateUserException("Username already exists");
        }

        Role role = request.role() == null ? Role.USER : request.role();
        AppUser user = AppUser.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();
        AppUser saved = userRepository.save(user);
        log.info("Registered user {}", saved.getUsername());
        return toResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        log.info("User {} logged in", user.getUsername());
        return new AuthResponse("Login successful", toResponse(user));
    }

    private UserResponse toResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}

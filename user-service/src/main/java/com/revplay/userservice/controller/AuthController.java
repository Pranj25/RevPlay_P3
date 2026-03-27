package com.revplay.userservice.controller;

import com.revplay.userservice.dto.AuthRequest;
import com.revplay.userservice.dto.AuthResponse;
import com.revplay.userservice.dto.RegisterRequest;
import com.revplay.userservice.entity.User;
import com.revplay.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Create user entity
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(registerRequest.getPassword());
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            
            // Register user
            User registeredUser = authService.registerUser(user);
            
            // Generate token
            String token = authService.jwtService.generateToken(
                registeredUser.getUsername(), 
                registeredUser.getRole().name()
            );
            
            AuthResponse response = new AuthResponse(
                token, 
                registeredUser.getUsername(), 
                registeredUser.getEmail(), 
                registeredUser.getRole().name()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            String token = authService.loginUser(authRequest.getUsername(), authRequest.getPassword());
            
            // Get user details for response
            User user = authService.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            AuthResponse response = new AuthResponse(
                token, 
                user.getUsername(), 
                user.getEmail(), 
                user.getRole().name()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(e.getMessage()));
        }
    }
    
    @PostMapping("/validate")
    public ResponseEntity<AuthResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body(new AuthResponse("Invalid token format"));
            }
            
            String token = authHeader.substring(7);
            String username = authService.jwtService.extractUsername(token);
            
            User user = authService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (authService.jwtService.validateToken(token, username)) {
                AuthResponse response = new AuthResponse(
                    token, 
                    user.getUsername(), 
                    user.getEmail(), 
                    user.getRole().name()
                );
                response.setMessage("Token is valid");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(new AuthResponse("Invalid token"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Token validation failed"));
        }
    }
}

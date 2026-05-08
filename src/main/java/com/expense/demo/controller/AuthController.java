package com.expense.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense.demo.entity.User;
import com.expense.demo.repository.UserRepository;
import com.expense.demo.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User db = repo.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (encoder.matches(user.getPassword(), db.getPassword())) {
            String token = jwt.generateToken(user.getEmail());
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
}
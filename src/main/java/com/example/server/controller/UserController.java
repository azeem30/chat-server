package com.example.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.entity.User;
import com.example.server.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> findUserByUsername(@RequestParam String username) {
        try {
            Boolean ok = userService.findUserByUsername(username);
            if (ok) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "User found");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "User not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Server error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            Boolean ok = userService.registerUser(user);
            if (ok) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "User registered successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to register user");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Server error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            Boolean ok = userService.loginUser(user);
            if (ok) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Login successful");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to login user");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Server error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

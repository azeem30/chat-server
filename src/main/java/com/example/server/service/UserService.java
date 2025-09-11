package com.example.server.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.entity.User;
import com.example.server.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Boolean findUserByUsername(String username) {
        try {
            Optional<User> optionalUser = userRepository.findById(username);
            if (optionalUser.isEmpty()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("Error finding user by username: {}", username, e);
            return false;
        }
    }

    public Boolean registerUser(User user) {
        try {
            User savedUser = userRepository.save(user);
            return savedUser != null;
        } catch (Exception e) {
            logger.error("Error registering user: {}", user.getUsername(), e);
            return false;
        }
    }

    public Boolean loginUser(User user) {
        try {
            Optional<User> optionalUser = userRepository.findById(user.getUsername());
            if (optionalUser.isEmpty()) {
                return false;
            }
            User savedUser = optionalUser.get();
            return user.getPassword().equals(savedUser.getPassword()) && user.getIp().equals(savedUser.getIp());
        } catch (Exception e) {
            logger.error("Error logging in user: {}", user.getUsername(), e);
            return false;
        }
    }

    @Transactional
    public Boolean updateUsername(String oldUsername, String newUsername) {
        try {
            userRepository.updateUsername(oldUsername, newUsername);
            return true;
        } catch (Exception e) {
            logger.error("Error updating username from {} to {}", oldUsername, newUsername, e);
            return false;
        }
    }

    public Boolean updatePassword(String username, String oldPassword, String newPassword) {
        try {
            userRepository.updatePassword(username, oldPassword, newPassword);
            return true;
        } catch (Exception e) {
            logger.error("Error updating password for user: {}", username, e);
            return false;
        }
    }
}

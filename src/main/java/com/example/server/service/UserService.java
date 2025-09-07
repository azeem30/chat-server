package com.example.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.entity.User;
import com.example.server.repository.UserRepository;
import com.example.server.utils.Cryptography;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Cryptography cryptography;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Boolean findUserByUsername(String username) {
        try {
            username = cryptography.compress(cryptography.encrypt(username));
            Optional<User> optionalUser = userRepository.findById(username);
            if (optionalUser.isEmpty()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean registerUser(User user) {
        try {
            user.setUsername(cryptography.compress(cryptography.encrypt(user.getUsername())));
            user.setPassword(cryptography.compress(cryptography.encrypt(user.getPassword())));
            User savedUser = userRepository.save(user);
            return savedUser != null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean loginUser(User user) {
        try {
            user.setUsername(cryptography.compress(cryptography.encrypt(user.getUsername())));
            Optional<User> optionalUser = userRepository.findById(user.getUsername());
            if (optionalUser.isEmpty()) {
                return false;
            }
            User savedUser = optionalUser.get();
            String decryptedPassword = cryptography.decrypt(cryptography.decompress(savedUser.getPassword()));
            return user.getPassword().equals(decryptedPassword) && user.getIp().equals(savedUser.getIp());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Transactional
    public Boolean updateUsername(String oldUsername, String newUsername) {
        try {
            String encryptedOldUsername = cryptography.compress(cryptography.encrypt(oldUsername));
            String encryptedNewUsername = cryptography.compress(cryptography.encrypt(newUsername));
            userRepository.updateUsername(encryptedOldUsername, encryptedNewUsername);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean updatePassword(String username, String oldPassword, String newPassword) {
        try {
            String encryptedUsername = cryptography.compress(cryptography.encrypt(username));
            String encryptedOldPassword = cryptography.compress(cryptography.encrypt(oldPassword));
            String encryptedNewPassword = cryptography.compress(cryptography.encrypt(newPassword));
            userRepository.updatePassword(encryptedUsername, encryptedOldPassword, encryptedNewPassword);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

package com.example.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.entity.User;
import com.example.server.repository.UserRepository;
import com.example.server.utils.Cryptography;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Boolean findUserByUsername(String username) {
        try {
            username = Cryptography.compress(Cryptography.encrypt(username));
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
            user.setUsername(Cryptography.compress(Cryptography.encrypt(user.getUsername())));
            user.setPassword(Cryptography.compress(Cryptography.encrypt(user.getPassword())));
            User savedUser = userRepository.save(user);
            return savedUser != null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean loginUser(User user) {
        try {
            user.setUsername(Cryptography.compress(Cryptography.encrypt(user.getUsername())));
            Optional<User> optionalUser = userRepository.findById(user.getUsername());
            if (optionalUser.isEmpty()) {
                return false;
            }
            User savedUser = optionalUser.get();
            String decryptedPassword = Cryptography.decrypt(Cryptography.decompress(savedUser.getPassword()));
            return user.getPassword().equals(decryptedPassword) && user.getIp().equals(savedUser.getIp());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

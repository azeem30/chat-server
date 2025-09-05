package com.example.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.entity.Message;
import com.example.server.service.MessageService;

@RestController
@RequestMapping("/api/chats")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<?> getChatsByUser(@RequestParam String username) {
        try {
            List<Message> chats = messageService.getChatsByUser(username);
            if (!chats.isEmpty() && chats != null) {
                Map<String, List<Message>> response = new HashMap<>();
                response.put("chats", chats);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to fetch chats by user");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Server error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessagesBetweenTwoUsers(@RequestParam String senderUsername,
            @RequestParam String receiverUsername) {
        try {
            List<Message> messages = messageService.getMessagesBetweenTwoUsers(senderUsername, receiverUsername);
            if (!messages.isEmpty() && messages != null) {
                Map<String, List<Message>> response = new HashMap<>();
                response.put("messages", messages);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to fetch chats by user");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Server error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

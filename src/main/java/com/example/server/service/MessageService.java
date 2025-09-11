package com.example.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.entity.Message;
import com.example.server.repository.MessageRepository;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getChatsByUser(String username) {
        Optional<List<Message>> optionalChats = messageRepository.findChatsByUser(username);
        if (optionalChats.isEmpty()) {
            return new ArrayList<>();
        }
        return optionalChats.get();
    }

    public List<Message> getMessagesBetweenTwoUsers(String senderUsername, String receiverUsername) {
        Optional<List<Message>> optionalMessages = messageRepository.findMessagesBetweenTwoUsers(
                senderUsername,
                receiverUsername);
        if (optionalMessages.isEmpty()) {
            return new ArrayList<>();
        }
        return optionalMessages.get();
    }
}

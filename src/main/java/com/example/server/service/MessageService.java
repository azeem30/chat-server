package com.example.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.entity.Message;
import com.example.server.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getChatsBySender(String username) {
        Optional<List<Message>> optionalChats = messageRepository.findChatsBySender(username);
        if (optionalChats.isEmpty()) {
            return new ArrayList<Message>();
        }
        return optionalChats.get();
    }
}

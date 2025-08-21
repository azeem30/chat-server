package com.example.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.entity.Message;
import com.example.server.repository.MessageRepository;
import com.example.server.utils.Cryptography;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getChatsByUser(String username) {
        Optional<List<Message>> optionalChats = messageRepository.findChatsByUser(username);
        if (optionalChats.isEmpty()) {
            return new ArrayList<Message>();
        }
        List<Message> chats = optionalChats.get();
        for (Message message : chats) {
            try {
                String decryptedText = Cryptography.decrypt(message.getText());
                message.setText(decryptedText);
            } catch (Exception ex) {
                message.setText("[Encrypted message - decryption failed]");
            }
        }

        return chats;
    }
}

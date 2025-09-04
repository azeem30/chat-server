package com.example.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.entity.Message;
import com.example.server.entity.User;
import com.example.server.repository.MessageRepository;
import com.example.server.utils.Cryptography;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getChatsByUser(String username) {
        String encryptedUsername = Cryptography.compress(Cryptography.encrypt(username));
        Optional<List<Message>> optionalChats = messageRepository.findChatsByUser(encryptedUsername);
        if (optionalChats.isEmpty()) {
            return new ArrayList<Message>();
        }
        List<Message> chats = optionalChats.get();
        for (Message message : chats) {
            try {
                String decryptedText = Cryptography.decrypt(Cryptography.decompress(message.getText()));
                message.setText(decryptedText);
            } catch (Exception e) {
                message.setText("[Encrypted message - decryption failed]");
            }

            try {
                User sender = message.getSender();
                String decryptedSenderUsername = Cryptography.decrypt(Cryptography.decompress(sender.getUsername()));
                sender.setUsername(decryptedSenderUsername);
            } catch (Exception e) {
                message.getSender().setUsername("[Sender decryption failed]");
            }

            try {
                User receiver = message.getReceiver();
                String decryptedReceiverUsername = Cryptography
                        .decrypt(Cryptography.decompress(receiver.getUsername()));
                receiver.setUsername(decryptedReceiverUsername);
            } catch (Exception e) {
                message.getReceiver().setUsername("[Receiver decryption failed]");
            }
        }

        return chats;
    }
}

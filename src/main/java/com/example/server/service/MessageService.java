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
            return new ArrayList<>();
        }

        List<Message> chats = optionalChats.get();
        List<Message> decryptedChats = new ArrayList<>();

        for (Message message : chats) {
            // Create a new Message object to avoid modifying the original entity
            Message decryptedMessage = new Message();
            decryptedMessage.setId(message.getId());
            decryptedMessage.setStatus(message.getStatus());
            decryptedMessage.setTime(message.getTime());

            try {
                // Decrypt the message text
                String decryptedText = Cryptography.decrypt(Cryptography.decompress(message.getText()));
                decryptedMessage.setText(decryptedText);
            } catch (Exception e) {
                decryptedMessage.setText("[Encrypted message - decryption failed]");
            }

            try {
                // Create a new User object for sender and set decrypted username
                User decryptedSender = new User();
                String decryptedSenderUsername = Cryptography.decrypt(
                        Cryptography.decompress(message.getSender().getUsername()));
                decryptedSender.setUsername(decryptedSenderUsername);
                decryptedMessage.setSender(decryptedSender);
            } catch (Exception e) {
                User errorSender = new User();
                errorSender.setUsername("[Sender decryption failed]");
                decryptedMessage.setSender(errorSender);
            }

            try {
                // Create a new User object for receiver and set decrypted username
                User decryptedReceiver = new User();
                String decryptedReceiverUsername = Cryptography.decrypt(
                        Cryptography.decompress(message.getReceiver().getUsername()));
                decryptedReceiver.setUsername(decryptedReceiverUsername);
                decryptedMessage.setReceiver(decryptedReceiver);
            } catch (Exception e) {
                User errorReceiver = new User();
                errorReceiver.setUsername("[Receiver decryption failed]");
                decryptedMessage.setReceiver(errorReceiver);
            }

            decryptedChats.add(decryptedMessage);
        }

        return decryptedChats;
    }
}

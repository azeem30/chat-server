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

    @Autowired
    private Cryptography cryptography;

    public List<Message> getChatsByUser(String username) {
        String encryptedUsername = cryptography.compress(cryptography.encrypt(username));
        Optional<List<Message>> optionalChats = messageRepository.findChatsByUser(encryptedUsername);
        if (optionalChats.isEmpty()) {
            return new ArrayList<>();
        }

        List<Message> chats = optionalChats.get();
        List<Message> decryptedChats = new ArrayList<>();

        for (Message message : chats) {
            Message decryptedMessage = new Message();
            decryptedMessage.setId(message.getId());
            decryptedMessage.setStatus(message.getStatus());
            decryptedMessage.setTime(message.getTime());

            try {
                String decryptedText = cryptography.decrypt(cryptography.decompress(message.getText()));
                decryptedMessage.setText(decryptedText);
            } catch (Exception e) {
                decryptedMessage.setText("[Encrypted message - decryption failed]");
            }

            try {
                User decryptedSender = new User();
                String decryptedSenderUsername = cryptography.decrypt(
                        cryptography.decompress(message.getSender().getUsername()));
                decryptedSender.setUsername(decryptedSenderUsername);
                decryptedMessage.setSender(decryptedSender);
            } catch (Exception e) {
                User errorSender = new User();
                errorSender.setUsername("[Sender decryption failed]");
                decryptedMessage.setSender(errorSender);
            }

            try {
                User decryptedReceiver = new User();
                String decryptedReceiverUsername = cryptography.decrypt(
                        cryptography.decompress(message.getReceiver().getUsername()));
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

    public List<Message> getMessagesBetweenTwoUsers(String senderUsername, String receiverUsername) {
        String encryptedSenderUsername = cryptography.compress(cryptography.encrypt(senderUsername));
        String encryptedReceiverUsername = cryptography.compress(cryptography.encrypt(receiverUsername));
        Optional<List<Message>> optionalMessages = messageRepository.findMessagesBetweenTwoUsers(
                encryptedSenderUsername,
                encryptedReceiverUsername);
        if (optionalMessages.isEmpty()) {
            return new ArrayList<>();
        }

        List<Message> messages = optionalMessages.get();
        List<Message> decryptedMessages = new ArrayList<>();

        for (Message message : messages) {
            Message decryptedMessage = new Message();
            decryptedMessage.setId(message.getId());
            decryptedMessage.setStatus(message.getStatus());
            decryptedMessage.setTime(message.getTime());

            try {
                String decryptedText = cryptography.decrypt(cryptography.decompress(message.getText()));
                decryptedMessage.setText(decryptedText);
            } catch (Exception e) {
                decryptedMessage.setText("[Encrypted message - decryption failed]");
            }

            try {
                User decryptedSender = new User();
                String decryptedSenderUsername = cryptography.decrypt(
                        cryptography.decompress(message.getSender().getUsername()));
                decryptedSender.setUsername(decryptedSenderUsername);
                decryptedMessage.setSender(decryptedSender);
            } catch (Exception e) {
                User errorSender = new User();
                errorSender.setUsername("[Sender decryption failed]");
                decryptedMessage.setSender(errorSender);
            }

            try {
                User decryptedReceiver = new User();
                String decryptedReceiverUsername = cryptography.decrypt(
                        cryptography.decompress(message.getReceiver().getUsername()));
                decryptedReceiver.setUsername(decryptedReceiverUsername);
                decryptedMessage.setReceiver(decryptedReceiver);
            } catch (Exception e) {
                User errorReceiver = new User();
                errorReceiver.setUsername("[Receiver decryption failed]");
                decryptedMessage.setReceiver(errorReceiver);
            }

            decryptedMessages.add(decryptedMessage);
        }
        return decryptedMessages;
    }
}

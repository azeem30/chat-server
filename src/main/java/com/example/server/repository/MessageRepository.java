package com.example.server.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.server.entity.Message;

import jakarta.transaction.Transactional;

public interface MessageRepository extends JpaRepository<Message, BigInteger> {

    @Query("SELECT m FROM messages m WHERE m.id IN (" +
            "SELECT MAX(m2.id) FROM messages m2 " +
            "WHERE m2.sender.username = :username OR m2.receiver.username = :username " +
            "GROUP BY CASE " +
            "   WHEN m2.sender.username = :username THEN m2.receiver.username " +
            "   ELSE m2.sender.username " +
            "END)")
    Optional<List<Message>> findChatsByUser(@Param("username") String username);

    @Query("SELECT m FROM messages m "
            + "WHERE (m.sender.username = :senderUsername AND m.receiver.username = :receiverUsername) "
            + "OR (m.sender.username = :receiverUsername AND m.receiver.username = :senderUsername) "
            + "ORDER BY m.time")
    Optional<List<Message>> findMessagesBetweenTwoUsers(@Param("senderUsername") String senderUsername,
            @Param("receiverUsername") String receiverUsername);
}

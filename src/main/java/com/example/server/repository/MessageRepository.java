package com.example.server.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.server.entity.Message;

public interface MessageRepository extends JpaRepository<Message, BigInteger> {
    @Query("SELECT message from messages message WHERE message.sender.username = :username OR message.receiver.username = :username")
    Optional<List<Message>> findChatsBySender(@Param("username") String username);
}

package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.server.entity.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, String> {

    @Modifying
    @Transactional
    @Query("UPDATE users u SET u.username = :newUsername WHERE u.username = :oldUsername")
    void updateUsername(@Param("oldUsername") String oldUsername, @Param("newUsername") String newUsername);

    @Modifying
    @Transactional
    @Query("UPDATE users u SET u.password = :newPassword WHERE u.username = :username AND u.password = :oldPassword")
    void updatePassword(@Param("username") String username, @Param("oldPassword") String oldPassword,
            @Param("newPassword") String newPassword);
}

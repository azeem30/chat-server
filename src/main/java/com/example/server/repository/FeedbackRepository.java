package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}

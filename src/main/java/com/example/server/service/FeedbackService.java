package com.example.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.entity.Feedback;
import com.example.server.repository.FeedbackRepository;

@Service
public class FeedbackService {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Boolean saveFeedback(Feedback feedback) {
        try {
            Feedback savedFeedback = feedbackRepository.save(feedback);
            return savedFeedback != null;
        } catch (Exception e) {
            logger.error("Error saving feedback: ", e);
            return false;
        }
    }
}

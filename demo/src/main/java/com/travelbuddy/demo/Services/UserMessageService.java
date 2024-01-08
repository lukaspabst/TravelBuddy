package com.travelbuddy.demo.Services;

import com.travelbuddy.demo.Entities.UserMessage;
import com.travelbuddy.demo.Repository.UserMessageRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserMessageService {
    @Autowired
    UserMessageRepo userMessageRepo;

    public Optional<List<UserMessage>> getAllMessagesByUsername(String username) {
        return userMessageRepo.findMessagesByUsername(username);
    }

    public UserMessage getMessageById(String id) {
        return userMessageRepo.findById(id).orElse(null);
    }

    public UserMessage saveOrUpdateMessage(UserMessage userMessage) {
        return userMessageRepo.save(userMessage);
    }

    public void deleteMessage(String id) {
        userMessageRepo.deleteById(id);
    }
}
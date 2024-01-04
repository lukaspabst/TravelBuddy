package com.travelbuddy.demo.Repository;

import com.travelbuddy.demo.Entities.UserMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserMessageRepo extends MongoRepository<UserMessage, String> {

    Optional<UserMessage> findByUsername(String username);
    Optional<List<UserMessage>> findMessagesByUsername(String username);
    Optional<UserMessage> findById(String id);
    void deleteByUsername(String username);

}

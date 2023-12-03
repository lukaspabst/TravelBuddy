package com.travelbuddy.demo.Repository;

import com.travelbuddy.demo.Entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepo extends MongoRepository<User, String> {

    User findByUsername(String username);

    void deleteByUsername(String username);
}

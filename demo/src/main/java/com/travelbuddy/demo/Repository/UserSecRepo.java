package com.travelbuddy.demo.Repository;

import com.travelbuddy.demo.Entities.User;
import com.travelbuddy.demo.Entities.UserSecruity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserSecRepo extends MongoRepository<UserSecruity,String> {

    Optional<UserSecruity> findByUsername(String username);

    void deleteByUsername(String username);
}

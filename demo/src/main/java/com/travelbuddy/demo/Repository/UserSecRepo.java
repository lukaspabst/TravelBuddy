package com.travelbuddy.demo.Repository;

import com.travelbuddy.demo.Entities.UserSecurity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserSecRepo extends MongoRepository<UserSecurity, String> {

    Optional<UserSecurity> findByUsername(String username);

    void deleteByUsername(String username);
}

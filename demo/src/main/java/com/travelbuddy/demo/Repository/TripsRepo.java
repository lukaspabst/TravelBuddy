package com.travelbuddy.demo.Repository;

import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Entities.UserSecurity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TripsRepo extends MongoRepository<Trips,String> {
    void deleteById(String id);

    List<Trips> findByMembersUsername(String username);

}

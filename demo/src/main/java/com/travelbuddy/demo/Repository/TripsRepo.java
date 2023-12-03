package com.travelbuddy.demo.Repository;

import com.travelbuddy.demo.Entities.Trips;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TripsRepo extends MongoRepository<Trips, String> {
    void deleteById(String id);

    List<Trips> findByMembersUsername(String username);

}

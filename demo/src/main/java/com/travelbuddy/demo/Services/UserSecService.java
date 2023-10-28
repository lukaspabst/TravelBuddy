package com.travelbuddy.demo.Services;


import com.travelbuddy.demo.Entities.UserSecruity;
import com.travelbuddy.demo.Repository.UserSecRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSecService{
    @Autowired
    private UserSecRepo userSecRepo;

    public Optional<UserSecruity> findByUsername(String username) {
        return userSecRepo.findByUsername(username);
    }

    public UserSecruity saveUser(UserSecruity userSec) {
        return userSecRepo.save(userSec);
    }

    public void deleteUserByUsername(String username) {
        userSecRepo.deleteByUsername(username);
    }
}


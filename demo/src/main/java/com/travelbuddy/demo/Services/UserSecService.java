package com.travelbuddy.demo.Services;


import com.travelbuddy.demo.Entities.UserSecurity;
import com.travelbuddy.demo.Repository.UserSecRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSecService{
    @Autowired
    private UserSecRepo userSecRepo;

    public UserSecService(UserSecRepo userSecRepo) {
        this.userSecRepo = userSecRepo;
    }


    public Optional<UserSecurity> findByUsername(String username) {
        return userSecRepo.findByUsername(username);
    }

    public UserSecurity saveUser(UserSecurity userSec) {
        return userSecRepo.save(userSec);
    }

    public void deleteUserByUsername(String username) {
        userSecRepo.deleteByUsername(username);
    }
}


package com.travelbuddy.demo.Services;

import com.travelbuddy.demo.Entities.User;
import com.travelbuddy.demo.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public void deleteUserByUsername(String username) {
        userRepo.deleteByUsername(username);
    }

}

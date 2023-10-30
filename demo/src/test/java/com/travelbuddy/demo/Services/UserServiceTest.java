package com.travelbuddy.demo.Services;

import com.travelbuddy.demo.Entities.User;
import com.travelbuddy.demo.Repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepo userRepo;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepo);
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        when(userRepo.findByUsername("1")).thenReturn(user);
        User result = userService.findByUsername("1");

        verify(userRepo).findByUsername("1");
        assertEquals(user, result);
    }

    @Test
    void saveUser() {
        User user = new User();
        userService.saveUser(user);
        verify(userRepo, times(1)).save(user);

    }

    @Test
    void deleteUserByUsername() {
        User user = new User();
        user.setUsername("1");
        userService.deleteUserByUsername("1");
        verify(userRepo, times(1)).deleteByUsername("1");
    }
}

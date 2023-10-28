package com.travelbuddy.demo.Secruity;

import com.travelbuddy.demo.Repository.UserSecRepo;
import com.travelbuddy.demo.Secruity.ShitIdkWohindamit.SecurityUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserSecRepo userSecRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userSecRepo
                .findByUsername(username)
                .map(SecurityUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username or password is wrong"));
    }
}

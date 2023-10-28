package com.travelbuddy.demo.Secruity.ShitIdkWohindamit;

import com.travelbuddy.demo.Entities.UserSecruity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class SecurityUserDetails implements UserDetails {

    private UserSecruity userSecruity;

   @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

     //  List<GrantedAuthority> grantedAuthorities = userSec.getRoleList().stream()
      //         .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
       //        .collect(Collectors.toList());

       // List<GrantedAuthority> grantedAuthorities1 = new ArrayList<>();
       // grantedAuthorities1.add(new SimpleGrantedAuthority("ROLE_" + userSecruity.getRole()));

        return null;
    }

    @Override
    public String getPassword() {
        return userSecruity.getPassword();
    }

    @Override
    public String getUsername() {
        return userSecruity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userSecruity.isNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

   @Override
   public boolean isEnabled() {
       return false;
   }
}

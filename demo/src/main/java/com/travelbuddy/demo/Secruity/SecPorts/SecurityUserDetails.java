package com.travelbuddy.demo.Secruity.SecPorts;

import com.travelbuddy.demo.Entities.UserSecurity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@AllArgsConstructor
public class SecurityUserDetails implements UserDetails {

    private UserSecurity userSecurity;

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
        return userSecurity.getPassword();
    }

    @Override
    public String getUsername() {
        return userSecurity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userSecurity.isNotLocked();
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

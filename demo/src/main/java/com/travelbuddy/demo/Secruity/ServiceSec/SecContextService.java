package com.travelbuddy.demo.Secruity.ServiceSec;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecContextService {

    public String secUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

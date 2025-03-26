package com.spring.fastfood.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken (UserDetails user);
    String refreshToken (UserDetails userDetails);
    String extractUsername (String token);
    boolean isValidToken (String token,UserDetails userDetails);

}

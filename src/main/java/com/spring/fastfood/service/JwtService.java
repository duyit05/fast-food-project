package com.spring.fastfood.service;

import com.spring.fastfood.enums.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken (UserDetails user);
    String refreshToken (UserDetails userDetails);
    String extractUsername (String token, TokenType type);
    boolean isValidToken (String token,TokenType type,UserDetails userDetails);

}

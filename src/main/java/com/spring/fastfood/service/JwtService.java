package com.spring.fastfood.service;

import com.spring.fastfood.enums.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtService {
    String generateToken (UserDetails user, List<String> authorities);
    String refreshToken (UserDetails userDetails,List<String> authorities);
    String extractUsername (String token, TokenType type);
    boolean isValidToken (String token,TokenType type,UserDetails userDetails);

}

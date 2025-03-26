package com.spring.fastfood.service.impl;

import com.spring.fastfood.enums.TokenType;
import com.spring.fastfood.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.spring.fastfood.enums.TokenType.ACCESS_TOKEN;
import static com.spring.fastfood.enums.TokenType.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiry-time}")
    private long JWT_EXPIRY_TIME;

    @Value("${jwt.secret-key}")
    private String JWT_SECRET_KEY;

    @Value("${jwt.secret-refresh-key}")
    private String JWT_REFRESH_KEY;

    @Value("${jwt.expiry-day}")
    private long JWT_EXPIRY_DAY;

    @Override
    public String generateToken(UserDetails user) {
        return generateToken(new HashMap<>(), user);
    }

    @Override
    public String refreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * JWT_EXPIRY_TIME))
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * JWT_EXPIRY_DAY))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        byte[] keyBytes;
        if (ACCESS_TOKEN.equals(type)) {
            keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        } else {
            keyBytes = Decoders.BASE64.decode(JWT_REFRESH_KEY);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token,TokenType type) {
        return extractClaim(token,type,Claims::getSubject);
    }

    @Override
    public boolean isValidToken(String token, TokenType type, UserDetails userDetails) {
        final String username = extractUsername(token,type);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token,type);
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaim(token,type);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaim(String token , TokenType type) {
        return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token,TokenType type) {
        return extractExpiration(token,type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token,type,Claims::getExpiration);
    }
}

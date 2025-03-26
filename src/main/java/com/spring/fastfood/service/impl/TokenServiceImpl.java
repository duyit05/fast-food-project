package com.spring.fastfood.service.impl;

import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.model.Token;
import com.spring.fastfood.model.User;
import com.spring.fastfood.repository.TokenRepository;
import com.spring.fastfood.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    @Override
    public String deleteToken(Token token) {
        tokenRepository.delete(token);
        return "Deleted";
    }

    @Override
    public int saveToken(Token token) {
        Optional<Token> username = tokenRepository.findByUsername(token.getUsername());
        if(username.isEmpty()){
            tokenRepository.save(token);
            return token.getId();
        }else {
            Token currentToken = username.get();
            currentToken.setAccessToken(token.getAccessToken());
            currentToken.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(currentToken);
            return currentToken.getId();
        }
    }
    @Override
    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("Token not found"));
    }
}

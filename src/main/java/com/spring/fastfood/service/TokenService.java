package com.spring.fastfood.service;

import com.spring.fastfood.model.Token;

public interface TokenService {
    public int saveToken (Token token);
    public String deleteToken (Token token);
    public Token getByUsername (String username);
}

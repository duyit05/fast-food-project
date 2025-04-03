package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.ResetPasswordRequest;
import com.spring.fastfood.dto.request.SigInRequest;
import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.response.ActiveResponse;
import com.spring.fastfood.dto.response.TokenResponse;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.model.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    TokenResponse authenticated (SigInRequest request);
    UserResponse signUp (UserRequest request);
    TokenResponse refreshToken (HttpServletRequest request) throws InterruptedException;
    String logout (HttpServletRequest request) throws InterruptedException;

    ActiveResponse sendMailActive(String email, String activeCode);

    ActiveResponse forgotPassword (String email);
    ActiveResponse resetPassword (String email, ResetPasswordRequest request);

    User findByEmail (String email);
    User findByUsername (String username);
}

package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.ResetPasswordRequest;
import com.spring.fastfood.dto.request.SigInRequest;
import com.spring.fastfood.dto.response.ActiveResponse;
import com.spring.fastfood.dto.response.TokenResponse;
import com.spring.fastfood.enums.TokenType;
import com.spring.fastfood.enums.UserStatus;
import com.spring.fastfood.model.Token;
import com.spring.fastfood.model.User;
import com.spring.fastfood.repository.UserRepository;
import com.spring.fastfood.service.AuthenticationService;
import com.spring.fastfood.service.EmailService;
import com.spring.fastfood.service.JwtService;
import com.spring.fastfood.service.TokenService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public TokenResponse authenticated(SigInRequest request) {
        List<String> authorities = new ArrayList<>();
        try {
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("user name or password incorrect"));

            if (user.getStatus().equals(UserStatus.INACTIVE)) {
                throw new BadCredentialsException("user has been not active");
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (request.getUsername(), request.getPassword()));
            log.info("isAuthenticated : {}", authentication.isAuthenticated());
            log.info("Authorities : {}", authentication.getAuthorities().toString());
            authentication.getAuthorities().forEach(auth -> authorities.add(auth.getAuthority()));
            String accessToken = jwtService.generateToken(user, authorities);
            String refreshToken = jwtService.refreshToken(user, authorities);
            // lưu token vào db
            tokenService.saveToken(Token.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .username(user.getUsername())
                    .build());
            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .build();
        } catch (BadCredentialsException | DisabledException e) {
            log.error("errorMessage: {}", e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public TokenResponse refreshToken(HttpServletRequest request) throws InterruptedException {
        // validate token
        String refreshToken = request.getHeader("x-token");
        if (StringUtils.isBlank(refreshToken)) {
            throw new InterruptedException("Token must be not blank");
        }
        // extract user from token
        final String username = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        // check it into db
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user name invalid"));
        // validate token valid
        if (!jwtService.isValidToken(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            throw new InterruptedException("Token invalid");
        }
        List<String> authorities = new ArrayList<>();
        user.getAuthorities().forEach(authority -> authorities.add(authority.getAuthority()));
        String accessToken = jwtService.generateToken(user, authorities);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public String logout(HttpServletRequest request) throws InterruptedException {
        // validate
        String token = request.getHeader("x-token");
        if (StringUtils.isBlank(token)) {
            throw new InterruptedException("Token must be not blank");
        }
        // extract token from username
        final String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
        //check token in db
        Token currentToken = tokenService.getByUsername(username);
        // delete token
        tokenService.deleteToken(currentToken);
        return "logout success";
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("email not exist"));
    }

    @Override
    public ActiveResponse sendMailActive(String email, String activeCode) {
        User user = findByEmail(email);
        if (user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new BadCredentialsException("user has been active");
        }

        if (activeCode.equals(user.getActiveCode())) {
            user.setStatus(UserStatus.ACTIVE);
        }
        userRepository.save(user);
        return ActiveResponse.builder()
                .message("active user successfully")
                .email(user.getEmail())
                .build();
    }

    @Override
    public ActiveResponse forgotPassword(String email) {
        User user = findByEmail(email);
        emailService.sendMailForgotPassword(email);
        return ActiveResponse.builder()
                .message("send mail forgot password successfully")
                .email(user.getEmail())
                .build();
    }

    @Override
    public ActiveResponse resetPassword(String email, ResetPasswordRequest request) {
        User user = findByEmail(email);
        if (!request.getNewPassword().equals(request.getNewPasswordRepeat()))
            throw new IllegalArgumentException("password must be match");
        user.setPassword(passwordEncoder.encode(request.getNewPasswordRepeat()));
        userRepository.save(user);
        return ActiveResponse.builder()
                .message("reset password successfully")
                .email(user.getEmail())
                .build();
    }
}

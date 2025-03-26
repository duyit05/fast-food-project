package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.SigInRequest;
import com.spring.fastfood.dto.response.TokenResponse;
import com.spring.fastfood.enums.TokenType;
import com.spring.fastfood.model.Token;
import com.spring.fastfood.model.User;
import com.spring.fastfood.repository.UserRepository;
import com.spring.fastfood.service.AuthenticationService;
import com.spring.fastfood.service.JwtService;
import com.spring.fastfood.service.TokenService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;

    @Override
    public TokenResponse authenticated(SigInRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("user name or password invalid"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (request.getUsername(), request.getPassword()));
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.refreshToken(user);

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
        String accessToken = jwtService.generateToken(user);
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
        final String username = jwtService.extractUsername(token,TokenType.ACCESS_TOKEN);
        //check token in db
        Token currentToken = tokenService.getByUsername(username);
        // delete token
        tokenService.deleteToken(currentToken);
        return "logout success";
    }
}

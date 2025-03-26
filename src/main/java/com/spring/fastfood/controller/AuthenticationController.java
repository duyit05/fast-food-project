package com.spring.fastfood.controller;

import com.spring.fastfood.dto.response.TokenResponse;
import com.spring.fastfood.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login (@RequestBody){
        return "success";
    }

    @PostMapping("/logout")
    public String logout () {
        return "success";
    }

    @PostMapping("/refresh")
    public String refresh () {
        return "success";
    }
}

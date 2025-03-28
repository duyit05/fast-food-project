package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.SigInRequest;
import com.spring.fastfood.dto.response.ResponseActive;
import com.spring.fastfood.dto.response.ResponseData;
import com.spring.fastfood.dto.response.ResponseError;
import com.spring.fastfood.dto.response.TokenResponse;
import com.spring.fastfood.exception.ErrorResponse;
import com.spring.fastfood.service.AuthenticationService;
import com.spring.fastfood.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody SigInRequest request) {
            return new ResponseEntity<>(authenticationService.authenticated(request), HttpStatus.OK);


    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken (HttpServletRequest request) throws InterruptedException {
        return new ResponseEntity<>(authenticationService.refreshToken(request), HttpStatus.OK);

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws InterruptedException {
        return new ResponseEntity<>(authenticationService.logout(request), HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseData<ResponseActive> sendMailActive(@RequestParam String email , @RequestParam String activeCode){
            return new ResponseData<>(HttpStatus.OK.value(),"active",authenticationService.sendMailActive(email,activeCode));
    }
}

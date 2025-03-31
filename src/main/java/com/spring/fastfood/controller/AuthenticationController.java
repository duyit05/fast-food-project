package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.ResetPasswordRequest;
import com.spring.fastfood.dto.request.SigInRequest;
import com.spring.fastfood.dto.response.ActiveResponse;
import com.spring.fastfood.dto.response.DateResponse;
import com.spring.fastfood.dto.response.TokenResponse;
import com.spring.fastfood.service.AuthenticationService;
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
    public DateResponse<ActiveResponse> sendMailActive(@RequestParam String email , @RequestParam String activeCode){
            return new DateResponse<>(HttpStatus.OK.value(),"active",authenticationService.sendMailActive(email,activeCode));
    }

    @GetMapping("/forgot-password")
    public DateResponse<ActiveResponse> forgotPassword(@RequestParam String email){
        return new DateResponse<>(HttpStatus.OK.value(),"forgot password",authenticationService.forgotPassword(email));
    }

    @PostMapping("/reset-password")
    public DateResponse<ActiveResponse> resetPassword (@RequestParam String email, @RequestBody ResetPasswordRequest request){
        return new DateResponse<>(HttpStatus.OK.value(),"reset password",authenticationService.resetPassword(email,request));
    }

}

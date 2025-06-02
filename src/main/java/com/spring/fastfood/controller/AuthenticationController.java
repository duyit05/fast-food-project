package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.ResetPasswordRequest;
import com.spring.fastfood.dto.request.SigInRequest;
import com.spring.fastfood.dto.request.UserRequest;
import com.spring.fastfood.dto.response.ActiveResponse;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.TokenResponse;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.service.AuthenticationService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.minio.errors.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@RequestMapping("/auth")
@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DataResponse<UserResponse> signUp(@Valid  UserRequest request) throws ServerException,
            InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return new DataResponse<>(HttpStatus.CREATED.value(), "sign up", authenticationService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody SigInRequest request) {
        return new ResponseEntity<>(authenticationService.authenticated(request), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) throws InterruptedException {
        return new ResponseEntity<>(authenticationService.refreshToken(request), HttpStatus.OK);

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws InterruptedException {
        return new ResponseEntity<>(authenticationService.logout(request), HttpStatus.OK);
    }

    @GetMapping("/active")
    public DataResponse<ActiveResponse> sendMailActive(@RequestParam String email, @RequestParam String activeCode) {
        return new DataResponse<>(HttpStatus.OK.value(), "active", authenticationService.sendMailActive(email, activeCode));
    }

    @GetMapping("/forgot-password")
    public DataResponse<ActiveResponse> forgotPassword(@RequestParam String email) {
        return new DataResponse<>(HttpStatus.OK.value(), "forgot password", authenticationService.forgotPassword(email));
    }

    @PostMapping("/reset-password")
    public DataResponse<ActiveResponse> resetPassword(@RequestParam String email, @RequestBody ResetPasswordRequest request) {
        return new DataResponse<>(HttpStatus.OK.value(), "reset password", authenticationService.resetPassword(email, request));
    }

}

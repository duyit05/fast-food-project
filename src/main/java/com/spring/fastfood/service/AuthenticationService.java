package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.SigInRequest;
import com.spring.fastfood.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse authenticated (SigInRequest request);
}

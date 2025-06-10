package com.spring.fastfood.dto.response;

import com.spring.fastfood.model.UserHasRole;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private List<String> roles;
}

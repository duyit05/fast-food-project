package com.spring.fastfood.dto.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    private String newPassword;
    private String newPasswordRepeat;
}

package com.spring.fastfood.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ResetPasswordRequest {

    @NotBlank(message = "new password can not be blank")
    @Size(min = 8, message = "new password at least 8 characters")
    private String newPassword;
    @NotBlank(message = "new password repeat can not be blank")
    @Size(min = 8, message = "new password repeat at least 8 characters")
    private String newPasswordRepeat;
}

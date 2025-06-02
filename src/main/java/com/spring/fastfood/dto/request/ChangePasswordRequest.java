package com.spring.fastfood.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "old password not be blank")
    private String oldPassword;
    @NotBlank(message = "new password not be blank")
    private String newPassword;
    @NotBlank(message = "new password repeat not be blank")
    private String newPasswordRepeat;
}

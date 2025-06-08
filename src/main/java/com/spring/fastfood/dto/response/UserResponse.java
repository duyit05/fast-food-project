package com.spring.fastfood.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.fastfood.enums.GenderType;
import com.spring.fastfood.enums.UserStatus;
import com.spring.fastfood.validation.GenderSubset;
import com.spring.fastfood.validation.UserStatusSubset;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private GenderType gender;
    private UserStatus status;
    private Date dateOrBirth;
    private String activeCode;
    private String avatar;
    private List<VoucherResponse> vouchers;
}

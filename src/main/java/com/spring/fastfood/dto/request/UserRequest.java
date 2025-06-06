package com.spring.fastfood.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.fastfood.enums.GenderType;
import com.spring.fastfood.enums.UserStatus;
import com.spring.fastfood.validation.GenderSubset;
import com.spring.fastfood.validation.UserStatusSubset;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "username must not be blank")
    @Size(min = 5 , message = "username must has 5 characters")
    private String username;

    @NotBlank(message = "password must not be blank")
    @Size(min = 8,message = "password at least 8 characters")
    private String password;

    @NotNull(message = "first name must not be null")
    private String firstName;

    @NotNull(message = "full name must not be null")
    private String lastName;

    @Email(message = "email invalid")
    private String email;

    @Pattern(regexp = "^\\d{10}$" , message = "phone invalid format")
    private String phoneNumber;

    @GenderSubset(anyOf = {GenderType.MALE, GenderType.FEMALE, GenderType.OTHER})
    private GenderType gender;

    @UserStatusSubset(anyOf = {UserStatus.ACTIVE, UserStatus.INACTIVE, UserStatus.BLOCK})
    private UserStatus status;

    @NotNull(message = "date of birth must not be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,pattern = "MM/dd/yyyy" )
    private Date dateOrBirth;

    private MultipartFile avatar;
}

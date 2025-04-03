package com.spring.fastfood.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.fastfood.enums.GenderType;
import com.spring.fastfood.enums.UserStatus;
import com.spring.fastfood.validation.GenderSubset;
import com.spring.fastfood.validation.UserStatusSubset;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

    @NotNull(message = "first name must not be null")
    private String firstName;

    @NotNull(message = "full name must not be null")
    private String lastName;

    @Pattern(regexp = "^\\d{10}$" , message = "phone invalid format")
    private String phoneNumber;

    @GenderSubset(anyOf = {GenderType.MALE, GenderType.FEMALE, GenderType.OTHER})
    private GenderType gender;

    @NotNull(message = "date of birth must not be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dateOrBirth;
}

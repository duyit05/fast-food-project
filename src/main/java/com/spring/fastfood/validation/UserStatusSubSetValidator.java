package com.spring.fastfood.validation;

import com.spring.fastfood.enums.UserStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.usertype.UserType;

import java.util.Arrays;

public class UserStatusSubSetValidator implements ConstraintValidator <UserStatusSubset , UserStatus> {
    private UserStatus [] status;
    @Override
    public void initialize(UserStatusSubset constraint) {
        this.status = constraint.anyOf();
    }

    @Override
    public boolean isValid(UserStatus value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(status).contains(value);
    }
}

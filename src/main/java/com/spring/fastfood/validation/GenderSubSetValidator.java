package com.spring.fastfood.validation;

import com.spring.fastfood.enums.GenderType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class GenderSubSetValidator implements ConstraintValidator <GenderSubset , GenderType> {
    private GenderType [] gender;

    @Override
    public void initialize(GenderSubset constraint) {
        this.gender = constraint.anyOf();
    }

    @Override
    public boolean isValid(GenderType value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(gender).contains(value);
    }
}

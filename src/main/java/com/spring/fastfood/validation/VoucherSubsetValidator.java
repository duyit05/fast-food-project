package com.spring.fastfood.validation;

import com.spring.fastfood.enums.VoucherType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class VoucherSubsetValidator implements ConstraintValidator<VoucherSubset, VoucherType> {
    private VoucherType[] status;
    @Override
    public void initialize(VoucherSubset constraint) {
        this.status = constraint.anyOf();
    }

    @Override
    public boolean isValid(VoucherType value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(status).contains(value);
    }
}

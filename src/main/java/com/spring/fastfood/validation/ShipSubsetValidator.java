package com.spring.fastfood.validation;

import com.spring.fastfood.enums.GenderType;
import com.spring.fastfood.enums.ShipType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ShipSubsetValidator implements ConstraintValidator<ShipSubset, ShipType> {
    private ShipType[] ship;


    @Override
    public void initialize(ShipSubset constraint) {
        this.ship = constraint.anyOf();
    }

    @Override
    public boolean isValid(ShipType value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(ship).contains(value);
    }
}

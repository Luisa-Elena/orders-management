package org.example.BusinessLogic.Validators;

import org.example.Model.Products;

import java.util.regex.Pattern;

/**
 * Utility class for validating the products amount
 */
public class AmountValidator {
    /**
     * Validates the amount for a product.
     *
     * @param t The product whose amount needs to be validated.
     * @throws IllegalArgumentException if the amount is negative or zero.
     */
    public void validate(Products t) {
        if (t.getAmount() <= 0) {
            throw new IllegalArgumentException("Email is not a valid email!");
        }
    }
}
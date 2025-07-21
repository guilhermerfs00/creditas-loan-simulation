package com.creditas.ce_loan_ms.service.strategy.impl;

import com.creditas.ce_loan_ms.service.strategy.LoanTaxStrategy;
import org.springframework.stereotype.Component;

@Component
public class SeniorAgeStrategy implements LoanTaxStrategy {

    private static final double RATE = 4.0;
    private static final int MIN_AGE = 61;

    @Override
    public double calculateTax(int age) {
        return RATE;
    }

    @Override
    public boolean isApplicable(int age) {
        return age >= MIN_AGE;
    }
}

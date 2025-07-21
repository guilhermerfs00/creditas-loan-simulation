package com.creditas.ce_loan_ms.service.strategy;

public interface LoanTaxStrategy {
    double calculateTax(int age);
    boolean isApplicable(int age);
}

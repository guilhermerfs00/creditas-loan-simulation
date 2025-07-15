package com.creditas.ce_loan_ms.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequest {
    private BigDecimal loanValue;
    private String birthDate;
    private int months;
}
package com.creditas.ce_loan_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LoanResponse {
    private BigDecimal totalAmount;
    private BigDecimal monthlyInstallment;
    private BigDecimal interestPaid;
}
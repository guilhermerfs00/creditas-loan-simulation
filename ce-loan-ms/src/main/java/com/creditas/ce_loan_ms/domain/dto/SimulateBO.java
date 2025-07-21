package com.creditas.ce_loan_ms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class SimulateBO {

    private BigDecimal loanValue;
    private String birthDate;
    private Integer months;

    private BigDecimal totalAmount;
    private BigDecimal monthlyPayment;
    private BigDecimal totalInterest;
}
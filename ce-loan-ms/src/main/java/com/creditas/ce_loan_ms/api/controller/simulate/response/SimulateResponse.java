package com.creditas.ce_loan_ms.api.controller.simulate.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "Loan simulation response containing calculated values")
public class SimulateResponse {

    @Schema(description = "Total amount to be paid including interest", example = "6000.00")
    private BigDecimal totalAmount;

    @Schema(description = "Monthly installment amount", example = "166.67")
    private BigDecimal monthlyInstallment;

    @Schema(description = "Total interest to be paid", example = "1000.00")
    private BigDecimal interestPaid;
}
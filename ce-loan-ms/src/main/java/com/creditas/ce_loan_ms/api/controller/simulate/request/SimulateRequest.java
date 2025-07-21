package com.creditas.ce_loan_ms.api.controller.simulate.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class SimulateRequest {

    @Schema(description = "Loan amount", example = "5000.00")
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "1.0", message = "Loan amount must be greater than zero")
    private BigDecimal loanValue;

    @Schema(description = "Birth date of the applicant", example = "1990-01-01")
    @NotNull(message = "Birth date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Birth date must be in YYYY-MM-DD format")
    private String birthDate;

    @Schema(description = "Number of months for payment", example = "36")
    @NotNull(message = "Number of months is required")
    @Min(value = 1, message = "Number of months must be greater than zero")
    private Integer months;
}
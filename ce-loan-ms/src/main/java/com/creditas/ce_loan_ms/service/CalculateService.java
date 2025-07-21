package com.creditas.ce_loan_ms.service;

import com.creditas.ce_loan_ms.domain.dto.SimulateBO;
import com.creditas.ce_loan_ms.domain.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculateService {

    private static final int MONTHS_IN_YEAR = 12;
    private static final double PERCENTAGE_DIVISOR = 100.0;
    private static final int MIN_MONTHS = 1;
    private static final double MIN_ANNUAL_RATE = 0.0;

    public SimulateBO calculate(BigDecimal loanValue, int months, double annualRate) {
        log.info("Starting loan calculation for amount: {}, months: {}, rate: {}%", loanValue, months, annualRate);

        try {
            validateInputs(loanValue, months, annualRate);

            double monthlyRate = convertToMonthlyRate(annualRate);
            double presentValue = loanValue.doubleValue();

            double monthlyPayment = calculateMonthlyPayment(presentValue, monthlyRate, months);
            double totalAmount = calculateTotalAmount(monthlyPayment, months);
            double totalInterest = calculateTotalInterest(totalAmount, presentValue);

            validateResults(monthlyPayment, totalAmount, totalInterest);

            log.info("Loan calculation completed successfully. Monthly payment: {}", monthlyPayment);

            return buildSimulateBO(monthlyPayment, totalAmount, totalInterest);

        } catch (BusinessException e) {
            log.error("Business error during loan calculation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during loan calculation", e);
            throw new BusinessException("loan.calculation.error", e.getMessage());
        }
    }

    private SimulateBO buildSimulateBO(double monthlyPayment, double totalAmount, double totalInterest) {
        return SimulateBO.builder()
                .monthlyPayment(formatAmount(monthlyPayment))
                .totalAmount(formatAmount(totalAmount))
                .totalInterest(formatAmount(totalInterest))
                .build();
    }

    private double calculateTotalAmount(double monthlyPayment, int months) {
        return monthlyPayment * months;
    }

    private double calculateTotalInterest(double totalAmount, double presentValue) {
        return totalAmount - presentValue;
    }

    private void validateInputs(BigDecimal loanValue, int months, double annualRate) {
        validateLoanValue(loanValue);
        validateLoanMonths(months);
        validateAnnualRate(annualRate);
    }

    private void validateLoanValue(BigDecimal loanValue) {
        if (loanValue == null) {
            throw new BusinessException("loan.value.required");
        }

        if (loanValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("loan.value.invalid", loanValue);
        }
    }

    private void validateLoanMonths(int months) {
        if (months < MIN_MONTHS) {
            throw new BusinessException("loan.months.invalid", months, MIN_MONTHS);
        }
    }

    private void validateAnnualRate(double annualRate) {
        if (annualRate < MIN_ANNUAL_RATE) {
            throw new BusinessException("loan.rate.invalid", annualRate, MIN_ANNUAL_RATE);
        }
    }

    private void validateResults(double monthlyPayment, double totalAmount, double totalInterest) {
        validateAmount("Monthly payment", monthlyPayment);
        validateAmount("Total amount", totalAmount);
        validateAmount("Total interest", totalInterest);

        if (monthlyPayment <= 0 || totalAmount <= 0) {
            throw new BusinessException("loan.calculation.negative.result");
        }
    }

    private void validateAmount(String name, double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new BusinessException("loan.calculation.invalid.result", name);
        }
    }

    private double convertToMonthlyRate(double annualRate) {
        return annualRate / MONTHS_IN_YEAR / PERCENTAGE_DIVISOR;
    }

    private double calculateMonthlyPayment(double presentValue, double monthlyRate, int months) {
        try {
            return monthlyRate == 0
                    ? presentValue / months
                    : presentValue * monthlyRate / calculateDenominator(monthlyRate, months);
        } catch (ArithmeticException e) {
            log.error("Arithmetic error during monthly payment calculation", e);
            throw new BusinessException("loan.payment.calculation.error");
        } catch (Exception e) {
            log.error("Unexpected error during monthly payment calculation", e);
            throw new BusinessException("loan.payment.calculation.unexpected.error");
        }
    }

    private double calculateDenominator(double monthlyRate, int months) {
        double denominator = 1 - Math.pow(1 + monthlyRate, -months);
        if (denominator == 0) {
            throw new BusinessException("loan.calculation.denominator.zero");
        }
        return denominator;
    }

    private BigDecimal formatAmount(Double amount) {
        validateAmountToFormat(amount);
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }

    private void validateAmountToFormat(Double amount) {
        if (amount == null || Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new BusinessException("loan.amount.format.invalid", amount);
        }
    }
}

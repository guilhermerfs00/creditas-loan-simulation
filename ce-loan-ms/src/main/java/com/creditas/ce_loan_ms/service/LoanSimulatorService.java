package com.creditas.ce_loan_ms.service;

import com.creditas.ce_loan_ms.domain.dto.SimulateBO;
import com.creditas.ce_loan_ms.domain.exceptions.BusinessException;
import com.creditas.ce_loan_ms.service.factory.LoanTaxStrategyFactory;
import com.creditas.ce_loan_ms.service.strategy.LoanTaxStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@Slf4j
@AllArgsConstructor
public class LoanSimulatorService {

    private static final int MIN_AGE = 18;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final LoanTaxStrategyFactory strategyFactory;
    private final CalculateService loanCalculator;

    public SimulateBO simulateLoan(SimulateBO request) {
        log.info("Starting loan simulation for request: {}", request);

        try {
            validateRequest(request);

            Integer age = calculateAge(request.getBirthDate());
            log.debug("Calculated age: {} years", age);

            validateAge(age);

            Double interestRate = getInterestRateByAge(age);
            log.debug("Applied interest rate: {}%", interestRate);

            SimulateBO response = loanCalculator.calculate(
                    request.getLoanValue(),
                    request.getMonths(),
                    interestRate
            );

            log.info("Loan simulation completed successfully");
            return response;

        } catch (BusinessException e) {
            log.error("Business error during loan simulation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during loan simulation", e);
            throw new BusinessException("loan.simulation.error", e.getMessage());
        }
    }

    private void validateRequest(SimulateBO request) {
        if (request == null) {
            throw new BusinessException("loan.request.null");
        }

        if (request.getLoanValue() == null) {
            throw new BusinessException("loan.value.required");
        }

        if (request.getBirthDate() == null || request.getBirthDate().trim().isEmpty()) {
            throw new BusinessException("loan.birthdate.required");
        }

        if (request.getMonths() == null) {
            throw new BusinessException("loan.months.required");
        }
    }

    private Integer calculateAge(String birthDateStr) {
        try {
            if (birthDateStr == null || birthDateStr.trim().isEmpty()) {
                throw new BusinessException("loan.birthdate.empty");
            }

            LocalDate birthDate = LocalDate.parse(birthDateStr.trim(), DATE_FORMATTER);
            LocalDate currentDate = LocalDate.now();

            if (birthDate.isAfter(currentDate)) {
                throw new BusinessException("loan.birthdate.future", birthDateStr);
            }

            int age = Period.between(birthDate, currentDate).getYears();
            log.debug("Calculated age from birthdate {}: {} years", birthDateStr, age);

            return age;

        } catch (DateTimeParseException e) {
            log.error("Invalid birth date format: {}", birthDateStr, e);
            throw new BusinessException("loan.birthdate.invalid.format", birthDateStr);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error calculating age from birthdate: {}", birthDateStr, e);
            throw new BusinessException("loan.age.calculation.error", birthDateStr);
        }
    }

    private void validateAge(Integer age) {
        if (age == null) {
            throw new BusinessException("loan.age.null");
        }

        if (age < MIN_AGE) {
            throw new BusinessException("loan.age.too.young", age, MIN_AGE);
        }

        log.debug("Age validation passed: {} years", age);
    }

    private Double getInterestRateByAge(int age) {
        try {
            LoanTaxStrategy strategy = strategyFactory.getStrategy(age);
            Double rate = strategy.calculateTax(age);

            log.debug("Interest rate calculated for age {}: {}%", age, rate);
            return rate;

        } catch (Exception e) {
            log.error("Error getting interest rate for age: {}", age, e);
            throw new BusinessException("loan.rate.calculation.error", age);
        }
    }
}

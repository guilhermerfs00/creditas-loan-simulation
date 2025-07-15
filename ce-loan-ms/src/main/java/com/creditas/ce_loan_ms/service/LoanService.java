package com.creditas.ce_loan_ms.service;

import com.creditas.ce_loan_ms.dto.LoanRequest;
import com.creditas.ce_loan_ms.dto.LoanResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@Service
public class LoanService {

    public LoanResponse simulateLoan(LoanRequest request) {
        BigDecimal loanValue = request.getLoanValue();
        int months = request.getMonths();
        int age = calculateAge(request.getBirthDate());

        double annualRate = getRateByAge(age);
        double monthlyRate = annualRate / 12 / 100;

        // Fórmula de PMT
        double pv = loanValue.doubleValue();
        double r = monthlyRate;
        double pmt = pv * r / (1 - Math.pow(1 + r, -months));
        double total = pmt * months;
        double interest = total - pv;

        return new LoanResponse(
                BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(pmt).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(interest).setScale(2, RoundingMode.HALF_UP)
        );
    }

    private int calculateAge(String birthDateStr) {
        LocalDate birthDate = LocalDate.parse(birthDateStr);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private double getRateByAge(int age) {
        if (age <= 25) return 5.0;
        if (age <= 40) return 3.0;
        if (age <= 60) return 2.0;
        return 4.0;
    }
}

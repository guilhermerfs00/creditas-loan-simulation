package com.creditas.ce_loan_ms.service.factory;

import com.creditas.ce_loan_ms.service.strategy.LoanTaxStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanTaxStrategyFactory {

    private final List<LoanTaxStrategy> strategies;

    @Autowired
    public LoanTaxStrategyFactory(List<LoanTaxStrategy> strategies) {
        this.strategies = strategies;
    }

    public LoanTaxStrategy getStrategy(int age) {
        return strategies.stream()
                .filter(strategy -> strategy.isApplicable(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for age: " + age));
    }
}

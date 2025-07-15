package com.creditas.ce_loan_ms.controller;

import com.creditas.ce_loan_ms.dto.LoanRequest;
import com.creditas.ce_loan_ms.dto.LoanResponse;
import com.creditas.ce_loan_ms.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/simulate")
    public ResponseEntity<LoanResponse> simulate(@RequestBody LoanRequest request) {
        LoanResponse response = loanService.simulateLoan(request);
        return ResponseEntity.ok(response);
    }
}
package com.creditas.ce_loan_ms.api.controller.simulate;

import com.creditas.ce_loan_ms.api.controller.simulate.request.SimulateRequest;
import com.creditas.ce_loan_ms.api.controller.simulate.response.SimulateResponse;
import com.creditas.ce_loan_ms.domain.SimulateMapper;
import com.creditas.ce_loan_ms.service.LoanSimulatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class LoanController implements ILoanController {

    private final LoanSimulatorService loanSimulatorService;
    private final SimulateMapper simulateMapper;

    @Override
    @PostMapping("/simulate")
    public ResponseEntity<SimulateResponse> simulate(@Valid @RequestBody SimulateRequest request) {
        var simulateDTO = simulateMapper.requestToBO(request);
        var response = loanSimulatorService.simulateLoan(simulateDTO);
        return ResponseEntity.status(OK).body(simulateMapper.boToResponse(response));
    }
}
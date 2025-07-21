package com.creditas.ce_loan_ms.api.controller.simulate;

import com.creditas.ce_loan_ms.api.controller.simulate.request.SimulateRequest;
import com.creditas.ce_loan_ms.api.controller.simulate.response.SimulateResponse;
import com.creditas.ce_loan_ms.domain.dto.ErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Tag(name = "Simulate Loans")
@RequestMapping(value = "/loans")
@Validated
public interface ILoanController {

    @Operation(
            summary = "Simulate loan",
            description = "Endpoint responsible for simulating a loan",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Simulation completed successfully.",
                            content = @Content(schema = @Schema(implementation = SimulateResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid data.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            })
    @PostMapping("/simulate")
    @ResponseStatus(OK)
    ResponseEntity<SimulateResponse> simulate(@Valid @RequestBody SimulateRequest request);
}

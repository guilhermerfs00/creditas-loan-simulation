package com.creditas.ce_loan_ms.domain;

import com.creditas.ce_loan_ms.api.controller.simulate.request.SimulateRequest;
import com.creditas.ce_loan_ms.api.controller.simulate.response.SimulateResponse;
import com.creditas.ce_loan_ms.domain.dto.SimulateBO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimulateMapper {

    SimulateBO requestToBO(SimulateRequest request);
    SimulateResponse boToResponse(SimulateBO request);

}

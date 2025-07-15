package com.creditas.ce_auth_ms.model.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}

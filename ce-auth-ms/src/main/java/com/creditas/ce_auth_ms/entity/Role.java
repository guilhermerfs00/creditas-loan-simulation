package com.creditas.ce_auth_ms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Long id;
    private String name;

    public String getRoleName() {
        return name;
    }
}

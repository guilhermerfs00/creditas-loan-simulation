package com.creditas.ce_loan_ms.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String SECRET = "segredo-123";

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}